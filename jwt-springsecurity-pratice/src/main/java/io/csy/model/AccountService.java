package io.csy.model;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;

import io.csy.common.RedisDAO;
import io.csy.exception.CustomException;
import io.csy.exception.UserErrorCode;
import io.csy.jwt.JwtProvider;
import io.csy.jwt.TokenResponse;
import io.csy.model.dto.AccountDTO;
import io.csy.model.dto.LoginDTO;
import io.csy.model.dto.SignUpDTO;
import io.csy.model.entity.AccountEntity;
import io.csy.model.repository.AccountRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AccountService {

	private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

	private final AccountRepository accountRepository;

	private final AuthenticationManager authenticationManager;

	private final JwtProvider jwtProvider;

	private final PasswordEncoder passwordEncoder;

	private final RedisDAO redisDAO;

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

		UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
				login.getAccountEmail(), login.getAccountPassword());

		Authentication authentication = authenticationManager.authenticate(authenticationToken);

		TokenResponse token = jwtProvider.createTokenByLogin(authentication);

		return token;
	}

	// 로그아웃
	@Transactional
	public void logout(HttpServletRequest request) throws JsonProcessingException {
		
		String atk = request.getHeader("Authorization").substring(7);

		String acccountEmail = jwtProvider.getSubject(atk).getAccountEmail();

		// 엑세스 토큰 남은 유효시간
		Long expiration = jwtProvider.getExpiration(atk);

		if (redisDAO.getValues(acccountEmail) != null) {
			// 리프레쉬 토큰 삭제
			redisDAO.deleteValues(acccountEmail, atk, expiration);
		}

	}

}
