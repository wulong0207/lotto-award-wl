package com.hhly.award.service.award.lottery.high.poker;

import static org.junit.Assert.fail;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.hhly.award.DefaultDao;
import com.hhly.award.bo.TicketInfoBO;
import com.hhly.award.service.award.entity.WinMoneyBO;
import com.hhly.award.util.MatchUtil;

public class SdPokerAwardTest extends DefaultDao{
	
	@Autowired
	private SdPokerAward award;
	
	public static int com(SdPokerAward award,String drawCode,int contentType,int childCode,String content){
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
		award.handleDrawDetail("同花包选|22,同花单选|90,同花顺包选|535,同花顺单选|2150,顺子包选|33,顺子单选|400,豹子包选|500,豹子单选|6400,对子包选|7,对子单选|88,任选一|5,任选二|33,任选三|116,任选四|46,任选五|22,任选六|12");
	}
	@Test
	public void one(){
		
		Assert.assertEquals(5, com(award, "1_10|2_K|3_A", 1, 22501, "A"));
		
		
		Assert.assertEquals(5, com(award, "1_A|2_K|3_A", 1, 22501, "A"));
		
		Assert.assertEquals(5, com(award, "1_A|2_A|3_A", 1, 22501, "A"));
		
		Assert.assertEquals(10, com(award, "1_10|2_K|3_A", 2, 22501, "K,A,4"));
	}
	@Test
	public void two(){
		Assert.assertEquals(33, com(award, "1_10|2_K|3_A", 1, 22502, "A,10;A,3"));
		
		Assert.assertEquals(33, com(award, "1_10|2_K|3_A", 2, 22502, "A,K,2"));
		
		
		Assert.assertEquals(33, com(award, "1_10|2_A|3_A", 3, 22502, "A#K,10,5"));
		
		Assert.assertEquals(33, com(award, "1_K|2_K|3_A", 3, 22502, "A#K,10,5"));
		
		Assert.assertEquals(33, com(award, "1_10|2_10|3_A", 1, 22502, "A,10;A,3"));
		
		Assert.assertEquals(33, com(award, "1_10|2_A|3_A", 1, 22502, "A,10;A,3"));
		
		Assert.assertEquals(66, com(award, "1_10|2_K|3_A", 3, 22502, "A#K,10,5"));
		
	}
	@Test
	public void three(){
		Assert.assertEquals(116, com(award, "1_10|2_K|3_A", 1, 22503, "A,10,K;A,3,4"));
		
		Assert.assertEquals(116, com(award, "1_10|2_K|3_A", 2, 22503, "A,J,K,10"));
		
		Assert.assertEquals(116 * MatchUtil.pac(5, 1), com(award, "1_10|2_A|3_A", 2, 22503, "A,J,K,10,2,3,4"));
		
		Assert.assertEquals(116 * MatchUtil.pac(5, 2), com(award, "1_A|2_A|3_A", 2, 22503, "A,J,K,10,2,3"));
		
		
		
		Assert.assertEquals(116 * 2, com(award, "1_10|2_A|3_A", 1, 22503, "A,10,K;A,10,4"));
		
		Assert.assertEquals(116 * 2, com(award, "1_A|2_A|3_A", 1, 22503, "A,10,K;A,10,4"));
		
		Assert.assertEquals(116, com(award, "1_10|2_K|3_A", 3, 22503, "10#K,A,4"));
	}
	@Test
	public void four(){
		
		Assert.assertEquals(46, com(award, "1_10|2_K|3_A", 1, 22504, "A,K,10,4"));
		
		Assert.assertEquals(46, com(award, "1_10|2_A|3_A", 1, 22504, "A,K,10,4"));
		
		Assert.assertEquals(46, com(award, "1_A|2_A|3_A", 1, 22504, "A,K,10,4"));
		
		Assert.assertEquals(46 * 2, com(award, "1_10|2_K|3_A", 2, 22504, "A,K,10,4,5"));
		
		Assert.assertEquals(46 * MatchUtil.pac(3, 2), com(award, "1_10|2_A|3_A", 2, 22504, "A,K,10,4,5"));
		
		Assert.assertEquals(46 * MatchUtil.pac(4, 3), com(award, "1_A|2_A|3_A", 2, 22504, "A,K,10,4,5"));
		
		Assert.assertEquals(46 * 3, com(award, "1_10|2_K|3_A", 3, 22504, "K#2,A,10,J,Q"));
		
	}
	
