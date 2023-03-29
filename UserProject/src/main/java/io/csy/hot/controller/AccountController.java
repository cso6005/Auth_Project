package io.csy.hot.controller;

import javax.validation.Valid;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.csy.hot.jwt.TokenResponse;
import io.csy.hot.model.AccountService;
import io.csy.hot.model.dto.AccountDTO;
import io.csy.hot.model.dto.LoginDTO;
import io.csy.hot.model.dto.SignUpDTO;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("auth")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class AccountController {

	private final AccountService accountService;

	@PostMapping("/sign-up")
	public void signUp(@Valid @RequestBody SignUpDTO signUp) throws Exception {

		AccountDTO account = accountService.signUp(signUp);
	}

	@PostMapping("/login")
	public TokenResponse login(@Valid @RequestBody LoginDTO login) throws Exception {
		
		TokenResponse token = accountService.login(login);
		
		return token;

	}

}
