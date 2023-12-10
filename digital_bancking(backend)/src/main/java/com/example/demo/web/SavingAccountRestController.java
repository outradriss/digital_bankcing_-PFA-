package com.example.demo.web;

import java.util.List;

import org.springframework.boot.context.properties.bind.DefaultValue;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dtos.AccountHistoryDTO;
import com.example.demo.dtos.AccountOperationDTO;
import com.example.demo.dtos.BankAccountDTO;
import com.example.demo.exception.BankAccountException;

import com.example.demo.service.BankAccountService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@AllArgsConstructor
@Slf4j
@CrossOrigin("*")
public class SavingAccountRestController {

	private BankAccountService bankAccountService;

	@GetMapping("/accounts/{accountId}")
	public BankAccountDTO getBankAccount(@PathVariable String accountId) throws BankAccountException {

		return bankAccountService.getBanckAccount(accountId);
	}

	@GetMapping("/accounts")

	public List<BankAccountDTO> listBankAccountDTOs() {

		return bankAccountService.listBanckAccounts(); 
	}

	@GetMapping("/accounts/{accountId}/operations")

	public List<AccountOperationDTO> getHistory(@PathVariable String accountId) {
		return bankAccountService.accountHistory(accountId);
	}

	@GetMapping("/accounts/{accountId}/pagesOperations")

	public AccountHistoryDTO getCurrentHistory(@PathVariable String accountId, @RequestParam(name = "page",defaultValue = "0")int page,
			@RequestParam(name = "size" , defaultValue = "5")  int size) throws BankAccountException {
		return bankAccountService.getAccountHistory(accountId, page, size);
	}

}