	@Test
	public void five(){
		
		Assert.assertEquals(22 , com(award, "1_10|2_K|3_A", 1, 22505, "A,K,10,4,J"));
		
		Assert.assertEquals(22 * MatchUtil.pac(4, 2), com(award, "1_10|2_K|3_A", 2, 22505, "A,K,10,J,Q,2,3"));
		
		Assert.assertEquals(22 * MatchUtil.pac(5, 3), com(award, "1_10|2_A|3_A", 2, 22505, "A,K,10,J,Q,2,3"));
		
		Assert.assertEquals(22 * MatchUtil.pac(6, 4), com(award, "1_A|2_A|3_A", 2, 22505, "A,K,10,J,Q,2,3"));
		
		
		Assert.assertEquals(22 * MatchUtil.pac(5, 2), com(award, "1_10|2_A|3_A", 3, 22505, "3#K,A,10,J,Q,2,3"));
		
		Assert.assertEquals(22 * MatchUtil.pac(6, 3), com(award, "1_A|2_A|3_A", 3, 22505, "3#K,A,10,J,Q,2,3"));
		
		Assert.assertEquals(22 * MatchUtil.pac(7, 4), com(award, "1_A|2_A|3_A", 3, 22505, "A#K,3,10,J,Q,2,3"));
		
		Assert.assertEquals(44, com(award, "1_10|2_K|3_A", 3, 22505, "3#K,A,10,J,Q"));
		
	}
	
	@Test
	public void six(){
		
		Assert.assertEquals(12, com(award, "1_10|2_K|3_A", 1, 22506, "A,K,10,4,J,2"));
		
		Assert.assertEquals(12 * MatchUtil.pac(4, 3) , com(award, "1_10|2_K|3_A", 2, 22506, "A,K,10,J,Q,2,3"));
		
		Assert.assertEquals(12 * MatchUtil.pac(5, 4) , com(award, "1_10|2_A|3_A", 2, 22506, "A,K,10,J,Q,2,3"));
		
		Assert.assertEquals(12 * MatchUtil.pac(6, 5) , com(award, "1_A|2_A|3_A", 2, 22506, "A,K,10,J,Q,2,3"));
		
		Assert.assertEquals(12 * MatchUtil.pac(4, 3), com(award, "1_10|2_K|3_A", 3, 22506, "A#K,10,J,Q,2,3"));
		
	}
	@Test
	public void flower(){
		award.handleDrawCode("1_10|1_K|1_A");
		TicketInfoBO detail = new TicketInfoBO();
		detail.setContentType(1);
		detail.setMultipleNum(1);
		detail.setTicketContent("AT");
		detail.setLotteryChildCode(22507);
		WinMoneyBO bo = award.computeWinMoney(detail);
		if (bo.getPreBonus() != 22) {
			fail("开奖错误"+bo.getPreBonus() );
		}
	}
	@Test
	public void sequence(){
		Assert.assertEquals(33, com(award, "2_3|1_2|1_A", 1, 22508, "XYZ"));
		
		Assert.assertEquals(400, com(award, "1_A|1_2|1_3", 1, 22508, "A,2,3;2,3,4;3,4,5;4,5,6;5,6,7"));
		
	}
	@Test
	public void pairs(){
		Assert.assertEquals(7, com(award, "2_A|1_2|1_A", 1, 22509, "X,X"));
		
		Assert.assertEquals(88, com(award, "2_A|1_2|1_A", 1, 22509, "A,A,*"));
		
		Assert.assertEquals(0, com(award, "1_A|2_A|3_A", 1, 22509, "A,A,*"));
		
		Assert.assertEquals(0, com(award, "1_A|2_A|3_A", 1, 22509, "X,X"));
		
	}
	@Test
	public void panther(){
		Assert.assertEquals(0, com(award, "3_4|3_5|3_6", 1, 22510, "4,4,4;J,J,J"));
	}
	
	@Test
	public void flowerSequence(){
		award.handleDrawCode("2_2|2_3|2_A");
		TicketInfoBO detail = new TicketInfoBO();
		detail.setContentType(1);
		detail.setMultipleNum(1);
		detail.setTicketContent("2S");
		detail.setLotteryChildCode(22511);
		WinMoneyBO bo = award.computeWinMoney(detail);
		if (bo.getPreBonus() != 2150) {
			fail("开奖错误"+bo.getPreBonus() );
		}
	}
}
