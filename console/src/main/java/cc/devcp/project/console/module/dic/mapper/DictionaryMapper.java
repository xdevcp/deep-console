package cc.devcp.project.console.module.dic.mapper;

import cc.devcp.project.console.module.dic.entity.DictionaryEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

import java.util.List;

/**
 * @author deep.wu
 * @version 1.0 on 2020/3/14
 */
public interface DictionaryMapper  extends BaseMapper<DictionaryEntity> {
    /**
     * 批量修改数据字典状态
     * @param idList
     * @param status
     * @return int
     * @date 2019/12/23 14:11
     */
    @Update({"<script>",
        "UPDATE data_dictionary SET `DIC_STATUS` = #{status} WHERE 1=1",
        " `DIC_VALUE_ID` IN ",
        "<foreach close=\")\" collection=\"items\" index=\"index\" item=\"item\" open=\"(\" separator=\",\">",
        "#{item}",
        "</foreach>",
        "</script>"})
    int batchUpdateStatus(@Param("items") List<Integer> items, @Param("status") Integer status);
}
