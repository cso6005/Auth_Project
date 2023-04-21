package io.csy.common;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;


@Component
@RequiredArgsConstructor
public class RedisDAO {
	
	private final StringRedisTemplate redisTemplate;
	
	public void setValues(String key, String data) {
		
		// ValueOperation 객체 반환
		ValueOperations<String, String> values = redisTemplate.opsForValue();
		values.set(key, data);
		
	}
	
	public void setValues(String key, String data, Duration duration) {
		
		ValueOperations<String, String> values = redisTemplate.opsForValue();
		values.set(key, data, duration); // rtk 유효시간으로 자동 삭제 되게
		
	}
	
	public String getValues(String key) {
		
		ValueOperations<String, String> values = redisTemplate.opsForValue();
	
		return values.get(key);
		
	}
	
	// 로그아웃 시, 삭제
	public void deleteValues(String key, String data, Long expiration) {
		
		//Redis Cache에 로그아웃 된 계정을 저장하기 - 블랙리스트로 등록하는 것. 
        redisTemplate.opsForValue().set(data, "logout", expiration, TimeUnit.MILLISECONDS);

		redisTemplate.delete(key);
	}

}
