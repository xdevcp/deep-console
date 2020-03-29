package cc.devcp.project.provider.component;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @version 1.0.0
 * @author: deep
 * @date: 2020/02/02 14:05
 */
@Getter
@Setter
@ToString
@ConfigurationProperties(prefix = "spring.datasource")
@Component
public class DataSourceProps {

    private String type;

    private String platform;

    private String username;

    private String password;

    private String customDbNum;

    private String customDbUrl;

}
