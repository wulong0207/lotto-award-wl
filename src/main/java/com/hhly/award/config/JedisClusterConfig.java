/**
 * @see     redis集群配置
 * @author  scott
 * @date    2018-03-03
 * @company 深圳益彩网络科技 
 */
package com.hhly.award.config;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.MapPropertySource;
import org.springframework.data.redis.connection.RedisClusterConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.JdkSerializationRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.JedisPoolConfig;

@Configuration
public class JedisClusterConfig {

	protected final Logger logger = LoggerFactory.getLogger(JedisClusterConfig.class);

	@Resource
	private JedisConfigProperties jedisConfigProperties;

	private JedisPoolConfig jedisPoolConfig() {
		JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
		jedisPoolConfig.setMaxIdle(jedisConfigProperties.getMaxIdle());
		jedisPoolConfig.setMaxWaitMillis(jedisConfigProperties.getMaxWaitMillis());
		jedisPoolConfig.setJmxEnabled(jedisConfigProperties.isJmxEnabled());
		jedisPoolConfig.setSoftMinEvictableIdleTimeMillis(jedisConfigProperties.getSoftMinEvictableIdleTimeMillis());
		jedisPoolConfig.setTestOnBorrow(jedisConfigProperties.isTestOnBorrow());
		jedisPoolConfig.setTestOnReturn(jedisConfigProperties.isTestOnReturn());
		return jedisPoolConfig;
	}

	@Bean
	public RedisClusterConfiguration redisClusterConfiguration() {
		Map<String, Object> source = new HashMap<String, Object>();
		source.put("spring.redis.cluster.nodes", jedisConfigProperties.getNodes());
		source.put("spring.redis.cluster.timeout", jedisConfigProperties.getConnectionTimeout());
		return new RedisClusterConfiguration(new MapPropertySource("RedisClusterConfiguration", source));
	}

	@Bean
	public JedisConnectionFactory jedisConnectionFactory() {
		JedisConnectionFactory factory = new JedisConnectionFactory(redisClusterConfiguration(), jedisPoolConfig());
		factory.setPassword(jedisConfigProperties.getPassword());
		return factory;
	}

	@Bean
	public JedisCluster jedisClusterFactory() {
        String[] serverArray = jedisConfigProperties.getNodes().split(",");
		Set<HostAndPort> nodes = new HashSet<>();
		for (String ipPort : serverArray) {
			String[] ipPortPair = ipPort.split(":");
			nodes.add(new HostAndPort(ipPortPair[0].trim(), Integer.valueOf(ipPortPair[1].trim())));
		}
		logger.info("nodes num=" + nodes.size());
		JedisCluster jedisCluster = new JedisCluster(nodes, jedisConfigProperties.getConnectionTimeout(),
				jedisConfigProperties.getSoTimeout(), jedisConfigProperties.getMaxAttempts(),
				jedisConfigProperties.getPassword(), jedisPoolConfig());
		return jedisCluster ;
	}
	
	
	 @Bean
    public RedisTemplate<String,Object> redisTemplateFactory() {
        RedisTemplate<String,Object> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(jedisConnectionFactory());
        RedisSerializer stringSerializer = new StringRedisSerializer();
        RedisSerializer jdkSerializer = new JdkSerializationRedisSerializer();
        redisTemplate.setKeySerializer(stringSerializer);
	    redisTemplate.setValueSerializer(jdkSerializer);
	    redisTemplate.setHashKeySerializer(stringSerializer);
	    redisTemplate.setHashValueSerializer(stringSerializer);
        return redisTemplate;
    }

	
	

}
