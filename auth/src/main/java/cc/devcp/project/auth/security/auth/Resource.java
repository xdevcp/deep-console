package cc.devcp.project.auth.security.auth;


import com.alibaba.fastjson.JSON;

/**
 * Resource used in authorization.
 *
 * @author nkorange
 * @since 1.2.0
 */
public class Resource {

    public static final String SPLITTER = ":";
    public static final String ANY = "*";

    /**
     * The unique key of resource.
     */
    private String key;

    public Resource(String key) {
        this.key = key;
    }

    public String getKey() {
        return key;
    }

    public String parseName() {
        return key.substring(0, key.lastIndexOf(SPLITTER));
    }

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }
}
