package com.rushhour_app.infrastructure.security.filter;

import com.rushhour_app.infrastructure.security.service.AuthService;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Set;

@Component
public class JwtFilter extends OncePerRequestFilter {

    private final AuthService authService;
    private final Set<String> excludedUrls = Set.of("/authenticate", "/v3/api-docs", "swagger-ui");

    public JwtFilter(AuthService authService) {
        this.authService = authService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        String bearerToken = request.getHeader(HttpHeaders.AUTHORIZATION);

        if (bearerToken == null || !bearerToken.startsWith("Bearer")) {
            response.sendError(403);
            return;
        }

        String token = bearerToken.split(" ")[1];

        var userDetails = authService.validateAndGetDetails(token);
        SecurityContext context = SecurityContextHolder.getContext();
        var authenticationToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
        context.setAuthentication(authenticationToken);

        filterChain.doFilter(request, response);
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        return excludedUrls.stream().anyMatch(url -> request.getRequestURI().contains(url));
    }
}
