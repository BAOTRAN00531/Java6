package com.example.assigment_java6.domain;

import com.example.assigment_java6.util.SecurityUtil;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.Instant;
import java.util.Date;

@Data
@Entity
@Table(name = "Products")
public class Product {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotBlank(message = "Tên sản phẩm không được để trống")
    private String name;
    @NotNull(message = "Phải nhập giá của sản phẩm")
    private Double price;
    @NotBlank(message = "Phải có phần mô tả")
    private String description;
    private Integer stock;
    private String image; // URL ảnh sản phẩm
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss a",timezone = "GMT+7")
    private Instant createdAt;
    private Instant updatedAt;
    private String createdBy;
    private String updatedBy;

    // Quan hệ với Category
    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;
    @PrePersist
    public void handleBeforePersist() {
        this.createdBy= SecurityUtil.getCurrentUserLogin().isPresent()== true
                ? SecurityUtil.getCurrentUserLogin() .get() : "";
        this.createdAt=Instant.now();
    }
}
