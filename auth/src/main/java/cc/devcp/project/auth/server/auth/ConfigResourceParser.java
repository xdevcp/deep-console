package cc.devcp.project.auth.server.auth;

import cc.devcp.project.auth.security.auth.Resource;
import cc.devcp.project.auth.security.auth.ResourceParser;
import cc.devcp.project.common.constant.CommonConst;
import org.apache.commons.lang3.StringUtils;

import javax.servlet.http.HttpServletRequest;

/**
 * Config resource parser
 *
 * @author nkorange
 * @since 1.2.0
 */
public class ConfigResourceParser implements ResourceParser {

    private static final String AUTH_CONFIG_PREFIX = "config/";

    @Override
    public String parseName(Object request) {
        HttpServletRequest req = (HttpServletRequest) request;
        String namespaceId = req.getParameter("tenant");
        String groupName = req.getParameter("group");
        String dataId = req.getParameter("dataId");

        if (StringUtils.isBlank(namespaceId)) {
            namespaceId = CommonConst.DEFAULT_NAMESPACE_ID;
        }

        StringBuilder sb = new StringBuilder();

        sb.append(namespaceId).append(Resource.SPLITTER);

        if (StringUtils.isBlank(dataId)) {
            sb.append("*")
                .append(Resource.SPLITTER)
                .append(AUTH_CONFIG_PREFIX)
                .append("*");
        } else {
            sb.append(groupName)
                .append(Resource.SPLITTER)
                .append(AUTH_CONFIG_PREFIX)
                .append(dataId);
        }

        return sb.toString();
    }
}
