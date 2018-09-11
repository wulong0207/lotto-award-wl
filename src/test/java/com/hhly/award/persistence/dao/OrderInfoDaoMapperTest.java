package com.hhly.award.persistence.dao;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.hhly.award.DefaultDao;
import com.hhly.award.bo.OrderInfoBO;

public class OrderInfoDaoMapperTest extends DefaultDao{
	
	@Autowired
	OrderInfoDaoMapper mapper;
	@Test
	public void testGetOrderInfoS() {
		List<String> orderCodes = new ArrayList<>();
		orderCodes.add("D1705190951110100001");
		orderCodes.add("D1705191544110100001");
		mapper.getOrderInfoS(orderCodes);
	}

	@Test
	public void testGetOrderInfosByMaxBuyScreen() {
		mapper.getOrderInfosByMaxBuyScreen(1705232302);
	}

	@Test
	public void testFindOrderDetail() {
		mapper.findOrderDetail("D1705231656560100094");
	}
	@Test
	public void testUpdateOrderInfo() {
		OrderInfoBO orderInfoBO = new OrderInfoBO();
		orderInfoBO.setWinningDetail("一等奖");
		orderInfoBO.setPreBonus(0.0);
		orderInfoBO.setAddedBonus(0.0);
		orderInfoBO.setAftBonus(0.0);
		orderInfoBO.setWinningStatus((short)2);
		orderInfoBO.setDrawCode("132");
		orderInfoBO.setId(124L);
		mapper.updateOrderInfo(orderInfoBO);
	}

	@Test
	public void testGetLotteryIssue() {
		mapper.getLotteryIssue(100, "2017060",(short)2);
	}

	@Test
	public void testUpdateLotteryIssueStatus() {
		mapper.updateLotteryIssueStatus(100, "2017060",(short)2);
	}
}
