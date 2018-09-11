package com.hhly.award.service.award.lottery.high.kl10;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.hhly.award.DefaultDao;
import com.hhly.award.bo.TicketInfoBO;
import com.hhly.award.service.award.entity.WinMoneyBO;

public class CqKl10AwardTest extends DefaultDao{
	
	public static int com(CqKl10Award award,String drawCode,int contentType,int childCode,String content){
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
	private CqKl10Award award;
	

	@Before
	public void before() {
		award.handleDrawDetail("选一数投|25|0,选一红投|5|0,任选二|8|0,选二连组|31|0,选二连直|62|0,任选三|24|0,选三前组|1300|0,选三前直|8000|0,任选四|80|0,任选五|320|0");
	}

	@Test
	public void one() {
		award.handleDrawCode("01,02,03,04,05,06,07,08");
		TicketInfoBO detail = new TicketInfoBO();
		detail.setMultipleNum(1);
		detail.setTicketContent("08;01");
		detail.setLotteryChildCode(22201);
		detail.setContentType(1);
		WinMoneyBO bo = award.computeWinMoney(detail);
		Assert.assertEquals(25, (int)bo.getPreBonus());
		detail.setTicketContent("02;03");
		bo = award.computeWinMoney(detail);
		Assert.assertNotEquals(25, (int)bo.getPreBonus());
		detail.setContentType(2);
		detail.setTicketContent("01,02");
		bo = award.computeWinMoney(detail);
		Assert.assertEquals(25, (int)bo.getPreBonus());
		detail.setTicketContent("08,02");
		bo = award.computeWinMoney(detail);
		Assert.assertNotEquals(25, (int)bo.getPreBonus());
	}
	
	@Test
	public void oneRed() {
		award.handleDrawCode("20,02,03,04,05,06,07,08");
		TicketInfoBO detail = new TicketInfoBO();
		detail.setMultipleNum(1);
		detail.setTicketContent("19;20");
		detail.setLotteryChildCode(22202);
		detail.setContentType(1);
		WinMoneyBO bo = award.computeWinMoney(detail);
		Assert.assertEquals(10, (int)bo.getPreBonus());
		
		detail.setTicketContent("19");
		bo = award.computeWinMoney(detail);
		Assert.assertEquals(5,(int)bo.getPreBonus());
		
		award.handleDrawCode("01,02,03,04,05,06,07,08");
		detail.setTicketContent("19");
		bo = award.computeWinMoney(detail);
		Assert.assertEquals(0, (int)bo.getPreBonus());
	}
	@Test
	public void two(){
		Assert.assertEquals(0, com(award, "01,02,03,04,05,06,07,08", 1, 22203, "19,20;18,19"));
		
		Assert.assertEquals(8, com(award, "01,02,03,04,05,06,07,08", 1, 22203, "01,05"));
		
		Assert.assertEquals(16, com(award, "01,02,03,04,05,06,07,08", 1, 22203, "01,05;07,08"));
		
		Assert.assertEquals(24, com(award, "01,02,03,04,05,06,07,08", 2, 22203, "01,02,05"));
		
		Assert.assertEquals(0, com(award, "01,02,03,04,05,06,07,08", 2, 22203, "17,15,05"));
	}
	
	@Test
	public void three(){
		Assert.assertEquals(0, com(award, "01,02,03,04,05,06,07,08", 1, 22206, "19,20,02;18,19,08"));
		
		Assert.assertEquals(24, com(award, "01,02,03,04,05,06,07,08", 1, 22206, "01,05,07"));
		
		Assert.assertEquals(48, com(award, "01,02,03,04,05,06,07,08", 1, 22206, "01,05,08;02,07,08"));
		
		Assert.assertEquals(0, com(award, "01,02,03,04,05,06,07,08", 2, 22206, "01,05,19,18"));
		
		Assert.assertEquals(24, com(award, "01,02,03,04,05,06,07,08", 2, 22206, "01,05,06,18"));
		
		Assert.assertEquals(96, com(award, "01,02,03,04,05,06,07,08", 2, 22206, "01,05,06,08"));
	}
	@Test
	public void four(){
		Assert.assertEquals(0, com(award, "01,02,03,04,05,06,07,08", 1, 22209, "19,20,02,01;01,18,19,08"));
		
		Assert.assertEquals(80, com(award, "01,02,03,04,05,06,07,08", 1, 22209, "01,05,07,02"));
		
		Assert.assertEquals(160, com(award, "01,02,03,04,05,06,07,08", 1, 22209, "01,02,05,08;01,02,07,08"));
		
		Assert.assertEquals(0, com(award, "01,02,03,04,05,06,07,08", 2, 22209, "01,05,19,18,20"));
		
		Assert.assertEquals(80, com(award, "01,02,03,04,05,06,07,08", 2, 22209, "01,02,05,06,18"));
		
		Assert.assertEquals(400, com(award, "01,02,03,04,05,06,07,08", 2, 22209, "01,05,06,08,07"));
	}
	
	@Test
	public void five(){
		Assert.assertEquals(0, com(award, "01,02,03,04,05,06,07,08", 1, 22210, "11,19,20,02,01;01,02,18,19,08"));
		
		Assert.assertEquals(320, com(award, "01,02,03,04,05,06,07,08", 1, 22210, "01,05,07,02,08"));
		
		Assert.assertEquals(640, com(award, "01,02,03,04,05,06,07,08", 1, 22210, "01,02,05,07,08;01,02,05,07,08"));
		
		Assert.assertEquals(0, com(award, "01,02,03,04,05,06,07,08", 2, 22210, "01,05,19,18,20,11"));
		
		Assert.assertEquals(320, com(award, "01,02,03,04,05,06,07,08", 2, 22210, "01,02,05,06,07,18"));
		
		Assert.assertEquals(1920, com(award, "01,02,03,04,05,06,07,08", 2, 22210, "01,02,05,06,08,07"));
	}
	
	@Test
	public void twoDriect(){
		Assert.assertEquals(0, com(award, "01,02,03,04,05,06,07,08", 1, 22205, "08|19;08|10"));
		
		Assert.assertEquals(62, com(award, "01,02,03,04,05,06,07,08", 1, 22205, "04|05;08|10"));
		
		Assert.assertEquals(0, com(award, "01,02,03,04,05,06,07,08", 1, 22205, "03|02;08|10"));
	}
	
	@Test
	public void twoGroup(){
		Assert.assertEquals(0, com(award, "01,02,03,04,05,06,07,08", 1, 22204, "08,19;08,10"));
		
		Assert.assertEquals(31, com(award, "01,02,03,04,05,06,07,08", 1, 22204, "04,05;08,10"));
		
		Assert.assertEquals(62, com(award, "01,02,03,04,05,06,07,08", 1, 22204, "03,02;08,07"));
		
		Assert.assertEquals(93, com(award, "01,02,03,04,05,06,07,08", 2, 22204, "01,02,03,04"));
		
		Assert.assertEquals(31, com(award, "01,02,03,04,05,06,07,08", 2, 22204, "10,02,03"));
		
		Assert.assertEquals(31 * 4, com(award, "01,02,03,04,05,06,07,08", 2, 22204, "07,02,03,06,08,05"));
	}
	
	@Test
	public void threeDriect(){
		Assert.assertEquals(0, com(award, "01,02,03,04,05,06,07,08", 1, 22208, "08|19|20;08|10|11"));
		
		Assert.assertEquals(8000, com(award, "01,02,03,04,05,06,07,08", 1, 22208, "01|02|07;01|02|03"));
		
		Assert.assertEquals(8000, com(award, "01,02,03,04,05,06,07,08", 1, 22208, "01|02|03"));
		
		Assert.assertEquals(0, com(award, "01,02,03,04,05,06,07,08", 1, 22208, "03|02|01"));
		
	}
	@Test
	public void threeGroup(){
		Assert.assertEquals(0, com(award, "01,02,03,04,05,06,07,08", 1, 22207, "08,19,20;08,10,11"));
		
		Assert.assertEquals(1300, com(award, "01,02,03,04,05,06,07,08", 1, 22207, "08,19,20;02,01,03"));
		
		Assert.assertEquals(1300, com(award, "01,02,03,04,05,06,07,08", 1, 22207, "01,02,03"));
		
		Assert.assertEquals(1300, com(award, "01,02,03,04,05,06,07,08", 1, 22207, "03,02,01"));
		
		Assert.assertEquals(1300, com(award, "01,02,03,04,05,06,07,08", 2, 22207, "03,02,01,04"));
		
		Assert.assertEquals(1300, com(award, "01,02,03,04,05,06,07,08", 2, 22207, "03,02,01,04,05,06"));
		
		Assert.assertEquals(0, com(award, "01,02,03,04,05,06,07,08", 2, 22207, "08,02,01,04,05,06"));
	}
	
}
