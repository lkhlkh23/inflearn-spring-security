package inflearn.practice.security.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RestController;

import inflearn.practice.security.entity.AccountEntity;
import inflearn.practice.security.repository.AccountRepository;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class AccountRegisterController {

	private final AccountRepository accountRepository;

	@GetMapping("/account/{role}/{name}/{password}")
	public AccountEntity register(@ModelAttribute AccountEntity account) {
		return accountRepository.save(account);
	}

}
