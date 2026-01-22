package com.alura.flight_prediction_api.gateway.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

import java.util.Collections;

@Component
public class SecurityFilter implements WebFilter {

    @Autowired
    private TokenService tokenService;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        var tokenJWT = recuperarToken(exchange);

        if (tokenJWT != null) {
            try {
                var subject = tokenService.getSubject(tokenJWT);
                // Creamos una autenticación simple solo con el usuario del token
                var authentication = new UsernamePasswordAuthenticationToken(subject, null, Collections.emptyList());

                return chain.filter(exchange)
                        .contextWrite(ReactiveSecurityContextHolder.withAuthentication(authentication));
            } catch (RuntimeException e) {
                // Si el token es invalido, no se deja pasar
                return chain.filter(exchange);
            }
        }
        return chain.filter(exchange);
    }

    private String recuperarToken(ServerWebExchange exchange) {
        var authHeader = exchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            return authHeader.replace("Bearer ", "");
        }
        return null;
    }
}