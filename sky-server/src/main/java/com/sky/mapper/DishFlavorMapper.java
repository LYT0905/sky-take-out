package com.sky.mapper;

import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @author Mark
 * @date 2024/2/11
 */

@Mapper
public interface DishFlavorMapper {
    void deleteBatch(List<Integer> dishIds);
}
