package com.voyagerss.api.controller;

import com.paypal.api.payments.Links;
import com.paypal.api.payments.Payment;
import com.paypal.base.rest.PayPalRESTException;
import com.voyagerss.api.service.PayPalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

@RestController
@RequestMapping("/api/payments")
public class PaymentController {

    @Autowired
    private PayPalService payPalService;

    @Value("${baseUrl}")
    private String baseUrl;

    // 결제 생성
    @PostMapping("/create")
    public RedirectView createPayment(@RequestParam("plan") String plan) {
        String successUrl = baseUrl + "/payment/success";
        String cancelUrl = baseUrl + "/payment/cancel";

        // 결제 금액 설정 (1개월, 6개월, 12개월)
        Double amount = 0.0;
        switch (plan) {
            case "1-month":
                amount = 10.0;  // 1개월 플랜 가격
                break;
            case "6-month":
                amount = 50.0;  // 6개월 플랜 가격
                break;
            case "12-month":
                amount = 90.0;  // 12개월 플랜 가격
                break;
        }

        try {
            Payment payment = payPalService.createPayment(amount, "USD", plan + " subscription", cancelUrl, successUrl);
            // PayPal로 리디렉트
            for (Links link : payment.getLinks()) {
                if (link.getRel().equals("approval_url")) {
                    return new RedirectView(link.getHref());
                }
            }
        } catch (PayPalRESTException e) {
            e.printStackTrace();
        }
        return new RedirectView("/payment/error");
    }

    // 결제 성공 처리
    @GetMapping("/success")
    public String successPayment(@RequestParam("paymentId") String paymentId, @RequestParam("PayerID") String payerId) {
        try {
            Payment payment = payPalService.executePayment(paymentId, payerId);
            if (payment.getState().equals("approved")) {
                return "Payment successful!";
            }
        } catch (PayPalRESTException e) {
            e.printStackTrace();
        }
        return "Payment failed!";
    }

    // 결제 취소 처리
    @GetMapping("/cancel")
    public String cancelPayment() {
        return "Payment cancelled!";
    }
}