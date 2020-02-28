package cc.devcp.project.config.server.constant;

/**
 * counter mode
 *
 * @author hexu.hxy
 * @date 2018/3/13
 */
public enum CounterMode {
    /**
     * 增加
     */
    INCREMENT,
    /**
     * 减少
     */
    DECREMENT;

    public CounterMode reverse() {
        if (INCREMENT == this) {
            return DECREMENT;
        }
        return INCREMENT;
    }
}
