package io.csy.controller;


import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;

import io.csy.jwt.AccountDetails;
import io.csy.jwt.JwtProvider;
import io.csy.jwt.TokenResponse;
import io.csy.model.AccountService;
import io.csy.model.dto.AccountDTO;
import io.csy.model.dto.LoginDTO;
import io.csy.model.dto.SignUpDTO;
import lombok.RequiredArgsConstructor;


@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/auth")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class AccountController {

	private final AccountService accountService;
	private final JwtProvider jwtProvider;

	@PostMapping("/sign-up")
	public void signUp(@Valid @RequestBody SignUpDTO signUp) throws Exception {

		AccountDTO account = accountService.signUp(signUp);
	}

	@PostMapping("/login")
	public TokenResponse login(@Valid @RequestBody LoginDTO login) throws Exception {
		
		System.out.println("Controller-login");
		TokenResponse token = accountService.login(login);
		
		return token;

	}
	
	@GetMapping("/reissue")
	public TokenResponse reissue(@AuthenticationPrincipal AccountDetails accountDetails) throws JsonProcessingException {
		
		return jwtProvider.reissueATK(accountDetails);
		
	}
	
	@GetMapping("/logout")
	public void logout(HttpServletRequest request) throws JsonProcessingException {
		
		accountService.logout(request);
		
	}

}
