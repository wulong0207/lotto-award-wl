package com.hhly.award.service.award.lottery.high.ssc;

import static org.junit.Assert.fail;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.hhly.award.DefaultDao;
import com.hhly.award.bo.TicketInfoBO;
import com.hhly.award.service.award.entity.WinMoneyBO;

public class CqSscAwardTest extends DefaultDao{
	@Autowired
	CqSscAward award;
	
	public static int com(CqSscAward award,String drawCode,int contentType,int childCode,String content){
		award.handleDrawCode(drawCode);
		TicketInfoBO detail = new TicketInfoBO();
		detail.setMultipleNum(1);
		detail.setTicketContent(content);
		detail.setLotteryChildCode(childCode);
		detail.setContentType(contentType);
		WinMoneyBO bo = award.computeWinMoney(detail);
		return (int)bo.getPreBonus();
	}
	
	@Before
	public void before() {
		award.handleDrawDetail("五星直选|100000,五星通选|20440,三星直选|1000,三星组三|320,三星组六|160,二星直选|100,二星组选|50,一星|10,大小单双|4");
	}
	
	@Test
	public void one(){
		award.handleDrawCode("1|2|3|4|5");
		TicketInfoBO detail = new TicketInfoBO();
		detail.setContentType(1);
		detail.setMultipleNum(1);
		detail.setTicketContent("-|-|-|-|5;-|-|-|-|1;-|-|-|-|2");
		detail.setLotteryChildCode(20108);
		WinMoneyBO bo = award.computeWinMoney(detail);
		if (bo.getPreBonus() != 10) {
			fail("开奖错误");
		}
		detail.setTicketContent("-|-|-|-|1");
	    bo = award.computeWinMoney(detail);
		if (bo.getPreBonus() > 0) {
			fail("开奖错误");
		}
	}
	@Test
	public void twoDirect(){
		award.handleDrawCode("1|2|3|4|5");
		TicketInfoBO detail = new TicketInfoBO();
		detail.setContentType(1);
		detail.setMultipleNum(1);
		detail.setTicketContent("-|-|-|4|5;-|-|-|4|1;-|-|-|4|5");
		detail.setLotteryChildCode(20106);
		WinMoneyBO bo = award.computeWinMoney(detail);
		if (bo.getPreBonus() != 200) {
			fail("开奖错误");
		}
		detail.setContentType(2);
		detail.setTicketContent("-|-|-|4,5|1,2,5");
		bo = award.computeWinMoney(detail);
		if (bo.getPreBonus() != 100) {
			fail("开奖错误");
		}
		detail.setContentType(6);
		detail.setTicketContent("9");
		bo = award.computeWinMoney(detail);
		if (bo.getPreBonus() != 100) {
			fail("开奖错误");
		}
	}
	@Test
	public void towGroup(){
		award.handleDrawCode("1|2|3|4|5");
		TicketInfoBO detail = new TicketInfoBO();
		detail.setContentType(1);
		detail.setMultipleNum(1);
		detail.setTicketContent("1,3;4,5;5,4;1,3");
		detail.setLotteryChildCode(20107);
		WinMoneyBO bo = award.computeWinMoney(detail);
		if (bo.getPreBonus() != 100) {
			fail("开奖错误");
		}
		detail.setContentType(2);
		detail.setTicketContent("1,4,5");
		bo = award.computeWinMoney(detail);
		if (bo.getPreBonus() != 50) {
			fail("开奖错误");
		}
		detail.setContentType(6);
		detail.setTicketContent("9");
		bo = award.computeWinMoney(detail);
		if (bo.getPreBonus() != 50) {
			fail("开奖错误");
		}
		
		Assert.assertEquals(50, com(award, "1|2|3|4|5", 6, 20107, "9"));
		
		Assert.assertEquals(100, com(award, "1|2|3|4|4", 6, 20107, "8"));
	}
	
