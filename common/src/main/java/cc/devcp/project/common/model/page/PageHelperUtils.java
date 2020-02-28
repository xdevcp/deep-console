package cc.devcp.project.common.model.page;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

/**
 * @version 1.0.0
 * @author: lzy
 * @date: 2019/12/12 17:30
 */
public class PageHelperUtils {

    public static void of(PageParam param) {
        PageHelper.startPage(param.getCurrent(), param.getSize());
    }

    public static PageResult to(PageInfo page) {
        return new PageResult(page.getPageNum(),
                page.getTotal(),
                page.getPageSize(),
                page.getSize(),
                page.getPages(),
                page.isIsFirstPage(),
                page.isIsLastPage(),
                page.getList());
    }

}
