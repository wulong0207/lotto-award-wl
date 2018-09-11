package com.hhly.award.service.award.lottery.array;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.hhly.award.DefaultDao;
import com.hhly.award.bo.TicketInfoBO;
import com.hhly.award.service.award.entity.WinMoneyBO;

public class FiveAwardTest extends DefaultDao {
	
	@Autowired
	FiveAward award;
	@Before
	public void before() {
		award.handleDrawDetail(
				"直选|1|100000");
	}
	@Test
	public void simple() {
		award.handleDrawCode("1|2|3|4|5");
		TicketInfoBO detail = new TicketInfoBO();
		detail.setMultipleNum(2);
		detail.setTicketContent("1|2|3|2|3;1|2|3|4|5;1|2|3|4|5");
		detail.setContentType(1);
		WinMoneyBO bo = award.computeWinMoney(detail);
		if (bo.getPreBonus() != 400000) {
			fail("排列5开奖错误");
		}
	}
	
	@Test
	public void repeated() {
		award.handleDrawCode("1|2|3|4|5");
		TicketInfoBO detail = new TicketInfoBO();
		detail.setMultipleNum(1);
		detail.setTicketContent("4,5,1|2,6,7|8,3|2,4|5,3");
		detail.setContentType(2);
		WinMoneyBO bo = award.computeWinMoney(detail);
		if (bo.getPreBonus() != 100000) {
			fail("排列5开奖错误");
		}
	}

}
