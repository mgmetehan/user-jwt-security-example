package com.mgmetehan.user_jwt_security_example.filter;

import com.mgmetehan.user_jwt_security_example.service.JwtService;
import com.mgmetehan.user_jwt_security_example.service.UserDetailsServiceImpl;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UserDetailsServiceImpl userDetailsServiceImpl;

    @Override

    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        String authHeader = request.getHeader("Authorization");

        if (isTokenPresent(authHeader)) {
            String token = extractToken(authHeader);
            String userName = jwtService.extractUsername(token);

            if (isUserNameValid(userName) && isAuthenticationAbsent()) {
                authenticateUser(request, token, userName);
            }
        }

        filterChain.doFilter(request, response);
    }

    private boolean isTokenPresent(String authHeader) {
        return authHeader != null && authHeader.startsWith("Bearer ");
    }

    private String extractToken(String authHeader) {
        return authHeader.substring(7);
    }

    private boolean isUserNameValid(String userName) {
        return userName != null;
    }

    private boolean isAuthenticationAbsent() {
        return SecurityContextHolder.getContext().getAuthentication() == null;
    }

    private void authenticateUser(HttpServletRequest request, String token, String userName) {
        UserDetails user = userDetailsServiceImpl.loadUserByUsername(userName);

        if (jwtService.validateToken(token, user)) {
            setAuthentication(request, user);
        }
    }

    private void setAuthentication(HttpServletRequest request, UserDetails user) {
        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
        authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        SecurityContextHolder.getContext().setAuthentication(authToken);
    }
}
