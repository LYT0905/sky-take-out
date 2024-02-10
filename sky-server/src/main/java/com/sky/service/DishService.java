package com.sky.service;

import com.sky.dto.DishDTO;

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
}
