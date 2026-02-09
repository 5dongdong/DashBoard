package monitoring.monitoringdashboard.domain.sales.repository;

import monitoring.monitoringdashboard.domain.sales.entity.Sales;
import monitoring.monitoringdashboard.domain.store.entity.Store;
import org.apache.ibatis.annotations.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface SalesRepository extends JpaRepository<Sales, Long> {

    // 특정 가게의 특정 날짜 매출 조회
    Optional<Sales> findByStoreAndSalesDate(Store store, LocalDate salesDate);

    // 특정 가게의 기간별 매출 조회
    List<Sales> findByStoreAndSalesDateBetween(Store store, LocalDate startDate, LocalDate endDate);

    // 특정 날짜의 모든 가게 매출 조회
    List<Sales> findBySalesDate(LocalDate salesDate);

    // 특정 가게의 매출 내림차순 조회 (최근순)
    List<Sales> findByStoreOrderBySalesDateDesc(Store store);

    // 특정 가게의 총 매출액 합계
    @Query("SELECT SUM(s.totalAmount) FROM Sales s WHERE s.store = :store")
    BigDecimal getTotalSalesByStore(@Param("store") Store store);

    // 특정 기간 동안의 총 매출액
    @Query("SELECT SUM(s.totalAmount) FROM Sales s WHERE s.salesDate BETWEEN :startDate AND :endDate")
    BigDecimal getTotalSalesByDateRange(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);

    // 가게별 일평균 매출
    @Query("SELECT AVG(s.totalAmount) FROM Sales s WHERE s.store = :store")
    BigDecimal getAverageDailySales(@Param("store") Store store);
}
