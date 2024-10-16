package com.example.demo.service;

import com.paypal.api.payments.Payment;
import org.springframework.stereotype.Service;

@Service
public class OrderService {

    public void createOrder(String userId, Payment payment) {
        // 결제 완료 후 주문 생성
        // 실제 DB에 주문을 생성하거나 관련 데이터를 처리하는 로직
        System.out.println("주문 생성 완료: 사용자 ID: " + userId + ", 결제 ID: " + payment.getId());
    }
}
