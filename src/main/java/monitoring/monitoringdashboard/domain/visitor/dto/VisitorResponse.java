package monitoring.monitoringdashboard.domain.visitor.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import monitoring.monitoringdashboard.domain.visitor.entity.Visitor;

import java.time.LocalDate;

@Getter
@Builder
@AllArgsConstructor
public class VisitorResponse {

    private Long id;
    private Long storeId;
    private String storeName;
    private LocalDate visitDate;
    private Integer visitorCount;
    private Integer peakHour;

    public static VisitorResponse from(Visitor visitor) {
        return VisitorResponse.builder()
                .id(visitor.getId())
                .storeId(visitor.getStore().getId())
                .storeName(visitor.getStore().getStoreName())
                .visitDate(visitor.getVisitDate())
                .visitorCount(visitor.getVisitorCount())
                .peakHour(visitor.getPeakHour())
                .build();
    }
}
