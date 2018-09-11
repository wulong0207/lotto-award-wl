/**
 * @desc    字符串 redis处理类
 * @author  scott
 * @date    2017-03-08
 * @company 益彩网络科技
 * @version V1.0
 */
package com.hhly.award.util;

import java.text.MessageFormat;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;

@Component("redisUtil")
public class RedisUtil {

	private static final Logger logger = Logger.getLogger(RedisUtil.class);

	/** 字符串缓存模板 */
	@Autowired
	private StringRedisTemplate strRedisTemplate;
	/** 对象，集合缓存模板  */
	@Autowired
	private RedisTemplate<String, Object> redisTemplate;

	/**
	 * @see    add缓存数据
	 * @author scott
	 * @param  key  
	 * @param  value
	 * @param  seconds 
	 */
	public void addString(String key, String value, Long seconds) {

		try {
			// 字符串redis 存储
			ValueOperations<String, String> valOps = strRedisTemplate.opsForValue();
			if (seconds != null) {
				valOps.set(key, value, seconds, TimeUnit.SECONDS);
			} else {
				valOps.set(key, value);
			}
		} catch (Exception e) {
			logger.warn(spellString("addString {0}={1},{2}", key, value, seconds), e);
		}
	}

	/**
	 * @see    get缓存数据
	 * @author scott
	 * @param  key 
	 * @return String
	 * 
	 */
	public String getString(String key) {
		String result = "";
		try {
			result = strRedisTemplate.opsForValue().get(key);
		} catch (Exception e) {
			logger.warn(spellString("getString {0}", key), e);
		}
		return result;
	}

	/**
	 * @see    del缓存数据
	 * @author scott
	 * @param  key 
	 */
	public void delString(String key) {
		try {
			strRedisTemplate.delete(key);
		} catch (Exception e) {
			logger.warn(spellString("delString {0}", key), e);
		}
	}

	/**  
	* 方法说明: 批量模糊删除key  key*
	* @auth: xiongJinGang
	* @param key
	* @time: 2017年5月26日 下午3:34:27
	* @return: void 
	*/
	public void delAllString(String key) {
		try {
			if (!ObjectUtil.isBlank(key)) {
				if (!key.endsWith("*")) {
					key += "*";
				}
				Set<String> keys = strRedisTemplate.keys(key);
				Iterator<String> it = keys.iterator();
				while (it.hasNext()) {
					String singleKey = it.next();
					delString(singleKey);
				}
			}
		} catch (Exception e) {
			logger.warn(spellString("delString {0}", key), e);
		}
	}

	/**  
	* 方法说明: 模糊获取key
	* @auth: xiongJinGang
	* @param key
	* @time: 2017年8月1日 上午10:02:59
	* @return: Set<String> 
	*/
	public Set<String> keys(String key) {
		Set<String> keys = null;
		if (!ObjectUtil.isBlank(key)) {
			if (!key.endsWith("*")) {
				key += "*";
			}
			return strRedisTemplate.keys(key);
		}
		return keys;
	}

	/**
	 * @see   add 缓存数据
	 * @param key 
	 * @param value
	 * @param time
	 */
	public void addObj(String key, Object obj, Long seconds) {
		try {
			// 对象redis存储
			ValueOperations<String, Object> objOps = redisTemplate.opsForValue();
			if (seconds != null) {
				objOps.set(key, obj, seconds, TimeUnit.SECONDS);
			} else {
				objOps.set(key, obj);
			}
		} catch (Exception e) {
			logger.warn(spellString("addObj {0}={1},{2}", key, obj, seconds), e);
		}
	}

	/**
	 * @see    get 缓存数据
	 * @param  key 
	 * @return Object
	 */
	public Object getObj(String key) {
		Object object = null;
		try {
			object = redisTemplate.opsForValue().get(key);
		} catch (Exception e) {
			logger.warn(spellString("getObj {0}", key), e);
		}
		return object;
	}

	/**
	 * @see    get 缓存数据
	 * @param  key 
	 * @return Object
	 */
	@SuppressWarnings({ "unchecked" })
	public <T> T getObj(String key, T t) {
		Object o = null;
		try {
			o = redisTemplate.opsForValue().get(key);
		} catch (Exception e) {
			logger.warn(spellString("getObj {0}->{1}", key, t), e);
		}
		return o == null ? null : (T) o;
	}

	/**
	 * 
	 * @Description: 重新设置key有效时间
	 * @param key 
	 * @param second
	 * @author wuLong
	 * @date 2017年3月22日 下午2:38:57
	 */
	public void expire(String key, long second) {
		try {
			strRedisTemplate.expire(key, second, TimeUnit.SECONDS);
		} catch (Exception e) {
			logger.warn(spellString("expire {0}={1}", key, second), e);
		}
	}

