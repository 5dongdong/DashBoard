package monitoring.monitoringdashboard.domain.store.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import monitoring.monitoringdashboard.domain.store.entity.Store;
import monitoring.monitoringdashboard.domain.store.entity.StoreStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Builder
@AllArgsConstructor
public class StoreResponse {

    private Long id;
    private String storeName;
    private String category;
    private String address;
    private BigDecimal latitude;
    private BigDecimal longitude;
    private String storePhoneNum;
    private String description;
    private StoreStatus storeStatus;
    private String statusDescription;
    private LocalDateTime createAt;
    private LocalDateTime updateAt;

    public static StoreResponse from(Store store) {
        return StoreResponse.builder()
                .id(store.getId())
                .storeName(store.getStoreName())
                .category(store.getCategory())
                .address(store.getAddress())
                .latitude(store.getLatitude())
                .longitude(store.getLongitude())
                .storePhoneNum(store.getPhoneNum())
                .description(store.getDescription())
                .storeStatus(store.getStoreStatus())
                .statusDescription(store.getStoreStatus().getDescription())
                .createAt(store.getCreatedAt())
                .updateAt(store.getUpdatedAt())
                .build();
    }
}
