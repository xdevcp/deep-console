package cc.devcp.project.provider.service;

import cc.devcp.project.core.listener.StartingSpringApplicationRunListener;
import cc.devcp.project.provider.component.DataSourceProps;
import cc.devcp.project.provider.monitor.MetricsMonitor;
import cc.devcp.project.provider.utils.LogUtil;
import cc.devcp.project.provider.utils.PropertyUtil;
import com.zaxxer.hikari.HikariDataSource;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.CannotGetJdbcConnectionException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionTemplate;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Base data source
 *
 * @author Nacos
 */
@Service("basicDataSourceService")
public class BasicDataSourceServiceImpl implements DataSourceService {

    private static final Logger LOGGER = LoggerFactory.getLogger(StartingSpringApplicationRunListener.class);
    private static final Logger log = LoggerFactory.getLogger(BasicDataSourceServiceImpl.class);
    private static final String DEFAULT_MYSQL_DRIVER = "com.mysql.jdbc.Driver";
    private static final String MYSQL_HIGH_LEVEL_DRIVER = "com.mysql.cj.jdbc.Driver";
    private static String JDBC_DRIVER_NAME;

    /**
     * JDBC执行超时时间, 单位秒
     */
    private int queryTimeout = 3;

    private static final int TRANSACTION_QUERY_TIMEOUT = 5;

    private static final String DB_LOAD_ERROR_MSG = "[db-load-error]load jdbc.properties error";

    private List<HikariDataSource> dataSourceList = new ArrayList<HikariDataSource>();
    private JdbcTemplate jt;
    private DataSourceTransactionManager tm;
    private TransactionTemplate tjt;

    private JdbcTemplate testMasterJT;
    private JdbcTemplate testMasterWritableJT;

    volatile private List<JdbcTemplate> testJTList;
    volatile private List<Boolean> isHealthList;
    private volatile int masterIndex;
    private static Pattern ipPattern = Pattern.compile("\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}");


    @Autowired
    private Environment env;

    @Autowired
    private PropertyUtil propertyUtil;

    @Autowired
    private DataSourceProps dataSourceProps;

    static {
        try {
            Class.forName(MYSQL_HIGH_LEVEL_DRIVER);
            JDBC_DRIVER_NAME = MYSQL_HIGH_LEVEL_DRIVER;
            log.info("Use Mysql 8 as the driver");
        } catch (ClassNotFoundException e) {
            log.info("Use Mysql as the driver");
            JDBC_DRIVER_NAME = DEFAULT_MYSQL_DRIVER;
        }
    }

    @PostConstruct
    public void init() {
        queryTimeout = NumberUtils.toInt(System.getProperty("QUERYTIMEOUT"), 3);
        jt = new JdbcTemplate();
        /**
         *  设置最大记录数，防止内存膨胀
         */
        jt.setMaxRows(50000);
        jt.setQueryTimeout(queryTimeout);

        testMasterJT = new JdbcTemplate();
        testMasterJT.setQueryTimeout(queryTimeout);

        testMasterWritableJT = new JdbcTemplate();
        /**
         * 防止login接口因为主库不可用而rt太长
         */
        testMasterWritableJT.setQueryTimeout(1);
        /**
         * 数据库健康检测
         */
        testJTList = new ArrayList<JdbcTemplate>();
        isHealthList = new ArrayList<Boolean>();

        tm = new DataSourceTransactionManager();
        tjt = new TransactionTemplate(tm);
        /**
         *  事务的超时时间需要与普通操作区分开
         */
        tjt.setTimeout(TRANSACTION_QUERY_TIMEOUT);
        if (propertyUtil.isStandaloneUseMysql()) {
            LOGGER.info("Use datasource [mysql/derby]: {}", "mysql");
            try {
                reload();
            } catch (IOException e) {
                e.printStackTrace();
                throw new RuntimeException(DB_LOAD_ERROR_MSG);
            }

            TimerTaskService.scheduleWithFixedDelay(new SelectMasterTask(), 10, 10,
                TimeUnit.SECONDS);
            TimerTaskService.scheduleWithFixedDelay(new CheckDBHealthTask(), 10, 10,
                TimeUnit.SECONDS);
        } else {
            LOGGER.info("Use datasource [mysql/derby]: {}", "derby");
        }
    }

