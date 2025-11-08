package com.vc.customer.model;
import jakarta.persistence.*; import lombok.*;
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
@Entity @Table(name="merchant")
public class Merchant extends BaseAudit {
  @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
  @Column(nullable=false, length=200) private String name;
  @Column(nullable=false, length=200) private String email;
  @Column(nullable=false, length=10) private String currency = "INR";
}
