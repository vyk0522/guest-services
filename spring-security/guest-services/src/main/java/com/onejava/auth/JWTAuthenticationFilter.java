package com.onejava.auth;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class JWTAuthenticationFilter extends OncePerRequestFilter {
    private static final Logger LOGGER = LoggerFactory.getLogger(JWTAuthenticationFilter.class);
    private static final String BEARER = "Bearer";
    @Autowired
    private MyHotelUserDetailsService userDetailsService;
    @Autowired
    private JwtUtil jwtUtil;


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        LOGGER.info("Process request to check for a JSON Web Token ");
        //Check for Authorization:Bearer JWT
        final String headerValue = request.getHeader("Authorization");
        String username = null;
        String jwt = null;
        Optional<String> bearerToken = getBearerToken(headerValue);

        if (bearerToken.isPresent()){
            jwt = bearerToken.get();
            username = jwtUtil.extractUsername(jwt);
        }

        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {

            UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);

            if (jwtUtil.validateToken(jwt, userDetails)) {
                UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities());

                String authorities = userDetails.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.joining());
                System.out.println("Authorities granted : " + authorities);

                usernamePasswordAuthenticationToken
                        .setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
            }
        }

        //move on to the next filter in the chains
        filterChain.doFilter(request, response);

    }


    /**
     * if present, extract the jwt token from the "Bearer <jwt>" header value.
     *
     * @param headerVal
     * @return jwt if present, empty otherwise
     */
    private Optional<String> getBearerToken(String headerVal) {
        if (headerVal != null && headerVal.startsWith(BEARER)) {
            return Optional.of(headerVal.replace("Bearer", "").trim());
        }
        return Optional.empty();
    }
}