    @Override
    public synchronized void reload() throws IOException {
        List<HikariDataSource> dbList = new ArrayList<HikariDataSource>();
        try {
            String val = null;
            val = env.getProperty("spring.datasource.custom-db-num");
            if (null == val) {
                throw new IllegalArgumentException("spring.datasource.custom-db-num is null");
            }
            int dbNum = Integer.parseInt(val.trim());

            for (int i = 1; i <= dbNum; i++) {
                HikariDataSource ds = new HikariDataSource();
                ds.setDriverClassName(JDBC_DRIVER_NAME);

                val = env.getProperty("spring.datasource.custom-db-url-" + i);
                if (null == val) {
                    LogUtil.fatalLog.error("spring.datasource.custom-db-url-" + i + " is null");
                    throw new IllegalArgumentException();
                }
                ds.setJdbcUrl(val.trim());

                val = env.getProperty("spring.datasource.username-" + i, dataSourceProps.getUsername());
                if (null == val) {
                    LogUtil.fatalLog.error("spring.datasource.username-" + i + " is null");
                    throw new IllegalArgumentException();
                }
                ds.setUsername(val.trim());

                val = env.getProperty("spring.datasource.password-" + i, dataSourceProps.getPassword());
                if (null == val) {
                    LogUtil.fatalLog.error("spring.datasource.password-" + i + " is null");
                    throw new IllegalArgumentException();
                }
                ds.setPassword(val.trim());

                // 连接只读数据库时配置为true， 保证安全
                ds.setReadOnly(false);
                // 等待连接池分配连接的最大时长（毫秒），超过这个时长还没可用的连接则发生SQLException， 缺省:30秒
                ds.setConnectionTimeout(30000);
                // 一个连接idle状态的最大时长（毫秒），超时则被释放（retired），缺省:10分钟
                ds.setIdleTimeout(600000);
                // 一个连接的生命时长（毫秒），超时而且没被使用则被释放（retired），缺省:30分钟，建议设置比数据库超时时长少30秒，
                // 参考MySQL wait_timeout参数（show variables like '%timeout%';）
                ds.setMaxLifetime(1800000);
                // 连接池中允许的最大连接数。缺省值：10；推荐的公式：((core_count * 2) + effective_spindle_count)
                ds.setMaximumPoolSize(15);
                ds.setConnectionTestQuery("SELECT 1 FROM dual");

                dbList.add(ds);

                JdbcTemplate jdbcTemplate = new JdbcTemplate();
                jdbcTemplate.setQueryTimeout(queryTimeout);
                jdbcTemplate.setDataSource(ds);

                testJTList.add(jdbcTemplate);
                isHealthList.add(Boolean.TRUE);
            }

            if (dbList == null || dbList.size() == 0) {
                throw new RuntimeException("no datasource available");
            }

            dataSourceList = dbList;
            new SelectMasterTask().run();
            new CheckDBHealthTask().run();
        } catch (RuntimeException e) {
            LogUtil.fatalLog.error(DB_LOAD_ERROR_MSG, e);
            throw new IOException(e);
        } finally {
        }
    }

    @Override
    public boolean checkMasterWritable() {

        testMasterWritableJT.setDataSource(jt.getDataSource());
        /**
         *  防止login接口因为主库不可用而rt太长
         */
        testMasterWritableJT.setQueryTimeout(1);
        String sql = " SELECT @@read_only ";

        try {
            Integer result = testMasterWritableJT.queryForObject(sql, Integer.class);
            if (result == null) {
                return false;
            } else {
                return result == 0;
            }
        } catch (CannotGetJdbcConnectionException e) {
            LogUtil.fatalLog.error("[db-error] " + e.toString(), e);
            return false;
        }

    }

