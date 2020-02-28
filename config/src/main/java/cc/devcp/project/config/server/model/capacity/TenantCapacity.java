package cc.devcp.project.config.server.model.capacity;

/**
 * Tenant Capacity
 *
 * @author hexu.hxy
 * @date 2018/3/13
 */
public class TenantCapacity extends Capacity {
    private String tenant;

    public String getTenant() {
        return tenant;
    }

    public void setTenant(String tenant) {
        this.tenant = tenant;
    }
}
