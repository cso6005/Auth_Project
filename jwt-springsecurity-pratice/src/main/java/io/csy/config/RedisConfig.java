package io.csy.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;
import org.springframework.data.redis.serializer.StringRedisSerializer;

//https://luvstudy.tistory.com/143
//https://bcp0109.tistory.com/328
/*
 * - Redis를 접근할 수 있는 프레임워크로 Lettuce, Jedis가 있다.
   Lettuce는 별도의 설정 없이 이용할 수 있고, Jedis는 별도의 의존성 추가가 필요하다.
 
* - Spring Data Redis로 Redis에 접근하는 방식으로는
1. RedisTemplate 2. RedisRepository
방식이 존재한다. 나는 1번으로 사용.

* */
@Configuration
@EnableRedisRepositories
public class RedisConfig {
	
	@Value("${spring.redis.host}")
	private String host;
	
	@Value("${spring.redis.port}")
	private int port;
	
	// Redis 저장소와 연결
	// RedisConnectionFactory 인터페이스를 통하여 LettuceConnectionFactory를 생성하여 반환
	@Bean
	public RedisConnectionFactory redisConnectionFactory() {
		return new LettuceConnectionFactory(host, port);
	}
	
	//  StringRedisTemplate bean 생성
	/* RedisTemplate: java Object를 redis에 저장하는 경우 사용
	 * StringRedisTemplate: 일반적인 String 값을 key, value로 사용하는 경우 사용*/
	@Bean
	public StringRedisTemplate redisTemplate() {
		
		StringRedisTemplate redisTemplate = new StringRedisTemplate();
		//RedisTemplate<String, String> redisTemplate = new RedisTemplate<String, String>();
		
		/*
		 * setKeySerializer, setValueSerializer 설정 이유는,
		 * RedisTemplate 사용 시에
		 * Spring-Redis 간 데이터 직렬화, 역직렬화에 사용하는 방식이 Jdk 직렬화 방식이기 때문이다.
		 * */	
		
		redisTemplate.setKeySerializer(new StringRedisSerializer());
		redisTemplate.setValueSerializer(new StringRedisSerializer());
		
		redisTemplate.setConnectionFactory(redisConnectionFactory());
		
		return redisTemplate;

	}

}
