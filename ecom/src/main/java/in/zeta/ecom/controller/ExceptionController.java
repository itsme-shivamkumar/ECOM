package in.zeta.ecom.controller;

import in.zeta.ecom.entity.ApiError;
import in.zeta.ecom.exceptions.*;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;


@RestController
@ControllerAdvice
public class ExceptionController extends ResponseEntityExceptionHandler {
    @ExceptionHandler(value = NotAuthorizedException.class)
    public ResponseEntity<ApiError> handleNotAuthenticatedError() {
        ApiError error = new ApiError(401, "You are not authorized!", new Date());
        return new ResponseEntity<>(error, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(value = WrongCredentialException.class)
    public ResponseEntity<ApiError> handleWrongCredentialError() {
        ApiError error = new ApiError(401, "Wrong Credentials", new Date());
        return new ResponseEntity<>(error, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(value = UserAlreadyExist.class)
    public ResponseEntity<ApiError> handleUserAlreadyExist() {
        ApiError error = new ApiError(400, "User already exist ", new Date());
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = NoAddressFoundForUser.class)
    public ResponseEntity<ApiError> handleNoAddressFoundForUser(String phone) {
        ApiError error = new ApiError(404, "No user is available with this phone number " + phone, new Date());
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(value = TokenExpiredException.class)
    public ResponseEntity<ApiError> handleTokenExpired() {
        ApiError error = new ApiError(400, "Your token is expired, please sign in again!", new Date());
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = UserNotFoundException.class)
    public ResponseEntity<ApiError> handleUserNotFound(){
        ApiError error= new ApiError(404,"user is not found",new Date());
        return new ResponseEntity<>(error,HttpStatus.NOT_FOUND);
    }

    @Override
    public ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
                                                               HttpHeaders headers, HttpStatus status, WebRequest request) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach(error -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
    }
}
