package cc.devcp.project.config.server.model;

/**
 * beta Info
 *
 * @author Nacos
 */
public class ConfigInfo4Beta extends ConfigInfo {

    /**
     *
     */
    private static final long serialVersionUID = 296578467953931353L;

    private String betaIps;

    public ConfigInfo4Beta() {
    }

    public ConfigInfo4Beta(String dataId, String group, String appName, String content, String betaIps) {
        super(dataId, group, appName, content);
        this.betaIps = betaIps;
    }

    public String getBetaIps() {
        return betaIps;
    }

    public void setBetaIps(String betaIps) {
        this.betaIps = betaIps;
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        return super.equals(obj);
    }
}
