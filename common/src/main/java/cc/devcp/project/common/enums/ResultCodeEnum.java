package cc.devcp.project.common.enums;

/**
 * @author deep.wu
 * @version 1.0 on 2019/12/19
 */
public enum ResultCodeEnum implements CommExEnum<Integer> {

    /**
     * common code
     **/
    SUCCESS(200, "处理成功"),
    ERROR(50000, "服务器内部错误"),

    /**
     * config use 40001 ~ 49999
     **/
    PARAM_DEL_ID_ERROR(40010, "经销商编号错误"),
    TOKEN_USER_INFO_ERROR(40020, "token中的用户信息错误"),
    TOKEN_INVALID(40030, "token无效"),
    TOKEN_DECRYPT(40040, "token解析失败"),
    COMPRESS_INVALID(40050, "压缩数据异常"),
    DECOMPRESS_INVALID(40060, "解压缩数据异常"),
    LEVEL_INVALID(40070, "用户等级无效"),
    ACCOUNT_SERVER_ERROR(40080, "用户服务不可用"),

    DATA_EMPTY(40110, "数据为空"),
    PARAM_VALIDATION_FAILED(40120, "参数校验不通过"),
    PARAM_ILLEGAL(40130, "参数不合法"),
    PARAM_EMPTY(40140, "参数为空"),
    DEALER_ID_ILLEGAL(40150, "dealerId 非法"),
    DEALER_ID_EMPTY(40160, "dealerId 为空"),
    FOLLOWER_EMPTY(40170, "实际处理人账号 为空"),
    CUSTOMER_RESERVATION_DATE_EMPTY(40180, "客户预约时间 为空"),
    CAMPAIGN_TYPE_NOT_SAME(40190, "线索活动类型不相同"),

    CONNECTED_ERROR(40210, "isConnected allowableValues = {Y,N}"),
    APPOINTMENT_TO_SHOP_DATE_EMPTY(40220, "是否预约到店 为空"),
    APPOINTMENT_TO_SHOP_DATE_ERROR(40230, "isAppointmentToShop allowableValues = {Y,N}"),

    EXCEL_EMPTY(40510, "excel为空"),

    /**
     * cause
     **/
    CAUSE_40150(DEALER_ID_ILLEGAL, "java.lang.IllegalStateException: no table route info"),

    ;

    private Integer code;

    private String content;

    private String desc;

    ResultCodeEnum(Integer code, String content) {
        this.code = code;
        this.content = content;
    }

    ResultCodeEnum(ResultCodeEnum resultCodeEnum, String desc) {
        this.code = resultCodeEnum.getCode();
        this.content = resultCodeEnum.getContent();
        this.desc = desc;
    }

    @Override
    public Integer getCode() {
        return code;
    }

    @Override
    public String getContent() {
        return content;
    }

    @Override
    public String getDesc() {
        return desc;
    }

}
