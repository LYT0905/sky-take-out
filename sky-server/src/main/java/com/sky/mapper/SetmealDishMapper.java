package com.sky.mapper;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @author Mark
 * @date 2024/2/11
 */
@Mapper
public interface SetmealDishMapper {
    /**
     * 根据菜品id查询套餐
     * @param dishIds
     * @return
     */
    List<Long> getSetmealsByDishIds(List<Long> dishIds);

    /**
     * 根据套餐id删除套餐和菜品的关联关系
     * @param ids
     */
    void deleteBatch(List<Long> ids);
}
