package com.flexibiz.flexgateway.helper;

import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.*;
import java.util.function.Predicate;

@Component
public class RouteValidator {

    private final JWTUtil jwtUtil;

    public RouteValidator(JWTUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    private static final List<String> openUri = List.of(
            "/auth/register",
            "/auth/login"
    );

    private final Map<String, List<String>> routeRoleMap = new HashMap<>() {{
        put("/admin", List.of("ADMIN"));
        put("/user", List.of("USER", "ADMIN"));
        put("/manager", List.of("MANAGER", "ADMIN"));
        put("/test", List.of("USER"));
    }};

    public Mono<Boolean> validateRoute(ServerHttpRequest request, String token) {
        return Mono.fromSupplier(() -> {
            List<String> roles = (List<String>) jwtUtil.getRoles(token);
            List<String> allowedRoutes = getRolesForPath(request.getURI().getPath());
            return allowedRoutes == null || !Collections.disjoint(roles, allowedRoutes);
        });
    }

    private List<String> getRolesForPath(String path) {
        for (Map.Entry<String, List<String>> entry : routeRoleMap.entrySet()) {
            if (path.startsWith(entry.getKey())) {
                return entry.getValue();
            }
        }
        return null; // Si aucune route correspond
    }

    public Predicate<ServerHttpRequest> isSecured = request ->
            openUri.stream()
                    .noneMatch(uri -> request.getURI().getPath().contains(uri));
}
