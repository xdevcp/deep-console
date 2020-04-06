package cc.devcp.project.gateway.controller;

import cc.devcp.project.common.constant.CxtRouter;
import cc.devcp.project.gateway.utils.JwtHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * <pre>
 *     description:
 * </pre>
 *
 * @author deep.wu
 * @version 2020-04-03
 */
@Slf4j
@RestController
@RequestMapping(value = CxtRouter.gateway)
public class GatewayController {

    @Autowired
    private JwtHelper jwtHelper;

    /**
     * 当前激活的环境配置
     */
    @Value("${spring.profiles.active:default}")
    private String env;

    private String envProdStr = "prod";
    private String heathUpStr = "UP";

    @GetMapping
    public Object getHealth() {
        Map map = new HashMap(16);
        map.put("environment", env);
        map.put("status", heathUpStr);
        return map;
    }

    /**
     * <pre>
     *     description: 仅本地测试使用
     * </pre>
     *
     * @author deep.wu
     * @version 2020-04-03
     */
    @GetMapping("/mock/token")
    public String mockToken(String account) {
        // 屏蔽生产环境
        if (envProdStr.equals(env)) {
            return "403";
        }
        String token = jwtHelper.createToken(account);
        return "token : " + token;
    }

}
