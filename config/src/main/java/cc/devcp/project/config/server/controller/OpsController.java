package cc.devcp.project.config.server.controller;

import cc.devcp.project.config.server.constant.Constants;
import cc.devcp.project.config.server.utils.LogUtil;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;

/**
 * 管理控制器。
 *
 * @author Nacos
 */
@RestController
@RequestMapping(Constants.OPS_CONTROLLER_PATH)
public class OpsController {

    @PutMapping(value = "/log")
    public String setLogLevel(@RequestParam String logName, @RequestParam String logLevel) {
        LogUtil.setLogLevel(logName, logLevel);
        return HttpServletResponse.SC_OK + "";
    }

}
