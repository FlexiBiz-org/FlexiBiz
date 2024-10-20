package com.flexibiz.flexgateway.config;

import org.springframework.cloud.gateway.filter.factory.SpringCloudCircuitBreakerFilterFactory;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import reactor.core.publisher.Mono;

@Configuration
public class GatewayConfig {

    @Bean
    public RouteLocator routeLocator (RouteLocatorBuilder builder) {
        return builder.routes()
            .route(r -> r.path("/**")
                    .filters(f ->
                            f.circuitBreaker(c ->
                                c.setName("defaultCircuitBreaker")
                                .setFallbackUri("forward:/fallback/Flex-Test")
                            )
                    )
                    .uri("lb://flex-test")
            )
            .build();
    }


}
