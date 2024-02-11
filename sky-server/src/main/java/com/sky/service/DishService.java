package com.sky.service;

import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.result.PageResult;

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
}
