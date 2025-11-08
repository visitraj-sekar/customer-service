package com.vc.customer.web;
import com.vc.customer.model.Customer; import com.vc.customer.service.CustomerAppService; import org.springframework.http.ResponseEntity; import org.springframework.web.bind.annotation.*;
@RestController @RequestMapping("/api/customers")
public class CustomerController {
  private final CustomerAppService svc; public CustomerController(CustomerAppService svc){ this.svc = svc; }
  @PostMapping public ResponseEntity<Customer> create(@RequestBody Customer c){ return ResponseEntity.ok(svc.createCustomer(c)); }
  @GetMapping("/{id}") public ResponseEntity<Customer> get(@PathVariable Long id){ Customer c = svc.getCustomer(id); return (c==null)?ResponseEntity.notFound().build():ResponseEntity.ok(c); }
}
