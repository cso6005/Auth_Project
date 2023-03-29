package io.csy.hot.jwt;

import java.util.List;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;

import io.csy.hot.model.entity.AccountEntity;
import lombok.Getter;

@Getter
public class AccountDetails extends User{
	
	private final AccountEntity account;
	
	// UserDetails
	AccountDetails(AccountEntity account) {
		super(account.getAccountEmail(), account.getAccountPassword(), List.of(new SimpleGrantedAuthority("ROLE_" + account.getAccountType())));
		this.account = account;
	}

}
