package com.hhly.award.service.award.lottery;

import static org.junit.Assert.fail;

import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.hhly.award.DefaultDao;
import com.hhly.award.bo.TicketInfoBO;
import com.hhly.award.service.award.entity.WinMoneyBO;
/**
 * @desc 七星彩开奖测试
 * @author jiangwei
 * @date 2017年7月31日
 * @company 益彩网络科技公司
 * @version 1.0
 */
public class QlcAwardTest extends DefaultDao {
	
	@Autowired
	QlcAward award;
	
	@Before
	public void before() {
		award.handleDrawDetail(
				"一等奖|3|100000,二等奖|11|10000,三等奖|196|1000,四等奖|596|200,五等奖|7364|50,六等奖|11788|10,七等奖|83886|5");
	}
	
	@Test
	public void simple() {
		award.handleDrawCode("01,02,03,04,05,06,07,08");
		TicketInfoBO detail = new TicketInfoBO();
		detail.setMultipleNum(1);
		detail.setTicketContent("01,02,03,04,05,06,07;01,02,03,04,05,16,08");
		detail.setContentType(1);
		WinMoneyBO bo = award.computeWinMoney(detail);
		if (bo.getPreBonus() != 100000 +200) {
			fail("开奖错误");
		}
	}
	
	@Test
	public void reexamine(){
		award.handleDrawCode("01,02,03,04,05,06,07,08");
		TicketInfoBO detail = new TicketInfoBO();
		detail.setMultipleNum(1);
		detail.setTicketContent("01,02,03,04,05,06,07,08");
		detail.setContentType(2);
		WinMoneyBO bo = award.computeWinMoney(detail);
		if (bo.getPreBonus() != 100000 + 10000 * 7) {
			fail("开奖错误");
		}
	}
	@Test
	public void gallDrag(){
		award.handleDrawCode("01,02,03,04,05,06,07,08");
		TicketInfoBO detail = new TicketInfoBO();
		detail.setMultipleNum(1);
		detail.setTicketContent("11,08#02,03,04,05,16,17");
		detail.setContentType(3);
		WinMoneyBO bo = award.computeWinMoney(detail);
		if (bo.getPreBonus() != 10*2) {
			fail("开奖错误");
		}
	}
}
