package com.ogooueTechnology.referentiel.Config;

import com.ogooueTechnology.referentiel.service.AuditLogService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class AuditFilter extends OncePerRequestFilter {

    private final AuditLogService auditLogService;

    public AuditFilter(AuditLogService auditLogService) {
        this.auditLogService = auditLogService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        filterChain.doFilter(request, response); // ⚠️ important : exécuter d'abord la requête

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth != null && auth.isAuthenticated()
                && !"anonymousUser".equals(auth.getPrincipal())) {

            String email = auth.getName();
            String role = auth.getAuthorities().toString();

            auditLogService.logAction(
                    request.getMethod() + " " + request.getRequestURI(),
                    "Appel API automatique",
                    null,
                    email,
                    role,
                    request.getRemoteAddr(),
                    request.getHeader("User-Agent")
            );
        }
    }
}