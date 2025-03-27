package com.example.assigment_java6.util.error;

import com.example.assigment_java6.domain.ResetResponse;
import jdk.jshell.spi.ExecutionControl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalException {
    @ExceptionHandler(value = {
            IdInvalidException.class,
            ExecutionControl.UserException.class,
            BadCredentialsException.class
    })

    //Config global exception and setup information to show status code,message,error.
    // After that return these variable into body
    public ResponseEntity< ResetResponse<Object>> IdInvalidException(IdInvalidException idException) {
        ResetResponse<Object> rest = new ResetResponse<Object>();
        rest.setStatusCode(HttpStatus.BAD_REQUEST.value());
        rest.setMessageError(idException.getMessage());
        rest.setMessage("IdInvalidException");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(rest);
    }
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ResetResponse<Object>> validError(MethodArgumentNotValidException methodArgumentNotValidException) {
        BindingResult bindingResult = methodArgumentNotValidException.getBindingResult();
        final List<FieldError> fieldErrors = bindingResult.getFieldErrors();
        ResetResponse<Object> res=new ResetResponse<Object>();
        res.setStatusCode(HttpStatus.BAD_REQUEST.value());
        res.setMessageError(methodArgumentNotValidException.getBody().getDetail());
        List<String> errors = fieldErrors.stream().map(f -> f.getDefaultMessage()).collect(Collectors.toList());
        res.setMessage(errors.size()>1?errors:errors.get(0));
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(res);
    }

}

