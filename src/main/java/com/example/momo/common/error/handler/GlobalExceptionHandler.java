package com.example.momo.common.error.handler;

import com.example.momo.common.error.dto.ErrorResponseDto;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;

@Slf4j // log 객체 생성 (예외 처리 시 로그 기록하는데 사용됨)
@RestControllerAdvice
public class GlobalExceptionHandler {

    //ResponseStatusException 처리
    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<ErrorResponseDto> handlerResponseStatusException(
            ResponseStatusException ex, HttpServletRequest request) { // 발생한 예외 객체
        log.error("ResponseStatusException : {}", ex.getMessage()); // 예외 메시지 로그에 기록
        return buildErrorResponse( // 응답 객체 생성
                ex.getStatusCode().value(),
                ex.getMessage(),
                request.getRequestURI()
        );
    }

    //유효성 검사 예외처리 @Valid, @Validated
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponseDto> handlerMethodArgumentNotValidException(
            MethodArgumentNotValidException ex, HttpServletRequest request
    ) {
        FieldError fieldError = ex.getBindingResult().getFieldError();
        String errorMessage = fieldError != null ? fieldError.getDefaultMessage() : "유효성 검사 실패";
        return buildErrorResponse(
                HttpStatus.BAD_REQUEST.value(),
                errorMessage,
                request.getRequestURI()
        );
    }

    //타입 불일치 예외처리
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ErrorResponseDto> handlerMethodArgumentTypeMismatchException(
            MethodArgumentTypeMismatchException ex, HttpServletRequest request
    ) {
        return buildErrorResponse(
                HttpStatus.BAD_REQUEST.value(),
                "유효하지 않은 데이터 형식입니다",
                request.getRequestURI()
        );
    }

    // 기본적인 예외 처리
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponseDto> handleGenericException(Exception ex,
                                                                   HttpServletRequest request) {
        log.error("Unhandled Exception: {}", ex.getMessage());
        return buildErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), "서버에서 문제가 발생했습니다.",
                request.getRequestURI());
    }

    private ResponseEntity<ErrorResponseDto> buildErrorResponse(int errorCode, String message,
                                                                String path) {

        ErrorResponseDto responseDto = ErrorResponseDto.builder()
                .errorCode(errorCode)
                .message(message)
                .path(path)
                .errorTime(LocalDateTime.now())
                .build();

        return ResponseEntity.status(errorCode).body(responseDto);
    }

}
