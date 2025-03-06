package com.example.demo.filter;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import com.example.demo.service.JwtService;
import com.example.demo.service.UserServiceImpl;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtFilter extends OncePerRequestFilter {


	@Autowired
    private JwtService jwtService;

    @Autowired
    private ApplicationContext context;

@Override
protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
    String authHeader = request.getHeader("Authorization");
    String token = null;
    String username = null;

    if (request.getRequestURI().equals("/auth/login") || request.getRequestURI().equals("/auth/register")) {
        filterChain.doFilter(request, response);
        return;
    }

    if (authHeader == null || !authHeader.startsWith("Bearer ")) {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.getWriter().write("Missing or invalid Authorization header");
        response.getWriter().flush();
        return;
    }

    token = authHeader.substring(7);
    try {
        username = jwtService.extractUserName(token);
    } catch (Exception e) {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.getWriter().write("Invalid token format");
        response.getWriter().flush();
        return;
    }

    if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
        UserDetails userDetails = context.getBean(UserServiceImpl.class).loadUserByUsername(username);

        if (jwtService.validateToken(token, userDetails)) {
            UsernamePasswordAuthenticationToken authToken =
                    new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
            authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            SecurityContextHolder.getContext().setAuthentication(authToken);
        } else {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("Token is invalid or expired");
            response.getWriter().flush();
            return;
        }
    }

    filterChain.doFilter(request, response);
}
	    
}

	

