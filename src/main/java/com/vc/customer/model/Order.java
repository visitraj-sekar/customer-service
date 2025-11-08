package com.vc.customer.model;
import jakarta.persistence.*; import lombok.*; import java.util.*;
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
@Entity @Table(name="orders")
public class Order extends BaseAudit {
  @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
  @Column(nullable=false) private Long customerId;
  @OneToMany(mappedBy="order", cascade=CascadeType.ALL, orphanRemoval=true) private List<Product> products = new ArrayList<>();
  @Column(nullable=false, length=30) private String status = "CREATED";
  private Double totalAmount;
  @Column(length=10) private String currency = "INR";
}
