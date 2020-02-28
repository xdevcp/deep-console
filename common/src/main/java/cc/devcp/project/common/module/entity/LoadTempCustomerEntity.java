package cc.devcp.project.common.module.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
* <p>
*
* </p>
*
* @author deep
* @version 1.0 on 2020-01-31
*/
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("load_temp_customer")
@ApiModel(value="LoadTempCustomerEntity对象", description="")
public class LoadTempCustomerEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "ID")
    @TableId(value = "LTC_ID", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "批次表ID")
    @TableField("LTC_LB_ID")
    private Long lbId;

    @ApiModelProperty(value = "上传时间")
    @TableField("LTC_CREATE_DATE")
    private LocalDateTime createDate;

    @ApiModelProperty(value = "有效标识，Y/N")
    @TableField("LTC_VALID_FLAG")
    private String validFlag;

    @ApiModelProperty(value = "错误描述")
    @TableField("LTC_ERROR_MSG")
    private String errorMsg;

    @ApiModelProperty(value = "客户手机号")
    @TableField("LTC_CELLPHONE")
    private String cellphone;

    @ApiModelProperty(value = "客户标签")
    @TableField("LTC_CUSTOMER_LABEL")
    private String customerLabel;

    @ApiModelProperty(value = "标签代码")
    @TableField("LTC_TAG_CODE")
    private String tagCode;

    @ApiModelProperty(value = "关联表KEY")
    @TableField("LTC_REF_KEY")
    private String refKey;


    public static final String LTC_ID = "LTC_ID";
    public static final String LTC_LB_ID = "LTC_LB_ID";
    public static final String LTC_CREATE_DATE = "LTC_CREATE_DATE";
    public static final String LTC_VALID_FLAG = "LTC_VALID_FLAG";
    public static final String LTC_ERROR_MSG = "LTC_ERROR_MSG";
    public static final String LTC_CELLPHONE = "LTC_CELLPHONE";
    public static final String LTC_CUSTOMER_LABEL = "LTC_CUSTOMER_LABEL";
    public static final String LTC_TAG_CODE = "LTC_TAG_CODE";
    public static final String LTC_REF_KEY = "LTC_REF_KEY";
}
