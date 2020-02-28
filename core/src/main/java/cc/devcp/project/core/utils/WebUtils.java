package cc.devcp.project.core.utils;

import cc.devcp.project.common.constant.HttpHeaderConsts;
import org.apache.commons.lang3.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;

/**
 * @author nkorange
 */
public class WebUtils {

    public static String required(HttpServletRequest req, String key) {
        String value = req.getParameter(key);
        if (StringUtils.isEmpty(value)) {
            throw new IllegalArgumentException("Param '" + key + "' is required.");
        }

        String encoding = req.getParameter("encoding");
        if (!StringUtils.isEmpty(encoding)) {
            try {
                value = new String(value.getBytes(StandardCharsets.UTF_8), encoding);
            } catch (UnsupportedEncodingException ignore) {
            }
        }

        return value.trim();
    }

    public static String optional(HttpServletRequest req, String key, String defaultValue) {

        if (!req.getParameterMap().containsKey(key) || req.getParameterMap().get(key)[0] == null) {
            return defaultValue;
        }

        String value = req.getParameter(key);

        if (StringUtils.isBlank(value)) {
            return defaultValue;
        }

        String encoding = req.getParameter("encoding");
        if (!StringUtils.isEmpty(encoding)) {
            try {
                value = new String(value.getBytes(StandardCharsets.UTF_8), encoding);
            } catch (UnsupportedEncodingException ignore) {
            }
        }

        return value.trim();
    }

    public static String getAcceptEncoding(HttpServletRequest req) {
        String encode = StringUtils.defaultIfEmpty(req.getHeader("Accept-Charset"), "UTF-8");
        encode = encode.contains(",") ? encode.substring(0, encode.indexOf(",")) : encode;
        return encode.contains(";") ? encode.substring(0, encode.indexOf(";")) : encode;
    }

    /**
     * Returns the value of the request header "user-agent" as a <code>String</code>.
     *
     * @param request HttpServletRequest
     * @return the value of the request header "user-agent", or the value of the
     *         request header "client-version" if the request does not have a
     *         header of "user-agent"
     */
    public static String getUserAgent(HttpServletRequest request) {
        String userAgent = request.getHeader(HttpHeaderConsts.USER_AGENT_HEADER);
        if (StringUtils.isEmpty(userAgent)) {
            userAgent = StringUtils.defaultIfEmpty(request.getHeader(HttpHeaderConsts.CLIENT_VERSION_HEADER), StringUtils.EMPTY);
        }
        return userAgent;
    }
}
