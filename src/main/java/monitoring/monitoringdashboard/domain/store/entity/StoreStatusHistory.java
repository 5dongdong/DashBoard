package monitoring.monitoringdashboard.domain.store.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
public class StoreStatusHistory {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_id", nullable = false)
    private Store store;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private StoreStatus status;

    @Column(name = "changed_at")
    private LocalDateTime changedAt = LocalDateTime.now();

    private String reason;

    @Builder
    public StoreStatusHistory(Store store, StoreStatus status, String reason) {
        this.store = store;
        this.status = status;
        this.reason = reason;
        this.changedAt = LocalDateTime.now();
    }
}
