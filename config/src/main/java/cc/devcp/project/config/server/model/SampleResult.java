package cc.devcp.project.config.server.model;

import java.io.Serializable;
import java.util.Map;

/**
 * sample result
 *
 * @author Nacos
 */
public class SampleResult implements Serializable {

    /**
     * 随机数
     */
    private static final long serialVersionUID = 2587823382317389453L;

    private Map<String, String> lisentersGroupkeyStatus;

    public Map<String, String> getLisentersGroupkeyStatus() {
        return lisentersGroupkeyStatus;
    }

    public void setLisentersGroupkeyStatus(
        Map<String, String> lisentersGroupkeyStatus) {
        this.lisentersGroupkeyStatus = lisentersGroupkeyStatus;
    }

}
