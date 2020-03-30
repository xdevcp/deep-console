package cc.devcp.project.common.enums;

/**
 * @version 1.0.0
 * @author: lzy
 * @date: 2019/5/10 13:42
 */
public interface CommEnum<T> {

    /**
     * Code可以是Int，String......(保存在数据库的值)
     *
     * @return
     */
    T getCode();

    /**
     * Content String（前端显示的值）
     *
     * @return
     */
    String getContent();

}
