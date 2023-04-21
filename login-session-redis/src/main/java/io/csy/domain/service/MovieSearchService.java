package io.csy.domain.service;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import io.csy.domain.dto.MovieDTO;

@Service
public class MovieSearchService {

	public Optional<List<MovieDTO>> findAll() {

		return Optional.of(Arrays.asList(MovieDTO.builder().movieName("스파이더맨").movieRanking(1).build(),
				MovieDTO.builder().movieName("아바타").movieRanking(2).build()));
	}

}
