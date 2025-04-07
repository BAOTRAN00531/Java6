package com.example.assigment_java6.controller;

import com.example.assigment_java6.domain.User;
import com.example.assigment_java6.domain.dto.ResultPaginationDTO;
import com.example.assigment_java6.service.UserService;
import com.example.assigment_java6.util.error.IdInvalidException;
import com.turkraft.springfilter.boot.Filter;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
public class AccountController {
    //Injecting AccountService to handle tasks about Account
       private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    public AccountController(UserService userService, PasswordEncoder passwordEncoder) {
            this.userService = userService;
           this.passwordEncoder = passwordEncoder;
    }

        @PostMapping("/users")
        public ResponseEntity<User> TestcreateAccountController(@RequestBody User postManUser) {
            String hashPassword = passwordEncoder.encode(postManUser.getPassword());
            postManUser.setPassword(hashPassword);
            User newuser=this.userService.handleCreateAccount(postManUser);
            return ResponseEntity.status(HttpStatus.CREATED).body(newuser);
        }
        //When deleting account if id's invalid else try exception and return a string have described about "Id don't bigger than 1500"
         @DeleteMapping("/users/{id}")
         public ResponseEntity<String> deleteAccount(@PathVariable("id") long id) throws IdInvalidException {
            if (id>=1500){
                throw new IdInvalidException("Id don't bigger than 1500");
            }
            this.userService.handleDeleteAccount(id);
          return ResponseEntity.status(HttpStatus.OK).body("Account deleted");
         }
         @GetMapping("/users/{id}")
         public ResponseEntity<User> getAccountbyId(@PathVariable("id") long id) {

           User getAccountbyId = this.userService.handleGetAccountbyId(id);
           return ResponseEntity.status(HttpStatus.OK).body(getAccountbyId);
         }
         @GetMapping("/users")
         public ResponseEntity<ResultPaginationDTO> getallAccount(
                 @Filter Specification<User> spec,
                 Pageable pageable
//                @RequestParam("current")Optional<String> currentOptional,
//                @RequestParam("pageSize")Optional<String> pageSizeOptional
         )
         {
//             String sCurent=currentOptional.isPresent()?currentOptional.get():"";
//             String sPageSize=pageSizeOptional.isPresent()?pageSizeOptional.get():"";
//
//             int current=Integer.parseInt(sCurent);
//             int pageSize=Integer.parseInt(sPageSize);
//
//             Pageable pageable = PageRequest.of(current - 1, pageSize);
             return ResponseEntity.status(HttpStatus.OK).body(this.userService.handleGetAllAccount(spec,pageable));
         }
         @PutMapping("/users")
         public ResponseEntity<User> updateAccount(@RequestBody User postManUser) {
            User updateAcc=this.userService.handleUpateAccount(postManUser);
            return ResponseEntity.status(HttpStatus.OK).body(updateAcc);
         }
}
