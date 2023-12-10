package com.example.demo.entities;

import java.util.Date;
import java.util.List;

import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorType;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import org.hibernate.annotations.ManyToAny;

import com.example.demo.entities.enums.AccountStatus;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "TYPE", length = 4)
@Data
@AllArgsConstructor
@NoArgsConstructor
public abstract class BanckAccount {
	@Id
	private String id;
	private double balance;
	private Date creatDate;
	@Enumerated(EnumType.STRING)
	private AccountStatus status;
	@ManyToOne
	private Customers customers;
	@OneToMany(mappedBy = "banckAccount", fetch = FetchType.LAZY)
	private List<AccountOperation> accountOperations;

}
