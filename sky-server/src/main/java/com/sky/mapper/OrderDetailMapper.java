package com.sky.mapper;

import com.sky.entity.OrderDetail;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @author Mark
 * @date 2024/2/17
 */

@Mapper
public interface OrderDetailMapper {
    /**
     * 批量插入
     * @param orderDetailList
     */
    void insertBatch(List<OrderDetail> orderDetailList);
}
