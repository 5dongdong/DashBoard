package monitoring.monitoringdashboard.domain.store.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import monitoring.monitoringdashboard.global.entity.BaseTimeEntity;

import java.math.BigDecimal;

@Entity
@Getter
@NoArgsConstructor
public class Store extends BaseTimeEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "store_id")
    private Long id;

    @Column(name = "store_name")
    private String storeName;

    @Column(nullable = false, length = 50)
    private String category;

    @Column(nullable = false)
    private String address;

    @Column(precision = 10, scale = 8)
    private BigDecimal latitude;

    @Column(precision = 10, scale = 8)
    private BigDecimal longitude;

    @Column(length = 28)
    private String phoneNum;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Enumerated(EnumType.STRING)
    private StoreStatus storeStatus = StoreStatus.OPEN;

    @Builder
    public Store(String storeName, String category, String address, BigDecimal latitude, BigDecimal longitude, String phoneNum, String description) {
        this.storeName = storeName;
        this.category = category;
        this.address = address;
        this.latitude = latitude;
        this.longitude = longitude;
        this.phoneNum = phoneNum;
        this.description = description;
    }

    public void updateInfo(String storeName, String category, String address
            , String phoneNum, String description) {
        this.storeName = storeName;
        this.category = category;
        this.address = address;
        this.phoneNum = phoneNum;
        this.description = description;
    }

    public void updateLocation(BigDecimal latitude, BigDecimal longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public void changeStoreStatus(StoreStatus storeStatus) {
        this.storeStatus = storeStatus;
    }

}
