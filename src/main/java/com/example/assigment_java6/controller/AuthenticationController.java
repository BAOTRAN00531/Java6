package com.example.assigment_java6.controller;

import com.example.assigment_java6.domain.dto.LoginDTO;
import com.example.assigment_java6.domain.dto.ResLoginDTO;
import com.example.assigment_java6.util.SecurityUtil;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthenticationController {
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final SecurityUtil securityUtil;
    public AuthenticationController(AuthenticationManagerBuilder authenticationManagerBuilder, SecurityUtil securityUtil) {
        this.authenticationManagerBuilder = authenticationManagerBuilder;
        this.securityUtil = securityUtil;
    }

    @PostMapping("/login")
    public ResponseEntity<ResLoginDTO>login(@Valid @RequestBody LoginDTO loginDTO) {
        //Injecting variable into input about username,password in Security
        UsernamePasswordAuthenticationToken authenticationToken
                = new UsernamePasswordAuthenticationToken(loginDTO.getUsername(), loginDTO.getPassword());
        //Authentication user but must to write function loadUserByUsername
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);

        // Injecting variable of authentication process into function for create a JWT to storage userdata
        String accsess_token=this.securityUtil.createToken(authentication);
        //Set information into security context
        SecurityContextHolder.getContext().setAuthentication(authentication);
        ResLoginDTO resLoginDTO=new ResLoginDTO();
        resLoginDTO.setAccess_token(accsess_token);
        return ResponseEntity.ok().body(resLoginDTO);

}}
