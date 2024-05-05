package ru.practicum.exception;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import ru.practicum.exception.dto.ApiError;
import ru.practicum.utils.Constants;

import javax.servlet.http.HttpServletRequest;

@RestControllerAdvice
public class CustomExceptionHandler {
    private static final Logger log = LoggerFactory.getLogger(CustomExceptionHandler.class);
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiError handleValidationException(MethodArgumentNotValidException ex) {
        log.debug("BAD_REQUEST MethodArgumentNotValidException : {}", ex.getMessage());

        return ApiError
                .builder()
                .message(ex.getFieldError().getDefaultMessage())
                .reason(Constants.INCORRECTLY_MADE_REQUEST)
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.BAD_REQUEST.name())
                .build();
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiError handleTypeMismatchException(MethodArgumentTypeMismatchException ex) {
        log.debug("BAD_REQUEST MethodArgumentTypeMismatchException: {}", ex.getMessage());

        return ApiError
                .builder()
                .message(ex.getMessage())
                .reason(Constants.INCORRECTLY_MADE_REQUEST)
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.BAD_REQUEST.name())
                .build();
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ApiError handleNotFoundException(final NotFoundException e) {
        log.debug("NOT_FOUND NotFoundException: {}", e.getMessage());

        return ApiError
                .builder()
                .message(e.getMessage())
                .reason(Constants.THE_REQUIRED_OBJECT_WAS_NOT_FOUND)
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.NOT_FOUND.name())
                .build();
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiError handleValidationException(final BadRequestException e) {
        log.debug("BAD_REQUEST BadRequestException: {}", e.getMessage());

        return ApiError
                .builder()
                .message(e.getMessage())
                .reason(Constants.INCORRECTLY_MADE_REQUEST)
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.BAD_REQUEST.name())
                .build();
    }

    @ExceptionHandler(UnsupportedStatusException.class)
    public ResponseEntity<Object> handleUnsupportedStatusException(final UnsupportedStatusException e) {
        log.debug("UNSUPPORTED_STATUS: {}", e.getMessage());
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("error", "Unknown state: UNSUPPORTED_STATUS");
        body.put("message", e.getMessage());
        return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.CONFLICT)
    public ApiError handleValidationException(final ConflictException e, HttpServletRequest request) {
        log.debug("BAD_REQUEST ConflictException: {}", e.getStackTrace());

        String reason = request.getParameter("reason");
        String statusName = request.getParameter("statusName");

        return ApiError
                .builder()
                .message(e.getMessage())
                .reason((reason != null && !reason.isEmpty()) ? reason : Constants.INTEGRITY_CONSTRAINT_VIOLATED)
                .timestamp(LocalDateTime.now())
                .status((statusName != null && !statusName.isEmpty()) ? statusName : HttpStatus.CONFLICT.name())
                .build();
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ApiError handleDataIntegrityViolationException(DataIntegrityViolationException e) {
        log.debug("CONFLICT DataIntegrityViolationException {}", e.getMessage());

        return ApiError
                .builder()
                .message("Ошибка в целостности данных.")
                .reason(Constants.INTEGRITY_CONSTRAINT_VIOLATED)
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.CONFLICT.name())
                .build();
    }

}