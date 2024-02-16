package com.sky.service;

import com.sky.dto.ShoppingCartDTO;

/**
 * @author Mark
 * @date 2024/2/16
 */
public interface ShoppingCartService {
    /**
     * 添加购物车
     * @param shoppingCartDTO
     */
    void addShoppingCart(ShoppingCartDTO shoppingCartDTO);
}
