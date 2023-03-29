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

		System.out.println("----AccountServcie -----");
		
		// 1. Authentication 객체 생성 
		UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(login.getAccountEmail(), login.getAccountPassword());
		/*   Authentication 객체를 생성함. 아직 인증 완료 되지 않음.  Granted Authorities=[]]
		 * 
		 * UsernamePasswordAuthenticationToken은 AbstractAuthenticationToken을 상속
		 * AbstractAuthenticationToken은 Authentication을 상속
		 *  jwtProvier 생성으로 넘어갈 것.
		 * */
		
		System.out.println(authenticationToken);
						
		// 2. AuthenticationManager 인증 시작 -  아이디 , 비밀번호 검증 모두 알아서
		// 아이디 틀려도 비번 틀려도 authentication.BadCredentialsException // exception.getMessage() : 자격 증명에 실패하였습니다.
		// AuthenticationManager 를 위해 UserDetailsService.loadUserByUsername 오버라이딩 필수. authenticationManager.authenticate 에서 loadUserByUsername 가 실행 포함.
		
		// authenticationManager.authenticate
		// AuthenticationProvider에게 위임하여 진행
		// => 유효한지 확인 후 UserDetailsService가 return 한 객체를 Principal로 담고 있는 Authentication 객체를 return
		Authentication authentication = authenticationManager.authenticate(authenticationToken);
		System.out.println(authentication); 
		System.out.println(authentication.getPrincipal()); 
		System.out.println(authentication.getAuthorities()); 
		System.out.println(authentication.toString()); 
		 
		// 이렇게 인증 완료 후  반환 받은 authentication를 session 에 저장하기 위해 securityContextHolder 에 저장하는 게 원래 마무리 작업
		// jwt 인증 방식으로 세션을 사용하지 않을 것이기 때문에 로그인 인증 후 securityContextHolder 에 따로 저장하지 않고, 토큰 생성 jwtProvider 실행

		TokenResponse token = jwtProvider.createTokenByLogin(authentication);
		
		return token;
	}
	


}
