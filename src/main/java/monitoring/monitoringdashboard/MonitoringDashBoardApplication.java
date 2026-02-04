package monitoring.monitoringdashboard;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class MonitoringDashBoardApplication {

  public static void main(String[] args) {
    SpringApplication.run(MonitoringDashBoardApplication.class, args);
  }

}
