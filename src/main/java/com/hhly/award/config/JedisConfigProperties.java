/**
 * @see     redis基础参数配置
 * @author  scott
 * @date    2018-03-03
 * @company 深圳益彩网络科技 
 */
package com.hhly.award.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@ConfigurationProperties(prefix="spring.redis.cluster")
@Component
public class JedisConfigProperties {
	
	private  String nodes ;

    private int maxTotal ;

    private int maxIdle ;

    private int  softMinEvictableIdleTimeMillis ;

    private int maxWaitMillis ;

    private boolean jmxEnabled;

    private boolean testOnBorrow ;

    private boolean testOnReturn ;

    private boolean blockWhenExhausted ;

    private int connectionTimeout ;

    private int soTimeout ;

    private int maxAttempts ;

    private String password ;
    
    private boolean cacheEnable;

	public String getNodes() {
		return nodes;
	}

	public void setNodes(String nodes) {
		this.nodes = nodes;
	}

	public int getMaxTotal() {
		return maxTotal;
	}

	public void setMaxTotal(int maxTotal) {
		this.maxTotal = maxTotal;
	}

	public int getMaxIdle() {
		return maxIdle;
	}

	public void setMaxIdle(int maxIdle) {
		this.maxIdle = maxIdle;
	}

	public int getSoftMinEvictableIdleTimeMillis() {
		return softMinEvictableIdleTimeMillis;
	}

	public void setSoftMinEvictableIdleTimeMillis(int softMinEvictableIdleTimeMillis) {
		this.softMinEvictableIdleTimeMillis = softMinEvictableIdleTimeMillis;
	}

	public int getMaxWaitMillis() {
		return maxWaitMillis;
	}

	public void setMaxWaitMillis(int maxWaitMillis) {
		this.maxWaitMillis = maxWaitMillis;
	}

	public boolean isJmxEnabled() {
		return jmxEnabled;
	}

	public void setJmxEnabled(boolean jmxEnabled) {
		this.jmxEnabled = jmxEnabled;
	}

	public boolean isTestOnBorrow() {
		return testOnBorrow;
	}

	public void setTestOnBorrow(boolean testOnBorrow) {
		this.testOnBorrow = testOnBorrow;
	}

	public boolean isTestOnReturn() {
		return testOnReturn;
	}

	public void setTestOnReturn(boolean testOnReturn) {
		this.testOnReturn = testOnReturn;
	}

	public boolean isBlockWhenExhausted() {
		return blockWhenExhausted;
	}

	public void setBlockWhenExhausted(boolean blockWhenExhausted) {
		this.blockWhenExhausted = blockWhenExhausted;
	}

	public int getConnectionTimeout() {
		return connectionTimeout;
	}

	public void setConnectionTimeout(int connectionTimeout) {
		this.connectionTimeout = connectionTimeout;
	}

	public int getSoTimeout() {
		return soTimeout;
	}

	public void setSoTimeout(int soTimeout) {
		this.soTimeout = soTimeout;
	}

	public int getMaxAttempts() {
		return maxAttempts;
	}

	public void setMaxAttempts(int maxAttempts) {
		this.maxAttempts = maxAttempts;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public boolean isCacheEnable() {
		return cacheEnable;
	}

	public void setCacheEnable(boolean cacheEnable) {
		this.cacheEnable = cacheEnable;
	}

}
