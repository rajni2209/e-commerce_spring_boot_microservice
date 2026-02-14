package com.ecommerce.apigatway.service;

import org.springframework.boot.autoconfigure.web.WebProperties;
import org.springframework.boot.autoconfigure.web.reactive.error.AbstractErrorWebExceptionHandler;
import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.boot.web.reactive.error.ErrorAttributes;
import org.springframework.context.ApplicationContext;
import org.springframework.http.MediaType;
import org.springframework.http.codec.ServerCodecConfigurer;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.*;


import java.util.Map;

@Component
public class GlobalErrorHandler extends AbstractErrorWebExceptionHandler {

    public GlobalErrorHandler(
            ErrorAttributes errorAttributes,
            WebProperties webProperties,
            ApplicationContext context,
            ServerCodecConfigurer codecConfigurer) {

        super(errorAttributes, webProperties.getResources(), context);

        this.setMessageWriters(codecConfigurer.getWriters());
        this.setMessageReaders(codecConfigurer.getReaders());
    }

    @Override
    protected RouterFunction<ServerResponse> getRoutingFunction(
            ErrorAttributes errorAttributes) {

        return RouterFunctions.route(RequestPredicates.all(), request -> {

            Map<String, Object> error =
                    getErrorAttributes(request, ErrorAttributeOptions.defaults());

            return ServerResponse
                    .status((int) error.get("status"))
                    .contentType(MediaType.APPLICATION_JSON)
                    .bodyValue(error);
        });
    }
}
