package com.example.demo.dtos;

import java.util.List;

import lombok.Data;
@Data
public class AccountHistoryDTO {
	private String id;
	private double balance ; 
	private int currentPage;
	private int totalePages;
	private int pageSize;
	private List<AccountOperationDTO>accountOperation;

}
