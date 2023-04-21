package io.csy.web.controller;

import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.csy.domain.dto.MovieDTO;
import io.csy.domain.service.MovieSearchService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("api/v1/movie")
@RequiredArgsConstructor
public class MovieSearchController {
	
	private final MovieSearchService movieSearchService;
	
	
	@GetMapping("/search")
	public List<MovieDTO> getAllMovie(HttpSession session) {
		
		log.info("sessionID : " + session.getId());
		log.info(String.valueOf(session.getAttribute("email")));
		log.info(String.valueOf(session.getAttribute("role")));
		
		return movieSearchService.findAll().orElse(Collections.emptyList());
	}
	

}
