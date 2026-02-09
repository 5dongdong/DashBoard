package monitoring.monitoringdashboard.domain.visitor.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import monitoring.monitoringdashboard.domain.store.entity.Store;

import java.time.LocalDate;

@Entity
@Getter
@NoArgsConstructor
public class Visitor {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "visitor_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_id", nullable = false)
    private Store store;

    @Column(name = " visit_date", nullable = false)
    private LocalDate visitDate;

    @Column(name = "visitor_cnt")
    private Integer visitorCount = 0;

    @Column(name = "peak_hour")
    private Integer peakHour;

    @Builder
    public Visitor(Store store, LocalDate visitDate, Integer visitorCount, Integer peakHour) {
        this.store = store;
        this.visitDate = visitDate;
        this.visitorCount = visitorCount;
        this.peakHour = peakHour;
    }

    public void addVisitor() {
        this.visitorCount++;
    }

    public void updateVisitorCount(Integer visitorCount) {
        this.visitorCount = visitorCount;
    }

    public void updatePeakHour(Integer peakHour) {
        this.peakHour = peakHour;
    }

}
