package com.sky.mapper;

import com.sky.entity.Orders;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author Mark
 * @date 2024/2/17
 */

@Mapper
public interface OrdersMapper {
    /**
     * 插入订单数据
     * @param orders
     */
    void insert(Orders orders);
}