	/**
	 * @see   del 缓存数据
	 * @param key
	 */
	public void delObj(String key) {
		try {
			redisTemplate.delete(key);
		} catch (Exception e) {
			logger.warn(spellString("delObj {0}", key), e);
		}
	}

	/** 
	* 压栈 
	*  
	* @param key 
	* @param value 
	* @return 
	*/
	public Long push(String key, String value) {
		Long result = 0l;
		try {
			result = strRedisTemplate.opsForList().leftPush(key, value);
		} catch (Exception e) {
			logger.warn(spellString("push {0}={1}", key, value), e);
		}
		return result;
	}

	/** 
	* 出栈 
	*  
	* @param key 
	* @return 
	*/
	public String pop(String key) {
		String popResult = "";
		try {
			popResult = strRedisTemplate.opsForList().leftPop(key);
		} catch (Exception e) {
			logger.warn(spellString("pop {0}", key), e);
		}
		return popResult;
	}

	/** 
	* 入队 
	*  
	* @param key 
	* @param value 
	* @return 
	*/
	public Long in(String key, String value) {
		Long inResult = 0l;
		try {
			inResult = strRedisTemplate.opsForList().rightPush(key, value);
		} catch (Exception e) {
			logger.warn(spellString("in {0}={1}", key, value), e);
		}
		return inResult;
	}

	/** 
	* 出队 
	*  
	* @param key 
	* @return 
	*/
	public String out(String key) {
		String outResult = "";
		try {
			outResult = strRedisTemplate.opsForList().leftPop(key);
		} catch (Exception e) {
			logger.warn(spellString("out {0}", key), e);
		}
		return outResult;
	}

	/** 
	* 栈/队列长 
	*  
	* @param key 
	* @return 
	*/
	public Long length(String key) {
		Long lengthResult = 0l;
		try {
			lengthResult = strRedisTemplate.opsForList().size(key);
		} catch (Exception e) {
			logger.warn(spellString("length {0}", key), e);
		}
		return lengthResult;
	}

	/** 
	* 范围检索 
	*  
	* @param key 
	* @param start 
	* @param end 
	* @return 
	*/
	public List<String> range(String key, int start, int end) {
		List<String> rangeResult = null;
		try {
			rangeResult = strRedisTemplate.opsForList().range(key, start, end);
		} catch (Exception e) {
			logger.warn(spellString("range {0},{1}-{2}", key, start, end), e);
		}
		return rangeResult;
	}

	/** 
	* 移除 
	*  
	* @param key 
	* @param i 
	* @param value 
	*/
	public void remove(String key, long i, String value) {
		try {
			strRedisTemplate.opsForList().remove(key, i, value);
		} catch (Exception e) {
			logger.warn(spellString("remove {0}={1},{2}", key, value, i), e);
		}
	}

	/** 
	* 检索 
	*  
	* @param key 
	* @param index 
	* @return 
	*/
	public String index(String key, long index) {
		String indexResult = "";
		try {
			indexResult = strRedisTemplate.opsForList().index(key, index);
		} catch (Exception e) {
			logger.warn(spellString("index {0}", key), e);
		}
		return indexResult;
	}

	/** 
	* 置值 
	*  
	* @param key 
	* @param index 
	* @param value 
	*/
	public void set(String key, long index, String value) {
		try {
			strRedisTemplate.opsForList().set(key, index, value);
		} catch (Exception e) {
			logger.warn(spellString("set {0}={1},{2}", key, value, index), e);
		}
	}

	/** 
	* 裁剪 
	*  
	* @param key 
	* @param start 
	* @param end 
	*/
	public void trim(String key, long start, int end) {
		try {
			strRedisTemplate.opsForList().trim(key, start, end);
		} catch (Exception e) {
			logger.warn(spellString("trim {0},{1}-{2}", key, start, end), e);
		}
	}

	/**  
	* 方法说明: 原子性自增
	* @param key 自增的key
	* @param value 每次自增的值
	* @time: 2017年3月9日 下午4:28:21
	* @return: Long 
	*/
	public Long incr(String key, long value) {
		Long incrResult = 0l;
		try {
			incrResult = strRedisTemplate.opsForValue().increment(key, value);
		} catch (Exception e) {
			logger.warn(spellString("incr {0}={1}", key, value), e);
		}
		return incrResult;
	}

	/**
	 * 拼异常内容
	 * @param errStr
	 * @param arguments
	 * @return
	 */
	private String spellString(String errStr, Object... arguments) {
		return MessageFormat.format(errStr, arguments);
	}
}
