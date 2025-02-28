package ru.yandex.practicum.filmorate.exception.handler;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.yandex.practicum.filmorate.exception.*;

import java.util.Map;

@RestControllerAdvice
public class ErrorHandler {
    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ErrorResponse> handleNotFound(final NotFoundException ex) {
        return buildErrorResponse(HttpStatus.NOT_FOUND, ex, "NOT_FOUND");
    }

    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<ErrorResponse> handleValidation(final ValidationException ex) {
        return buildErrorResponse(HttpStatus.BAD_REQUEST, ex, "VALIDATION_ERROR");
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleSpringValidation(final MethodArgumentNotValidException ex) {
        return buildErrorResponse(HttpStatus.BAD_REQUEST, ex, "SPRING_VALIDATION_ERROR");
    }

    @ExceptionHandler(FriendValidationException.class)
    public ResponseEntity<ErrorResponse> handleFriendValidation(final FriendValidationException ex) {
        return buildErrorResponse(HttpStatus.BAD_REQUEST, ex, "FRIEND_VALIDATION_ERROR");
    }

    @ExceptionHandler(FilmValidationException.class)
    public ResponseEntity<ErrorResponse> handleFilmValidation(final FilmValidationException ex) {
        return buildErrorResponse(HttpStatus.BAD_REQUEST, ex, "FILM_VALIDATION_ERROR");
    }

    @ExceptionHandler(LikeValidationException.class)
    public ResponseEntity<ErrorResponse> handleLikeValidation(final LikeValidationException ex) {
        return buildErrorResponse(HttpStatus.NOT_FOUND, ex, "LIKE_VALIDATION_ERROR");
    }

    private static ResponseEntity<ErrorResponse> buildErrorResponse(HttpStatus status, Exception ex, String errorCode) {
        ErrorResponse errorResponse = new ErrorResponse(
                status.value(),
                errorCode,
                "Ошибка_валидации_полей",
                Map.of("errorMessage", ex.getMessage()));
        return ResponseEntity.status(status).body(errorResponse);
    }
}
