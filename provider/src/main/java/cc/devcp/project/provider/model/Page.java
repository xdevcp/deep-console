package cc.devcp.project.provider.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 分页对象
 *
 * @param <E>
 * @author boyan
 * @date 2010-5-6
 */
public class Page<E> implements Serializable {
    static final long serialVersionUID = -1L;
    /**
     * 总记录数
     */
    private int totalCount;
    /**
     * 页数
     */
    private int pageNumber;
    /**
     * 总页数
     */
    private int pagesAvailable;
    /**
     * 该页内容
     */
    private List<E> pageItems = new ArrayList<E>();

    public void setPageNumber(int pageNumber) {
        this.pageNumber = pageNumber;
    }

    public void setPagesAvailable(int pagesAvailable) {
        this.pagesAvailable = pagesAvailable;
    }

    public void setPageItems(List<E> pageItems) {
        this.pageItems = pageItems;
    }

    public int getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }

    public int getPageNumber() {
        return pageNumber;
    }

    public int getPagesAvailable() {
        return pagesAvailable;
    }

    public List<E> getPageItems() {
        return pageItems;
    }
}
