package com.mughrabi.rest.webservices.restful_web_services.exception;

import java.time.LocalDateTime;

public class ErrorDetails {
    // timestamp
    private LocalDateTime timestamp;
    // message
    private String message;
    // details
    private String details;
    
    public ErrorDetails(LocalDateTime timestamp, String message, String details) {
        this.timestamp = timestamp;
        this.message = message;
        this.details = details;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public String getMessage() {
        return message;
    }

    public String getDetails() {
        return details;
    }
    
}
