package com.example.demo.service;

import com.paypal.base.rest.PayPalRESTException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;

@Service
public class WebHookService {

    @Value("${paypal.client.id}")
    private String clientId;

    @Value("${paypal.client.secret}")
    private String clientSecret;

    @Value("${paypal.mode}")
    private String mode;

    // Webhook을 통해 결제 상태를 처리하고 검증하는 메서드
    public void handlePayPalWebhook(String webhookEvent) {
        // Webhook에서 전달된 이벤트를 처리하는 로직 구현
        // 이벤트를 분석하고 필요한 작업을 진행 (예: 결제 승인, 취소 등)

        // 예시로 간단한 출력 처리
        System.out.println("PayPal Webhook Event: " + webhookEvent);

        // 이벤트를 기반으로 결제 상태를 검증하고 처리하는 부분 추가 필요
        // 예: 주문 상태 변경, 이메일 알림 전송 등
    }

    // Webhook 이벤트 처리용 API Endpoint
    @RestController
    public class WebHookController {

        @RequestMapping("/api/paypal/webhook")
        public ResponseEntity<String> handleWebhook(@RequestBody String webhookEvent) {
            try {
                WebHookService webHookService = new WebHookService();
                webHookService.handlePayPalWebhook(webhookEvent);
                return ResponseEntity.ok("Webhook event processed");
            } catch (Exception e) {
                e.printStackTrace();
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error processing webhook");
            }
        }
    }
}
