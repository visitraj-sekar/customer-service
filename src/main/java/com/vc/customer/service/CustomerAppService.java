package com.vc.customer.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.vc.customer.model.Customer;
import com.vc.customer.model.Order;
import com.vc.customer.model.Wallet;
import com.vc.customer.repo.CustomerRepository;
import com.vc.customer.repo.WalletRepository;

/**
 * Service class handling business logic for customer operations.
 * Manages customer creation, retrieval, and purchase order processing.
 *
 * @author Rajasekhar Setty
 * @version 1.0
 */
@Service
public class CustomerAppService {
    private static final Logger logger = LoggerFactory.getLogger(CustomerAppService.class);
    private final CustomerRepository customerRepo;
    private final WalletRepository walletRepository;
    // Add other required repositories

    /**
     * Constructs a new CustomerAppService with required repositories.
     *
     * @param customerRepo the customer repository
     */
    public CustomerAppService(CustomerRepository customerRepo, WalletRepository walletRepository) {
        this.customerRepo = customerRepo;
        this.walletRepository = walletRepository;
        	
        logger.info("CustomerAppService initialized");
    }

    /**
     * Creates a new customer with associated wallet.
     *
     * @param customer the customer to create
     * @return the created customer
     */
    @Transactional
    public Customer createCustomer(Customer customer) {
        logger.info("Creating new customer with email: {}", customer.getEmail());
        try {
            // Add your implementation here
            
            Wallet wallet = customer.getWallet();
            wallet.setCustomer(customer);
            Customer saved = customerRepo.save(customer);
            logger.debug("Customer created successfully with ID: {}", saved.getId());
            return saved;
        } catch (Exception e) {
            logger.error("Failed to create customer: {}", e.getMessage(), e);
            throw e;
        }
    }

    /**
     * Retrieves a customer by their ID.
     *
     * @param id the customer ID
     * @return the customer if found, null otherwise
     */
    @Transactional(readOnly = true)
    public Customer getCustomer(Long id) {
        logger.info("Fetching customer with ID: {}", id);
        try {
        	Customer customer = customerRepo.findById(id).orElse(new Customer());
        	Wallet wallet = walletRepository.findByCustomer_Id(id).get();
            customer.setWallet(wallet);
            return customer;
        } catch (Exception e) {
            logger.error("Failed to fetch customer: {}", e.getMessage(), e);
            throw e;
        }
    }

    /**
     * Creates a new purchase order for a customer.
     *
     * @param customer the customer with purchase order details
     * @return the updated customer
     */
    @Transactional
    public Customer createPurchaseOrder(Customer customer) {
        logger.info("Creating purchase order for customer ID: {}", customer.getId());
        try {
            // Add your implementation here
        	List<Order> orders = customer.getOrders();
        	
        	orders.forEach(order -> order.setCustomer(customer));
        	
            Customer updated = customerRepo.save(customer);
            logger.debug("Purchase order created successfully for customer ID: {}", updated.getId());
            return updated;
        } catch (Exception e) {
            logger.error("Failed to create purchase order: {}", e.getMessage(), e);
            throw e;
        }
    }
}