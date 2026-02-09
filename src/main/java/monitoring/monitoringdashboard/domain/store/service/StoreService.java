package monitoring.monitoringdashboard.domain.store.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import monitoring.monitoringdashboard.domain.store.dto.StoreCreateRequest;
import monitoring.monitoringdashboard.domain.store.dto.StoreResponse;
import monitoring.monitoringdashboard.domain.store.dto.StoreStatusUpdateRequest;
import monitoring.monitoringdashboard.domain.store.dto.StoreUpdateRequest;
import monitoring.monitoringdashboard.domain.store.entity.Store;
import monitoring.monitoringdashboard.domain.store.entity.StoreStatus;
import monitoring.monitoringdashboard.domain.store.entity.StoreStatusHistory;
import monitoring.monitoringdashboard.domain.store.repository.StoreRepository;
import monitoring.monitoringdashboard.domain.store.repository.StoreStatusHistoryRepository;
import monitoring.monitoringdashboard.global.exception.BusinessException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class StoreService {

    private final StoreRepository storeRepository;
    private final StoreStatusHistoryRepository storeStatusHistoryRepository;
//    private final KakaoMapService kakaoMapService;


    /**
     * 가게 등록
     */

    public StoreResponse createStore(StoreCreateRequest request) {
        log.info("가게 등록 시작 : {}", request.getStoreName());

//        BigDecimal[] coordinates =
        Store store = Store.builder()
                .storeName(request.getStoreName())
                .category(request.getCategory())
                .address(request.getAddress())
                .phoneNum(request.getPhoneNum())
                .description(request.getDescription())
                .build();

        Store savedStore = storeRepository.save(store);

        //상태 변경 이력 저장
        saveStatusHistory(savedStore, StoreStatus.OPEN, "최초 등록");

        log.info("가게 등록 완료 : {}", savedStore.getId());
        return StoreResponse.from(savedStore);
    }

    /**
     * 가게 상세 조회
     */
    public StoreResponse getStore(Long storeId) {
        Store store = findStoreById(storeId);
        return StoreResponse.from(store);
    }

    /**
     * 전체 가게 조회
     */
    public List<StoreResponse> getAllStores() {
        return storeRepository.findAll().stream()
                .map(StoreResponse::from)
                .collect(Collectors.toList());
    }

    /**
     * 카테고리별 가게 조회
     */
    public List<StoreResponse> getStoreByCategory(String category) {
        return storeRepository.findByCategory(category).stream()
                .map(StoreResponse::from)
                .collect(Collectors.toList());
    }

    /**
     * 상태별 가게 조회
     */
    public List<StoreResponse> getStoreByStatus(StoreStatus storeStatus) {
        return storeRepository.findByStatus(storeStatus).stream()
                .map(StoreResponse::from)
                .collect(Collectors.toList());
    }

    /**
     * 가게명으로 검색
     */
    public List<StoreResponse> searchStoreByName(String storeName) {
        return storeRepository.findByStoreNameContaining(storeName).stream()
                .map(StoreResponse::from)
                .collect(Collectors.toList());
    }

    /**
     * 가게 정보 수정
     */
    @Transactional
    public StoreResponse updateStore(Long storeId, StoreUpdateRequest request) {
        log.info("가게 정보 수정 시작 : {}", storeId);

        Store store = findStoreById(storeId);

        // 주소 변경된 경우 좌표 재계산
        if (!store.getAddress().equals(request.getAddress())) {
//            BigDecimal[] coordinates = kakaoMapService.getCoordinatesByAddress(request.getAddress());
//            store.updateLocation(coordinates[0], coordinates[1]);
        }

        store.updateInfo(
                request.getStoreName()
                , request.getCategory()
                , request.getPhone()
                , request.getAddress()
                , request.getDescription()
        );

        log.info("가게 정보 수정 완료 : {}", storeId);
        return StoreResponse.from(store);
    }

    /**
     * 가게 상태 변경
     */

    @Transactional
    public StoreResponse updateStoreStatus(Long storeId, StoreStatusUpdateRequest request) {
        log.info("가게 상태 정보 변경 시작 : ID = {}, 새로운 상태 = {} ", storeId, request.getStoreStatus());

        Store store = findStoreById(storeId);

        StoreStatus oldStoreStatus = store.getStoreStatus();
        store.changeStoreStatus(request.getStoreStatus());

        //상태 변경 이력 저장
        saveStatusHistory(store, request.getStoreStatus(), request.getReason());

        log.info("가게 상태 변경 완료 : ID = {}, {} -> {}", storeId, oldStoreStatus, request.getStoreStatus());
        return StoreResponse.from(store);
    }

    /**
     * 가게 삭제
     */
    @Transactional
    public void deleteStore(Long storeId) {
        log.info("가게 삭제 시작 : {}", storeId);
        Store store = findStoreById(storeId);
        storeRepository.delete(store);

        log.info("가게 삭제 완료 : {}", storeId);
    }

    //==private Methods===

    /**
     * ID로 가게 찾기
     */
    private Store findStoreById(Long storeId) {
        return storeRepository.findById(storeId).orElseThrow(() -> new BusinessException(
                "가게를 찾을 수 없습니다. ID : " + storeId, HttpStatus.NOT_FOUND
        ));
    }

    /**
     * 가게 정보 수정을 위한 Method
     */
    private void saveStatusHistory(Store store, StoreStatus storeStatus, String reason) {
        StoreStatusHistory history = StoreStatusHistory.builder()
                .store(store)
                .status(storeStatus)
                .reason(reason)
                .build();
        storeStatusHistoryRepository.save(history);
    }



}
