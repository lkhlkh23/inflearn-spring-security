package inflearn.practice.security.service;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import inflearn.practice.security.entity.AccountEntity;
import inflearn.practice.security.repository.AccountRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AccountService implements UserDetailsService {

	private final AccountRepository accountRepository;
	private final PasswordEncoder passwordEncoder;

	@Override
	public UserDetails loadUserByUsername(final String username) throws UsernameNotFoundException {
		final AccountEntity account = accountRepository.findByName(username)
													   .orElseThrow(() -> new UsernameNotFoundException(username + " not found"));
		return User.builder()
				   .username(account.getName())
				   .password(account.getPassword())
				   .roles(account.getRole())
				   .build();
	}

	public void register(final AccountEntity account) {
		account.encodePassword(passwordEncoder);
		accountRepository.save(account);
	}

}
