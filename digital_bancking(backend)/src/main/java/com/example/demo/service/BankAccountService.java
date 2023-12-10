package com.example.demo.service;

import java.util.List;

import com.example.demo.dtos.AccountHistoryDTO;
import com.example.demo.dtos.AccountOperationDTO;
import com.example.demo.dtos.BankAccountDTO;
import com.example.demo.dtos.CurrentBanckAccountDTO;
import com.example.demo.dtos.CustomersDTO;
import com.example.demo.dtos.SavingBanckAccountDTO;
import com.example.demo.exception.BalanceNotFoundException;
import com.example.demo.exception.BankAccountException;
import com.example.demo.exception.CustomerNotFoundException;

public interface BankAccountService {

	CustomersDTO saveCustomers(CustomersDTO customersDTO);

	CurrentBanckAccountDTO saveCurrentAccount(double initialBalance, Long customerId, double OverDrafet)
			throws CustomerNotFoundException;

	SavingBanckAccountDTO saveEpargnAccountDTO(double initialBalance, Long customerId, double rate)
			throws CustomerNotFoundException;

	List<CustomersDTO> listCustomers();

	BankAccountDTO getBanckAccount(String accountId) throws BankAccountException;

	void debit(String accountId, double amount, String description)
			throws BankAccountException, BalanceNotFoundException;

	void credit(String accountId, double amount, String description) throws BankAccountException;

	void transfert(String accountIdSource, String accountIdDestination, double montantOperation)
			throws BankAccountException, BalanceNotFoundException;

	List<BankAccountDTO> listBanckAccounts();

	CustomersDTO getCustomersDTO(Long customerId) throws CustomerNotFoundException;

	CustomersDTO updateCustomers(CustomersDTO customersDTO);

	void deleteCustomer(Long customerId);

	List<AccountOperationDTO> accountHistory(String accountId);

	AccountHistoryDTO getAccountHistory(String accountId, int page, int size) throws BankAccountException;

	List<CustomersDTO> searchCustomers(String keyword);

}
