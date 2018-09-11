package com.hhly.award.service.award.lottery.high.poker;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.hhly.award.base.exception.ServiceRuntimeException;
import com.hhly.award.bo.TicketInfoBO;
import com.hhly.award.service.award.entity.WinMoneyBO;
import com.hhly.award.service.award.lottery.AbstractNumberAward;
import com.hhly.award.service.award.lottery.high.WinInfo;
import com.hhly.award.service.award.lottery.high.poker.calculate.FivePokerCalculate;
import com.hhly.award.service.award.lottery.high.poker.calculate.FlowerCalculate;
import com.hhly.award.service.award.lottery.high.poker.calculate.FlowerSequenceCalculate;
import com.hhly.award.service.award.lottery.high.poker.calculate.FourPokerCalculate;
import com.hhly.award.service.award.lottery.high.poker.calculate.ICalculate;
import com.hhly.award.service.award.lottery.high.poker.calculate.OnePokerCalculate;
import com.hhly.award.service.award.lottery.high.poker.calculate.PairsCalculate;
import com.hhly.award.service.award.lottery.high.poker.calculate.PantherCalculate;
import com.hhly.award.service.award.lottery.high.poker.calculate.SequenceCalculate;
import com.hhly.award.service.award.lottery.high.poker.calculate.SixPokerCalculate;
import com.hhly.award.service.award.lottery.high.poker.calculate.ThreePokerCalculate;
import com.hhly.award.service.award.lottery.high.poker.calculate.TwoPokerCalculate;
/**
 * @desc 山东快乐扑克开奖
 * @author jiangwei
 * @date 2017年7月14日
 * @company 益彩网络科技公司
 * @version 1.0
 */
@Service
public class SdPokerAward extends AbstractNumberAward {
	/**
	 * 开奖号码数字
	 */
	private List<String> num;
	/**
	 * 对子
	 */
	private String pairs = "";
	/**
	 * 同花花色
	 */
	private String flower = "";
	/**
	 * 是否是顺子
	 */
	private boolean sequence = false;
	
	@Autowired
	private OnePokerCalculate oneCalculate;
	@Autowired
	private TwoPokerCalculate twoCalculate;
	@Autowired
	private ThreePokerCalculate threeCalculate;
	@Autowired
	private FourPokerCalculate fourCalculate;
	@Autowired
	private FivePokerCalculate fiveCalculate;
	@Autowired
	private SixPokerCalculate sixCalculate;
	@Autowired
	private FlowerCalculate flowerCalculate;
	@Autowired
	private FlowerSequenceCalculate flowerSequenceCalculate;
	@Autowired
	private PairsCalculate pairsCalculate;
	@Autowired
	private PantherCalculate pantherCalculate;
	@Autowired
	private SequenceCalculate sequenceCalculate;
	@Override
	protected void handleDrawCode(String code) {
		// 2_10|2_K|1_A
		// 黑桃：1;
		// 红心：2;
		// 梅花：3;
		// 方块：4;
		num = new ArrayList<>();
		Set<String> flowerSet = new HashSet<>();
		String[] codes = StringUtils.tokenizeToStringArray(code, "|_");
		for (int i = 0; i < codes.length; i++) {
			String str = codes[i];
			if (i % 2 == 0) {
				flowerSet.add(str);
			} else {
				if (num.contains(str)) {
					pairs = str;
				} else {
					num.add(str);
				}
			}
		}
		//判断如果是豹子，就不能是对子
		if(num.size() == 1){
			pairs = "";
		}
		// 是否是同花
		if (flowerSet.size() == 1) {
			flower = codes[0];
		}
		// 判断是否是顺子
		if (num.size() == 3) {
			int[] sort = new int[3];
			for (int i = 0; i < num.size(); i++) {
				switch (num.get(i)) {
				case "A":
					sort[i] = 1;
					break;
				case "J":
					sort[i] = 11;
					break;
				case "Q":
					sort[i] = 12;
					break;
				case "K":
					sort[i] = 13;
					break;
				default:
					sort[i] = Integer.parseInt(num.get(i));
					break;
				}
			}
			Arrays.sort(sort);
			if (sort[1] - sort[0] == 1 
					&& sort[2] - sort[1] == 1) {
				sequence = true;
			}else if(sort[0] == 1 && sort[1] == 12 
					&& sort[2] == 13){
				sequence = true;
				//QKA 也是顺子
			}
		}
	}

	@Override
	protected void handleDrawDetail(String drawDetail) {
		draw = splitDrawDetail(drawDetail, 0, 1);
		addDraw = splitDrawDetail(drawDetail, 0, 2);
	}

	@Override
	protected boolean haveDrawCode() {
		return CollectionUtils.isNotEmpty(num);
	}

	@Override
	protected String getLevel(String prize) {
		return prize;
	}

	@Override
	protected WinMoneyBO computeWinMoney(TicketInfoBO detail) {
		String content = detail.getTicketContent();
		ICalculate calculate = getCalculate(detail.getLotteryChildCode());
		WinInfo info = null;
		//单式，复试，胆拖
		if(detail.getContentType() == 1){
			info = calculate.simple(num, pairs, flower, sequence, content);
		}else if(detail.getContentType() == 2){
			info = calculate.complex(num, pairs, flower, sequence, content);
		}else{
			info = calculate.gallDrag(num, pairs, flower, sequence, content);
		}
		//计算中奖金额
		return computeMoney(info.getPrize(), detail.getMultipleNum(), true);
	}
	/**
	 * 玩法获取开奖计算方法
	 * @author jiangwei
	 * @Version 1.0
	 * @CreatDate 2017年7月14日 下午3:30:42
	 * @param lotteryChildCode
	 * @return
	 */
	private ICalculate getCalculate(int lotteryChildCode) {
		switch (lotteryChildCode) {
		case 22501:// 山东快乐扑克3任1
			return oneCalculate;
		case 22502:// 山东快乐扑克3任2
			return twoCalculate;
		case 22503:// 山东快乐扑克3任3
			return threeCalculate;
		case 22504:// 山东快乐扑克3任4
			return fourCalculate;
		case 22505:// 山东快乐扑克3任5
			return fiveCalculate;
		case 22506:// 山东快乐扑克3任6
			return sixCalculate;
		case 22507:// 山东快乐扑克3同花
			return flowerCalculate;
		case 22508:// 山东快乐扑克3顺子
			return sequenceCalculate;
		case 22509:// 山东快乐扑克3对子
			return pairsCalculate;
		case 22510:// 山东快乐扑克3豹子
			return pantherCalculate;
		case 22511:// 山东快乐扑克3同花顺
			return flowerSequenceCalculate;
		default:
			throw new ServiceRuntimeException("山东快乐扑克不存在玩法");
		}
	}
	
	
	
}
