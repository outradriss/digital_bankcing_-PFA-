package com.example.demo;

import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import com.example.demo.dtos.BankAccountDTO;
import com.example.demo.dtos.CurrentBanckAccountDTO;
import com.example.demo.dtos.CustomersDTO;
import com.example.demo.dtos.SavingBanckAccountDTO;
import com.example.demo.entities.AccountOperation;
import com.example.demo.entities.BanckAccount;
import com.example.demo.entities.CurrentAccount;
import com.example.demo.entities.Customers;
import com.example.demo.entities.SavingAccount;
import com.example.demo.entities.enums.AccountStatus;
import com.example.demo.entities.enums.OperationType;
import com.example.demo.exception.BalanceNotFoundException;
import com.example.demo.exception.BankAccountException;
import com.example.demo.exception.CustomerNotFoundException;
import com.example.demo.repository.AccountOperationRepository;
import com.example.demo.repository.BankAccountRepository;
import com.example.demo.repository.CustomerRepository;
import com.example.demo.service.BankAccountService;
import com.example.demo.service.BankService;

import net.bytebuddy.dynamic.scaffold.MethodRegistry.Handler.ForImplementation;

@SpringBootApplication
public class EbankBackend2Application {

	public static void main(String[] args) {
		SpringApplication.run(EbankBackend2Application.class, args);
	}

	@Bean
	CommandLineRunner commandLineRunner(BankAccountService bankService) {
		return args -> {
			Stream.of("tamina","rachid","hatim","imad","karim","ilyoss","brother","mohmmed").forEach(compte -> {
				CustomersDTO customersDTO = new CustomersDTO(); 
				customersDTO.setName(compte);
				customersDTO.setEmail(compte + "@gmail.com");
				bankService.saveCustomers(customersDTO);			});
			bankService.listCustomers().forEach(customer -> { 
				try { 
					bankService.saveCurrentAccount(Math.random() * 9000, customer.getId(), 9000);
					bankService.saveEpargnAccountDTO(Math.random() * 90000, customer.getId(), 5.5);
					
					
				} catch (CustomerNotFoundException e) {
					e.printStackTrace();
				}

			});
			List<BankAccountDTO > banckAccountDTO = bankService.listBanckAccounts();
			for (BankAccountDTO banckAccount :banckAccountDTO) {
				for (int i = 0; i < 10; i++) {
					String accountId;
					if (banckAccount instanceof SavingBanckAccountDTO) {
						accountId = ((SavingBanckAccountDTO) banckAccount).getId();
					}else {
					   accountId = ((CurrentBanckAccountDTO) banckAccount).getId();	
					}
					bankService.credit(accountId, 1000 + Math.random() * 120000, "Credit");
					bankService.debit(accountId, 1000 + 900, "Débit");

				}
			}
			

		};
	}

	// @Bean
	CommandLineRunner start(CustomerRepository customerRepository, BankAccountRepository bankAccountRepository,
			AccountOperationRepository accountOperationRepository) {
		return args -> {
			Stream.of("Hassan", "Driss", "Amina").forEach(name -> {
				Customers customers = new Customers();
				customers.setName(name);
				customers.setEmail(name + "@gmail.Com");
				customerRepository.save(customers);
			});

			customerRepository.findAll().forEach(c -> {
				CurrentAccount account = new CurrentAccount();
				account.setId(UUID.randomUUID().toString());
				account.setBalance(Math.random() * 9000);
				account.setCreatDate(new Date());
				account.setStatus(AccountStatus.CREATED);
				account.setCustomers(c);
				account.setOverDraft(9000);
				bankAccountRepository.save(account);

				SavingAccount savingAccount = new SavingAccount();
				savingAccount.setId(UUID.randomUUID().toString());
				savingAccount.setBalance(Math.random() * 9000);
				savingAccount.setCreatDate(new Date());
				savingAccount.setStatus(AccountStatus.CREATED);
				savingAccount.setCustomers(c);
				savingAccount.setIntersetRate(5.5);
				bankAccountRepository.save(savingAccount);

			});
			bankAccountRepository.findAll().forEach(acc -> {
				for (int i = 0; i < 10; i++) {
					AccountOperation accountOperation = new AccountOperation();
					accountOperation.setOpDate(new Date());
					accountOperation.setAmount(Math.random() * 12000);
					accountOperation.setType(Math.random() > 0.5 ? OperationType.DEBIT : OperationType.CREDIT);
					accountOperation.setBanckAccount(acc);
					accountOperationRepository.save(accountOperation);

				}

			});

		};
	}

}
