package com.vc.customer.web;

import com.vc.customer.model.Customer;
import com.vc.customer.service.CustomerAppService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * REST controller for managing Customer operations.
 * Provides endpoints for creating and retrieving customer information.
 *
 * @author Rajasekhar Setty
 * @version 1.0
 */
@RestController
@RequestMapping("/api/customers")
public class CustomerController {
    private static final Logger logger = LoggerFactory.getLogger(CustomerController.class);
    private final CustomerAppService svc;

    /**
     * Constructs a new CustomerController with the specified service.
     *
     * @param svc the customer application service
     */
    public CustomerController(CustomerAppService svc) {
        this.svc = svc;
        logger.info("CustomerController initialized");
    }

    /**
     * Creates a new customer with associated wallet.
     *
     * @param customer the customer to create
     * @return ResponseEntity containing the created customer
     */
    @PostMapping
    public ResponseEntity<Customer> create(@RequestBody Customer customer) {
        logger.info("Creating new customer: {}", customer.getEmail());
        try {
            Customer created = svc.createCustomer(customer);
            logger.debug("Customer created successfully with ID: {}", created.getId());
            return ResponseEntity.ok(created);
        } catch (Exception e) {
            logger.error("Error creating customer: {}", e.getMessage(), e);
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * Retrieves a customer by their ID.
     *
     * @param id the customer ID
     * @return ResponseEntity containing the customer if found, or not found status
     */
    @GetMapping("/{id}")
    public ResponseEntity<Customer> get(@PathVariable Long id) {
        logger.info("Fetching customer with ID: {}", id);
        try {
            Customer customer = svc.getCustomer(id);
            if (customer == null) {
                logger.debug("Customer not found with ID: {}", id);
                return ResponseEntity.notFound().build();
            }
            logger.debug("Customer found: {}", customer.getEmail());
            return ResponseEntity.ok(customer);
        } catch (Exception e) {
            logger.error("Error fetching customer: {}", e.getMessage(), e);
            return ResponseEntity.internalServerError().build();
        }
    }
    
    /**
     * Creates a new purchase order for a customer.
     *
     * @param customer the customer with purchase order details
     * @return ResponseEntity containing the updated customer
     */
    @PostMapping("/purchaseOrder")
    public ResponseEntity<Customer> createPurchaseOrder(@RequestBody Customer customer) {
        logger.info("Creating purchase order for customer ID: {}", customer.getId());
        try {
            Customer updated = svc.createPurchaseOrder(customer);
            logger.debug("Purchase order created successfully for customer ID: {}", updated.getId());
            return ResponseEntity.ok(updated);
        } catch (Exception e) {
            logger.error("Error creating purchase order: {}", e.getMessage(), e);
            return ResponseEntity.badRequest().build();
        }
    }
}