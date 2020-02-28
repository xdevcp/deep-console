package cc.devcp.project.config.server.model.capacity;

import java.sql.Timestamp;

/**
 * Capacity
 *
 * @author hexu.hxy
 * @date 2018/3/13
 */
public class Capacity {
    private Long id;
    private Integer quota;
    private Integer usage;
    private Integer maxSize;
    private Integer maxAggrCount;
    private Integer maxAggrSize;
    private Timestamp gmtCreate;
    private Timestamp gmtModified;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getQuota() {
        return quota;
    }

    public void setQuota(Integer quota) {
        this.quota = quota;
    }

    public Integer getUsage() {
        return usage;
    }

    public void setUsage(Integer usage) {
        this.usage = usage;
    }

    public Integer getMaxSize() {
        return maxSize;
    }

    public void setMaxSize(Integer maxSize) {
        this.maxSize = maxSize;
    }

    public Integer getMaxAggrCount() {
        return maxAggrCount;
    }

    public void setMaxAggrCount(Integer maxAggrCount) {
        this.maxAggrCount = maxAggrCount;
    }

    public Integer getMaxAggrSize() {
        return maxAggrSize;
    }

    public void setMaxAggrSize(Integer maxAggrSize) {
        this.maxAggrSize = maxAggrSize;
    }

    public Timestamp getGmtCreate() {
        if (gmtCreate == null) {
            return null;
        }
        return new Timestamp(gmtCreate.getTime());
    }

    public void setGmtCreate(Timestamp gmtCreate) {
        if (gmtCreate == null) {
            this.gmtCreate = null;
        } else {
            this.gmtCreate = new Timestamp(gmtCreate.getTime());
        }

    }

    public Timestamp getGmtModified() {
        if (gmtModified == null) {
            return null;
        }
        return new Timestamp(gmtModified.getTime());
    }

    public void setGmtModified(Timestamp gmtModified) {
        if (gmtModified == null) {
            this.gmtModified = null;
        } else {
            this.gmtModified = new Timestamp(gmtModified.getTime());
        }
    }
}
