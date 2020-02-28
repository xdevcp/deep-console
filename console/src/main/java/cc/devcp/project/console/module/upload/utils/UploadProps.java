package cc.devcp.project.console.module.upload.utils;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @version 1.0.0
 * @author: lzy
 * @date: 2020/1/9 15:20
 */
@Getter
@Setter
@ToString
@Component
@ConfigurationProperties(prefix = "cpd.upload")
public class UploadProps {

    private String host;

    private Integer port;

    private String username;

    private String key;

    private String path;

    private String urlPrefix;

}
