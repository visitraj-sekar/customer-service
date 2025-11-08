package com.vc.customer.model;
import jakarta.persistence.*; import lombok.*;
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
@Entity @Table(name="wallet")
public class Wallet extends BaseAudit {
  @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
  @OneToOne @JoinColumn(name="customer_id", nullable=false, unique=true) private Customer customer;
  @Column(nullable=false) private Double balance = 0.0;
  @Column(nullable=false, length=10) private String currency = "INR";
}
