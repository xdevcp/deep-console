package cc.devcp.project.common.model.param;

import lombok.Data;

/**
 * @author deep.wu
 * @version 1.0 on 2020
 */
@Data
public class CurrentRequest {

    /**
     * 操作类型
     */
    private String optType;

    /**
     * 用户账号
     */
    private String account;
}
