package cc.devcp.project.provider.controller;

import cc.devcp.project.provider.constant.Constants;
import cc.devcp.project.provider.model.GroupkeyListenserStatus;
import cc.devcp.project.provider.model.SampleResult;
import cc.devcp.project.provider.service.ConfigSubService;
import cc.devcp.project.provider.utils.GroupKey2;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * Config longpolling
 *
 * @author Nacos
 */
@RestController
@RequestMapping(Constants.LISTENER_CONTROLLER_PATH)
public class ListenerController {

    private final ConfigSubService configSubService;

    @Autowired
    public ListenerController(ConfigSubService configSubService) {
        this.configSubService = configSubService;
    }

    /**
     * 获取客户端订阅配置信息
     */
    @GetMapping
    public GroupkeyListenserStatus getAllSubClientConfigByIp(@RequestParam("ip") String ip,
                                                             @RequestParam(value = "all", required = false) boolean all,
                                                             @RequestParam(value = "tenant", required = false)
                                                                 String tenant,
                                                             @RequestParam(value = "sampleTime", required = false,
                                                                 defaultValue = "1") int sampleTime, ModelMap modelMap)
        throws Exception {
        SampleResult collectSampleResult = configSubService.getCollectSampleResultByIp(ip, sampleTime);
        GroupkeyListenserStatus gls = new GroupkeyListenserStatus();
        gls.setCollectStatus(200);
        Map<String, String> configMd5Status = new HashMap<String, String>(100);
        if (collectSampleResult.getLisentersGroupkeyStatus() != null) {
            Map<String, String> status = collectSampleResult.getLisentersGroupkeyStatus();
            for (Map.Entry<String, String> config : status.entrySet()) {
                if (!StringUtils.isBlank(tenant)) {
                    if (config.getKey().contains(tenant)) {
                        configMd5Status.put(config.getKey(), config.getValue());
                    }
                } else {
                    // 默认值获取公共配置，如果想看所有配置，要加all
                    if (all) {
                        configMd5Status.put(config.getKey(), config.getValue());
                    } else {
                        String[] configKeys = GroupKey2.parseKey(config.getKey());
                        if (StringUtils.isBlank(configKeys[2])) {
                            configMd5Status.put(config.getKey(), config.getValue());
                        }
                    }
                }
            }
            gls.setLisentersGroupkeyStatus(configMd5Status);
        }

        return gls;
    }

}

