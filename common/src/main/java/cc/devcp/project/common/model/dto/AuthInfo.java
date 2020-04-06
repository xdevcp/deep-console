package cc.devcp.project.common.model.dto;

import lombok.Data;

/**
 * @author deep.wu
 * @version 1.0 on 2020-04-06 17:12.
 */
@Data
public class AuthInfo {

    /**
     * Unique string representing user
     */
    private String account;

    private String accountName;

}
