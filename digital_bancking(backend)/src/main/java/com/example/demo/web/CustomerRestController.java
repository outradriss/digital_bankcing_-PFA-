package com.example.demo.web;

import java.util.List;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dtos.CustomersDTO;
import com.example.demo.entities.Customers;
import com.example.demo.exception.CustomerNotFoundException;
import com.example.demo.service.BankAccountService;
import com.example.demo.service.BankService;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@AllArgsConstructor
@Slf4j
@CrossOrigin("*")

public class CustomerRestController {
	private BankAccountService accountService;

	@GetMapping("/customer")
	public List<CustomersDTO> customers() {

		return accountService.listCustomers();
	}

	@GetMapping("/customer/{id}")
	public CustomersDTO getCustomersDTO(@PathVariable(name = "id") Long customerId) throws CustomerNotFoundException {

		return accountService.getCustomersDTO(customerId);
	}

	@PostMapping("/customer")
	public CustomersDTO saveCustomersDTO(@RequestBody CustomersDTO customersDTO) {
		return accountService.saveCustomers(customersDTO);

	}

	@PutMapping("/customer/{customerId}")
	public CustomersDTO updatCustomersDTO(@PathVariable Long customerId, @RequestBody CustomersDTO customersDTO) {
		customersDTO.setId(customerId);
		CustomersDTO updateCustomers = accountService.updateCustomers(customersDTO);
		return updateCustomers;

	}

	@DeleteMapping("/customer/{id}")
	public void deleteCustomer(@PathVariable Long id) {
		accountService.deleteCustomer(id);

	}

	@GetMapping("/customer/search")
	public List<CustomersDTO> customersSearch(@RequestParam(name = "keyword") String keyword) {

		return accountService.searchCustomers(keyword);
	}

}
