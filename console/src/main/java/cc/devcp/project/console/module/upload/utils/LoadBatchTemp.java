package cc.devcp.project.console.module.upload.utils;

import cc.devcp.project.console.module.upload.entity.LoadBatchEntity;

/**
 * @author deep.wu
 * @version 1.0 on 2020/1/31 22:01.
 */
public class LoadBatchTemp {

    public static void set(LoadBatchEntity entity, Long numberAll, Long numberInvalid, Long numberValid, String status) {
        entity.setNumberAll(numberAll);
        entity.setNumberInvalid(numberInvalid);
        entity.setNumberValid(numberValid);
        entity.setStatus(status);
    }
}
