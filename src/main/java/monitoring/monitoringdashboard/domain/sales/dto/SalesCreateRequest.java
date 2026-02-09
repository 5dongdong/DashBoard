package monitoring.monitoringdashboard.domain.sales.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class SalesCreateRequest {

    @NotNull(message = "매출 날짜는 필수입니다.")
    private LocalDate saledDate;

    @NotNull(message = "총 매출액은 필수입니다.")
    @Positive(message = "매출액은 0보다 커야 합니다.")
    private BigDecimal totalAmount;

    @NotNull(message = "거래 건수는 필수입니다.")
    @Positive(message = "거래 건수는 0보다 커야 합니다.")
    private Integer transactionCount;
}
