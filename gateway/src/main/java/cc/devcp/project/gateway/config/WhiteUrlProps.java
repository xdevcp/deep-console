package cc.devcp.project.gateway.config;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @version 1.0.0
 * @author: lzy
 * @date: 2019/12/24 11:28
 */
@Getter@Setter@ToString
@Component
@ConfigurationProperties(prefix = "gateway")
public class WhiteUrlProps {

    private List<String> whiteUrls;

}
