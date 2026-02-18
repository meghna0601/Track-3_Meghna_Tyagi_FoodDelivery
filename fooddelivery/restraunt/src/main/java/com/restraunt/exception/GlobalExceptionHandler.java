package com.restraunt.exception;

import com.restraunt.dto.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(RequestSentFailedException.class)
    public ResponseEntity<ApiResponse> handleResourceNotSavedException(RequestSentFailedException resourceNotSavedException) {
        ApiResponse apiResponse = ApiResponse.builder()
                .message(resourceNotSavedException.getMessage())
                .successStatus(false)
                .httpStatus(HttpStatus.UNPROCESSABLE_ENTITY)
                .build();
        return new ResponseEntity<>(apiResponse,HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(RestrauntNotFoundException.class)
    public ResponseEntity<ApiResponse> handleResourceNotSavedException(RestrauntNotFoundException resourceNotSavedException) {
        ApiResponse apiResponse = ApiResponse.builder()
                .message(resourceNotSavedException.getMessage())
                .successStatus(false)
                .httpStatus(HttpStatus.NOT_FOUND)
                .build();
        return new ResponseEntity<>(apiResponse,HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ItemNotFoundException.class)
    public ResponseEntity<ApiResponse> handleResourceNotSavedException(ItemNotFoundException resourceNotSavedException) {
        ApiResponse apiResponse = ApiResponse.builder()
                .message(resourceNotSavedException.getMessage())
                .successStatus(false)
                .httpStatus(HttpStatus.NOT_FOUND)
                .build();
        return new ResponseEntity<>(apiResponse,HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse> handleResourceNotSavedException(Exception resourceNotSavedException) {
        ApiResponse apiResponse = ApiResponse.builder()
                .message(resourceNotSavedException.getMessage())
                .successStatus(false)
                .httpStatus(HttpStatus.INTERNAL_SERVER_ERROR)
                .build();
        return new ResponseEntity<>(apiResponse,HttpStatus.BAD_REQUEST);
    }

}
