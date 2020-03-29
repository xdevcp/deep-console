package cc.devcp.project.provider.service;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.support.TransactionTemplate;

import java.io.IOException;

/**
 * datasource interface
 *
 * @author Nacos
 */
public interface DataSourceService {
    /**
     * reload
     *
     * @throws IOException exception
     */
    void reload() throws IOException;

    /**
     * check master db
     *
     * @return is master
     */
    boolean checkMasterWritable();

    /**
     * get jdbc template
     *
     * @return JdbcTemplate
     */
    JdbcTemplate getJdbcTemplate();

    /**
     * get transaction template
     *
     * @return TransactionTemplate
     */
    TransactionTemplate getTransactionTemplate();

    /**
     * get current db url
     *
     * @return
     */
    String getCurrentDBUrl();

    /**
     * get heath
     *
     * @return heath info
     */
    String getHealth();
}
