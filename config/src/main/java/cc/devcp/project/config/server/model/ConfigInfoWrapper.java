package cc.devcp.project.config.server.model;

/**
 * ConfigInfo Wrapper
 *
 * @author Nacos
 */
public class ConfigInfoWrapper extends ConfigInfo {
    private static final long serialVersionUID = 4511997359365712505L;

    private long lastModified;

    public ConfigInfoWrapper() {
    }

    public long getLastModified() {
        return lastModified;
    }

    public void setLastModified(long lastModified) {
        this.lastModified = lastModified;
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
