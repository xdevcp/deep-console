package cc.devcp.project.common.model.result;

import lombok.Data;

import java.util.List;
import java.util.Map;

/**
 * @author deep.wu
 * @version 1.0 on 2020-03-18 21:16.
 */
@Data
public class ResultData<T> {

    private long total;

    private List<T> list;

    private Map map;
}
