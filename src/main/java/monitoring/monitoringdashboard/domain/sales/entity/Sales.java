package monitoring.monitoringdashboard.domain.sales.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import monitoring.monitoringdashboard.domain.store.entity.Store;
import monitoring.monitoringdashboard.global.entity.BaseTimeEntity;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;

@Entity
@Getter
@NoArgsConstructor
public class Sales extends BaseTimeEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "sales_id")
    private Long id;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_id", nullable = false)
    private Store store;

    @Column(name = "sales_date", nullable = false)
    private LocalDate salesDate;

    @Column(name = "total_amount", nullable = false)
    private BigDecimal totalAmount;

    @Column(name = "transaction_cnt")
    private Integer transactionCnt = 0;

    @Column(name = "avg_amount", precision = 10, scale = 2)
    private BigDecimal averageAmount;

    @Builder
    public Sales(Store store, LocalDate salesDate, BigDecimal totalAmount, Integer transactionCnt) {
        this.store = store;
        this.salesDate = salesDate;
        this.totalAmount = totalAmount;
        this.transactionCnt = transactionCnt;
        calculateAvgAmount();
    }

    public void addSales(BigDecimal amount) {
        this.totalAmount = this.totalAmount.add(amount);
        this.transactionCnt++;
        calculateAvgAmount();
    }

    public void updateSales(BigDecimal totalAmount, Integer transactionCnt) {
        this.totalAmount = totalAmount;
        this.transactionCnt = transactionCnt;
        calculateAvgAmount();
    }

    private void calculateAvgAmount() {
        if (transactionCnt > 0) {
            this.averageAmount = totalAmount.divide(
                    BigDecimal.valueOf(transactionCnt), 2, RoundingMode.HALF_UP
            );
        } else {
            this.averageAmount = BigDecimal.ZERO;
        }
    }
}
