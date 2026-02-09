package monitoring.monitoringdashboard.domain.visitor.repository;

import monitoring.monitoringdashboard.domain.store.entity.Store;
import monitoring.monitoringdashboard.domain.visitor.entity.Visitor;
import org.apache.ibatis.annotations.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface VisitorRepository extends JpaRepository<Visitor, Long> {

    // 특정 가게의 특정 날짜 방문자 조회
    Optional<Visitor> findByStoreAndVisitDate(Store store, LocalDate visitDate);

    // 특정 가게의 기간별 방문자 조회
    List<Visitor> findByStoreAndVisitDateBetween(Store store, LocalDate startDate, LocalDate endDate);

    // 특정 날짜의 모든 가게 방문자 조회
    List<Visitor> findByVisitDate(LocalDate visitDate);

    // 특정 가게의 방문자 내림차순 조회 (최근순)
    List<Visitor> findByStoreOrderByVisitDateDesc(Store store);

    // 특정 가게의 총 방문자 수
    @Query("SELECT SUM(v.visitorCount) FROM Visitor v WHERE v.store = :store")
    Long getTotalVisitorsByStore(@Param("store") Store store);

    // 특정 기간 동안의 총 방문자 수
    @Query("SELECT SUM(v.visitorCount) FROM Visitor v WHERE v.visitDate BETWEEN :startDate AND :endDate")
    Long getTotalVisitorsByDateRange(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);

    // 가게별 일평균 방문자 수
    @Query("SELECT AVG(v.visitorCount) FROM Visitor v WHERE v.store = :store")
    Double getAverageDailyVisitors(@Param("store") Store store);
}
