package io.csy.controller;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("board")
@CrossOrigin(origins = "http://localhost:3000", allowedHeaders = "*")
public class BoardController {
	
	@PostMapping("/test")
	public void test(@RequestParam("id") int num) {
		System.out.println("테스트");
	}
}
