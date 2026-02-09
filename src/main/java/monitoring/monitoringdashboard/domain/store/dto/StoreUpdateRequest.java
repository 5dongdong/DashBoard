package monitoring.monitoringdashboard.domain.store.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class StoreUpdateRequest {

    @NotBlank(message = "가게명은 필수입니다.")
    private String storeName;

    @NotBlank(message = "카테고리는 필수입니다.")
    private String category;

    @NotBlank(message = "주소는 필수입니다.")
    private String address;

    @Pattern(regexp = "^\\d{2,3}-\\d{3,4}-\\d{4}$", message = "올바른 전화번호 형식이 아닙니다.")
    private String phone;

    private String description;
}
