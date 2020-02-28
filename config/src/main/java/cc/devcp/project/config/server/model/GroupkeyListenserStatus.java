package cc.devcp.project.config.server.model;

import java.io.Serializable;
import java.util.Map;

/**
 * litener status
 *
 * @author Nacos
 */
public class GroupkeyListenserStatus implements Serializable {

    /**
     * 随机数
     */
    private static final long serialVersionUID = -2094829323598842474L;

    private int collectStatus;

    private Map<String, String> lisentersGroupkeyStatus;

    public int getCollectStatus() {
        return collectStatus;
    }

    public void setCollectStatus(int collectStatus) {
        this.collectStatus = collectStatus;
    }

    public Map<String, String> getLisentersGroupkeyStatus() {
        return lisentersGroupkeyStatus;
    }

    public void setLisentersGroupkeyStatus(
        Map<String, String> lisentersGroupkeyStatus) {
        this.lisentersGroupkeyStatus = lisentersGroupkeyStatus;
    }
}
