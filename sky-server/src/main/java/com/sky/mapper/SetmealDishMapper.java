package com.sky.mapper;

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
}
