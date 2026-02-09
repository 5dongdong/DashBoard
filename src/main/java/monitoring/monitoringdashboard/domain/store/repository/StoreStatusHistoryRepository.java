package monitoring.monitoringdashboard.domain.store.repository;

import monitoring.monitoringdashboard.domain.store.entity.Store;
import monitoring.monitoringdashboard.domain.store.entity.StoreStatusHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface StoreStatusHistoryRepository extends JpaRepository<StoreStatusHistory, Long> {

    // 특정 가게의 상태 변경 이력 조회 (최근순)
    List<StoreStatusHistory> findByStoreOrderByChangedAtDesc(Store store);

    // 특정 가게의 기간별 상태 변경 이력
    List<StoreStatusHistory> findByStoreAndChangedAtBetween(Store store, LocalDateTime startDate, LocalDateTime endDate);

    // 특정 가게의 최근 상태 변경 이력
    StoreStatusHistory findFirstByStoreOrderByChangedAtDesc(Store store);
}
