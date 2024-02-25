package com.sky.service;

import com.sky.vo.TurnoverReportVO;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * @author Mark
 * @date 2024/2/25
 */
public interface ReportService {

    /**
     * 获取统计营业额
     * @param begin
     * @param end
     * @return
     */
    TurnoverReportVO getTurnoverStatistics(LocalDate begin, LocalDate end);
}
