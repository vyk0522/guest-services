package com.onejava.exception;

import com.onejava.model.ServiceErrorResponse;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.validation.ConstraintViolationException;
import java.io.IOException;
import java.nio.file.AccessDeniedException;
import java.time.OffsetDateTime;
import java.time.temporal.ChronoUnit;
import java.util.stream.Collectors;

@ControllerAdvice
@RestController
public class GuestServiceExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(Exception.class)
    public final ResponseEntity<ServiceErrorResponse> handleAllExceptions(Exception ex, WebRequest request) {
        ServiceErrorResponse errorResponse = new ServiceErrorResponse(ex.getMessage()
                ,ServiceErrorCode.INTERNAL_SERVICE_ERROR, OffsetDateTime.now().truncatedTo( ChronoUnit.SECONDS ));

        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(GuestNotFoundException.class)
    public final ResponseEntity<ServiceErrorResponse> handleGuestNotFoundException(GuestNotFoundException ex, WebRequest request) {
        ServiceErrorResponse errorResponse = new ServiceErrorResponse(ex.getMessage()
                ,ServiceErrorCode.GUEST_NOT_FOUND, OffsetDateTime.now().truncatedTo( ChronoUnit.SECONDS ));

        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        String defaultMessage = ex.getBindingResult().getFieldErrors()
                .stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .collect(Collectors.joining(", "));

        ServiceErrorResponse errorResponse = new ServiceErrorResponse(defaultMessage
                ,ServiceErrorCode.BAD_REQUEST, OffsetDateTime.now().truncatedTo( ChronoUnit.SECONDS ));
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }


    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ServiceErrorResponse> constraintViolationException(ConstraintViolationException ex) throws IOException {
        ServiceErrorResponse errorResponse = new ServiceErrorResponse(ex.getMessage()
                ,ServiceErrorCode.INVALID_PARAMETER, OffsetDateTime.now().truncatedTo( ChronoUnit.SECONDS ));

        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler({AccessDeniedException.class})
    public ResponseEntity<ServiceErrorResponse> handlePreAuthenticationException(AccessDeniedException ex, WebRequest request) {
        ServiceErrorResponse errorResponse = new ServiceErrorResponse(ex.getMessage()
                ,ServiceErrorCode.UNAUTHORIZED, OffsetDateTime.now().truncatedTo( ChronoUnit.SECONDS ));
        return new ResponseEntity<>(errorResponse, HttpStatus.UNAUTHORIZED);
    }

    /*

    @ExceptionHandler({ExpiredJwtException.class})
    public ResponseEntity<ServiceErrorResponse> handleExpiredJwtException(ExpiredJwtException ex, WebRequest request) {
        ServiceErrorResponse errorResponse = new ServiceErrorResponse(ex.getMessage()
                ,ServiceErrorCode.JWT_EXPIRED, OffsetDateTime.now().truncatedTo( ChronoUnit.SECONDS ));

        return new ResponseEntity<>(errorResponse, HttpStatus.UNAUTHORIZED);
    }*/



}
