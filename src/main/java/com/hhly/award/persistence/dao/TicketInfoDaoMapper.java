package com.hhly.award.persistence.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.hhly.award.bo.TicketInfoBO;

/**
 * @desc 票信息数据接口
 * @author huangb
 * @date 2017年2月20日
 * @company 益彩网络
 * @version v1.0
 */
public interface TicketInfoDaoMapper {
	/**
	 * 修改订单票
	 * @author jiangwei
	 * @Version 1.0
	 * @CreatDate 2017年5月30日 上午9:39:07
	 * @param lotteryCode
	 * @param lotteryIssue
	 */
	void updateTicketRestart(@Param("orders")List<String> orders);
	
	/**
	 * 开奖后修改票信息
	 * @author jiangwei
	 * @Version 1.0
	 * @CreatDate 2017年5月27日 下午2:43:20
	 * @param ticketInfoBOs
	 */
	void updateTicketInfo(@Param("tickets")List<TicketInfoBO> ticketInfoBOs);
	/**
	 * 查询未开奖的订单
	 * @author jiangwei
	 * @Version 1.0
	 * @CreatDate 2017年7月31日 上午10:03:39
	 * @param lotteryCode
	 * @param lotteryIssue
	 * @return
	 */
	int countTicketNotDraw(@Param("lotteryCode")int lotteryCode, @Param("lotteryIssue")String lotteryIssue);
}