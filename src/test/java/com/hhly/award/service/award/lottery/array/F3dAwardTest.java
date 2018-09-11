package com.hhly.award.service.award.lottery.array;

import static org.junit.Assert.fail;

import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.hhly.award.DefaultDao;
import com.hhly.award.bo.TicketInfoBO;
import com.hhly.award.service.award.entity.WinMoneyBO;

public class F3dAwardTest extends DefaultDao {
	
	@Autowired
	F3dAward award;
	
	@Before
	public void before() {
		award.handleDrawDetail(
				"直选|1|1040,组三|244|346,组六|10|173");
	}
	@Test
	public void direct() {
		award.handleDrawCode("3|2|1");
		TicketInfoBO detail = new TicketInfoBO();
		detail.setMultipleNum(1);
		detail.setTicketContent("3|2|1;1|2|2;4|2|1");
		detail.setContentType(1);
		detail.setLotteryChildCode(10501);
		WinMoneyBO bo = award.computeWinMoney(detail);
		if (bo.getPreBonus() != 1040) {
			fail("直选开奖错误");
		}
		detail.setTicketContent("5,3|5,2|1,7");
		detail.setContentType(2);
		bo = award.computeWinMoney(detail);
		if (bo.getPreBonus() != 1040) {
			fail("直选开奖错误");
		}
		detail.setTicketContent("3|1|2,7");
		detail.setContentType(2);
		bo = award.computeWinMoney(detail);
		if (bo.getPreBonus() > 0) {
			fail("直选开奖错误");
		}
		
		detail.setTicketContent("6");
		detail.setContentType(6);
		bo = award.computeWinMoney(detail);
		if (bo.getPreBonus() != 1040) {
			fail("直选开奖错误");
		}
		
		detail.setTicketContent("5");
		detail.setContentType(6);
		bo = award.computeWinMoney(detail);
		if (bo.getPreBonus() > 0) {
			fail("直选开奖错误");
		}
	}
	
	@Test
	public void group3Simple() {
		award.handleDrawCode("3|2|2");
		TicketInfoBO detail = new TicketInfoBO();
		detail.setMultipleNum(1);
		detail.setTicketContent("2,3,1;3,2,2;4,2,1");
		detail.setContentType(1);
		detail.setLotteryChildCode(10502);
		WinMoneyBO bo = award.computeWinMoney(detail);
		if (bo.getPreBonus() != 346) {
			fail("组3 开奖错误");
		}
	}
	
	@Test
	public void group3Repeated() {
		award.handleDrawCode("3|2|3");
		TicketInfoBO detail = new TicketInfoBO();
		detail.setMultipleNum(1);
		detail.setTicketContent("1,3,2");
		detail.setContentType(2);
		detail.setLotteryChildCode(10502);
		WinMoneyBO bo = award.computeWinMoney(detail);
		if (bo.getPreBonus() != 346) {
			fail("组3 开奖错误");
		}
	}
	
	
	@Test
	public void group3Sum() {
		award.handleDrawCode("3|2|3");
		TicketInfoBO detail = new TicketInfoBO();
		detail.setMultipleNum(1);
		detail.setTicketContent("8");
		detail.setContentType(6);
		detail.setLotteryChildCode(10502);
		WinMoneyBO bo = award.computeWinMoney(detail);
		if (bo.getPreBonus() != 346) {
			fail("组3 开奖错误");
		}
		detail.setTicketContent("6");
		bo = award.computeWinMoney(detail);
		if (bo.getPreBonus() >0) {
			fail("组3 开奖错误");
		}
	}
	
	@Test
	public void group6Simple() {
		award.handleDrawCode("3|2|1");
		TicketInfoBO detail = new TicketInfoBO();
		detail.setMultipleNum(1);
		detail.setTicketContent("2,3,4;3,2,1;4,1,3");
		detail.setContentType(1);
		detail.setLotteryChildCode(10503);
		WinMoneyBO bo = award.computeWinMoney(detail);
		if (bo.getPreBonus() != 173) {
			fail("组3 开奖错误");
		}
	}
	
	@Test
	public void group6Repeated() {
		award.handleDrawCode("1|3|2");
		TicketInfoBO detail = new TicketInfoBO();
		detail.setMultipleNum(1);
		detail.setTicketContent("1,3,4,5,2");
		detail.setContentType(2);
		detail.setLotteryChildCode(10503);
		WinMoneyBO bo = award.computeWinMoney(detail);
		if (bo.getPreBonus() != 173) {
			fail("组3 开奖错误");
		}
	}
	
	
	@Test
	public void group6Sum() {
		award.handleDrawCode("1|2|5");
		TicketInfoBO detail = new TicketInfoBO();
		detail.setMultipleNum(1);
		detail.setTicketContent("8");
		detail.setContentType(6);
		detail.setLotteryChildCode(10503);
		WinMoneyBO bo = award.computeWinMoney(detail);
		if (bo.getPreBonus() != 173) {
			fail("组3 开奖错误");
		}
		detail.setTicketContent("6");
		bo = award.computeWinMoney(detail);
		if (bo.getPreBonus() >0) {
			fail("组3 开奖错误");
		}
	}
}
