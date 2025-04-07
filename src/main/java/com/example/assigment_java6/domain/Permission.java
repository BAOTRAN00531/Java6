package com.example.assigment_java6.domain;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.Instant;
import java.util.Date;

@Data
@Entity
@Table(name = "Permissions")
public class Permission {
        @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name; // VD: "MANAGE_ORDERS"
    private String apiPath; // API được cấp quyền
    private String method; // GET, POST, PUT, DELETE
    private String module; // ORDER, PRODUCT...
    private Instant createdAt;
    private Instant updatedAt;

}
