package com.example.assigment_java6.controller;

import com.example.assigment_java6.domain.User;
import com.example.assigment_java6.domain.dto.ResultPaginationDTO;
import com.example.assigment_java6.service.UserService;
import com.example.assigment_java6.util.anotation.ApiMessage;
import com.example.assigment_java6.util.error.IdInvalidException;
import com.turkraft.springfilter.boot.Filter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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
    private static final Logger log = LoggerFactory.getLogger(AccountController.class);
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
        @ApiMessage("fetch all users")
        public ResponseEntity<ResultPaginationDTO> getallAccount(
                @RequestParam(value = "email", required = false) String email, // Lọc tương đối theo email
                @RequestParam(value = "fullname", required = false) String fullname, // Lọc tương đối theo tên
                @RequestParam(value = "phone", required = false) String phone, // Lọc tương đối theo số điện thoại
                @RequestParam(value = "username", required = false) String username, // Lọc tương đối theo username
                @RequestParam(value = "current", defaultValue = "1") int current, // Trang hiện tại
                @RequestParam(value = "pageSize", defaultValue = "10") int pageSize, // Kích thước trang
                @RequestParam(value = "sort", required = false) String sort // Sắp xếp
        ) {
            // Xử lý tham số sort
            Sort sortOption = Sort.unsorted();
            if (sort != null && !sort.isEmpty()) {
                String[] sortParts = sort.split(",");
                if (sortParts.length == 2) {
                    String field = sortParts[0];
                    String direction = sortParts[1];
                    if ("asc".equalsIgnoreCase(direction)) {
                        sortOption = Sort.by(Sort.Direction.ASC, field);
                    } else if ("desc".equalsIgnoreCase(direction)) {
                        sortOption = Sort.by(Sort.Direction.DESC, field);
                    }
                }
            }

            // Tạo Pageable với sort
            Pageable pageable = PageRequest.of(current - 1, pageSize, sortOption);

            // Tạo Specification với các bộ lọc tương đối
            Specification<User> spec = Specification.where(null);
            if (email != null && !email.isEmpty()) {
                spec = spec.and((root, query, criteriaBuilder) ->
                        criteriaBuilder.like(root.get("email"), "%" + email + "%"));
            }
            if (fullname != null && !fullname.isEmpty()) {
                spec = spec.and((root, query, criteriaBuilder) ->
                        criteriaBuilder.like(root.get("fullname"), "%" + fullname + "%"));
            }
            if (phone != null && !phone.isEmpty()) {
                spec = spec.and((root, query, criteriaBuilder) ->
                        criteriaBuilder.like(root.get("phone"), "%" + phone + "%"));
            }
            if (username != null && !username.isEmpty()) {
                spec = spec.and((root, query, criteriaBuilder) ->
                        criteriaBuilder.like(root.get("username"), "%" + username + "%"));
            }

            // Gọi service
            return ResponseEntity.status(HttpStatus.OK).body(this.userService.handleGetAllAccount(spec, pageable));
        }
         @PutMapping("/users")
         public ResponseEntity<User> updateAccount(@RequestBody User postManUser) {
            User updateAcc=this.userService.handleUpateAccount(postManUser);
            return ResponseEntity.status(HttpStatus.OK).body(updateAcc);
         }

}
