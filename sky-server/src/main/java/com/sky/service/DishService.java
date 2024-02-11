package com.sky.service;

import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.result.PageResult;

import java.util.List;

/**
 * @author Mark
 * @date 2024/2/10
 */
public interface DishService {
    /**
     * 新增菜品
     * @param dishDTO
     */
    void saveWithFlavor(DishDTO dishDTO);

    /**
     * 菜品分页功能
     * @param queryDTO
     * @return
     */
    PageResult page(DishPageQueryDTO queryDTO);

    /**
     * 菜品批量删除
     * @param ids
     */
    void deleteBatch(List<Integer> ids);
}
