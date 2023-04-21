package io.csy.web.controller;

import static io.csy.domain.dto.SecurityConstants.KEY_ROLE;

import javax.servlet.http.HttpSession;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.csy.domain.service.AuthService;
import io.csy.web.dto.LoginRequestDTO;
import io.csy.web.exception.LoginFailedException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("api/v1/auth")
@RequiredArgsConstructor
public class AuthContorller {
	
	private final AuthService authService;
	
	@PostMapping("/login")
	public ResponseEntity<String> login(HttpSession httpSession, @RequestBody LoginRequestDTO loginRequestDTO) {
		
		var account = authService.login(loginRequestDTO.getEmail(), loginRequestDTO.getPassword());
		
		log.info(account.get().getEmail() + "고객님 login 시도" );
		
		if(account.isPresent()) {
			httpSession.setAttribute(KEY_ROLE, account.get().getRole().name());
			httpSession.setAttribute("email", account.get().getEmail());
			httpSession.setMaxInactiveInterval(1800);
		} else {
			throw new LoginFailedException();
		}
		
		return ResponseEntity.ok("OK");
		 
	}

}
