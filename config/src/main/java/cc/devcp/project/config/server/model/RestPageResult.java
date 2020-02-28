package cc.devcp.project.config.server.model;

import java.io.Serializable;

/**
 * rest page result
 *
 * @param <T> data type
 * @author Nacos
 */
public class RestPageResult<T> implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = -8048577763828650575L;

    private int code;
    private String message;
    private int total;
    private int pageSize;
    private int currentPage;
    private T data;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public static long getSerialversionuid() {
        return serialVersionUID;
    }

}
