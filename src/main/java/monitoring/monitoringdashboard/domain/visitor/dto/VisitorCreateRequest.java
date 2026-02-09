package monitoring.monitoringdashboard.domain.visitor.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class VisitorCreateRequest {

    @NotNull(message = "방문 날짜는 필수입니다.")
    private LocalDate visitDate;

    @NotNull(message = "방문자 수는 필수입니다.")
    @PositiveOrZero(message = "방문자 수는 0 이상이어야 합니다.")
    private Integer visitorCount;

    private Integer peakHour;
}
