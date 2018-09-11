package com.hhly.award.service.award.lottery.high.k3;

import static org.junit.Assert.fail;

import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.hhly.award.DefaultDao;
import com.hhly.award.bo.TicketInfoBO;
import com.hhly.award.service.award.entity.WinMoneyBO;

public class Jsk3AwardTest extends DefaultDao {

	@Autowired
	Jsk3Award award;

	@Before
	public void before() {
		award.handleDrawDetail(
				"和值3|240,和值4|80,和值5|40,和值6|25,和值7|16,和值8|12,和值9|10,和值10|9,和值11|9,和值12|10,和值13|12,和值14|16,和值15|25,和值16|40,和值17|80,和值18|240,三同号通选|40,三同号单选|240,三不同号|40,三连号通选|10,二同号复选|15,二同号单选|80,二不同号|8");
	}

	@Test
	public void testSum() {
		award.handleDrawCode("1,1,1");
		TicketInfoBO detail = new TicketInfoBO();
		detail.setMultipleNum(1);
		detail.setTicketContent("3");
		detail.setLotteryChildCode(23301);
		WinMoneyBO bo = award.computeWinMoney(detail);
		if (bo.getPreBonus() != 240) {
			fail("和值开奖错误");
		}
		detail.setTicketContent("11");
	    bo = award.computeWinMoney(detail);
		if (bo.getPreBonus() > 0) {
			fail("和值开奖错误");
		}
	}
	
	@Test
	public void threeSame() {
		award.handleDrawCode("1,1,1");
		TicketInfoBO detail = new TicketInfoBO();
		detail.setMultipleNum(1);
		detail.setTicketContent("9,9,9");
		detail.setLotteryChildCode(23307);
		WinMoneyBO bo = award.computeWinMoney(detail);
		if (bo.getPreBonus() != 40) {
			fail("三同号通选");
		}
		award.handleDrawCode("1,1,2");
		bo = award.computeWinMoney(detail);
		if (bo.getPreBonus() > 0) {
			fail("三同号通选");
		}
		award.handleDrawCode("6,6,6");
		bo = award.computeWinMoney(detail);
		if (bo.getPreBonus() != 40) {
			fail("三同号通选");
		}
	}
	@Test
	public void threeSameSimple(){
		award.handleDrawCode("1,1,1");
		TicketInfoBO detail = new TicketInfoBO();
		detail.setMultipleNum(1);
		detail.setTicketContent("1,1,1;9,9,9");
		detail.setLotteryChildCode(23306);
		WinMoneyBO bo = award.computeWinMoney(detail);
		if (bo.getPreBonus() != 240) {
			fail("三同号单选");
		}
		award.handleDrawCode("1,2,3");
		bo = award.computeWinMoney(detail);
		if (bo.getPreBonus() > 0) {
			fail("三同号单选");
		}
	}
	@Test
	public void threeDifferenceSimple(){
		award.handleDrawCode("1,1,1");
		TicketInfoBO detail = new TicketInfoBO();
		detail.setMultipleNum(1);
		detail.setTicketContent("1,3,4;1,2,4;1,3,4");
		detail.setLotteryChildCode(23305);
		WinMoneyBO bo = award.computeWinMoney(detail);
		if (bo.getPreBonus() > 0) {
			fail("三不同号");
		}
		award.handleDrawCode("1,3,4");
		bo = award.computeWinMoney(detail);
		if (bo.getPreBonus() != 80) {
			fail("三不同号");
		}
	}
	@Test
	public void consecutive(){
		award.handleDrawCode("3,1,2");
		TicketInfoBO detail = new TicketInfoBO();
		detail.setMultipleNum(1);
		detail.setTicketContent("1,2,3");
		detail.setLotteryChildCode(23308);
		WinMoneyBO bo = award.computeWinMoney(detail);
		if (bo.getPreBonus() != 10) {
			fail("三连号通选");
		}
	}
	
	@Test
	public void twoSame(){
		award.handleDrawCode("2,4,2");
		TicketInfoBO detail = new TicketInfoBO();
		detail.setMultipleNum(2);
		detail.setTicketContent("11*;22*;22*;44*");
		detail.setLotteryChildCode(23303);
		WinMoneyBO bo = award.computeWinMoney(detail);
		if (bo.getPreBonus() != 60) {
			fail("二同号复选");
		}
	}
	
	@Test
	public void twoSameSimple(){
		award.handleDrawCode("1,4,1");
		TicketInfoBO detail = new TicketInfoBO();
		detail.setMultipleNum(1);
		detail.setTicketContent("1,1,2;1,1,3;1,1,3;1,1,4");
		detail.setLotteryChildCode(23302);
		WinMoneyBO bo = award.computeWinMoney(detail);
		if (bo.getPreBonus() != 80) {
			fail("二同号单选");
		}
	}
	
	@Test
	public void twoDifferenceSimple(){
		award.handleDrawCode("1,4,2");
		TicketInfoBO detail = new TicketInfoBO();
		detail.setMultipleNum(1);
		detail.setTicketContent("1,2;2,5;1,4;1,2;1,3");
		detail.setLotteryChildCode(23304);
		WinMoneyBO bo = award.computeWinMoney(detail);
		if (bo.getPreBonus() != 24) {
			fail("二不同号");
		}
	}
}
