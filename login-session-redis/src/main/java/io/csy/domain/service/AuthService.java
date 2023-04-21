package io.csy.domain.service;

import java.util.Optional;

import org.springframework.stereotype.Service;

import io.csy.domain.dto.AccountDTO;
import io.csy.domain.dto.RoleEnum;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor 
public class AuthService {
	
	public Optional<AccountDTO> login(String email, String password) {
		
		AccountDTO accountDTO = AccountDTO.builder().userName("소영").email(email).role(RoleEnum.USER).build();
		
		return Optional.ofNullable(accountDTO);
	}

}
