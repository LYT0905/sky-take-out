package com.sky.mapper;

import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @author Mark
 * @date 2024/2/11
 */

@Mapper
public interface DishFlavorMapper {
    /**
     * 批量删除
     * @param dishIds
     */
    void deleteBatch(List<Integer> dishIds);
}
