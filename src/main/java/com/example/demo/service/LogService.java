package com.example.demo.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class LogService {

    private static final Logger logger = LoggerFactory.getLogger(LogService.class);

    // 결제 요청 시 기록
    public void logPaymentRequest(String userId, Double total, String currency) {
        logger.info("결제 요청: 사용자: {}, 금액: {}, 통화: {}", userId, total, currency);
    }

    // 결제 승인 시 기록
    public void logPaymentApproval(String paymentId, String payerId) {
        logger.info("결제 승인: 결제 ID: {}, Payer ID: {}", paymentId, payerId);
    }

    // 결제 실패 시 기록
    public void logPaymentFailure(String paymentId, String errorMessage) {
        logger.error("결제 실패: 결제 ID: {}, 오류: {}", paymentId, errorMessage);
    }
}
