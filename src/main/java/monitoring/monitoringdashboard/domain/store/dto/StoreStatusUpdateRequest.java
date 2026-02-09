package monitoring.monitoringdashboard.domain.store.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import monitoring.monitoringdashboard.domain.store.entity.StoreStatus;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class StoreStatusUpdateRequest {

    @NotNull(message = "상태는 필수입니다.")
    private StoreStatus storeStatus;

    private String reason;
}
