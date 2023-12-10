package com.example.demo.dtos;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Id;

import com.example.demo.entities.enums.OperationType;

import lombok.Data;
@Data
public  class AccountOperationDTO {

	private Long id;
	private Date opDate;
	private double amount;
	private OperationType type;
	private String Description;

}
