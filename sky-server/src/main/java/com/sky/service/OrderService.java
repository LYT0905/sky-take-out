package com.sky.service;

import com.sky.dto.OrdersSubmitDTO;
import com.sky.vo.OrderSubmitVO;

/**
 * @author Mark
 * @date 2024/2/17
 */
public interface OrderService {
    /**
     * 用户下单
     * @param orders
     * @return
     */
    OrderSubmitVO submitOrder(OrdersSubmitDTO orders);
}
