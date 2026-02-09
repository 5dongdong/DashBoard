package monitoring.monitoringdashboard.domain.sales.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Builder
@AllArgsConstructor
public class SalesSummaryResponse {

    private LocalDate startDate;
    private LocalDate endDate;
    private BigDecimal totalSales;
    private BigDecimal averageDailySales;
    private Integer totalTransactions;
    private Integer daysCnt;
}
