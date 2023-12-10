package com.example.demo.service;

import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import javax.security.auth.login.AccountNotFoundException;
import javax.transaction.Transactional;

import org.springframework.aop.ThrowsAdvice;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.example.demo.dtos.AccountHistoryDTO;
import com.example.demo.dtos.AccountOperationDTO;
import com.example.demo.dtos.BankAccountDTO;
import com.example.demo.dtos.CurrentBanckAccountDTO;
import com.example.demo.dtos.CustomersDTO;
import com.example.demo.dtos.SavingBanckAccountDTO;
import com.example.demo.entities.AccountOperation;
import com.example.demo.entities.BanckAccount;
import com.example.demo.entities.CurrentAccount;
import com.example.demo.entities.Customers;
import com.example.demo.entities.SavingAccount;
import com.example.demo.entities.enums.OperationType;
import com.example.demo.exception.BalanceNotFoundException;
import com.example.demo.exception.BankAccountException;
import com.example.demo.exception.CustomerNotFoundException;
import com.example.demo.mappers.ServiceBankMapperImpl;
import com.example.demo.repository.AccountOperationRepository;
import com.example.demo.repository.BankAccountRepository;
import com.example.demo.repository.CustomerRepository;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Transactional
@AllArgsConstructor
@Slf4j
public class BankAccountServiceImpl implements BankAccountService {

	private CustomerRepository customerRepository;
	private BankAccountRepository accountRepository;
	private AccountOperationRepository accountOperationRepository;
	private ServiceBankMapperImpl dtoMapper;
	private List<AccountOperationDTO> accountOperationDTOs;

	@Override
	public CustomersDTO saveCustomers(CustomersDTO customersDTO) {
		log.info("saving customer");
		Customers customers = dtoMapper.fromCustomersDto(customersDTO);
		Customers saveCustomers = customerRepository.save(customers);
		return dtoMapper.fromCustomer(saveCustomers);
	}

	@Override
	public List<CustomersDTO> listCustomers() {
		List<Customers> customers = customerRepository.findAll();
		List<CustomersDTO> listCustomersDTOs = customers.stream().map(cust -> dtoMapper.fromCustomer(cust))
				.collect(Collectors.toList());
		return listCustomersDTOs;
	}

	@Override
	public BankAccountDTO getBanckAccount(String accountId) throws BankAccountException {
		BanckAccount banckAccount = accountRepository.findById(accountId)
				.orElseThrow(() -> new BankAccountException("Account Not Found"));
		if (banckAccount instanceof SavingAccount) {
			SavingAccount savingAccount = (SavingAccount) banckAccount;
			return dtoMapper.fromSavingBanckAccount(savingAccount);
		} else {
			CurrentAccount currentAccount = (CurrentAccount) banckAccount;
			return dtoMapper.fromCurrentBanckAccount(currentAccount);
		}
	}

	@Override
	public void debit(String accountId, double amount, String description)
			throws BankAccountException, BalanceNotFoundException {
		BanckAccount banckAccount = accountRepository.findById(accountId)
				.orElseThrow(() -> new BankAccountException("Account Not Found"));
		if (banckAccount.getBalance() < amount)
			throw new BalanceNotFoundException("Sold insufisant");
		AccountOperation accountOperation = new AccountOperation();
		accountOperation.setType(OperationType.DEBIT);
		accountOperation.setAmount(amount);
		accountOperation.setDescription(description);
		accountOperation.setOpDate(new Date());
		accountOperation.setBanckAccount(banckAccount);
		accountOperationRepository.save(accountOperation);
		banckAccount.setBalance(banckAccount.getBalance() - amount);
		accountRepository.save(banckAccount);

	}

	@Override
	public void credit(String accountId, double amount, String description) throws BankAccountException {
		BanckAccount banckAccount = accountRepository.findById(accountId)
				.orElseThrow(() -> new BankAccountException("Account Not Found"));
		AccountOperation accountOperation = new AccountOperation();
		accountOperation.setType(OperationType.CREDIT);
		accountOperation.setAmount(amount);
		accountOperation.setDescription(description);
		accountOperation.setOpDate(new Date());
		accountOperation.setBanckAccount(banckAccount);
		accountOperationRepository.save(accountOperation);
		banckAccount.setBalance(banckAccount.getBalance() + amount);
		accountRepository.save(banckAccount);

	}

	@Override
	public void transfert(String accountIdSource, String accountIdDestination, double amount)
			throws BankAccountException, BalanceNotFoundException {
		debit(accountIdSource, amount, "transfer to " + accountIdDestination);
		credit(accountIdSource, amount, "transfer from " + accountIdSource);

	}

