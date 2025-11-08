package com.vc.customer.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.vc.customer.model.Customer;
import com.vc.customer.model.Order;
import com.vc.customer.model.Wallet;
import com.vc.customer.repo.CustomerRepository;

@ExtendWith(MockitoExtension.class)
class CustomerAppServiceTest {

    @Mock
    private CustomerRepository customerRepo;

    @InjectMocks
    private CustomerAppService customerAppService;

    private Customer testCustomer;
    private Wallet testWallet;
    private Order testOrder;

    @BeforeEach
    void setUp() {
        testWallet = new Wallet();
        testWallet.setId(1L);
        testWallet.setBalance(1000.0);
        testWallet.setCurrency("INR");
        
        testOrder = new Order();
        testOrder.setCurrency("INR");
        testOrder.setStatus("CREATED");
        testOrder.setTotalAmount(Double.valueOf("10000"));

        testCustomer = new Customer();
        testCustomer.setId(1L);
        testCustomer.setName("Test Customer");
        testCustomer.setEmail("test@example.com");
        testCustomer.setWallet(testWallet);
        testCustomer.setOrders(List.of(testOrder));
    }

    @Test
    @DisplayName("Should successfully create a customer")
    void createCustomer_Success() {
        when(customerRepo.save(any(Customer.class))).thenReturn(testCustomer);

        Customer result = customerAppService.createCustomer(testCustomer);

        assertNotNull(result);
        assertEquals(testCustomer.getId(), result.getId());
        assertEquals(testCustomer.getName(), result.getName());
        assertEquals(testCustomer.getEmail(), result.getEmail());
        assertNotNull(result.getWallet());
        assertEquals(testCustomer.getWallet().getBalance(), result.getWallet().getBalance());
        assertEquals(testCustomer.getWallet().getCurrency(), result.getWallet().getCurrency());
        
        verify(customerRepo, times(1)).save(any(Customer.class));
    }

    @Test
    @DisplayName("Should throw exception when creating customer fails")
    void createCustomer_ThrowsException() {
        when(customerRepo.save(any(Customer.class))).thenThrow(new RuntimeException("Database error"));

        Exception exception = assertThrows(RuntimeException.class, 
            () -> customerAppService.createCustomer(testCustomer));
        
        assertEquals("Database error", exception.getMessage());
        verify(customerRepo, times(1)).save(any(Customer.class));
    }

    @Test
    @DisplayName("Should successfully retrieve a customer by ID")
    void getCustomer_Success() {
        when(customerRepo.findById(1L)).thenReturn(Optional.of(testCustomer));

        Customer result = customerAppService.getCustomer(1L);

        assertNotNull(result);
        assertEquals(testCustomer.getId(), result.getId());
        assertEquals(testCustomer.getName(), result.getName());
        assertEquals(testCustomer.getEmail(), result.getEmail());
        assertNotNull(result.getWallet());
        verify(customerRepo, times(1)).findById(1L);
    }

    @Test
    @DisplayName("Should return null when customer is not found")
    void getCustomer_NotFound() {
        when(customerRepo.findById(1L)).thenReturn(Optional.empty());

        Customer result = customerAppService.getCustomer(1L);

        assertNull(result);
        verify(customerRepo, times(1)).findById(1L);
    }

    @Test
    @DisplayName("Should throw exception when retrieving customer fails")
    void getCustomer_ThrowsException() {
        when(customerRepo.findById(1L)).thenThrow(new RuntimeException("Database error"));

        Exception exception = assertThrows(RuntimeException.class, 
            () -> customerAppService.getCustomer(1L));
        
        assertEquals("Database error", exception.getMessage());
        verify(customerRepo, times(1)).findById(1L);
    }

    @Test
    @DisplayName("Should successfully create a purchase order")
    void createPurchaseOrder_Success() {
        when(customerRepo.save(any(Customer.class))).thenReturn(testCustomer);

        Customer result = customerAppService.createPurchaseOrder(testCustomer);

        assertNotNull(result);
        assertEquals(testCustomer.getId(), result.getId());
        assertEquals(testCustomer.getName(), result.getName());
        assertEquals(testCustomer.getEmail(), result.getEmail());
        verify(customerRepo, times(1)).save(any(Customer.class));
    }

    @Test
    @DisplayName("Should throw exception when creating purchase order fails")
    void createPurchaseOrder_ThrowsException() {
        when(customerRepo.save(any(Customer.class))).thenThrow(new RuntimeException("Database error"));

        Exception exception = assertThrows(RuntimeException.class, 
            () -> customerAppService.createPurchaseOrder(testCustomer));
        
        assertEquals("Database error", exception.getMessage());
        verify(customerRepo, times(1)).save(any(Customer.class));
    }
}