package com.example.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.entities.BanckAccount;

public interface BankAccountRepository extends JpaRepository<BanckAccount, String> {

}
