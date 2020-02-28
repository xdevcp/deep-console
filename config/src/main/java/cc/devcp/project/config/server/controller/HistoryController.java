package cc.devcp.project.config.server.controller;

import cc.devcp.project.config.server.constant.Constants;
import cc.devcp.project.config.server.model.ConfigHistoryInfo;
import cc.devcp.project.config.server.model.Page;
import cc.devcp.project.config.server.service.PersistService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 管理控制器。
 *
 * @author Nacos
 */
@RestController
@RequestMapping(Constants.HISTORY_CONTROLLER_PATH)
public class HistoryController {

    @Autowired
    protected PersistService persistService;

    @GetMapping(params = "search=accurate")
    public Page<ConfigHistoryInfo> listConfigHistory(@RequestParam("dataId") String dataId, //
                                                     @RequestParam("group") String group, //
                                                     @RequestParam(value = "tenant", required = false,
                                                         defaultValue = StringUtils.EMPTY) String tenant,
                                                     @RequestParam(value = "appName", required = false) String appName,
                                                     @RequestParam(value = "pageNo", required = false) Integer pageNo,
                                                     //
                                                     @RequestParam(value = "pageSize", required = false)
                                                         Integer pageSize, //
                                                     ModelMap modelMap) {
        pageNo = null == pageNo ? 1 : pageNo;
        pageSize = null == pageSize ? 100 : pageSize;
        pageSize = Math.min(500,pageSize);
        // configInfoBase没有appName字段
        return persistService.findConfigHistory(dataId, group, tenant, pageNo, pageSize);
    }

    /**
     * 查看配置历史信息详情
     */
    @GetMapping
    public ConfigHistoryInfo getConfigHistoryInfo(HttpServletRequest request, HttpServletResponse response,
                                                  @RequestParam("nid") Long nid, ModelMap modelMap) {
        return persistService.detailConfigHistory(nid);
    }

}
