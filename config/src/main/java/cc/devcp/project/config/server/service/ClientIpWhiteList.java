package cc.devcp.project.config.server.service;

import cc.devcp.project.config.server.model.ACLInfo;
import cc.devcp.project.config.server.utils.JSONUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

import static cc.devcp.project.config.server.utils.LogUtil.defaultLog;

/**
 * Client ip whitelist
 *
 * @author Nacos
 */
@Service
public class ClientIpWhiteList {

    /**
     * 判断指定的ip在白名单中
     */
    static public boolean isLegalClient(String clientIp) {
        if (StringUtils.isBlank(clientIp)) {
            throw new IllegalArgumentException();
        }
        clientIp = clientIp.trim();
        if (CLIENT_IP_WHITELIST.get().contains(clientIp)) {
            return true;
        }
        return false;
    }

    /**
     * whether start client ip whitelist
     *
     * @return true: enable ; false disable
     */
    static public boolean isEnableWhitelist() {
        return isOpen;
    }

    /**
     * 传入内容，重新加载客户端ip白名单
     */
    static public void load(String content) {
        if (StringUtils.isBlank(content)) {
            defaultLog.warn("clientIpWhiteList is blank.close whitelist.");
            isOpen = false;
            CLIENT_IP_WHITELIST.get().clear();
            return;
        }
        defaultLog.warn("[clientIpWhiteList] {}", content);
        try {
            ACLInfo acl = (ACLInfo)JSONUtils.deserializeObject(content, ACLInfo.class);
            isOpen = acl.getIsOpen();
            CLIENT_IP_WHITELIST.set(acl.getIps());
        } catch (Exception ioe) {
            defaultLog.error(
                "failed to load clientIpWhiteList, " + ioe.toString(), ioe);
        }
    }

    // =======================

    static public final String CLIENT_IP_WHITELIST_METADATA = "cc.devcp.project.metadata.clientIpWhitelist";

    static final AtomicReference<List<String>> CLIENT_IP_WHITELIST = new AtomicReference<List<String>>(
        new ArrayList<String>());
    static Boolean isOpen = false;
}
