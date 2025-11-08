package com.vc.customer.web;

import com.vc.customer.model.Order;
import com.vc.customer.service.CustomerAppService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/orders")
public class OrderController {
	private final CustomerAppService svc;

	public OrderController(CustomerAppService svc) {
		this.svc = svc;
	}

	@PostMapping
	public ResponseEntity<Order> create(@RequestBody Order order) {
		Order saved = svc.createOrder(order);
		return ResponseEntity.created(URI.create("/api/orders/" + saved.getId())).body(saved);
	}

	@GetMapping("/{id}")
	public ResponseEntity<Order> byId(@PathVariable Long id) {
		Order o = svc.getOrder(id);
		return (o == null) ? ResponseEntity.notFound().build() : ResponseEntity.ok(o);
	}

	@GetMapping
	public List<Order> all() {
		return svc.listOrders();
	}
}
