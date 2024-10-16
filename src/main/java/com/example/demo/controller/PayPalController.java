package com.example.demo.controller;

import com.example.demo.service.PayPalService;
import com.paypal.api.payments.Links;
import com.paypal.api.payments.Payment;
import com.paypal.base.rest.PayPalRESTException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/paypal")
public class PayPalController {

    @Autowired
    private PayPalService payPalService;

    @PostMapping("/pay")
    public ResponseEntity<?> pay(@RequestParam("total") Double total,
                                 @RequestParam("currency") String currency) {
        try {
            Payment payment = payPalService.createPayment(
                    total,
                    currency,
                    "paypal",  // 결제 방법
                    "sale",    // 결제 intent
                    "결제 설명",
                    "http://localhost:8080/api/paypal/cancel",  // 취소 URL
                    "http://localhost:8080/api/paypal/success"  // 성공 URL
            );

            // PayPal 승인 URL로 리다이렉트
            for (Links link : payment.getLinks()) {
                if (link.getRel().equals("approval_url")) {
                    return ResponseEntity
                            .status(HttpStatus.FOUND)
                            .header(HttpHeaders.LOCATION, link.getHref())  // 리디렉션 헤더 설정
                            .build();
                }
            }
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("PayPal 결제 승인 URL을 찾을 수 없습니다.");
        } catch (PayPalRESTException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("결제 중 오류 발생: " + e.getMessage());
        }
    }

    @GetMapping("/success")
    public ResponseEntity<?> successPayment(@RequestParam("paymentId") String paymentId,
                                            @RequestParam("PayerID") String payerId) {
        try {
            Payment payment = payPalService.executePayment(paymentId, payerId);
            if (payment.getState().equals("approved")) {
                return ResponseEntity.ok(Map.of("status", "success", "payment", payment));
            }
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("결제가 승인되지 않았습니다.");
        } catch (PayPalRESTException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("결제 승인 중 오류 발생: " + e.getMessage());
        }
    }

    @GetMapping("/cancel")
    public ResponseEntity<?> cancelPayment() {
        return ResponseEntity.ok(Map.of("status", "cancelled"));
    }
}
