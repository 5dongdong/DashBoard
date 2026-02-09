package monitoring.monitoringdashboard.domain.visitor.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@Builder
@AllArgsConstructor
public class VisitorSummaryResponse {

    private LocalDate startDate;
    private LocalDate endDate;
    private Long totalVisitors;
    private Double averageDailyVisitors;
    private Integer daysCnt;
}
