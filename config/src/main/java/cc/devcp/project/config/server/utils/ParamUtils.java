package cc.devcp.project.config.server.utils;

import java.util.Map;

import cc.devcp.project.common.exception.GlobalException;

import org.apache.commons.lang3.StringUtils;

/**
 * 参数合法性检查工具类
 *
 * @author Nacos
 */
public class ParamUtils {

    private static char[] validChars = new char[] {'_', '-', '.', ':'};

    private final static int TAG_MAX_LEN = 16;

    private final static int TANANT_MAX_LEN = 128;

    /**
     * 白名单的方式检查, 合法的参数只能包含字母、数字、以及validChars中的字符, 并且不能为空
     *
     * @param param
     * @return
     */
    public static boolean isValid(String param) {
        if (param == null) {
            return false;
        }
        int length = param.length();
        for (int i = 0; i < length; i++) {
            char ch = param.charAt(i);
            if (!Character.isLetterOrDigit(ch) && !isValidChar(ch)) {
                return false;
            }
        }
        return true;
    }

    private static boolean isValidChar(char ch) {
        for (char c : validChars) {
            if (c == ch) {
                return true;
            }
        }
        return false;
    }

    public static void checkParam(String dataId, String group, String datumId, String content) throws GlobalException {
        if (StringUtils.isBlank(dataId) || !ParamUtils.isValid(dataId.trim())) {
            throw new GlobalException(GlobalException.INVALID_PARAM, "invalid dataId");
        } else if (StringUtils.isBlank(group) || !ParamUtils.isValid(group)) {
            throw new GlobalException(GlobalException.INVALID_PARAM, "invalid group");
        } else if (StringUtils.isBlank(datumId) || !ParamUtils.isValid(datumId)) {
            throw new GlobalException(GlobalException.INVALID_PARAM, "invalid datumId");
        } else if (StringUtils.isBlank(content)) {
            throw new GlobalException(GlobalException.INVALID_PARAM, "content is blank");
        } else if (content.length() > PropertyUtil.getMaxContent()) {
            throw new GlobalException(GlobalException.INVALID_PARAM,
                "invalid content, over " + PropertyUtil.getMaxContent());
        }
    }

    public static void checkParam(String tag) {
        if (StringUtils.isNotBlank(tag)) {
            if (!ParamUtils.isValid(tag.trim())) {
                throw new IllegalArgumentException("invalid tag");
            }
            if (tag.length() > TAG_MAX_LEN) {
                throw new IllegalArgumentException("too long tag, over 16");
            }
        }
    }

    public static void checkTenant(String tenant) {
        if (StringUtils.isNotBlank(tenant)) {
            if (!ParamUtils.isValid(tenant.trim())) {
                throw new IllegalArgumentException("invalid tenant");
            }
            if (tenant.length() > TANANT_MAX_LEN) {
                throw new IllegalArgumentException("too long tag, over 128");
            }
        }
    }

    public static void checkParam(Map<String, Object> configAdvanceInfo) throws GlobalException {
        for (Map.Entry<String, Object> configAdvanceInfoTmp : configAdvanceInfo.entrySet()) {
            if ("config_tags".equals(configAdvanceInfoTmp.getKey())) {
                if (configAdvanceInfoTmp.getValue() != null) {
                    String[] tagArr = ((String)configAdvanceInfoTmp.getValue()).split(",");
                    if (tagArr.length > 5) {
                        throw new GlobalException(GlobalException.INVALID_PARAM, "too much config_tags, over 5");
                    }
                    for (String tag : tagArr) {
                        if (tag.length() > 64) {
                            throw new GlobalException(GlobalException.INVALID_PARAM, "too long tag, over 64");
                        }
                    }
                }
            } else if ("desc".equals(configAdvanceInfoTmp.getKey())) {
                if (configAdvanceInfoTmp.getValue() != null
                    && ((String)configAdvanceInfoTmp.getValue()).length() > 128) {
                    throw new GlobalException(GlobalException.INVALID_PARAM, "too long desc, over 128");
                }
            } else if ("use".equals(configAdvanceInfoTmp.getKey())) {
                if (configAdvanceInfoTmp.getValue() != null
                    && ((String)configAdvanceInfoTmp.getValue()).length() > 32) {
                    throw new GlobalException(GlobalException.INVALID_PARAM, "too long use, over 32");
                }
            } else if ("effect".equals(configAdvanceInfoTmp.getKey())) {
                if (configAdvanceInfoTmp.getValue() != null
                    && ((String)configAdvanceInfoTmp.getValue()).length() > 32) {
                    throw new GlobalException(GlobalException.INVALID_PARAM, "too long effect, over 32");
                }
            } else if ("type".equals(configAdvanceInfoTmp.getKey())) {
                if (configAdvanceInfoTmp.getValue() != null
                    && ((String)configAdvanceInfoTmp.getValue()).length() > 32) {
                    throw new GlobalException(GlobalException.INVALID_PARAM, "too long type, over 32");
                }
            } else if ("schema".equals(configAdvanceInfoTmp.getKey())) {
                if (configAdvanceInfoTmp.getValue() != null
                    && ((String)configAdvanceInfoTmp.getValue()).length() > 32768) {
                    throw new GlobalException(GlobalException.INVALID_PARAM, "too long schema, over 32768");
                }
            } else {
                throw new GlobalException(GlobalException.INVALID_PARAM, "invalid param");
            }
        }
    }

}
