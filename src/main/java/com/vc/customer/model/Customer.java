package com.vc.customer.model;
import jakarta.persistence.*; import jakarta.validation.constraints.*; import lombok.*;
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
@Entity @Table(name="customer")
public class Customer extends BaseAudit {
  @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
  @NotBlank @Column(nullable=false, length=200) private String name;
  @Email @Column(nullable=false, unique=true, length=200) private String email;
  @OneToOne(mappedBy="customer", cascade=CascadeType.ALL, orphanRemoval=true, fetch=FetchType.LAZY)
  private Wallet wallet;
}
