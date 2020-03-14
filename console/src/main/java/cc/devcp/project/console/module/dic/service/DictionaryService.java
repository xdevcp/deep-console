package cc.devcp.project.console.module.dic.service;

import cc.devcp.project.common.exception.CommRuntimeException;
import cc.devcp.project.common.model.page.PageParam;
import cc.devcp.project.common.model.page.PageResult;
import cc.devcp.project.common.model.response.ResEntity;
import cc.devcp.project.console.module.dic.entity.DBEnum;
import cc.devcp.project.console.module.dic.entity.DicEnum;
import cc.devcp.project.console.module.dic.entity.DictionaryEntity;
import cc.devcp.project.console.module.dic.mapper.DictionaryMapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Description 数据字典业务层
 * @Author DING DONG
 * @Date 2019/12/13 11:26
 * @Version 1.0
 */
@Service
public class DictionaryService {

    @Autowired
    private DictionaryMapper dictionaryMapper;

    /**
     * @param
     * @return ResEntity
     * @desc: 分页查询字典类型
     * @date 2019/12/17 19:16
     */
    public ResEntity<PageResult<DictionaryEntity>> queryDictionary(PageParam param, Integer parentId) {
        QueryWrapper<DictionaryEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("DIC_PARENT_ID", parentId)
                .eq("DIC_STATUS", DBEnum.VALID.getCode())
                .orderByAsc("DIC_SORT_NO")
                .orderByDesc("DIC_UPDATE_TIME");

        PageInfo<DictionaryEntity> pageInfo = PageHelper.startPage(param.getCurrent(), param.getSize()).doSelectPageInfo(() -> {
            dictionaryMapper.selectList(queryWrapper);
        });

        return ResEntity.ok(pageInfo);
    }

    /**
     * @param dictionaryEntity
     * @return ResEntity
     * @desc: 创建数据字典
     * @date 2019/12/17 19:18
     */
    @Transactional(rollbackFor = CommRuntimeException.class)
    public ResEntity createDictionary(DictionaryEntity dictionaryEntity) {
        int res = 0;
        QueryWrapper<DictionaryEntity> queryWrapper = new QueryWrapper<>();
        if (dictionaryEntity.getParentId() == 0) {
            // 校验字典类型是否重复
            queryWrapper.eq("DIC_DATA_TYPE", dictionaryEntity.getDataType())
                    .eq("DIC_PARENT_ID", dictionaryEntity.getParentId());
            if (dictionaryMapper.selectOne(queryWrapper) != null) {
                return ResEntity.fail(DicEnum.DIC_TYPE_EXITS);
            }
        } else {
            if(StringUtils.isBlank(dictionaryEntity.getDataCode())){
                return ResEntity.fail(DicEnum.DIC_CODE_CANNOT_BE_NULL);
            }
            // 校验数据编码是否重复
            queryWrapper.eq("DIC_DATA_CODE", dictionaryEntity.getDataCode())
                    .eq("DIC_PARENT_ID", dictionaryEntity.getParentId());
            if (dictionaryMapper.selectOne(queryWrapper) != null) {
                return ResEntity.fail(DicEnum.DIC_CODE_EXITS);
            }
        }
        dictionaryEntity.setStatus(DBEnum.VALID.getCode());
        dictionaryEntity.setUpdateTime(LocalDateTime.now());
        res = dictionaryMapper.insert(dictionaryEntity);
        if (res != 1) {
            throw new CommRuntimeException(DicEnum.DIC_CREATE_ERROR);
        }
        DictionaryEntity updateEntity = new DictionaryEntity();
        updateEntity.setId(dictionaryEntity.getId());
        updateEntity.setValueId(dictionaryEntity.getId());
        res = dictionaryMapper.updateById(updateEntity);
        if (res != 1) {
            throw new CommRuntimeException(DicEnum.DIC_CREATE_ERROR);
        }
        return ResEntity.ok();
    }

    /**
     * @param dictionaryEntity
     * @return ResEntity
     * @desc: 修改数据字典
     * @date 2019/12/18 14:05
     */
    @Transactional(rollbackFor = CommRuntimeException.class)
    public ResEntity modifyDictionary(DictionaryEntity dictionaryEntity) {
        int res = 0;
        QueryWrapper<DictionaryEntity> queryWrapper = new QueryWrapper<>();
        if (dictionaryEntity.getParentId() == 0) {
            if(StringUtils.isBlank(dictionaryEntity.getDataType())){
                return ResEntity.fail(DicEnum.DIC_TYPE_CANNOT_BE_NULL);
            }
            // 校验字典类型是否存在不包含本身
            queryWrapper.eq("DIC_DATA_TYPE", dictionaryEntity.getDataType())
                    .eq("DIC_PARENT_ID", dictionaryEntity.getParentId())
                    .ne("DIC_ID", dictionaryEntity.getId());
            if (dictionaryMapper.selectOne(queryWrapper) != null) {
                throw new CommRuntimeException(DicEnum.DIC_TYPE_EXITS);
            }
            // 如果更改了数据类型 同步更新所有子集数据类型
            DictionaryEntity resEntity = dictionaryMapper.selectById(dictionaryEntity.getId());
            if (!resEntity.getDataType().equals(dictionaryEntity.getDataType())) {
                // 修改的值
                DictionaryEntity updateEntity = new DictionaryEntity();
                updateEntity.setDataType(dictionaryEntity.getDataType());
                // 修改条件
                UpdateWrapper<DictionaryEntity> updateWrapper = new UpdateWrapper<>();
                updateWrapper.eq("DIC_DATA_TYPE", resEntity.getDataType());
                // 批量更新
                dictionaryMapper.update(updateEntity, updateWrapper);
            }
            // 更新字典类型
            dictionaryEntity.setUpdateTime(LocalDateTime.now());
            res = dictionaryMapper.updateById(dictionaryEntity);
            if (res != 1) {
                throw new CommRuntimeException(DicEnum.DIC_MODIFY_ERROR);
            }
        } else {
            if(StringUtils.isBlank(dictionaryEntity.getDataCode())){
                return ResEntity.fail(DicEnum.DIC_CODE_CANNOT_BE_NULL);
            }
            // 校验数据编码是否存在不包含本身
            queryWrapper.eq("DIC_DATA_CODE", dictionaryEntity.getDataCode())
                    .eq("DIC_PARENT_ID", dictionaryEntity.getParentId())
                    .ne("DIC_ID", dictionaryEntity.getId());
            if (dictionaryMapper.selectOne(queryWrapper) != null) {
                throw new CommRuntimeException(DicEnum.DIC_CODE_EXITS);
            }
            dictionaryEntity.setUpdateTime(LocalDateTime.now());
            res = dictionaryMapper.updateById(dictionaryEntity);
            if (res != 1) {
                throw new CommRuntimeException(DicEnum.DIC_MODIFY_ERROR);
            }
        }
        return ResEntity.ok();
    }

