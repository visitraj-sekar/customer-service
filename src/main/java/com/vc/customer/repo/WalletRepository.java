package com.vc.customer.repo;

import com.vc.customer.model.Wallet;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface WalletRepository extends JpaRepository<Wallet, Long> {

	
	Optional<Wallet> findByCustomer_Id(Long customerId);
}