package com.onejava.exception;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.onejava.model.ServiceErrorResponse;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.MalformedJwtException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class JwtExceptionHandlerFilter extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            filterChain.doFilter(request, response);
        }
        catch (ExpiredJwtException ex){
            writeErrorResponse(response, "JWT expired", ServiceErrorCode.JWT_EXPIRED);
        }
        catch (MalformedJwtException ex){
            writeErrorResponse(response, ex.getMessage(), ServiceErrorCode.JWT_MALFORMED);

        }
        catch (JwtException ex) {
            writeErrorResponse(response, ex.getMessage(), ServiceErrorCode.JWT_EXCEPTION);
        }
    }

    private void writeErrorResponse(HttpServletResponse response, String message, ServiceErrorCode serviceErrorCode) throws IOException {
        ServiceErrorResponse errorResponse = new ServiceErrorResponse(message
                , serviceErrorCode, null);
        response.setStatus(HttpStatus.BAD_REQUEST.value());
        response.setContentType("application/json");
        response.getWriter().write(convertObjectToJson(errorResponse));
    }

    private String convertObjectToJson(ServiceErrorResponse errorResponse) throws JsonProcessingException {
        if (errorResponse == null) {
            return null;
        }
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsString(errorResponse);
    }

}
