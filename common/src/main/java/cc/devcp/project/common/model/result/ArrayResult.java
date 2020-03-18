package cc.devcp.project.common.model.result;

import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author deep.wu
 * @version 1.0 on 2020-03-19
 */
public class ArrayResult implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "返回数据")
    private List result;

    public List getResult() {
        return result;
    }

    public void setResult(List result) {
        this.result = result;
    }

    /**
     * ok
     *
     * @param list
     * @return result
     */
    public static ArrayResult ok(List list) {
        ArrayResult result = new ArrayResult();
        if (list != null && list.size() > 0) {
            result.setResult(list);
        } else {
            result.setResult(new ArrayList<>());
        }
        return result;
    }

    /**
     * ok
     *
     * @return success
     */
    public static ArrayResult empty() {
        return ok(new ArrayList<>());
    }

    @Override
    public String toString() {
        return "ArrayResult{" + "result: " + result + '}';
    }
}
