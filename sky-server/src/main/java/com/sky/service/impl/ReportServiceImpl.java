package com.sky.service.impl;


import com.sky.dto.GoodsSalesDTO;
import com.sky.entity.Orders;
import com.sky.mapper.OrdersMapper;
import com.sky.mapper.ReportMapper;
import com.sky.mapper.UserMapper;
import com.sky.service.ReportService;
import com.sky.service.WorkspaceService;
import com.sky.vo.*;
import io.swagger.models.auth.In;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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

    @Autowired
    private WorkspaceService workspaceService;
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

    /**
     * 获取订单数据
     * @param begin
     * @param end
     * @return
     */
    @Override
    public OrderReportVO getOrderStatistics(LocalDate begin, LocalDate end) {
        List<LocalDate> dateList = new ArrayList<>();
        dateList.add(begin);
        while (!begin.equals(end)){
            begin = begin.plusDays(1);
            dateList.add(begin);
        }
        // 存放每天的订单总数
        List<Integer> orderCountList = new ArrayList<>();
        // 存放每天的有效订单数
        List<Integer> validOrderCountList = new ArrayList<>();

        for (LocalDate date : dateList) {
            LocalDateTime beginTime = LocalDateTime.of(date, LocalTime.MIN);
            LocalDateTime endTime = LocalDateTime.of(date, LocalTime.MAX);

            Map<String, Object> map = new HashMap<>();
            map.put("begin", beginTime);
            map.put("end", endTime);
            Integer orderCount = ordersMapper.countByMap(map);
            map.put("status", Orders.COMPLETED);
            Integer validOrderCount = ordersMapper.countByMap(map);

            orderCountList.add(orderCount);
            validOrderCountList.add(validOrderCount);
        }

        Integer totalOrderCount = orderCountList.stream().reduce(Integer::sum).get();
        Integer validOrderCount = validOrderCountList.stream().reduce(Integer::sum).get();

        Double orderCompletionRate = 0.0;
        if(totalOrderCount != 0){
            //计算订单完成率
            orderCompletionRate = validOrderCount.doubleValue() / totalOrderCount;
        }

        return OrderReportVO.builder()
                .dateList(org.apache.commons.lang3.StringUtils.join(dateList,","))
                .orderCountList(org.apache.commons.lang3.StringUtils.join(orderCountList,","))
                .validOrderCountList(org.apache.commons.lang3.StringUtils.join(validOrderCountList,","))
                .totalOrderCount(totalOrderCount)
                .validOrderCount(validOrderCount)
                .orderCompletionRate(orderCompletionRate)
                .build();
    }

    /**
     * 获取销量排名前十的菜品
     * @param begin
     * @param end
     * @return
     */
    @Override
    public SalesTop10ReportVO getSalesTop10(LocalDate begin, LocalDate end) {
        LocalDateTime beginTime = LocalDateTime.of(begin, LocalTime.MIN);
        LocalDateTime endTime = LocalDateTime.of(end, LocalTime.MAX);

        List<GoodsSalesDTO> salesTop10 = ordersMapper.getSalesTop10(beginTime, endTime);
        List<String> names = salesTop10.stream().map(GoodsSalesDTO::getName).collect(Collectors.toList());
        List<Integer> numbers = salesTop10.stream().map(GoodsSalesDTO::getNumber).collect(Collectors.toList());

        return SalesTop10ReportVO.builder().
                nameList(StringUtils.join(names, ",")).
                numberList(StringUtils.join(numbers, ",")).build();
    }

    /**
     * 导出数据
     * @param response
     */
    @Override
    public void exportData(HttpServletResponse response) throws Exception {
        //1. 查询数据库，获取营业数据---查询最近30天的运营数据
        LocalDate dateBegin = LocalDate.now().minusDays(30);
        LocalDate dateEnd = LocalDate.now().minusDays(1);

        //查询概览数据
        BusinessDataVO businessDataVO = workspaceService.getBusinessData(LocalDateTime.of(dateBegin, LocalTime.MIN), LocalDateTime.of(dateEnd, LocalTime.MAX));

        //2. 通过POI将数据写入到Excel文件中
        InputStream in = this.getClass().getClassLoader().getResourceAsStream("template/运营数据报表模板.xlsx");

        try {
            //基于模板文件创建一个新的Excel文件
            XSSFWorkbook excel = new XSSFWorkbook(in);

            //获取表格文件的Sheet页
            XSSFSheet sheet = excel.getSheet("Sheet1");

            //填充数据--时间
            sheet.getRow(1).getCell(1).setCellValue("时间：" + dateBegin + "至" + dateEnd);

            //获得第4行
            XSSFRow row = sheet.getRow(3);
            row.getCell(2).setCellValue(businessDataVO.getTurnover());
            row.getCell(4).setCellValue(businessDataVO.getOrderCompletionRate());
            row.getCell(6).setCellValue(businessDataVO.getNewUsers());

            //获得第5行
            row = sheet.getRow(4);
            row.getCell(2).setCellValue(businessDataVO.getValidOrderCount());
            row.getCell(4).setCellValue(businessDataVO.getUnitPrice());

            //填充明细数据
            for (int i = 0; i < 30; i++) {
                LocalDate date = dateBegin.plusDays(i);
                //查询某一天的营业数据
                BusinessDataVO businessData = workspaceService.getBusinessData(LocalDateTime.of(date, LocalTime.MIN), LocalDateTime.of(date, LocalTime.MAX));

                //获得某一行
                row = sheet.getRow(7 + i);
                row.getCell(1).setCellValue(date.toString());
                row.getCell(2).setCellValue(businessData.getTurnover());
                row.getCell(3).setCellValue(businessData.getValidOrderCount());
                row.getCell(4).setCellValue(businessData.getOrderCompletionRate());
                row.getCell(5).setCellValue(businessData.getUnitPrice());
                row.getCell(6).setCellValue(businessData.getNewUsers());
            }

            //3. 通过输出流将Excel文件下载到客户端浏览器
            ServletOutputStream out = response.getOutputStream();
            excel.write(out);

            //关闭资源
            out.close();
            excel.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
