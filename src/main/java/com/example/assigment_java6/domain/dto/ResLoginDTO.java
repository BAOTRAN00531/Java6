package com.example.assigment_java6.domain.dto;

import lombok.*;

@Data
public class ResLoginDTO {
    private String access_token;
    private UserLogin userp;
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
   public static class UserLogin {
       private long id;
       private String email;
       private String name;
   }
}