	@Override
	public SavingBanckAccountDTO saveEpargnAccountDTO(double initialBalance, Long customerId, double rate)
			throws CustomerNotFoundException {
		Customers customers = customerRepository.findById(customerId).orElse(null);
		if (customers == null) {
			throw new CustomerNotFoundException("Customer not found");
		}
		SavingAccount rateAccount = new SavingAccount();
		rateAccount.setId(UUID.randomUUID().toString());
		rateAccount.setCreatDate(new Date());
		rateAccount.setBalance(initialBalance);
		rateAccount.setCustomers(customers);
		rateAccount.setIntersetRate(rate);
		SavingAccount savingRateAccount = accountRepository.save(rateAccount);

		return dtoMapper.fromSavingBanckAccount(savingRateAccount);
	}

	@Override
	public CurrentBanckAccountDTO saveCurrentAccount(double initialBalance, Long customerId, double Overdraft)
			throws CustomerNotFoundException {

		Customers customers = customerRepository.findById(customerId).orElse(null);
		if (customers == null) {
			throw new CustomerNotFoundException("Customer not found");
		}
		CurrentAccount account = new CurrentAccount();
		account.setId(UUID.randomUUID().toString());
		account.setCreatDate(new Date());
		account.setBalance(initialBalance);
		account.setCustomers(customers);
		account.setOverDraft(Overdraft);
		CurrentAccount saveCurrentAccount = accountRepository.save(account);

		return dtoMapper.fromCurrentBanckAccount(saveCurrentAccount);
	}

	@Override
	public List<BankAccountDTO> listBanckAccounts() {

		List<BanckAccount> listBanckAccounts = accountRepository.findAll();
		List<BankAccountDTO> banckAccountDTO = listBanckAccounts.stream().map(acc -> {
			if (acc instanceof SavingAccount) {
				SavingAccount savingAccount = (SavingAccount) acc;
				return dtoMapper.fromSavingBanckAccount(savingAccount);
			} else {
				CurrentAccount currentAccount = (CurrentAccount) acc;
				return dtoMapper.fromCurrentBanckAccount(currentAccount);
			}
		}).collect(Collectors.toList());
		return banckAccountDTO;
	}

	@Override
	public CustomersDTO getCustomersDTO(Long customerId) throws CustomerNotFoundException {
		Customers customers = customerRepository.findById(customerId)
				.orElseThrow(() -> new CustomerNotFoundException("Customer Not found"));
		return dtoMapper.fromCustomer(customers);

	}

	@Override
	public CustomersDTO updateCustomers(CustomersDTO customersDTO) {
		log.info("saving customer");
		Customers customers = dtoMapper.fromCustomersDto(customersDTO);
		Customers saveCustomers = customerRepository.save(customers);
		return dtoMapper.fromCustomer(saveCustomers);
	}

	@Override
	public void deleteCustomer(Long customerId) {

		customerRepository.deleteById(customerId);
	}

	@Override
	public List<AccountOperationDTO> accountHistory(String accountId) {
		List<AccountOperation> accountOperation = accountOperationRepository.findByBanckAccountId(accountId);
		return accountOperation.stream().map(accountop -> dtoMapper.fromAccountOperation(accountop))
				.collect(Collectors.toList());
	}

	@Override
	public AccountHistoryDTO getAccountHistory(String accountId, int page, int size) throws BankAccountException {
		BanckAccount banckAccount = accountRepository.findById(accountId).orElse(null);
		if (banckAccount == null)
			throw new BankAccountException("account Not Found");
		Page<AccountOperation> accountopHistory = accountOperationRepository.findByBanckAccountId(accountId,
				PageRequest.of(page, size));
		AccountHistoryDTO accountHistoryDTO = new AccountHistoryDTO();
		List<AccountOperationDTO> listAccountHistoryDTO = accountopHistory.getContent().stream()
				.map(acc -> dtoMapper.fromAccountOperation(acc)).collect(Collectors.toList());
		accountHistoryDTO.setAccountOperation(listAccountHistoryDTO);
		accountHistoryDTO.setId(banckAccount.getId());
		accountHistoryDTO.setBalance(banckAccount.getBalance());
		accountHistoryDTO.setCurrentPage(page);
		accountHistoryDTO.setPageSize(size);
		accountHistoryDTO.setTotalePages(accountopHistory.getTotalPages());

		return accountHistoryDTO;
	}

	@Override
	public List<CustomersDTO> searchCustomers(String keyword) {
		 List<Customers> customers = customerRepository.findByNameContains(keyword);
		 System.out.println(customers);       
		List<CustomersDTO> listCustomerDTO = customers.stream().map(cus -> dtoMapper.fromCustomer(cus))
				.collect(Collectors.toList());

		return listCustomerDTO;
	}

}
