package monitoring.monitoringdashboard.external.kakao.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "kakao.api")
public class KakaoProperties {
  private String restKey;
  private String baseUrl;
}
