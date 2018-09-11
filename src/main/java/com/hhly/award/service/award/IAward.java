package com.hhly.award.service.award;

import java.util.List;
import java.util.Map;

import com.hhly.award.service.award.entity.AwardOrderBO;
import com.hhly.award.service.award.entity.FailOrderBO;

/**
 * @desc 开奖
 * @author jiangwei
 * @date 2017年5月24日
 * @company 益彩网络科技公司
 * @version 1.0
 */
public interface IAward {
	/**
	 * 开奖
	 * @author jiangwei
	 * @Version 1.0
	 * @CreatDate 2017年5月24日 下午12:06:06
	 * @param orders 订单号
	 * @return 开奖失败订单信息
	 */
	AwardOrderBO handle(List<String> orders,boolean isBonus,int type);

	/**
	 * 派奖
	 * @param orders
	 * @return
	 * @date 2017年6月19日下午6:29:49
	 * @author cheng.chen
	 */
	List<FailOrderBO> payoutHandle(List<String> orders);
	
	/**
	 * 获取执行时间
	 * @author jiangwei
	 * @Version 1.0
	 * @CreatDate 2017年5月30日 上午10:02:49
	 * @return
	 */
	Map<String,Long> getExecuteTime();
	/**
	 * 获取开奖号码
	 * @author jiangwei
	 * @Version 1.0
	 * @CreatDate 2017年6月15日 上午11:14:01
	 * @return
	 */
	String getDrawCode();
}
