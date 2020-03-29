package cc.devcp.project.provider.model.app;

import static cc.devcp.project.core.utils.SystemUtils.LOCAL_IP;

/**
 * app info
 *
 * @author Nacos
 */
public class ApplicationInfo {

    private static final long LOCK_EXPIRE_DURATION = 30 * 1000L;
    private static final long RECENTLY_DURATION = 24 * 60 * 60 * 1000L;

    private String appName;

    private boolean isDynamicCollectDisabled = false;

    private long lastSubscribeInfoCollectedTime = 0L;

    private String subInfoCollectLockOwner = null;

    private long subInfoCollectLockExpireTime = 0L;

    public ApplicationInfo(String appName) {
        this.appName = appName;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public boolean isDynamicCollectDisabled() {
        return isDynamicCollectDisabled;
    }

    public void setDynamicCollectDisabled(boolean isDynamicCollectDisabled) {
        this.isDynamicCollectDisabled = isDynamicCollectDisabled;
    }

    public long getLastSubscribeInfoCollectedTime() {
        return lastSubscribeInfoCollectedTime;
    }

    public void setLastSubscribeInfoCollectedTime(
        long lastSubscribeInfoCollectedTime) {
        this.lastSubscribeInfoCollectedTime = lastSubscribeInfoCollectedTime;
    }

    public String getSubInfoCollectLockOwner() {
        return subInfoCollectLockOwner;
    }

    public void setSubInfoCollectLockOwner(String subInfoCollectLockOwner) {
        this.subInfoCollectLockOwner = subInfoCollectLockOwner;
    }

    public long getSubInfoCollectLockExpireTime() {
        return subInfoCollectLockExpireTime;
    }

    public void setSubInfoCollectLockExpireTime(
        long subInfoCollectLockExpireTime) {
        this.subInfoCollectLockExpireTime = subInfoCollectLockExpireTime;
    }

    public boolean isSubInfoRecentlyCollected() {
        if (System.currentTimeMillis() - this.lastSubscribeInfoCollectedTime < RECENTLY_DURATION) {
            return true;
        }
        return false;
    }

    public boolean canCurrentServerOwnTheLock() {
        boolean currentOwnerIsMe = subInfoCollectLockOwner == null || LOCAL_IP
            .equals(subInfoCollectLockOwner);

        if (currentOwnerIsMe) {
            return true;
        }
        if (System.currentTimeMillis() - this.subInfoCollectLockExpireTime > LOCK_EXPIRE_DURATION) {
            return true;
        }

        return false;
    }

    public String currentServer() {
        return LOCAL_IP;
    }

}
