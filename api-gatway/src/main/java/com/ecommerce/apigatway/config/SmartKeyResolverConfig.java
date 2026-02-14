package com.ecommerce.apigatway.config;

import org.springframework.cloud.gateway.filter.ratelimit.KeyResolver;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import reactor.core.publisher.Mono;

@Configuration
public class SmartKeyResolverConfig {

    @Bean
    public KeyResolver smartKeyResolver() {
        return exchange ->
                exchange.getPrincipal()
                        .map(principal -> {
                            return "USER_" + principal.getName();
                        })
                        .switchIfEmpty(Mono.defer(() -> {
                            // ✅ Anonymous user → IP-based rate limit
                            String xForwardedFor = exchange.getRequest()
                                    .getHeaders()
                                    .getFirst("X-Forwarded-For");

                            if (xForwardedFor != null && !xForwardedFor.isBlank()) {
                                return Mono.just("IP_" + xForwardedFor.split(",")[0].trim());
                            }

                            if (exchange.getRequest().getRemoteAddress() != null) {
                                return Mono.just(
                                        "IP_" + exchange.getRequest()
                                                .getRemoteAddress()
                                                .getAddress()
                                                .getHostAddress()
                                );
                            }

                            return Mono.just("IP_unknown");
                        }));
    }
}