    /**
     * @param childId
     * @return ResEntity<Map<String,Object>>
     * @desc: 查找父级id以及父级的值
     * @date 2019/12/19 19:31
     */
    public ResEntity<Map<String,Object>> queryParentId(String childId) {
        Map<String,Object> map = new HashMap<>(16);

        QueryWrapper<DictionaryEntity> queryUpLevelWrapper = new QueryWrapper<>();
        queryUpLevelWrapper.eq("DIC_VALUE_ID", childId);

        DictionaryEntity upEntity = dictionaryMapper.selectOne(queryUpLevelWrapper);
        if (upEntity == null) {
            throw new CommRuntimeException(DicEnum.DIC_PARENT_NOT_EXITS);
        }
        if (upEntity.getParentId() == null) {
            throw new CommRuntimeException(DicEnum.DIC_PARENT_NOT_EXITS);
        }
        map.put("pid",upEntity.getParentId());

        // 如果不是字典类型 需要查找父级字典的值
        if(upEntity.getParentId() != 0){
            QueryWrapper<DictionaryEntity> queryDicValueWrapper = new QueryWrapper<>();
            queryDicValueWrapper.eq("DIC_VALUE_ID", upEntity.getParentId());

            DictionaryEntity entity = dictionaryMapper.selectOne(queryDicValueWrapper);
            if (entity == null) {
                throw new CommRuntimeException(DicEnum.DIC_PARENT_NOT_EXITS);
            }
            map.put("dataValue", entity.getDataValue());
        }
        return ResEntity.ok(map);
    }

    /**
     * @param ids
     * @return ResEntity
     * @desc: 批量删除数据字典(逻辑删除)
     * @date 2019/12/23 15:04
     */
    @Transactional(rollbackFor = CommRuntimeException.class)
    public ResEntity removeDictionary(String[] ids) {
        List<Integer> idList = new ArrayList<>();
        for (String s : ids) {
            // 子节点集合
            List<Integer> childList = new ArrayList<>();
            Integer id = Integer.parseInt(s);
            // 调用递归方法
            List<Integer> list = getChildList(childList, id);
            idList.add(id);
            idList.addAll(list);
        }
        int res = dictionaryMapper.batchUpdateStatus(idList, DBEnum.INVALID.getCode());
        if (res != idList.size()) {
            throw new CommRuntimeException(DicEnum.DIC_REMOVE_ERROR);
        }
        return ResEntity.ok();
    }

    /**
     * @param childList
     * @param id
     * @return List<Integer>
     * @desc: 递归查询子节点
     * @date 2019/12/21 14:54
     */
    private List<Integer> getChildList(List<Integer> childList, Integer id) {
        //遍历出父id等于参数的id，add进子节点集合
        QueryWrapper<DictionaryEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("DIC_PARENT_ID", id);
        List<DictionaryEntity> list = dictionaryMapper.selectList(queryWrapper);
        if (list.size() > 0) {
            for (DictionaryEntity entity : list) {
                childList.add(entity.getValueId());
                // 递归遍历下一层
                getChildList(childList, entity.getValueId());
            }
        }
        return childList;
    }

    /**
     * @param dicType
     * @return ResEntity
     * @desc: 对外查询数据字典
     * @date 2019/12/17 19:16
     */
    public ResEntity<List<DictionaryEntity>> findDictionaryByType(String dicType) {

        QueryWrapper<DictionaryEntity> queryDicTypeWrapper = new QueryWrapper<>();
        queryDicTypeWrapper.eq("DIC_DATA_TYPE", dicType)
                .eq("DIC_PARENT_ID", 0)
                .eq("DIC_STATUS", DBEnum.VALID.getCode());
        DictionaryEntity dicTypeEntity = dictionaryMapper.selectOne(queryDicTypeWrapper);
        if (dicTypeEntity == null) {
            throw new CommRuntimeException(DicEnum.DIC_TYPE_NOT_EXITS);
        }

        QueryWrapper<DictionaryEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("DIC_PARENT_ID", dicTypeEntity.getValueId())
                .eq("DIC_STATUS", DBEnum.VALID.getCode())
                .orderByAsc("DIC_SORT_NO")
                .orderByDesc("DIC_UPDATE_TIME");
        List<DictionaryEntity> list = dictionaryMapper.selectList(queryWrapper);

        return ResEntity.ok(list);
    }
}



