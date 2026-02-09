package monitoring.monitoringdashboard.domain.visitor.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import monitoring.monitoringdashboard.domain.store.entity.Store;
import monitoring.monitoringdashboard.domain.store.repository.StoreRepository;
import monitoring.monitoringdashboard.domain.visitor.dto.VisitorCreateRequest;
import monitoring.monitoringdashboard.domain.visitor.dto.VisitorResponse;
import monitoring.monitoringdashboard.domain.visitor.dto.VisitorSummaryResponse;
import monitoring.monitoringdashboard.domain.visitor.entity.Visitor;
import monitoring.monitoringdashboard.domain.visitor.repository.VisitorRepository;
import monitoring.monitoringdashboard.global.exception.BusinessException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class VisitorService {

    private final VisitorRepository visitorRepository;
    private final StoreRepository storeRepository;

    /**
     * 방문자 등록
     */
    @Transactional
    public VisitorResponse createVisitor(Long storeId, VisitorCreateRequest request) {
        log.info("방문자 등록 시작: storeId={}, date={}", storeId, request.getVisitDate());

        Store store = findStoreById(storeId);

        // 중복 확인
        visitorRepository.findByStoreAndVisitDate(store, request.getVisitDate())
                .ifPresent(v -> {
                    throw new BusinessException("해당 날짜의 방문자 정보가 이미 존재합니다.", HttpStatus.BAD_REQUEST);
                });

        Visitor visitor = Visitor.builder()
                .store(store)
                .visitDate(request.getVisitDate())
                .visitorCount(request.getVisitorCount())
                .peakHour(request.getPeakHour())
                .build();

        Visitor savedVisitor = visitorRepository.save(visitor);

        log.info("방문자 등록 완료: ID={}", savedVisitor.getId());
        return VisitorResponse.from(savedVisitor);
    }

    /**
     * 특정 가게의 특정 날짜 방문자 조회
     */
    public VisitorResponse getVisitorByDate(Long storeId, LocalDate visitDate) {
        Store store = findStoreById(storeId);
        Visitor visitor = visitorRepository.findByStoreAndVisitDate(store, visitDate)
                .orElseThrow(() -> new BusinessException(
                        "해당 날짜의 방문자 정보를 찾을 수 없습니다.",
                        HttpStatus.NOT_FOUND
                ));
        return VisitorResponse.from(visitor);
    }

    /**
     * 특정 가게의 기간별 방문자 조회
     */
    public List<VisitorResponse> getVisitorsByPeriod(Long storeId, LocalDate startDate, LocalDate endDate) {
        Store store = findStoreById(storeId);
        return visitorRepository.findByStoreAndVisitDateBetween(store, startDate, endDate)
                .stream()
                .map(VisitorResponse::from)
                .collect(Collectors.toList());
    }

    /**
     * 특정 가게의 방문자 요약 정보
     */
    public VisitorSummaryResponse getVisitorSummary(Long storeId, LocalDate startDate, LocalDate endDate) {
        Store store = findStoreById(storeId);

        List<Visitor> visitorList = visitorRepository.findByStoreAndVisitDateBetween(store, startDate, endDate);

        if (visitorList.isEmpty()) {
            return VisitorSummaryResponse.builder()
                    .startDate(startDate)
                    .endDate(endDate)
                    .totalVisitors(0L)
                    .averageDailyVisitors(0.0)
                    .daysCnt(0)
                    .build();
        }

        Long totalVisitors = visitorList.stream()
                .map(Visitor::getVisitorCount)
                .mapToLong(Integer::longValue)
                .sum();

        int daysCount = visitorList.size();
        Double averageDailyVisitors = (double) totalVisitors / daysCount;

        return VisitorSummaryResponse.builder()
                .startDate(startDate)
                .endDate(endDate)
                .totalVisitors(totalVisitors)
                .averageDailyVisitors(averageDailyVisitors)
                .daysCnt(daysCount)
                .build();
    }

    /**
     * 방문자 수정
     */
    @Transactional
    public VisitorResponse updateVisitor(Long visitorId, VisitorCreateRequest request) {
        log.info("방문자 수정 시작: ID={}", visitorId);

        Visitor visitor = visitorRepository.findById(visitorId)
                .orElseThrow(() -> new BusinessException(
                        "방문자 정보를 찾을 수 없습니다.",
                        HttpStatus.NOT_FOUND
                ));

        visitor.updateVisitorCount(request.getVisitorCount());
        visitor.updatePeakHour(request.getPeakHour());

        log.info("방문자 수정 완료: ID={}", visitorId);
        return VisitorResponse.from(visitor);
    }

    /**
     * 방문자 삭제
     */
    @Transactional
    public void deleteVisitor(Long visitorId) {
        log.info("방문자 삭제 시작: ID={}", visitorId);

        Visitor visitor = visitorRepository.findById(visitorId)
                .orElseThrow(() -> new BusinessException(
                        "방문자 정보를 찾을 수 없습니다.",
                        HttpStatus.NOT_FOUND
                ));

        visitorRepository.delete(visitor);

        log.info("방문자 삭제 완료: ID={}", visitorId);
    }

    // === Private Methods ===

    private Store findStoreById(Long storeId) {
        return storeRepository.findById(storeId)
                .orElseThrow(() -> new BusinessException(
                        "가게를 찾을 수 없습니다.",
                        HttpStatus.NOT_FOUND
                ));
    }
}
