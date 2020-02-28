package cc.devcp.project.core.auth;

import org.apache.commons.lang3.StringUtils;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Annotation indicating that the annotated request should be authorized.
 *
 * @author nkorange
 * @since 1.2.0
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface Secured {

    /**
     * The action type of the request
     *
     * @return action type, default READ
     */
    ActionTypes action() default ActionTypes.READ;

    /**
     * The name of resource related to the request
     *
     * @return resource name
     */
    String resource() default StringUtils.EMPTY;

    /**
     * Resource name parser. Should have lower priority than name()
     *
     * @return class type of resource parser
     */
    Class<? extends ResourceParser> parser() default DefaultResourceParser.class;
}
