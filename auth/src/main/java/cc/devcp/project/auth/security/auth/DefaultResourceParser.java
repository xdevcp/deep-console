package cc.devcp.project.auth.security.auth;

import org.apache.commons.lang3.StringUtils;

/**
 * Default resource parser
 *
 * @author nkorange
 * @since 1.2.0
 */
public class DefaultResourceParser implements ResourceParser {

    @Override
    public String parseName(Object request) {
        return StringUtils.EMPTY;
    }
}
