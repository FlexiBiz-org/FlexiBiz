package com.flexibiz.flexgateway.config;

import com.flexibiz.flexgateway.filter.AuthFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import reactor.core.publisher.Mono;

@Configuration
public class GatewayConfig {

    @Autowired
    AuthFilter authFilter;
    @Bean
    public RouteLocator routeLocator (RouteLocatorBuilder builder) {
        return builder.routes()
            .route(r -> r.path("/auth/**")
                    .filters(f -> f
                            .filter(authFilter.apply(new AuthFilter.Config()))
                            .circuitBreaker(c ->
                                c.setName("defaultCircuitBreaker")
                                .setFallbackUri("forward:/fallback/flex-identity")
                            )
                    )
                    .uri("lb://flex-identity")
            )
            .route(r -> r.path("/test/**")
                    .filters(f -> f
                            .filter(authFilter.apply(new AuthFilter.Config()))
                            .circuitBreaker(c ->
                                    c.setName("defaultCircuitBreaker")
                                    .setFallbackUri("forward:/fallback/flex-test")
                            )
                    )
                    .uri("lb://flex-test")
            )
            .build();
    }


}
