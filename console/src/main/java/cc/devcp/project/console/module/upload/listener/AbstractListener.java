package cc.devcp.project.console.module.upload.listener;

import com.alibaba.excel.event.AnalysisEventListener;

import java.util.ArrayList;
import java.util.List;

/**
 * @version 1.0.0
 * @author: lzy
 * @date: 2020/1/8 11:52
 */
public abstract class AbstractListener<S, T> extends AnalysisEventListener<S> {

    protected final static int batchSize = 1000;
    protected int insertSize = 0;
    protected List<T> dataList =  new ArrayList<>();

}
