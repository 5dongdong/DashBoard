package monitoring.monitoringdashboard.domain.store.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import monitoring.monitoringdashboard.domain.store.entity.Store;
import monitoring.monitoringdashboard.domain.store.entity.StoreStatus;

import java.math.BigDecimal;

@Getter
@Builder
@AllArgsConstructor
public class StoreListResponse {

    private Long id;
    private String storeName;
    private String category;
    private String address;
    private BigDecimal latitude;
    private BigDecimal longitude;
    private StoreStatus storeStatus;
    private String statusDescription;

    public static StoreListResponse from(Store store) {
        return StoreListResponse.builder()
                .id(store.getId())
                .storeName(store.getStoreName())
                .category(store.getCategory())
                .address(store.getAddress())
                .latitude(store.getLatitude())
                .longitude(store.getLongitude())
                .storeStatus(store.getStoreStatus())
                .statusDescription(store.getStoreStatus().getDescription())
                .build();
    }
}
