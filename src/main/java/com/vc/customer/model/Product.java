package com.vc.customer.model;
import jakarta.persistence.*; import lombok.*;
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
@Entity @Table(name="order_product")
public class Product extends BaseAudit {
  @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
  @ManyToOne(fetch=FetchType.LAZY) @JoinColumn(name="order_id", nullable=false) private Order order;
  @Column(nullable=false, length=200) private String name;
  @Column(columnDefinition="TEXT") private String description;
  @Column(nullable=false) private Double price;
  @Column(nullable=false, length=10) private String currency = "INR";
  @Column(nullable=false) private Long merchantId;
  @Column(nullable=false) private Integer order_quantity;
}
