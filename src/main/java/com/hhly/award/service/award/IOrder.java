package com.hhly.award.service.award;

import java.util.List;

import com.hhly.award.service.award.entity.AwardResultBO;

/**
 * @desc
 * @author jiangwei
 * @date 2017年5月24日
 * @company 益彩网络科技公司
 * @version 1.0
 */
public interface IOrder {
	/**
	 * 分配订单进行开奖
	 * @author jiangwei
	 * @Version 1.0
	 * @CreatDate 2017年5月24日 下午2:41:56
	 * @param orders
	 * @param type 0正常开奖  1重置开奖 （开奖专用）
	 */
	void  distribute(List<String> orders,int lotteryCode,String issue, short handleType,Integer type);
	/**
	 * @Description: 查询需要开奖的推荐订单进行开奖
	 * @param lotteryCode
	 * @param lotteryChild 
	 * @param 1:重置开奖，0：开奖
	 * @param orderStatus
	 * @param systemCode 重置的赛事编号
	 * @author wuLong
	 * @date 2018年1月8日 上午9:58:26
	 */
	void RecommendDistribute(Integer lotteryCode,Integer lotteryChild,Integer orderStatus,int type,String systemCode);
	/**
	 * 获取实时开奖结果
	 * @author jiangwei
	 * @Version 1.0
	 * @CreatDate 2017年5月25日 上午10:15:13
	 * @param lotteryCode
	 * @param issue
	 * @return
	 */
	AwardResultBO getResult(int lotteryCode, String issue, short handleType);
}
