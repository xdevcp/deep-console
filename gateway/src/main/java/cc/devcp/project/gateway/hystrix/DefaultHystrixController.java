package cc.devcp.project.gateway.hystrix;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.netty.http.server.HttpServerResponse;

import java.util.HashMap;
import java.util.Map;

/**
 * @version 1.0.0
 * @author: lzy
 * @date: 2019/6/28 15:28
 */
@RestController
public class DefaultHystrixController {

    @RequestMapping("/callback")
    public Map<String, Object> callback(HttpServerResponse response) {
        Map<String, Object> map = new HashMap<>(3);
        map.put("success", false);
        map.put("errCode", 50000);
        map.put("errMsg", "Service is not available, try again or contact service provider.");
        response.status(HttpStatus.INTERNAL_SERVER_ERROR.value());
        return map;
    }

}
