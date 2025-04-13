package com.example.assigment_java6.controller;

import com.example.assigment_java6.domain.User;
import com.example.assigment_java6.domain.dto.LoginDTO;
import com.example.assigment_java6.domain.dto.ResLoginDTO;
import com.example.assigment_java6.service.UserService;
import com.example.assigment_java6.util.SecurityUtil;
import com.example.assigment_java6.util.anotation.ApiMessage;
import com.example.assigment_java6.util.error.IdInvalidException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

@RestController()
public class AuthenticationController {
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final SecurityUtil securityUtil;
    private final UserService userService;
    @Value("${assigment_java6.jwt.refresh-token-validity-in-seconds}")
    private long refreshToken;
    public AuthenticationController(AuthenticationManagerBuilder authenticationManagerBuilder, SecurityUtil securityUtil, UserService userService) {
        this.authenticationManagerBuilder = authenticationManagerBuilder;
        this.securityUtil = securityUtil;
        this.userService = userService;
    }
    @PostMapping("/login")
    public ResponseEntity<ResLoginDTO>login(@Valid @RequestBody LoginDTO loginDTO) {
        //Injecting variable into input about username,password in Security
        UsernamePasswordAuthenticationToken authenticationToken
                = new UsernamePasswordAuthenticationToken(loginDTO.getUsername(), loginDTO.getPassword());
        //Authentication user but must to write function loadUserByUsername
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);

        //Set information into security context
        SecurityContextHolder.getContext().setAuthentication(authentication);
        ResLoginDTO resLoginDTO=new ResLoginDTO();
        User currentUser=this.userService.handleGetAccountbyEmail(loginDTO.getUsername());
        if(currentUser!=null){
            ResLoginDTO.UserLogin userLogin= new ResLoginDTO.UserLogin(currentUser.getId(), currentUser.getEmail(), currentUser.getUsername());
            resLoginDTO.setUserp(userLogin);
        }
        String access_token=this.securityUtil.createAccessToken(authentication.getName(), resLoginDTO.getUserp());

        resLoginDTO.setAccess_token(access_token);
        String fresh_token=this.securityUtil.createRefreshToken(loginDTO.getUsername(), resLoginDTO);
        this.userService.updateUserToken(fresh_token, loginDTO.getUsername());
        ResponseCookie responseCookie = ResponseCookie
                .from("refresh_token", fresh_token)
                .httpOnly(true)
                .secure(true)
                .path("/")
                .maxAge(refreshToken)
                .build();
        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, responseCookie.toString())
                .body(resLoginDTO);

        }
    @GetMapping("/account")
    @ApiMessage("fetch account")
    public ResponseEntity<ResLoginDTO.UserLogin> getAccount() {
        String name=SecurityUtil.getCurrentUserLogin().isPresent() ?
                SecurityUtil.getCurrentUserLogin().get() : "";
        User currentUser=this.userService.handleGetAccountbyUsername(name);
        ResLoginDTO.UserLogin userLogin = new ResLoginDTO.UserLogin();
        if(currentUser!=null){
            userLogin.setId(currentUser.getId());
            userLogin.setEmail(currentUser.getEmail());
            userLogin.setName(currentUser.getUsername());
        }
        return ResponseEntity.ok().body(userLogin);
    }
    @GetMapping("/refreshh")
    @ApiMessage("get user by refresh token")
    public ResponseEntity<ResLoginDTO> getRefreshToken(
            @CookieValue(name = "refresh_token")String refresh
    ) throws IdInvalidException {
        Jwt decodedTokne= this.securityUtil.checkValidRefreshToken(refresh);
        String email = decodedTokne.getSubject();
        User curUser=this.userService.getUserByRefreshTokenAndEmail(refresh,email);
        if(curUser==null){
            throw new IdInvalidException("refresh ko hợp lệ");
        }
        ResLoginDTO resLoginDTO=new ResLoginDTO();
        User currentUser=this.userService.handleGetAccountbyEmail(email);
        if(currentUser!=null){
            ResLoginDTO.UserLogin userLogin= new ResLoginDTO.UserLogin(currentUser.getId(), currentUser.getEmail(), currentUser.getUsername());
            resLoginDTO.setUserp(userLogin);
        }
        String access_token=this.securityUtil.createAccessToken(email, resLoginDTO.getUserp());

        resLoginDTO.setAccess_token(access_token);
        String new_fresh_token=this.securityUtil.createRefreshToken(email, resLoginDTO);
        this.userService.updateUserToken(new_fresh_token,email );
        ResponseCookie responseCookie = ResponseCookie
                .from("refresh_token", new_fresh_token)
                .httpOnly(true)
                .secure(true)
                .path("/")
                .maxAge(refreshToken)
                .build();
        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, responseCookie.toString())
                .body(resLoginDTO);

    }
    @PostMapping("/logout_user")
    @ApiMessage("Logout User")
    public ResponseEntity<Void> logout() throws IdInvalidException {
        String email=SecurityUtil.getCurrentUserLogin().isPresent()? SecurityUtil.getCurrentUserLogin().get() : "";
        if(email.equals("")){
            throw new IdInvalidException("acces token ko hợp lệ");
        }
        this.userService.updateUserToken(null,email);
        ResponseCookie deleteSpringCookie = ResponseCookie
                .from("refresh_token", null)
                .httpOnly(true)
                .secure(true)
                .path("/")
                .maxAge(0)
                .build();
        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, deleteSpringCookie.toString())
                .body(null);

    }
    @GetMapping("/public")
    public String home() {
        return "Public Endpoint";
    }

    @GetMapping("/secure")
    public String secure() {
        return "Secured Endpoint - Authenticated User Only";
    }
    @GetMapping("/indexx")
    public String indexx() {
        return "This is indexx";
    }
    @GetMapping("/refresh")
    public String refresh() {
        return "This is refresh";
    }


}
