package com.example.assigment_java6.util;

import com.example.assigment_java6.domain.ResetResponse;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

@ControllerAdvice
public class FormatRestResponse implements ResponseBodyAdvice{
    //Calling function have supported of ControllerAdvice to handle thread flow about exception
    @Override
    public boolean supports(MethodParameter returnType, Class converterType) {
        return true;
    }

    @Override
    public Object beforeBodyWrite(
            Object body,
            MethodParameter returnType,
            MediaType selectedContentType,
            Class selectedConverterType,
            ServerHttpRequest request,
            ServerHttpResponse response)
    {

        HttpServletResponse servletResponse = ((ServletServerHttpResponse) response).getServletResponse();
        //Fetching value to have status code and set value type is int to easy performance
        int statusCode = servletResponse.getStatus();
        ResetResponse<Object> rest = new ResetResponse<Object>();
        rest.setStatusCode(statusCode);
        if (body instanceof String) {
            return body;
        }
        //Handling for status code case with different variable value will be handled as
        //If it's equal to or above 400 then it will call body
        if(statusCode >=400 )
        {
            //case error
           return body;
        //Otherwise it will still return body and a message about "CALL API SUCCESS"
        }else {
            //case success
            rest.setData(body);
             rest.setMessageError("Dont Something went wrong");
            rest.setMessage("CALL API SUCCESS");
        }
        return rest;
    }
}
