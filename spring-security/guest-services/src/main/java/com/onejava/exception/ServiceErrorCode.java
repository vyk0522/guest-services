package com.onejava.exception;


import com.fasterxml.jackson.annotation.JsonValue;
import org.springframework.http.HttpStatus;

public enum ServiceErrorCode {

    INTERNAL_SERVICE_ERROR("001", HttpStatus.INTERNAL_SERVER_ERROR),
    UNAUTHORIZED("002", HttpStatus.UNAUTHORIZED),
    GUEST_NOT_FOUND("003", HttpStatus.NOT_FOUND),

    JWT_EXPIRED("020", HttpStatus.UNAUTHORIZED),
    JWT_MALFORMED("021", HttpStatus.UNAUTHORIZED),
    JWT_EXCEPTION("022", HttpStatus.UNAUTHORIZED),

    DOWNSTREAM_SERVICE_ERROR("004", HttpStatus.INTERNAL_SERVER_ERROR),
    DOWNSTREAM_SERVICE_UNAVAILABLE("005", HttpStatus.SERVICE_UNAVAILABLE),
    CAPACITY_EXCEEDED("006", HttpStatus.SERVICE_UNAVAILABLE),
    BAD_REQUEST("007", HttpStatus.BAD_REQUEST),
    MISSING_PARAMETER("008", HttpStatus.BAD_REQUEST),
    MISSING_ADDRESS_PARAMETER("009", HttpStatus.BAD_REQUEST),
    MISSING_HEADER("010", HttpStatus.BAD_REQUEST),
    INVALID_PARAMETER("011", HttpStatus.BAD_REQUEST);


    public final String label;
    public final HttpStatus associatedStatus;

    private ServiceErrorCode(String label, HttpStatus associatedStatus) {
        this.label = label;
        this.associatedStatus = associatedStatus;
    }

    @JsonValue
    public String getLabel() {
        return label;
    }

    public HttpStatus getAssociatedStatus() {
        return associatedStatus;
    }

}
