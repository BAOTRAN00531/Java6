package com.example.assigment_java6.controller;

import com.example.assigment_java6.util.error.IdInvalidException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class IndexController {
    @GetMapping("/")
    public String index() throws IdInvalidException {
       // Simulate all case when return always variable's true then try exception and return a string have described about:"check index"
        if (true) {
            throw new IdInvalidException("check index");
        }
        return "Hello World";
    }
}
