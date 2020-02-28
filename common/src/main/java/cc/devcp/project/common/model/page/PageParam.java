package cc.devcp.project.common.model.page;

import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

/**
 * @version 1.0.0
 * @author: lzy
 * @date: 2019/11/14 16:05
 */
public class PageParam implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "当前页", example = "1")
    private Integer current;

    @ApiModelProperty(value = "当前页条数", example = "10")
    private Integer size;

    @ApiModelProperty(value = "正序排列字段，分隔符,", example = "COU_ID")
    private String orderByAsc;

    @ApiModelProperty(value = "倒序排列字段，分隔符,", example = "COU_ID")
    private String orderByDesc;

    @Deprecated
    @ApiModelProperty(value = "正序排列字段，分隔符,", example = "COU_ID")
    private String orderType;

    public PageParam() {
    }

    public PageParam(int current, int size) {
        this.current = current;
        this.size = size;
    }

    public static PageParam of(int current, int size) {
        return new PageParam(current, size);
    }

    public Integer getCurrent() {
        return null == current ? 1 : current;
    }

    public void setCurrent(Integer current) {
        this.current = current;
    }

    public Integer getSize() {
        return null == size ? 10 : size;
    }

    public void setSize(Integer size) {
        this.size = size;
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public String getOrderByAsc() {
        return orderByAsc;
    }

    public void setOrderByAsc(String orderByAsc) {
        this.orderByAsc = orderByAsc;
    }

    public String getOrderByDesc() {
        return orderByDesc;
    }

    public void setOrderByDesc(String orderByDesc) {
        this.orderByDesc = orderByDesc;
    }

    @Deprecated
    public String getOrderType() {
        return null == orderType ? "DESC" : orderType;
    }

    @Deprecated
    public void setOrderType(String orderType) {
        this.orderType = orderType;
    }

    @Override
    public String toString() {
        return "PageParam{" +
                "current=" + current +
                ", size=" + size +
                '}';
    }

}