	@Test
	public void  threeDirect(){
		award.handleDrawCode("1|2|3|4|5");
		TicketInfoBO detail = new TicketInfoBO();
		detail.setContentType(1);
		detail.setMultipleNum(1);
		detail.setTicketContent("-|-|3|4|5;-|-|3|4|5;-|-|1|4|5");
		detail.setLotteryChildCode(20103);
		WinMoneyBO bo = award.computeWinMoney(detail);
		if (bo.getPreBonus() != 2000) {
			fail("开奖错误");
		}
		detail.setContentType(2);
		detail.setTicketContent("-|-|3,2,1|4,2|5,4");
		bo = award.computeWinMoney(detail);
		if (bo.getPreBonus() != 1000) {
			fail("开奖错误");
		}
		detail.setContentType(6);
		detail.setTicketContent("12");
		bo = award.computeWinMoney(detail);
		if (bo.getPreBonus() != 1000) {
			fail("开奖错误");
		}
	}
	@Test
	public void threeGroupThree(){
		award.handleDrawCode("1|2|3|1|1");
		TicketInfoBO detail = new TicketInfoBO();
		detail.setContentType(1);
		detail.setMultipleNum(1);
		detail.setTicketContent("1,1,2;1,1,3;1,3,1");
		detail.setLotteryChildCode(20104);
		WinMoneyBO bo = award.computeWinMoney(detail);
		if (bo.getPreBonus() != 640) {
			fail("开奖错误");
		}
		detail.setContentType(2);
		detail.setTicketContent("3,1");
		bo = award.computeWinMoney(detail);
		if (bo.getPreBonus() != 320) {
			fail("开奖错误");
		}
		
		detail.setContentType(2);
		detail.setTicketContent("2,3,1");
		bo = award.computeWinMoney(detail);
		if (bo.getPreBonus() != 320) {
			fail("开奖错误");
		}
	}
	@Test
	public void threeGroupSix(){
		award.handleDrawCode("1|1|3|4|5");
		TicketInfoBO detail = new TicketInfoBO();
		detail.setMultipleNum(1);
		detail.setLotteryChildCode(20105);
		
		detail.setContentType(1);
		detail.setTicketContent("3,4,5;1,3,4;1,3,5");
		WinMoneyBO bo = award.computeWinMoney(detail);
		if (bo.getPreBonus() != 160) {
			fail("开奖错误");
		}
		
		detail.setContentType(2);
		detail.setTicketContent("0,2,4,8,9");
		bo = award.computeWinMoney(detail);
		if (bo.getPreBonus() != 0) {
			fail("开奖错误");
		}
		
		detail.setContentType(2);
		detail.setTicketContent("0,2,4,8,5");
		bo = award.computeWinMoney(detail);
		if (bo.getPreBonus() != 0) {
			fail("开奖错误");
		}
		
		award.handleDrawCode("1|1|4|4|5");
		detail.setContentType(2);
		detail.setTicketContent("0,2,4,8,5");
		bo = award.computeWinMoney(detail);
		if (bo.getPreBonus() > 0) {
			fail("开奖错误");
		}
		
	}
	@Test
	public void fiveDirect(){
		award.handleDrawCode("1|2|3|4|5");
		TicketInfoBO detail = new TicketInfoBO();
		detail.setContentType(1);
		detail.setMultipleNum(1);
		detail.setTicketContent("1|2|3|4|5");
		detail.setLotteryChildCode(20101);
		WinMoneyBO bo = award.computeWinMoney(detail);
		if (bo.getPreBonus() != 100000) {
			fail("开奖错误");
		}
		detail.setContentType(2);
		detail.setTicketContent("1|2|3|1,2,4|4,5");
		bo = award.computeWinMoney(detail);
		if (bo.getPreBonus() != 100000) {
			fail("开奖错误");
		}
	}
	@Test
	public void fiveAll(){
		award.handleDrawCode("1|2|3|4|5");
		TicketInfoBO detail = new TicketInfoBO();
		detail.setContentType(1);
		detail.setMultipleNum(1);
		detail.setTicketContent("1|2|3|4|5");
		detail.setLotteryChildCode(20102);
		WinMoneyBO bo = award.computeWinMoney(detail);
		if (bo.getPreBonus() != 20440) {
			fail("开奖错误");
		}
		detail.setTicketContent("2|2|3|4|5");
		bo = award.computeWinMoney(detail);
		if (bo.getPreBonus() != 220) {
			fail("开奖错误");
		}
		detail.setTicketContent("1|2|3|2|5");
		bo = award.computeWinMoney(detail);
		if (bo.getPreBonus() != 220) {
			fail("开奖错误");
		}

		detail.setTicketContent("2|2|1|4|5");
		bo = award.computeWinMoney(detail);
		if (bo.getPreBonus() != 20) {
			fail("开奖错误");
		}
		detail.setTicketContent("1|2|1|4|5");
		bo = award.computeWinMoney(detail);
		if (bo.getPreBonus() != 40) {
			fail("开奖错误");
		}
	}
}
