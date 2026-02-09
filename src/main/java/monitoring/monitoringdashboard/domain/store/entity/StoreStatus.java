package monitoring.monitoringdashboard.domain.store.entity;

public enum StoreStatus {
    OPEN("영업중"),
    PREPARING("준비중"),
    CLOSED("휴무"),
    TEMPORAROLY_CLOSED("임시휴무");
    
    private final String description;
    
    StoreStatus(String description) {
        this.description = description;
    }
    
    public String getDescription() {
        return description;
    }
}
