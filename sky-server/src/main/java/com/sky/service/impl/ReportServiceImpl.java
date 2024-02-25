package com.sky.service.impl;


import com.sky.entity.Orders;
import com.sky.mapper.OrdersMapper;
import com.sky.mapper.ReportMapper;
import com.sky.mapper.UserMapper;
import com.sky.service.ReportService;
import com.sky.vo.TurnoverReportVO;
import com.sky.vo.UserReportVO;
import io.swagger.models.auth.In;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Mark
 * @date 2024/2/25
 */

@Service
public class ReportServiceImpl implements ReportService {

    @Autowired
    private ReportMapper reportMapper;

    @Autowired
    private OrdersMapper ordersMapper;

    @Autowired
    private UserMapper userMapper;
    /**
     * 获取统计营业额
     * @param begin
     * @param end
     * @return
     */
    @Override
    public TurnoverReportVO getTurnoverStatistics(LocalDate begin, LocalDate end) {
        List<LocalDate> dateList = new ArrayList<>();
        dateList.add(begin);
        while (!begin.equals(end)){
            begin = begin.plusDays(1);
            dateList.add(begin);
        }

        List<Double> turnoverList = new ArrayList<>();
        for (LocalDate dateTime : dateList) {
            LocalDateTime beginTime = LocalDateTime.of(dateTime, LocalTime.MIN);
            LocalDateTime endTime = LocalDateTime.of(dateTime, LocalTime.MAX);

            Map<String, Object> map = new HashMap<>();
            map.put("begin", beginTime);
            map.put("end", endTime);
            map.put("status", Orders.COMPLETED);

            Double sum = ordersMapper.sumByMap(map);
            sum = sum == null ? 0.0 : sum;
            turnoverList.add(sum);
        }

        return TurnoverReportVO.builder().
                dateList(StringUtils.join(dateList, ",")).
                turnoverList(StringUtils.join(turnoverList, ",")).
                build();
    }

    /**
     * 获得用户数量
     * @param begin
     * @param end
     * @return
     */
    @Override
    public UserReportVO getUserSratistics(LocalDate begin, LocalDate end) {
        List<LocalDate> dateList = new ArrayList<>();
        dateList.add(begin);
        while (!begin.equals(end)){
            begin = begin.plusDays(1);
            dateList.add(begin);
        }
        // 用户总数
        List<Integer> totalUserList = new ArrayList<>();
        // 新增人数
        List<Integer> newUserList = new ArrayList<>();

        for (LocalDate dateTime : dateList) {
            LocalDateTime beginTime = LocalDateTime.of(dateTime, LocalTime.MIN);
            LocalDateTime endTime = LocalDateTime.of(dateTime, LocalTime.MAX);

            Map<String, Object> map = new HashMap<>();
            map.put("end", endTime);

            Integer totalUser = userMapper.getUserByMap(map);

            map.put("begin", beginTime);
            Integer newUser = userMapper.getUserByMap(map);

            totalUserList.add(totalUser);
            newUserList.add(newUser);
        }

        return UserReportVO.builder().
                totalUserList(StringUtils.join(totalUserList, ",")).
                dateList(StringUtils.join(dateList, ",")).
                newUserList(StringUtils.join(newUserList, ",")).
                build();
    }
}
