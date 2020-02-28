package cc.devcp.project.core.auth;


/**
 * Resource parser
 *
 * @author nkorange
 * @since 1.2.0
 */
public interface ResourceParser {

    /**
     * Parse a unique name of the resource from the request
     *
     * @param request where we can find the resource info. Given it may vary from Http request to gRPC request,
     *                we use a Object type for future accommodation.
     * @return resource name
     */
    String parseName(Object request);
}
