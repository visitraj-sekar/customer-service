package com.vc.customer.model;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "order_product")
public class Product extends BaseAudit {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	public Order getOrder() {
		return order;
	}
	public void setOrder(Order order) {
		this.order = order;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public Double getPrice() {
		return price;
	}
	public void setPrice(Double price) {
		this.price = price;
	}
	public String getCurrency() {
		return currency;
	}
	public void setCurrency(String currency) {
		this.currency = currency;
	}
	public Long getMerchantId() {
		return merchantId;
	}
	public void setMerchantId(Long merchantId) {
		this.merchantId = merchantId;
	}
	public Integer getOrder_quantity() {
		return order_quantity;
	}
	public void setOrder_quantity(Integer order_quantity) {
		this.order_quantity = order_quantity;
	}
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "order_id", nullable = false)
	private Order order;
	@Column(nullable = false, length = 200)
	private String name;
	@Column(columnDefinition = "TEXT")
	private String description;
	@Column(nullable = false)
	private Double price;
	@Column(nullable = false, length = 10)
	private String currency = "INR";
	@Column(nullable = false)
	private Long merchantId;
	@Column(nullable = false)
	private Integer order_quantity;

}
