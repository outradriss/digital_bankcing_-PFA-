package com.example.demo.mappers;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import com.example.demo.dtos.AccountOperationDTO;
import com.example.demo.dtos.CurrentBanckAccountDTO;
import com.example.demo.dtos.CustomersDTO;
import com.example.demo.dtos.SavingBanckAccountDTO;
import com.example.demo.entities.AccountOperation;
import com.example.demo.entities.CurrentAccount;
import com.example.demo.entities.Customers;
import com.example.demo.entities.SavingAccount;

@Service
public class ServiceBankMapperImpl {
	public CustomersDTO fromCustomer(Customers customers) {
		CustomersDTO customersDTO = new CustomersDTO();
		BeanUtils.copyProperties(customers, customersDTO);
		return customersDTO;
	}

	public Customers fromCustomersDto(CustomersDTO customersDTO) {
		Customers customers = new Customers();
		BeanUtils.copyProperties(customersDTO, customers);

		return customers;

	}

	public SavingBanckAccountDTO fromSavingBanckAccount(SavingAccount savingAccount) {
		SavingBanckAccountDTO savingBanckAccountDTO = new SavingBanckAccountDTO();
		BeanUtils.copyProperties(savingAccount, savingBanckAccountDTO);
		savingBanckAccountDTO.setCustomers(fromCustomer(savingAccount.getCustomers()));
		savingBanckAccountDTO.setTypes(savingAccount.getClass().getSimpleName());
		return savingBanckAccountDTO;
	}

	public SavingAccount fromSavingBanckAccountDTO(SavingBanckAccountDTO banckAccountDTO) {
		SavingAccount savingAccount = new SavingAccount();
		BeanUtils.copyProperties(banckAccountDTO, savingAccount);
		savingAccount.setCustomers(fromCustomersDto(banckAccountDTO.getCustomers()));

		return savingAccount;
	}

	public CurrentBanckAccountDTO fromCurrentBanckAccount(CurrentAccount currentAccount) {

		CurrentBanckAccountDTO currentBanckAccountDTO = new CurrentBanckAccountDTO();
		BeanUtils.copyProperties(currentAccount, currentBanckAccountDTO);
		currentBanckAccountDTO.setCustomersDTO(fromCustomer(currentAccount.getCustomers()));
		currentBanckAccountDTO.setTypes(currentAccount.getClass().getSimpleName());

		return currentBanckAccountDTO;
	}

	public CurrentAccount fromCurrentAccountDTO(CurrentBanckAccountDTO currentBanckAccountDTO) {

		CurrentAccount currentAccount = new CurrentAccount();
		BeanUtils.copyProperties(currentBanckAccountDTO, currentAccount);
		currentAccount.setCustomers(fromCustomersDto(currentBanckAccountDTO.getCustomersDTO()));
		return currentAccount;
	}

	public AccountOperationDTO fromAccountOperation(AccountOperation accountOperation) {
		AccountOperationDTO accountOperationDTO = new AccountOperationDTO();
		BeanUtils.copyProperties(accountOperation, accountOperationDTO);
		return accountOperationDTO;
	}


}
