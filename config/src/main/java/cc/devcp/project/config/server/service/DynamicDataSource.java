package cc.devcp.project.config.server.service;

import cc.devcp.project.config.server.utils.PropertyUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * datasource adapter
 *
 * @author Nacos
 */
@Component
public class DynamicDataSource implements ApplicationContextAware {

    private ApplicationContext applicationContext;

    @Autowired
    private PropertyUtil propertyUtil;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    public ApplicationContext getApplicationContext() {
        return applicationContext;
    }

    public DataSourceService getDataSource() {
        DataSourceService dataSourceService = null;

        if (propertyUtil.isStandaloneUseMysql()) {
            dataSourceService = (DataSourceService) applicationContext.getBean("basicDataSourceService");
        } else {
            dataSourceService = (DataSourceService) applicationContext.getBean("localDataSourceService");
        }
        return dataSourceService;
    }

}
