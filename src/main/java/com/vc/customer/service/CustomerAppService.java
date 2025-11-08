package com.vc.customer.service;
import com.vc.customer.model.*; import com.vc.customer.repo.*; import org.springframework.stereotype.Service; import org.springframework.transaction.annotation.Transactional; import java.util.List;
@Service
public class CustomerAppService {
  private final CustomerRepository customers; private final WalletRepository wallets; private final OrderRepository orders; private final ProductRepository products;
  public CustomerAppService(CustomerRepository customers, WalletRepository wallets, OrderRepository orders, ProductRepository products){
    this.customers = customers; this.wallets = wallets; this.orders = orders; this.products = products;
  }
  public Customer createCustomer(Customer c){ return customers.save(c); }
  public Customer getCustomer(Long id){ return customers.findById(id).orElse(null); }
  @Transactional public Order createOrder(Order order){ if(order.getProducts()!=null){ order.getProducts().forEach(p->p.setOrder(order)); } return orders.save(order); }
  public Order getOrder(Long id){ return orders.findById(id).orElse(null); }
  public List<Order> listOrders(){ return orders.findAll(); }
}
