package cc.devcp.project.common.model.page;

import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.util.List;

/**
 * @version 1.0.0
 * @author: lzy
 * @date: 2019/11/27 17:29
 */
public class PageResult<T> implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "当前页", example = "5")
    private int current;
    @ApiModelProperty(value = "总数", example = "41")
    private long total;
    @ApiModelProperty(value = "分页条数", example = "10")
    private int pageSize;
    @ApiModelProperty(value = "当前页实际条数", example = "10")
    private int currentSize;
    @ApiModelProperty(value = "总页数", example = "5")
    private int totalPage;
    @ApiModelProperty(value = "是否是第一页", example = "false")
    private boolean first;
    @ApiModelProperty(value = "是否是最后一页", example = "true")
    private boolean last;
    @ApiModelProperty(value = "数据集合")
    private List<T> list;

    public PageResult(){
    }

    public PageResult(int current, long total, int pageSize, int currentSieze, int totalPage, boolean first, boolean last, List<T> list) {
        this.current = current;
        this.total = total;
        this.pageSize = pageSize;
        this.currentSize = currentSieze;
        this.totalPage = totalPage;
        this.first = first;
        this.last = last;
        this.list = list;
    }

    public int getCurrent() {
        return current;
    }

    public void setCurrent(int current) {
        this.current = current;
    }

    public long getTotal() {
        return total;
    }

    public void setTotal(long total) {
        this.total = total;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getCurrentSize() {
        return currentSize;
    }

    public void setCurrentSieze(int currentSize) {
        this.currentSize = currentSize;
    }

    public int getTotalPage() {
        return totalPage;
    }

    public void setTotalPage(int totalPage) {
        this.totalPage = totalPage;
    }

    public boolean isFirst() {
        return first;
    }

    public void setFirst(boolean first) {
        this.first = first;
    }

    public boolean isLast() {
        return last;
    }

    public void setLast(boolean last) {
        this.last = last;
    }

    public List<T> getList() {
        return list;
    }

    public void setList(List<T> list) {
        this.list = list;
    }
}
