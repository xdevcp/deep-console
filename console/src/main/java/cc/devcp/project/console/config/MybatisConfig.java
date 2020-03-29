package cc.devcp.project.console.config;

import cc.devcp.project.provider.service.DataSourceService;
import cc.devcp.project.provider.service.DynamicDataSource;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.support.TransactionTemplate;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;

/**
 * <pre>
 *     description:
 * </pre>
 *
 * @author deep.wu
 * @version 2020-03-29
 */
@MapperScan({"cc.devcp.project.**.mapper"})
@EnableTransactionManagement
@Configuration
public class MybatisConfig {

    @Autowired
    private DynamicDataSource dynamicDataSource;

    private DataSourceService dataSourceService;

    protected JdbcTemplate jt;
    protected TransactionTemplate tjt;

    public JdbcTemplate getJdbcTemplate() {
        return this.dataSourceService.getJdbcTemplate();
    }

    public TransactionTemplate getTransactionTemplate() {
        return this.dataSourceService.getTransactionTemplate();
    }

    @PostConstruct
    public void init() {
        dataSourceService = dynamicDataSource.getDataSource();
        jt = getJdbcTemplate();
        tjt = getTransactionTemplate();
    }

    @Bean(name = "dataSource")
    public DataSource dataSource() {
        return jt.getDataSource();

    }

}

