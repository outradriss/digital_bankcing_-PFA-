package com.example.demo.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.entities.AccountOperation;

public interface AccountOperationRepository extends JpaRepository<AccountOperation, Long> {

	List<AccountOperation> findByBanckAccountId(String accountId);

	Page<AccountOperation> findByBanckAccountId(String accountId, Pageable pageable);

}
