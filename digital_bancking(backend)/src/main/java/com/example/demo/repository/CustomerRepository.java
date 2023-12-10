package com.example.demo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.example.demo.entities.Customers;

public interface CustomerRepository extends JpaRepository<Customers, Long> {
		  List<Customers>findByNameContains(String keyword); 

	
}
