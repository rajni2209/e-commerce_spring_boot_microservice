package com.ecommerce.orderservice.service;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.stereotype.Component;

@Component
public class OrderMetrics {

    private final Counter orderCreated;
    private final Counter orderCancelled;
    private final Counter orderConfirmed;

    public OrderMetrics(MeterRegistry meterRegistry){
        this.orderCreated = Counter.builder("order.created")
                .description("Total number of order created")
                .register(meterRegistry);
        this.orderCancelled = Counter.builder("orders.cancelled")
                .description("Total number of orders cancelled")
                .register(meterRegistry);

        this.orderConfirmed = Counter.builder("orders.confirmed")
                .description("Total number of orders confirmed")
                .register(meterRegistry);
    }


    public void incrementCreated() {
        orderCreated.increment();
    }

    public void incrementCancelled() {
        orderCancelled.increment();
    }

    public void incrementConfirmed() {
        orderConfirmed.increment();
    }

}
