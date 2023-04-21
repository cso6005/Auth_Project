package io.csy.jwt;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import io.csy.model.entity.AccountEntity;
import io.csy.model.repository.AccountRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AccountDetailsService implements UserDetailsService {

	private final AccountRepository accountRepository;

	@Override
	public UserDetails loadUserByUsername(String accountEmail) throws UsernameNotFoundException {
        
		System.out.println("AccountDetailsService : 인증 확인");
		AccountEntity account = accountRepository.findAllByAccountEmail(accountEmail)
				.orElseThrow(() -> new UsernameNotFoundException(accountEmail + " 사용자 조회 불가")); 

		return new AccountDetails(account);

	}
	
}
