package com.example.webapp.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Global exception handler for the application
 * 
 * Handles exceptions thrown by controllers and provides
 * appropriate error responses to users.
 */
@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    /**
     * Handles RuntimeException and its subclasses
     * Returns a 500 Internal Server Error status
     * 
     * @param ex The exception that was thrown
     * @param model The model to add error attributes to
     * @return The error view name
     */
    @ExceptionHandler(RuntimeException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public String handleRuntimeException(RuntimeException ex, Model model) {
        log.error("Runtime exception occurred: {}", ex.getMessage(), ex);
        model.addAttribute("error", "An unexpected error occurred");
        model.addAttribute("message", ex.getMessage());
        return "error";
    }

    /**
     * Handles IllegalArgumentException
     * Returns a 400 Bad Request status
     * 
     * @param ex The exception that was thrown
     * @param model The model to add error attributes to
     * @return The error view name
     */
    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String handleIllegalArgumentException(IllegalArgumentException ex, Model model) {
        log.error("Invalid argument: {}", ex.getMessage(), ex);
        model.addAttribute("error", "Invalid request");
        model.addAttribute("message", ex.getMessage());
        return "error";
    }

    /**
     * Handles generic Exception as a fallback
     * Returns a 500 Internal Server Error status
     * 
     * @param ex The exception that was thrown
     * @param model The model to add error attributes to
     * @return The error view name
     */
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public String handleException(Exception ex, Model model) {
        log.error("Unexpected exception occurred: {}", ex.getMessage(), ex);
        model.addAttribute("error", "An unexpected error occurred");
        model.addAttribute("message", "Please try again later");
        return "error";
    }
}
