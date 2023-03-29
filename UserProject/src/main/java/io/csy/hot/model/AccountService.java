package io.csy.hot.model;

import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import io.csy.hot.exception.CustomException;
import io.csy.hot.exception.UserErrorCode;
import io.csy.hot.jwt.JwtProvider;
import io.csy.hot.jwt.TokenResponse;
import io.csy.hot.model.dto.AccountDTO;
import io.csy.hot.model.dto.LoginDTO;
import io.csy.hot.model.dto.SignUpDTO;
import io.csy.hot.model.entity.AccountEntity;
import io.csy.hot.model.repository.AccountRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AccountService {
	
	private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

	private final AccountRepository accountRepository;
	
    private final AuthenticationManager authenticationManager;
	
	private final JwtProvider jwtProvider;

	private final PasswordEncoder passwordEncoder;

	@Transactional
	public AccountDTO signUp(SignUpDTO signUpRequest) throws Exception {

		boolean isExist = accountRepository.existsByAccountEmail(signUpRequest.getAccountEmail());

		if (isExist) {
			throw new CustomException(UserErrorCode.EXISTED_SIGNUP_EMAIL_PARAMETER);
		}

		String encodedPassword = passwordEncoder.encode(signUpRequest.getAccountPassword());

		AccountEntity account = AccountEntity.builder().accountEmail(signUpRequest.getAccountEmail())
				.accountPassword(encodedPassword).accountName(signUpRequest.getAccountName())
				.accountBirth(signUpRequest.getAccountBirth()).accountAddress(signUpRequest.getAccountAddress())
				.accountPhoneNumber(signUpRequest.getAccountPhoneNumber())
				.accountGender(signUpRequest.getAccountGender()).accountType("USER").build();

		account = accountRepository.save(account);

		return AccountDTO.toDTO(account);

	}

	@Transactional
	public TokenResponse login(LoginDTO login) throws Exception {
		
		UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(login.getAccountEmail(), login.getAccountPassword());

		Authentication authentication = authenticationManager.authenticate(authenticationToken);

		TokenResponse token = jwtProvider.createTokenByLogin(authentication);
		
		return token;
	}
	


}
