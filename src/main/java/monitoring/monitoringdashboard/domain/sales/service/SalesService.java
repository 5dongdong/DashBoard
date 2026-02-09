package monitoring.monitoringdashboard.domain.sales.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import monitoring.monitoringdashboard.domain.sales.dto.SalesCreateRequest;
import monitoring.monitoringdashboard.domain.sales.dto.SalesResponse;
import monitoring.monitoringdashboard.domain.sales.dto.SalesSummaryResponse;
import monitoring.monitoringdashboard.domain.sales.entity.Sales;
import monitoring.monitoringdashboard.domain.sales.repository.SalesRepository;
import monitoring.monitoringdashboard.domain.store.entity.Store;
import monitoring.monitoringdashboard.domain.store.repository.StoreRepository;
import monitoring.monitoringdashboard.global.exception.BusinessException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class SalesService {

    private final SalesRepository salesRepository;
    private final StoreRepository storeRepository;

    /**
     * 매출 등록
     */
    @Transactional
    public SalesResponse createSales(Long storeId, SalesCreateRequest request) {
        log.info("매출 등록 시작: storeID = {}, date = {}", storeId, request.getSaledDate());

        Store store = findStoreById(storeId);

        //중복 확인
        salesRepository.findByStoreAndSalesDate(store, request.getSaledDate())
                .ifPresent(sales -> {
                    throw new BusinessException("해당 날짜의 매출이 이미 존재합니다.", HttpStatus.BAD_REQUEST);
                });

        Sales sales = Sales.builder()
                .store(store)
                .salesDate(request.getSaledDate())
                .totalAmount(request.getTotalAmount())
                .transactionCnt(request.getTransactionCount())
                .build();

        Sales savedSales = salesRepository.save(sales);

        log.info("매출 등록 완료 : id = {}", savedSales.getId());
        return SalesResponse.from(savedSales);
    }

    /**
     * 특정 가게의 특정 날짜 매출 조회
     */
    public SalesResponse getSalesByDate(Long storeId, LocalDate salesDate) {
        Store store = findStoreById(storeId);
        Sales sales = salesRepository.findByStoreAndSalesDate(store, salesDate)
                .orElseThrow(() -> new BusinessException(
                        "해당 날짜의 매출 정보를 찾을 수 없습니다.",
                        HttpStatus.NOT_FOUND
                ));
        return SalesResponse.from(sales);
    }

    /**
     * 특정 가게의 기간별 매출 조회
     */
    public List<SalesResponse> getSalesByPeriod(Long storeId, LocalDate startDate, LocalDate endDate) {
        Store store = findStoreById(storeId);
        return salesRepository.findByStoreAndSalesDateBetween(store, startDate, endDate)
                .stream()
                .map(SalesResponse::from)
                .collect(Collectors.toList());
    }

    /**
     * 특정 가게의 매출 요약 정보
     */
    public SalesSummaryResponse getSalesSummary(Long storeId, LocalDate startDate, LocalDate endDate) {
        Store store = findStoreById(storeId);

        List<Sales> salesList = salesRepository.findByStoreAndSalesDateBetween(store, startDate, endDate);

        if (salesList.isEmpty()) {
            return SalesSummaryResponse.builder()
                    .startDate(startDate)
                    .endDate(endDate)
                    .totalSales(BigDecimal.ZERO)
                    .averageDailySales(BigDecimal.ZERO)
                    .totalTransactions(0)
                    .daysCnt(0)
                    .build();
        }

        BigDecimal totalSales = salesList.stream()
                .map(Sales::getTotalAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        Integer totalTransactions = salesList.stream()
                .map(Sales::getTransactionCnt)
                .reduce(0, Integer::sum);

        int daysCount = salesList.size();
        BigDecimal averageDailySales = totalSales.divide(
                BigDecimal.valueOf(daysCount),
                2,
                RoundingMode.HALF_UP
        );

        return SalesSummaryResponse.builder()
                .startDate(startDate)
                .endDate(endDate)
                .totalSales(totalSales)
                .averageDailySales(averageDailySales)
                .totalTransactions(totalTransactions)
                .daysCnt(daysCount)
                .build();
    }

    /**
     * 매출 수정
     */
    @Transactional
    public SalesResponse updateSales(Long salesId, SalesCreateRequest request) {
        log.info("매출 수정 시작: ID={}", salesId);

        Sales sales = salesRepository.findById(salesId)
                .orElseThrow(() -> new BusinessException(
                        "매출 정보를 찾을 수 없습니다.",
                        HttpStatus.NOT_FOUND
                ));

        sales.updateSales(request.getTotalAmount(), request.getTransactionCount());

        log.info("매출 수정 완료: ID={}", salesId);
        return SalesResponse.from(sales);
    }

    /**
     * 매출 삭제
     */
    @Transactional
    public void deleteSales(Long salesId) {
        log.info("매출 삭제 시작: ID={}", salesId);

        Sales sales = salesRepository.findById(salesId)
                .orElseThrow(() -> new BusinessException(
                        "매출 정보를 찾을 수 없습니다.",
                        HttpStatus.NOT_FOUND
                ));

        salesRepository.delete(sales);

        log.info("매출 삭제 완료: ID={}", salesId);
    }

    private Store findStoreById(Long storeId) {
        return storeRepository.findById(storeId)
                .orElseThrow(() -> new BusinessException(
                        "가게를 찾을 수 없습니다."
                        , HttpStatus.NOT_FOUND
                ));
    }
}
