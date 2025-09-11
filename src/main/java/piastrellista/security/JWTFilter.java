package piastrellista.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;
import piastrellista.entities.User;
import piastrellista.exceptions.UnauthorizedException;
import piastrellista.services.UserService;

import java.io.IOException;

@Component
public class JWTFilter extends OncePerRequestFilter {

    @Autowired
    private JWTTools jwtTools;

    @Autowired
    private UserService userService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        // 1. Check if the current request has an Authorization Header
        String authHeader = request.getHeader("Authorization");

        // 2. If it exists, extract the token from the header
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new UnauthorizedException("Enter the token in the Authorization Header");
        }
        String accessToken = authHeader.substring(7);

        // 3. Verify if the token has been manipulated and if it has not expired
        jwtTools.verifyToken(accessToken);

        // 4. If everything is OK, go to the next element of the Filter Chain, to eventually reach the endpoint
        // 4.1 Search for the user in the DB by id
        String id = jwtTools.extractIdFromToken(accessToken);
        User user = this.userService.getUserById(Long.parseLong(id));

        // 4.2 I need to inform Spring Security about who is the current user making the request. Somehow
        Authentication authentication = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);

        filterChain.doFilter(request, response);

    }

    // disable the filter for login or register type requests
    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        AntPathMatcher pathMatcher = new AntPathMatcher();
        String path = request.getServletPath();
        String method = request.getMethod();
        // Escludi i GET su /blog e /blog/**
        return pathMatcher.match("/auth/**", path) ||
                (method.equals("GET") && (pathMatcher.match("/blog", path) || pathMatcher.match("/blog/**", path))) ||
                pathMatcher.match("/bettellipavimenti/**", path);
    }

}
