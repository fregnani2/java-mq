package com.example.mq.security;

import com.example.mq.repository.ClientRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import com.example.mq.service.TokenService;
import java.io.IOException;

/**
 * Override default filter to validate token
 * Annotations:
 * - @Component: Indicates that an annotated class is a "Component"
 */
@Component
public class SecurityFilter extends OncePerRequestFilter {

    @Autowired
    private TokenService TokenService;

    @Autowired
    private ClientRepository clientRepository;


    /**
     * Method to validate token and set the authentication
     * @param request HttpServletRequest object
     * @param response HttpServletResponse object
     * @param filterChain FilterChain object
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        var token = this.recoverToken(request);
        if (token != null) {
           var client = TokenService.validateToken(token);
            UserDetails userDetails = clientRepository.findByEmail(client);

            var auth = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(auth);
        }
        filterChain.doFilter(request, response);
    }

    /**
     * Method to recover token from request for validation
     */
    private String recoverToken(HttpServletRequest request) {
        var token = request.getHeader("Authorization");
        if (token == null) return null;
        return token.replace("Bearer ", "");
    }
}
