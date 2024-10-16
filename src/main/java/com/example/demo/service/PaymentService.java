package com.example.demo.service;

import com.paypal.api.payments.Payment;
import com.paypal.api.payments.PaymentExecution;
import com.paypal.base.rest.APIContext;
import com.paypal.base.rest.PayPalRESTException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class PaymentService {

    @Value("${paypal.client.id}")
    private String clientId;

    @Value("${paypal.client.secret}")
    private String clientSecret;

    @Value("${paypal.mode}")
    private String mode;

    private final PayPalService payPalService;

    public PaymentService(PayPalService payPalService) {
        this.payPalService = payPalService;
    }

    // 결제 상태 검증
    public boolean verifyPayment(String paymentId, String payerId) throws PayPalRESTException {
        // PayPal API Context 설정
        APIContext apiContext = new APIContext(clientId, clientSecret, mode);

        // 결제 상태 확인
        Payment payment = payPalService.executePayment(paymentId, payerId);

        // 결제 상태가 'approved'인지 확인
        return payment.getState().equals("approved");
    }

    // 결제 금액 및 통화 정합성 검증
    public boolean validatePaymentDetails(Double total, String currency, Payment payment) {
        // 결제 금액과 통화가 요청한 값과 일치하는지 확인
        String paidAmount = payment.getTransactions().get(0).getAmount().getTotal();
        String paidCurrency = payment.getTransactions().get(0).getAmount().getCurrency();

        // 금액 및 통화가 일치하는지 확인
        return paidAmount.equals(String.format("%.2f", total)) && paidCurrency.equals(currency);
    }

    // 결제 ID를 통한 중복 결제 방지
    public boolean isPaymentIdUnique(String paymentId) {
        // 실제 DB에 결제 ID가 존재하는지 확인하는 로직 (DB 작업 필요)
        // 예시에서는 항상 true 반환 (DB 연동 후 실제로 처리 필요)
        return true;
    }
}
