package cc.devcp.project.console.module.upload.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

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
@TableName("load_data_batch")
@ApiModel(value="LoadBatchEntity对象", description="")
public class LoadDataBatchEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "id")
    @TableId(value = "LB_ID", type = IdType.ASSIGN_ID)
    private Long id;

    @ApiModelProperty(value = "创建人")
    @TableField("LB_CREATE_USER")
    private String createUser;

    @ApiModelProperty(value = "创建时间")
    @TableField("LB_CREATE_DATE")
    private LocalDateTime createDate;

    @ApiModelProperty(value = "客户标签/车辆标签/人车关系/自定义线索")
    @TableField("LB_TYPE")
    private String type;

    @TableField("LB_NUMBER_ALL")
    private Long numberAll;

    @TableField("LB_NUMBER_VALID")
    private Long numberValid;

    @TableField("LB_NUMBER_INVALID")
    private Long numberInvalid;

    @ApiModelProperty(value = "处理状态")
    @TableField("LB_STATUS")
    private String status;

    @ApiModelProperty(value = "耗时/s")
    @TableField("LB_SPEND_TIME")
    private Long spendTime;

    @TableField("LB_UPLOAD_FILE_NAME")
    private String uploadFileName;

    @TableField("LB_DOWNLOAD_FILE_NAME")
    private String downloadFileName;


    public static final String LB_ID = "LB_ID";
    public static final String LB_CREATE_USER = "LB_CREATE_USER";
    public static final String LB_CREATE_DATE = "LB_CREATE_DATE";
    public static final String LB_TYPE = "LB_TYPE";
    public static final String LB_NUMBER_ALL = "LB_NUMBER_ALL";
    public static final String LB_NUMBER_VALID = "LB_NUMBER_VALID";
    public static final String LB_NUMBER_INVALID = "LB_NUMBER_INVALID";
    public static final String LB_STATUS = "LB_STATUS";
    public static final String LB_SPEND_TIME = "LB_SPEND_TIME";
    public static final String LB_UPLOAD_FILE_NAME = "LB_UPLOAD_FILE_NAME";
    public static final String LB_DOWNLOAD_FILE_NAME = "LB_DOWNLOAD_FILE_NAME";

    @TableField(exist = false)
    private String uploadUrl;

    @TableField(exist = false)
    private String downloadUrl;

    @TableField(exist = false)
    private String result;
}
