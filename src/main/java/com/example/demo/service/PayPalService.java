package com.example.demo.service;

import com.paypal.api.payments.*;
import com.paypal.base.rest.APIContext;
import com.paypal.base.rest.PayPalRESTException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class PayPalService {

    @Value("${paypal.client.id}")
    private String clientId;

    @Value("${paypal.client.secret}")
    private String clientSecret;

    @Value("${paypal.mode}")
    private String mode;

    // 결제 생성
    public Payment createPayment(Double total, String currency, String method, String intent, String description, String cancelUrl, String successUrl) throws PayPalRESTException {

        // 금액 설정
        Amount amount = new Amount();
        amount.setCurrency(currency);
        amount.setTotal(String.format("%.2f", total));

        // 거래 정보 설정
        Transaction transaction = new Transaction();
        transaction.setDescription(description);
        transaction.setAmount(amount);

        // 결제 방법 설정
        Payer payer = new Payer();
        payer.setPaymentMethod(method.toString());

        // 결제 생성 요청 설정
        Payment payment = new Payment();
        payment.setIntent(intent.toString());
        payment.setPayer(payer);
        payment.setTransactions(Collections.singletonList(transaction));

        // 승인 및 취소 URL 설정
        RedirectUrls redirectUrls = new RedirectUrls();
        redirectUrls.setCancelUrl(cancelUrl);
        redirectUrls.setReturnUrl(successUrl);
        payment.setRedirectUrls(redirectUrls);

        // PayPal API 호출
        APIContext apiContext = new APIContext(clientId, clientSecret, mode);
        return payment.create(apiContext);
    }

    public Payment executePayment(String paymentId, String payerId) throws PayPalRESTException {
        // 결제 요청
        Payment payment = new Payment();
        payment.setId(paymentId);

        // 결제 실행 요청
        PaymentExecution paymentExecution = new PaymentExecution();
        paymentExecution.setPayerId(payerId);

        // PayPal API 호출
        APIContext apiContext = new APIContext(clientId, clientSecret, mode);
        return payment.execute(apiContext, paymentExecution);
    }

}