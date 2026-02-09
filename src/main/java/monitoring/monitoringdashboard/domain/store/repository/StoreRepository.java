package monitoring.monitoringdashboard.domain.store.repository;

import monitoring.monitoringdashboard.domain.store.entity.Store;
import monitoring.monitoringdashboard.domain.store.entity.StoreStatus;
import org.apache.ibatis.annotations.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface StoreRepository extends JpaRepository<Store, Long> {

    // 카테고리별 가게 조회
    List<Store> findByCategory(String category);

    // 상태별 가게 조회
    List<Store> findByStatus(StoreStatus status);

    // 가게명으로 검색(부분 일치)
    List<Store> findByStoreNameContaining(String storeName);

    // 특정 위치 근처의 가게 조회(거리 기반)
    @Query("SELECT s FROM Store s WHERE " +
            "s.latitude BETWEEN :minLat AND :maxLat And " +
            "s.longitude BETWEEN :minLon AND :maxLon")
    List<Store> findStoresInArea(
            @Param("minLat") BigDecimal minLat,
            @Param("minLon") BigDecimal minLon,
            @Param("maxLat") BigDecimal maxLat,
            @Param("maxLon") BigDecimal maxLon
            );
    
    //카테고리와 상태로 조회
    List<Store> findByCategoryAndStatus(String category, StoreStatus status);
}
