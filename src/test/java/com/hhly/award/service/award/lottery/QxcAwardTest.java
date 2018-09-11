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
public class QxcAwardTest extends DefaultDao {
	
	@Autowired
	QxcAward award;
	
	@Before
	public void before() {
		award.handleDrawDetail(
				"一等奖|0|1000000,二等奖|0|100000,三等奖|0|1800,四等奖|0|300,五等奖|0|20,六等奖|0|5");
	}
	
	@Test
	public void simple() {
		award.handleDrawCode("1|2|3|4|5|6|7");
		TicketInfoBO detail = new TicketInfoBO();
		detail.setMultipleNum(1);
		detail.setTicketContent("1|5|3|9|8|6|7;1|5|3|9|8|6|7;1|2|3|9|8|6|7");
		detail.setContentType(1);
		WinMoneyBO bo = award.computeWinMoney(detail);
		if (bo.getPreBonus() != 30 ) {
			fail("开奖错误");
		}
	}
	
	@Test
	public void reexamine(){
		award.handleDrawCode("1|2|3|4|5|6|7");
		TicketInfoBO detail = new TicketInfoBO();
		detail.setMultipleNum(1);
		detail.setTicketContent("1,5|6,2|3|4|5|6|7");
		detail.setContentType(2);
		WinMoneyBO bo = award.computeWinMoney(detail);
		if (bo.getPreBonus() != 1000000+ 100000 + 1800*2) {
			fail("开奖错误");
		}
	}

	public static int com(QxcAward award,String drawCode,int contentType,int childCode,String content){
		award.handleDrawCode(drawCode);
		TicketInfoBO detail = new TicketInfoBO();
		detail.setMultipleNum(1);
		detail.setTicketContent(content);
		detail.setLotteryChildCode(childCode);
		detail.setContentType(contentType);
		WinMoneyBO bo = award.computeWinMoney(detail);
		return (int)bo.getPreBonus();
	}
}
