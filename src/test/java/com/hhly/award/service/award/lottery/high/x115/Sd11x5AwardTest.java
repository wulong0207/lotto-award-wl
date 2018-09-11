package com.hhly.award.service.award.lottery.high.x115;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.hhly.award.DefaultDao;
import com.hhly.award.bo.TicketInfoBO;
import com.hhly.award.service.award.entity.WinMoneyBO;

public class Sd11x5AwardTest  extends DefaultDao{
	
	public static int com(Sd11x5Award award,String drawCode,int contentType,int childCode,String content){
		award.handleDrawCode(drawCode);
		TicketInfoBO detail = new TicketInfoBO();
		detail.setMultipleNum(1);
		detail.setTicketContent(content);
		detail.setLotteryChildCode(childCode);
		detail.setContentType(contentType);
		WinMoneyBO bo = award.computeWinMoney(detail);
		return (int)bo.getPreBonus();
	}
	@Autowired
	private Sd11x5Award award;
	

	@Before
	public void before() {
		award.handleDrawDetail("任二|6|0,任三|19|0,任四|78|0,任五|540|0,任六|90|0,任七|26|0,任八|9|0,前一|13|0,前二直选|130|0,前二组选|65|0,前三直选|1170|0,前三组选|195|0,乐二一等|201|0,乐二二等|71|0,乐二三等|6|0,乐三一等|1384|0,乐三二等|214|0,乐三三等|19|0,乐四一等|154|0,乐四二等|19|0,乐五一等|1080|0,乐五二等|90|0");
	}
	
	@Test
	public void l2() {
		Assert.assertEquals(201, com(award, "01,02,03,04,05", 1, 21514, "01|02"));
		
		Assert.assertEquals(201 + 71, com(award, "01,02,03,04,05", 1, 21514, "01|02;02|01"));
		
		Assert.assertEquals(6, com(award, "01,02,03,04,05", 1, 21514, "04|02"));
		
		Assert.assertEquals(0, com(award, "01,02,03,04,05", 1, 21514, "04|08"));
	}
	
	@Test
	public void l3() {
		Assert.assertEquals(1384, com(award, "01,02,03,04,05", 1, 21515, "01|02|03"));
		
		Assert.assertEquals(1384 + 214, com(award, "01,02,03,04,05", 1, 21515, "01|02|03;02|01|03"));
		
		Assert.assertEquals(19, com(award, "01,02,03,04,05", 1, 21515, "04|02|05"));
		
		Assert.assertEquals(0, com(award, "01,02,03,04,05", 1, 21515, "04|08|02"));
	}
	
	@Test
	public void l4() {
		Assert.assertEquals(154, com(award, "01,02,03,04,05", 1, 21516, "01,02,04,05"));
		
		Assert.assertEquals(19, com(award, "01,02,03,04,05", 1, 21516, "01,02,05;02,01,08"));
		
		Assert.assertEquals(0, com(award, "01,02,03,04,05", 1, 21516, "01,02,06;02,01,08"));
	}
	
	@Test
	public void l5() {
		Assert.assertEquals(1080, com(award, "01,02,03,04,05", 1, 21517, "01,02,03,04,05"));
		
		Assert.assertEquals(90, com(award, "01,02,03,04,05", 1, 21517, "01,02,06,04,05"));
		
		Assert.assertEquals(0, com(award, "01,02,03,04,05", 1, 21517, "01,02,06,07,05"));
		
	}

}
