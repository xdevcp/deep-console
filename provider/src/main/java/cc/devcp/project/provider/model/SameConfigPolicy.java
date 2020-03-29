package cc.devcp.project.provider.model;

/**
 * @author klw
 * @ClassName: SameConfigPolicy
 * @Description: processing policy of the same configuration
 * @date 2019/5/21 10:55
 */
public enum SameConfigPolicy {

    /**
     * @Description: abort import  on duplicate
     */
    ABORT,

    /**
     * @Description: skipping on duplicate
     */
    SKIP,

    /**
     * @Description: overwrite on duplicate
     */
    OVERWRITE

}
