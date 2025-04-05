package com.example.assigment_java6.domain;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import java.time.Instant;
import java.util.Date;

@Data
@Entity
@Table(name = "Orders")
public class Order {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Double totalPrice;
    @Enumerated(EnumType.STRING)
    private OrderStatus status; // PENDING, SHIPPED, COMPLETED

    private Instant createdAt;

    // Quan hệ với Account (Khách hàng)
    @ManyToOne
    @JoinColumn(name = "account_id")
    private User user;
}
