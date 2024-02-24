package com.sky.task;

import com.sky.entity.Orders;
import com.sky.mapper.OrdersMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author Mark
 * @date 2024/2/24
 */

/**
 * 定时任务触发
 */
@Component
@Slf4j
public class OrderTask {
    @Autowired
    private OrdersMapper ordersMapper;

    /**
     * 处理超时订单(每分钟触发一次)
     */
    @Scheduled(cron = "0 * * * * ? ")
    public void processTimeoutOrder(){

        LocalDateTime time = LocalDateTime.now().plusMinutes(-15);

        List<Orders> orders = ordersMapper.getByStatusAndOrderTimeLT(Orders.PENDING_PAYMENT, time);

        if(orders != null && orders.size() > 0){
            for (Orders order : orders) {
                order.setStatus(Orders.CANCELLED);
                order.setCancelReason("订单超时未支付，已取消");
                order.setCancelTime(LocalDateTime.now());
                ordersMapper.update(order);
            }
        }
    }


    /**
     * 处理一直处于派送中状态的订单(每天凌晨一点触发)
     */
    @Scheduled(cron = "0 0 1 * * ?")
    public void processDeliveryOrder(){
        LocalDateTime time = LocalDateTime.now().plusMinutes(-60);

        List<Orders> ordersList = ordersMapper.getByStatusAndOrderTimeLT(Orders.DELIVERY_IN_PROGRESS, time);

        if(ordersList != null && ordersList.size() > 0){
            for (Orders orders : ordersList) {
                orders.setStatus(Orders.COMPLETED);
                ordersMapper.update(orders);
            }
        }
    }
}
