package com.example.demo.service;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.entities.BanckAccount;
import com.example.demo.entities.CurrentAccount;
import com.example.demo.entities.SavingAccount;
import com.example.demo.repository.BankAccountRepository;

@Service
@Transactional
public class BankService {
	@Autowired
	private BankAccountRepository bankAccountRepository;

	public void consulter() {
		BanckAccount bAccount = bankAccountRepository.findById("7110e12f-132b-4200-bee4-a650c44be065").orElse(null);
		if (bAccount != null) {

			System.out.println("**************************************");
			System.out.println(bAccount.getId());
			System.out.println(bAccount.getBalance());
			System.out.println(bAccount.getCreatDate());
			System.out.println(bAccount.getCustomers().getName());
			System.out.println(bAccount.getStatus());
			System.out.println(bAccount.getClass().getSimpleName());
			if (bAccount instanceof CurrentAccount) {
				System.out.println("Over draft ==> " + ((CurrentAccount) bAccount).getOverDraft());

			} else if (bAccount instanceof SavingAccount) {
				System.out.println("Rate ==> " + ((SavingAccount) bAccount).getIntersetRate());

			}
			bAccount.getAccountOperations().forEach(ope -> {
				System.out.println(ope.getAmount() + "\t " + ope.getOpDate() + "\t " + ope.getType());
			});
		}

	}

}
