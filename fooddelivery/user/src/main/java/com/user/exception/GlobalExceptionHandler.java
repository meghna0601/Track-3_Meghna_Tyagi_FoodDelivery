package com.user.exception;

import com.user.dto.ApiResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(ResourceNotSavedException.class)
    public ResponseEntity<ApiResponse> handleResourceNotSavedException(ResourceNotSavedException resourceNotSavedException) {
        ApiResponse apiResponse = ApiResponse.builder()
                .message(resourceNotSavedException.getMessage())
                .successStatus(false)
                .httpStatus(HttpStatus.UNPROCESSABLE_ENTITY)
                .build();
        return new ResponseEntity<>(apiResponse,HttpStatus.BAD_REQUEST);
    }



    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiResponse> handleResourceNotFoundException(ResourceNotFoundException resourceNotFoundException) {
        ApiResponse apiResponse = ApiResponse.builder()
                .message(resourceNotFoundException.getMessage())
                .successStatus(false)
                .httpStatus(HttpStatus.NOT_FOUND)
                .build();
        return new ResponseEntity<>(apiResponse,HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ResourceAlreadyExistException.class)
    public ResponseEntity<ApiResponse> handleResourceAlreadyExists(ResourceAlreadyExistException resourceAlreadyExistException) {
        ApiResponse apiResponse = ApiResponse.builder()
                .message(resourceAlreadyExistException.getMessage())
                .successStatus(false)
                .httpStatus(HttpStatus.FOUND)
                .build();
        return new ResponseEntity<>(apiResponse,HttpStatus.NOT_ACCEPTABLE);
    }

    @ExceptionHandler(NoSuchAlgorithmException.class)
    public ResponseEntity<ApiResponse> handleNoSuchAlgorithmException(NoSuchAlgorithmException noSuchAlgorithmException) {
        ApiResponse apiResponse = ApiResponse.builder()
                .message(noSuchAlgorithmException.getMessage())
                .successStatus(false)
                .httpStatus(HttpStatus.NOT_FOUND)
                .build();
        return new ResponseEntity<>(apiResponse,HttpStatus.OK);
    }

    @ExceptionHandler(ServiceNotAvailableException.class)
    public ResponseEntity<ApiResponse> handleServiceNotAvailableException(ServiceNotAvailableException serviceNotAvailableException) {
        ApiResponse apiResponse = ApiResponse.builder()
                .message(serviceNotAvailableException.getMessage())
                .successStatus(false)
                .httpStatus(HttpStatus.SERVICE_UNAVAILABLE)
                .build();
        return new ResponseEntity<>(apiResponse,HttpStatus.OK);
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


    ///  for validation handler
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
                                                                  HttpHeaders headers, HttpStatus status,
                                                                  WebRequest request) {

        Map<String,String> errors = new HashMap<>();
        List<ObjectError> errorList = ex.getBindingResult().getAllErrors();
        errorList.forEach(error -> {
            String field = ((FieldError)error).getField();
            String message = error.getDefaultMessage();
            errors.put(field,message);
        });
        return new ResponseEntity<>(errors,HttpStatus.BAD_REQUEST);
    }
}
