package com.ecommerce.paymentservice.service;

import com.ecommerce.paymentservice.entity.Payment;
import com.ecommerce.paymentservice.kafka.PaymentEventProducer;
import com.ecommerce.paymentservice.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final PaymentEventProducer eventProducer;

    public Payment paymentCreation(Long orderId , BigDecimal amount){

//        Optional<PaymentIdempotencyKey> existing =
//                idempotencyRepository.findByOrderId(orderId);
//
//        if (existing.isPresent()) {
//            return existing.get().get
//        }

        Optional<Payment> existing =
                paymentRepository.findByOrderId(orderId);

        if (existing.isPresent()) {
            return existing.get();
        }

        Payment payment = new Payment();
        payment.setOrderId(orderId);
        payment.setAmount(amount);
        payment.setStatus("SUCCESS");

//        boolean success = Math.random() > 0.3;
//        payment.setStatus(success ? "success" : "failed");
        Payment save = paymentRepository.save(payment);
        eventProducer.publishResult(orderId , "SUCCESS");
        return save;
    }
}
