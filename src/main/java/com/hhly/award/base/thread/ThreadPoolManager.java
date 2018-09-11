package com.hhly.award.base.thread;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
/**
 * @desc 线程池
 * @author jiangwei
 * @date 2017年8月28日
 * @company 益彩网络科技公司
 * @version 1.0
 */
public class ThreadPoolManager {
	
	private static final ExecutorService  THREAD_POOL = Executors.newCachedThreadPool();
	/**
	 * 通过CachedThreadPool执行任务
	 * @author jiangwei
	 * @Version 1.0
	 * @CreatDate 2017年8月28日 下午3:29:34
	 * @param task
	 */
	public static void executeCachedThread(Runnable task){
		THREAD_POOL.execute(task);
	}
}
