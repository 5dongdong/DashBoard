package monitoring.monitoringdashboard.domain.sales.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import monitoring.monitoringdashboard.domain.sales.entity.Sales;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Builder
@AllArgsConstructor
public class SalesResponse {

    private Long id;
    private Long storeId;
    private String storeName;
    private LocalDate salesDate;
    private BigDecimal totalAmount;
    private Integer transactionCount;
    private BigDecimal averageAmount;

    public static SalesResponse from(Sales sales) {
        return SalesResponse.builder()
                .id(sales.getId())
                .storeId(sales.getStore().getId())
                .storeName(sales.getStore().getStoreName())
                .salesDate(sales.getSalesDate())
                .totalAmount(sales.getTotalAmount())
                .transactionCount(sales.getTransactionCnt())
                .averageAmount(sales.getAverageAmount())
                .build();
    }
}