    @Override
    public JdbcTemplate getJdbcTemplate() {
        return this.jt;
    }

    @Override
    public TransactionTemplate getTransactionTemplate() {
        return this.tjt;
    }

    @Override
    public String getCurrentDBUrl() {
        DataSource ds = this.jt.getDataSource();
        if (ds == null) {
            return StringUtils.EMPTY;
        }
        HikariDataSource bds = (HikariDataSource) ds;
        return bds.getJdbcUrl();
    }

    @Override
    public String getHealth() {
        for (int i = 0; i < isHealthList.size(); i++) {
            if (!isHealthList.get(i)) {
                if (i == masterIndex) {
                    /**
                     * 主库不健康
                     */
                    return "DOWN:" + getIpFromUrl(dataSourceList.get(i).getJdbcUrl());
                } else {
                    /**
                     * 从库不健康
                     */
                    return "WARN:" + getIpFromUrl(dataSourceList.get(i).getJdbcUrl());
                }
            }
        }

        return "UP";
    }

    private String getIpFromUrl(String url) {

        Matcher m = ipPattern.matcher(url);
        if (m.find()) {
            return m.group();
        }

        return "";
    }

    static String defaultIfNull(String value, String defaultValue) {
        return null == value ? defaultValue : value;
    }

    class SelectMasterTask implements Runnable {

        @Override
        public void run() {
            if (LogUtil.defaultLog.isDebugEnabled()) {
                LogUtil.defaultLog.debug("check master db.");
            }
            boolean isFound = false;

            int index = -1;
            for (HikariDataSource ds : dataSourceList) {
                index++;
                testMasterJT.setDataSource(ds);
                testMasterJT.setQueryTimeout(queryTimeout);
                try {
                    testMasterJT
                        .update(
                            "DELETE FROM config_info WHERE data_id='cc.devcp.project.testMasterDB'");
                    if (jt.getDataSource() != ds) {
                        LogUtil.fatalLog.warn("[master-db] {}", ds.getJdbcUrl());
                    }
                    jt.setDataSource(ds);
                    tm.setDataSource(ds);
                    isFound = true;
                    masterIndex = index;
                    break;
                } catch (DataAccessException e) { // read only
                    e.printStackTrace(); // TODO remove
                }
            }

            if (!isFound) {
                LogUtil.fatalLog.error("[master-db] master db not found.");
                MetricsMonitor.getDbException().increment();
            }
        }
    }

    @SuppressWarnings("PMD.ClassNamingShouldBeCamelRule")
    class CheckDBHealthTask implements Runnable {

        @Override
        public void run() {
            if (LogUtil.defaultLog.isDebugEnabled()) {
                LogUtil.defaultLog.debug("check db health.");
            }
            String sql = "SELECT * FROM config_info_beta WHERE id = 1";

            for (int i = 0; i < testJTList.size(); i++) {
                JdbcTemplate jdbcTemplate = testJTList.get(i);
                try {
                    jdbcTemplate.query(sql, PersistService.CONFIG_INFO4BETA_ROW_MAPPER);
                    isHealthList.set(i, Boolean.TRUE);
                } catch (DataAccessException e) {
                    if (i == masterIndex) {
                        LogUtil.fatalLog.error("[db-error] master db {} down.",
                            getIpFromUrl(dataSourceList.get(i).getJdbcUrl()));
                    } else {
                        LogUtil.fatalLog.error("[db-error] slave db {} down.",
                            getIpFromUrl(dataSourceList.get(i).getJdbcUrl()));
                    }
                    isHealthList.set(i, Boolean.FALSE);

                    MetricsMonitor.getDbException().increment();
                }
            }
        }
    }
}
