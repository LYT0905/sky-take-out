package com.sky.mapper;

import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @author Mark
 * @date 2024/2/11
 */
@Mapper
public interface SetmealDishMapper {
    List<Long> getSetmealsByDishIds(List<Integer> dishIds);
}
