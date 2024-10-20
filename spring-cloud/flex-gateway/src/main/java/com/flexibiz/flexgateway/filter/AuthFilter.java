package com.flexibiz.flexgateway.filter;


import com.flexibiz.flexgateway.helper.JWTUtil;
import com.flexibiz.flexgateway.helper.RouteValidator;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Mono;

@Component
public class AuthFilter extends AbstractGatewayFilterFactory<AuthFilter.Config> {

    private final RouteValidator routeValidator;
    private final JWTUtil jwtUtil;

    public AuthFilter(RouteValidator routeValidator, JWTUtil jwtUtil) {
        super(Config.class);
        this.routeValidator = routeValidator;
        this.jwtUtil = jwtUtil;
    }

    @Override
    public GatewayFilter apply(AuthFilter.Config config) {
        return (exchange, chain) -> {
            if (routeValidator.isSecured.test(exchange.getRequest())) {
                if (!exchange.getRequest().getHeaders().containsKey(HttpHeaders.AUTHORIZATION)) {
                    return Mono.error(new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Authorization header not found"));
                }

                String token = exchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
                if (token != null && token.startsWith("Bearer ")) {
                    token = token.substring(7);
                }

                if (!jwtUtil.validateToken(token)) {
                    return Mono.error(new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid token"));
                }

                return routeValidator.validateRoute(exchange.getRequest(), token)
                    .flatMap(isAuthorized -> {
                        if (!isAuthorized) {
                            return Mono.error(new ResponseStatusException(HttpStatus.FORBIDDEN, "Unauthorized for this route"));
                        }
                        return chain.filter(exchange);
                    }
                );
            }
            return chain.filter(exchange);
        };
    }

    public static class Config {
    }
}
