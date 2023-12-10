package com.example.demo.dtos;

import java.util.Date;

import com.example.demo.entities.enums.AccountStatus;

import lombok.Data;

@Data

public class SavingBanckAccountDTO extends BankAccountDTO {

	private String id;
	private double balance;
	private Date creatDate;

	private AccountStatus status;

	private CustomersDTO customers;
	private double intersetRate;

}
