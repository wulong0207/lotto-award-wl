package com.hhly.award.service.award.lottery.sports;


import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hhly.award.base.common.MatchStatusEnum;
import com.hhly.award.base.common.OrderEnum.OrderStatus;
import com.hhly.award.base.common.SymbolConstants;
import com.hhly.award.base.exception.ServiceRuntimeException;
import com.hhly.award.bo.IssueContentInfoBO;
import com.hhly.award.bo.OrderInfoBO;
import com.hhly.award.bo.SportAgainstInfoBO;
import com.hhly.award.bo.SportDrawBBBO;
import com.hhly.award.bo.SportDrawFBBO;
import com.hhly.award.bo.TicketInfoBO;
import com.hhly.award.persistence.dao.OrderInfoDaoMapper;
import com.hhly.award.service.award.entity.Sports.BonusBO;
import com.hhly.award.service.award.entity.Sports.ResultTypeBO;
import com.hhly.award.service.award.lottery.AbstractSportAward;
import com.hhly.award.util.ObjectUtil;
import com.hhly.award.util.calcutils.BaseLottery;
import com.hhly.award.util.calcutils.CalculatingBonus;
import com.hhly.award.util.calcutils.Combine;
/**
 * 竞彩开奖+兑奖
 * @ClassName: SportsAward 
 * @author wuLong
 * @date 2017年5月28日 下午2:11:40 
 *
 */
@Service
public class SportsAward extends AbstractSportAward {
	@Autowired
	OrderInfoDaoMapper orderInfoDaoMapper;

	@Override
	protected void compute(OrderInfoBO orderInfoBO){
		openPrize(orderInfoBO);
		//抄单
		if(orderInfoBO.getOrderType() == 2){
			List<String> followOrders = orderService.getFollowOrderInfo(orderInfoBO.getOrderCode());
			if(!ObjectUtil.isBlank(followOrders)){
				List<OrderInfoBO>  infoBOs = orderService.getOrderInfos(followOrders);
				for(OrderInfoBO infoBO:infoBOs){
					openPrize(infoBO);
					orderService.updateOrderAward(infoBO);
				}
			}
		}
		//合买
		if (orderInfoBO.getBuyType() == 3) {
			orderService.updateOrderGroup(orderInfoBO);
		}
	}
	/**
	 * @param contentInfoBOs
	 * @author wuLong
	 * @date 2018年1月8日 上午10:42:40
	 */
	public void recommendCompute(IssueContentInfoBO issueContentInfoBO){
			int lotteryCode = issueContentInfoBO.getLotteryCode();
			List<String> systemCodes = systemCodeMap.get(lotteryCode);
			String buyScreen = issueContentInfoBO.getBuyScreen();
			String[] a = buyScreen.split(SymbolConstants.COMMA);
			for(String b : a){
				if(!systemCodes.contains(b)){
					throw new ServiceRuntimeException("订单中的该场:"+b+",赛事没有开赛结果");
				}
			}
			Map<String, Object> map =  super.parseSportAgainstInfo(lotteryCode);
			Map<String, Map<String,String>> drawCodeMap = new HashMap<>();
			Combine combine = new Combine(); 
			BaseLottery baseLottery = new BaseLottery();
			Map<String, Integer> winPasswayZsMap = new HashMap<>();
			issueContentInfoBO.setAmount(null);
			issueContentInfoBO.setOrderStatus(1);
			String id = issueContentInfoBO.getId()+"";
			BonusBO bonus = new BonusBO();
			String ticketContent = issueContentInfoBO.getPlanContent();
			ticketContent = ticketContent.substring(0, ticketContent.lastIndexOf("^"));
			String[] tc = ticketContent.split(SymbolConstants.DOUBLE_SLASH+SymbolConstants.UP_CAP);
			//内容
			String content = tc[0];
			//检查是有包含延期赛事
			this.checkSportAgainstInfo(content, id ,lotteryCode);
			//过关方式
			String passway = tc[1];
			//最低过关场次数
			int passNum = Integer.valueOf(passway.split(SymbolConstants.DOUBLE_SLASH+SymbolConstants.UNDERLINE)[0]);
			//出票sp 9.00-3.40A15.00-8.00
			String receiptContent = null;
			//子彩种id
			int lotteryChilCode = issueContentInfoBO.getLotteryChildCode();
			List<String> hitSp = new ArrayList<>();
			if(lotteryChilCode == ID_FHT||lotteryChilCode == ID_JCLQ_HHGG){
				this.handleHt(map, content, receiptContent, lotteryChilCode, hitSp,drawCodeMap,lotteryCode);
			}else{
				//赛事
				String[] buyScreens = issueContentInfoBO.getBuyScreen().split(SymbolConstants.COMMA);
				Map<String, String> contentMap = super.pakegeContenMap(content);
				String[] rc = null;
				String[] rqHzjq = null;
				if(!ObjectUtil.isBlank(receiptContent)){
					String vs[] = receiptContent.split(SymbolConstants.DOUBLE_SLASH+SymbolConstants.AT);
					rc = vs[0].split(SymbolConstants.DOUBLE_SLASH+SymbolConstants.TRAVERSE_SLASH);
					if(vs.length>1){
						rqHzjq = vs[1].split("B");
					}
				}
				hitSp = this.encabHit(map, lotteryChilCode, buyScreens, contentMap, rc,rqHzjq,drawCodeMap,lotteryCode);
			}
			if(!ObjectUtil.isBlank(hitSp)&&hitSp.size()>=passNum){
				this.recommendCalcPrize(baseLottery ,combine, bonus, issueContentInfoBO, passNum, hitSp,winPasswayZsMap,passway.replace(SymbolConstants.UNDERLINE, "串"));
				issueContentInfoBO.setOrderStatus(3);
			}else{
				issueContentInfoBO.setOrderStatus(2);
			}
	}
	
	/** 
	 * @Description: 计算奖金
	 * @param combine 计算工具类
	 * @param orderInfoWinBonus 订单中奖金额
	 * @param ticketInfoBO 票表实体对象
	 * @param passNum 过关场次数
	 * @param hitSp 命中的场次的 赔率  结合
	 * @param winPasswayZsMap 中奖的过关和注数
	 * @return 
	 * @throws NumberFormatException
	 * @author wuLong
	 * @date 2017年5月26日 下午4:24:18
	 */
	@SuppressWarnings("static-access")
	private void recommendCalcPrize(BaseLottery baseLottery, Combine combine, BonusBO bonus , IssueContentInfoBO issueContentInfoBO, int passNum,
			List<String> hitSp,Map<String, Integer> winPasswayZsMap,String passway) throws NumberFormatException {
		@SuppressWarnings("unchecked")
		List<String> tuoCfList = combine.mn(hitSp.toArray(new String[hitSp.size()]), passNum);
		BigDecimal winAmount = BigDecimal.ZERO;
		BigDecimal multip = BigDecimal.valueOf(1);
		boolean cgs = false;
		for(String w : tuoCfList){
			String[] e = w.split(SymbolConstants.DOUBLE_SLASH + SymbolConstants.OBLIQUE_LINE);
			String[][] ex = new String[e.length][];
			int i = 0;
			for(String ey : e){
				String[] ed = ey.split(SymbolConstants.COMMA);
				ex[i] = ed;
				i++;
			}
			String ds[] = baseLottery.getCombineArrToStrabc(ex).split(SymbolConstants.DOUBLE_SLASH + SymbolConstants.OBLIQUE_LINE);
			for(String b : ds){
				String[] c = b.split(SymbolConstants.DOUBLE_SLASH + SymbolConstants.VERTICAL_BAR);
				BigDecimal val = BigDecimal.ONE;
				for(String d : c){
					val = val.multiply(BigDecimal.valueOf(Double.valueOf(d)));
				}
				val = val.multiply(BigDecimal.valueOf(2L));
				double oneBet = CalculatingBonus.cauScale(2, val);
				BigDecimal oneBetBigDecimal = BigDecimal.valueOf(oneBet);
				if(!cgs&&oneBetBigDecimal.compareTo(TAX_UPPER_LIMIT_AMOUNT)>=0){
					cgs = true;
				}
				oneBetBigDecimal = oneBetBigDecimal.multiply(multip);
				winAmount = winAmount.add(oneBetBigDecimal);
			}
		}
		double preBonus = Double.valueOf(winAmount+"");
		bonus.setPreWinBonus(winAmount);
		double afterBonus = preBonus;
		if(cgs){
			afterBonus = preBonus - (preBonus * TAX_RATE);
		}
		issueContentInfoBO.setAmount(BigDecimal.valueOf(afterBonus));
	}


	/** 
	 * @Description: TODO(这里用一句话描述这个方法的作用) 
	 * @param orderInfoBO
	 * @throws ServiceRuntimeException
	 * @throws NumberFormatException
	 * @author wuLong
	 * @date 2017年10月12日 下午3:46:36
	 */
	private void openPrize(OrderInfoBO orderInfoBO) throws ServiceRuntimeException, NumberFormatException {
		List<String> systemCodes = systemCodeMap.get(orderInfoBO.getLotteryCode());
		if(!systemCodes.contains(orderInfoBO.getMaxBuyScreen())){
			throw new ServiceRuntimeException("最大购买场次编号:"+orderInfoBO.getMaxBuyScreen()+",没有开赛结果");
		}
		String buyScreen = orderInfoBO.getBuyScreen();
		String[] a = buyScreen.split(SymbolConstants.COMMA);
		for(String b : a){
			if(!systemCodes.contains(b)){
				throw new ServiceRuntimeException("订单中的该场:"+b+",赛事没有开赛结果");
			}
		}
		List<TicketInfoBO> ticketInfoBOs = orderInfoBO.getTicketInfoBOs();
		int lotteryCode = orderInfoBO.getLotteryCode();
		Map<String, Object> map =  super.parseSportAgainstInfo(lotteryCode);
		stepOne(orderInfoBO, ticketInfoBOs, map);
	}

	/** 
	 * @Description: 开奖兑奖
	 * @param orderInfoBO
	 * @param ticketInfoBOs
	 * @param map
	 * @throws ServiceRuntimeException
	 * @throws NumberFormatException
	 * @author wuLong
	 * @date 2017年6月5日 上午11:04:12
	 */
	public void stepOne(OrderInfoBO orderInfoBO, List<TicketInfoBO> ticketInfoBOs, Map<String, Object> map)
			throws ServiceRuntimeException, NumberFormatException {
		short winStatus = NOT_WIN;
		BigDecimal preWinBonus = BigDecimal.ZERO;
		BigDecimal afterWinBonus = BigDecimal.ZERO;
		Map<String, Map<String,String>> drawCodeMap = new HashMap<>();
		Combine combine = new Combine(); 
		BaseLottery baseLottery = new BaseLottery();
		Map<String, Integer> winPasswayZsMap = new HashMap<>();
		String orderCode = orderInfoBO.getOrderCode();
		OrderStatus orderStatus = OrderStatus.parseOrderStatus(orderInfoBO.getOrderStatus());
		String maxBuyScreen = orderInfoBO.getMaxBuyScreen();
		int lotteryCode = orderInfoBO.getLotteryCode();
		this.checkMaxBuyScreenSportAgainstInfo(maxBuyScreen, orderCode,lotteryCode);
		orderInfoBO.setAddedBonus(null);
		orderInfoBO.setAftBonus(null);
		orderInfoBO.setPreBonus(null);
		orderInfoBO.setWebsiteBonus(null);
		orderInfoBO.setWinningDetail(null);
		for(TicketInfoBO ticketInfoBO : ticketInfoBOs){
			ticketInfoBO.setWinningStatus((short)1);
			ticketInfoBO.setPreBonus(null);
			ticketInfoBO.setAftBonus(null);
			BonusBO bonus = new BonusBO();
			String ticketContent = ticketInfoBO.getTicketContent();
			String[] tc = ticketContent.split(SymbolConstants.DOUBLE_SLASH+SymbolConstants.UP_CAP);
			//内容
			String content = tc[0];
			//检查是有包含延期赛事
			this.checkSportAgainstInfo(content, orderCode,lotteryCode);
			//过关方式
			String passway = tc[1];
			//最低过关场次数
			int passNum = Integer.valueOf(passway.split(SymbolConstants.DOUBLE_SLASH+SymbolConstants.UNDERLINE)[0]);
			//出票sp 9.00-3.40A15.00-8.00
			String receiptContent = ticketInfoBO.getReceiptContent();
			//子彩种id
			int lotteryChilCode = ticketInfoBO.getLotteryChildCode();
			List<String> hitSp = new ArrayList<>();
			if(orderStatus != OrderStatus.TICKETED){
				receiptContent = null;
			}
			if(lotteryChilCode == ID_FHT||lotteryChilCode == ID_JCLQ_HHGG){
				this.handleHt(map, content, receiptContent, lotteryChilCode, hitSp,drawCodeMap,lotteryCode);
			}else{
				//赛事
				String[] buyScreens = ticketInfoBO.getBuyScreen().split(SymbolConstants.COMMA);
				Map<String, String> contentMap = super.pakegeContenMap(content);
				String[] rc = null;
				String[] rqHzjq = null;
				if(!ObjectUtil.isBlank(receiptContent)){
					String vs[] = receiptContent.split(SymbolConstants.DOUBLE_SLASH+SymbolConstants.AT);
					rc = vs[0].split(SymbolConstants.DOUBLE_SLASH+SymbolConstants.TRAVERSE_SLASH);
					if(vs.length>1){
						rqHzjq = vs[1].split("B");
					}
				}
				hitSp = this.encabHit(map, lotteryChilCode, buyScreens, contentMap, rc,rqHzjq,drawCodeMap,lotteryCode);
			}
			if(!ObjectUtil.isBlank(hitSp)&&hitSp.size()>=passNum){
				this.calcPrize(baseLottery ,combine, bonus, ticketInfoBO, passNum, hitSp,winPasswayZsMap,passway.replace(SymbolConstants.UNDERLINE, "串"));
				winStatus = WIN;
				afterWinBonus = afterWinBonus.add(bonus.getAfterWinBonus());
				preWinBonus = preWinBonus.add(bonus.getPreWinBonus());
			}else{
				ticketInfoBO.setWinningStatus(NOT_WIN);
			}
		}
		if(winStatus == WIN){
			orderInfoBO.setPreBonus(Double.valueOf(preWinBonus+""));
			orderInfoBO.setAftBonus(Double.valueOf(afterWinBonus+""));
			orderInfoBO.setWinningDetail(super.getWinningDetail(winPasswayZsMap));
			orderInfoBO.setAddedBonus(0d);
		}
		orderInfoBO.setWinningStatus(winStatus);
		String drawCode = super.generateDrawCode(drawCodeMap);
		if(!ObjectUtil.isBlank(drawCode)){
			orderInfoBO.setDrawCode(drawCode);
		}
	}
	
	public void checkMaxBuyScreenSportAgainstInfo(String maxBuyScreen,String orderCode,Integer lotteryCode){
		SportAgainstInfoBO againstInfoBO = sportAginstInfoMap.get(maxBuyScreen+","+lotteryCode);
		if(againstInfoBO==null){
			throw new ServiceRuntimeException("赛事编号:"+maxBuyScreen+",不存在");
		}
		MatchStatusEnum.SportMatchStatusEnum matchStatusEnum = MatchStatusEnum.SportMatchStatusEnum.parsePayStatus(againstInfoBO.getMatchStatus());
		if(matchStatusEnum != MatchStatusEnum.SportMatchStatusEnum.REVIEW && 
				matchStatusEnum != MatchStatusEnum.SportMatchStatusEnum.CANCLE_PAY && 
				matchStatusEnum != MatchStatusEnum.SportMatchStatusEnum.OPEN_AWARD &&
				matchStatusEnum != MatchStatusEnum.SportMatchStatusEnum.SEND_AWARD){
			throw new ServiceRuntimeException("订单:"+orderCode+"中的最大赛事:"+maxBuyScreen+",赛事状态为"+matchStatusEnum.getDesc()+",不能进行开奖操作");
		}
	}
	
	public void checkSportAgainstInfo(String content,String orderCode,Integer lotteryCode) throws ServiceRuntimeException{
		String maxBuyScreen = null;
		String errorMsg = null;
		Map<String, String> contentMap = super.pakegeContenMap(content);
		for(String key : contentMap.keySet()){
			SportAgainstInfoBO againstInfoBO = sportAginstInfoMap.get(key+","+lotteryCode);
			if(againstInfoBO==null){
				throw new ServiceRuntimeException("赛事编号:"+key+",不存在");
			}
			MatchStatusEnum.SportMatchStatusEnum matchStatusEnum = MatchStatusEnum.SportMatchStatusEnum.parsePayStatus(againstInfoBO.getMatchStatus());
			if(matchStatusEnum != MatchStatusEnum.SportMatchStatusEnum.REVIEW && 
					matchStatusEnum != MatchStatusEnum.SportMatchStatusEnum.CANCLE_PAY && 
					matchStatusEnum != MatchStatusEnum.SportMatchStatusEnum.OPEN_AWARD &&
					matchStatusEnum != MatchStatusEnum.SportMatchStatusEnum.SEND_AWARD){
				//9：销售中；10：暂停销售；11：销售截止；12：比赛进行中；13：延期
				if(matchStatusEnum == MatchStatusEnum.SportMatchStatusEnum.IN_SALE||matchStatusEnum == MatchStatusEnum.SportMatchStatusEnum.SUSPENDED_SALE
						||matchStatusEnum == MatchStatusEnum.SportMatchStatusEnum.SALE_DEAD||matchStatusEnum == MatchStatusEnum.SportMatchStatusEnum.PAYING
						||matchStatusEnum == MatchStatusEnum.SportMatchStatusEnum.DELAY_PAY){
					maxBuyScreen = key;
				}
				errorMsg = "订单:"+orderCode+"中的赛事:"+key+",赛事状态为"+matchStatusEnum.getDesc()+",不能进行开奖操作";
			}
		}
		if(errorMsg!=null){
			if(maxBuyScreen!=null){
				orderInfoDaoMapper.updateOrderMaxBuyScreen(maxBuyScreen, orderCode);
			}
			throw new ServiceRuntimeException(errorMsg);
		}
	}
	
	/** 
	 * @Description: 处理混投
	 * @param map 得到彩种的赛事赛果集合   
	 * @param content 投注内容
	 * @param receiptContent 出票sp 
	 * @param lotteryChilCode 子彩种
	 * @param hitSp 用户收集  命中场次数的sp的集合  
	 * @param drawCodeMap 用于收集订单 中奖结果  map 集合
	 * @param matchRqMap 订单中有购买让球玩法的赛事的让球数  集合
	 * @param passNum 过关数
	 * @throws ServiceRuntimeException
	 * @throws NumberFormatException
	 * @author wuLong
	 * @date 2017年5月26日 下午4:24:51
	 */
	private void handleHt(Map<String, Object> map, String content, String receiptContent, int lotteryChilCode,
			List<String> hitSp,Map<String, Map<String,String>> drawCodeMap,Integer lotteryCode) throws ServiceRuntimeException, NumberFormatException {
		String[] b = content.split(SymbolConstants.DOUBLE_SLASH+SymbolConstants.VERTICAL_BAR);
		String[] rc = null;
		String[] rqHzjq = null;
		if(!ObjectUtil.isBlank(receiptContent)){
			String vs[] = receiptContent.split(SymbolConstants.DOUBLE_SLASH+SymbolConstants.AT);
			rc = vs[0].split(SymbolConstants.DOUBLE_SLASH+SymbolConstants.TRAVERSE_SLASH);
			if(vs.length>1){
				rqHzjq = vs[1].split("B");
			}
		}
		Integer lotteryCc = null;
		for(int i = 0,len=b.length;i<len;i++){
			String c = b[i];
			Map<String, String> contentMap = this.pakegeContenMap(c);
			if(super.checkFBLotteryId(lotteryChilCode)){
				if(c.contains(R)){
					lotteryCc = ID_RQS;
				}else if(c.contains(S)){
					lotteryCc = ID_JCZQ;
				}else if(c.contains(Q)){
					lotteryCc = ID_FBF;
				}else if(c.contains(Z)){
					lotteryCc = ID_FZJQ;
				}else if(c.contains(B)){
					lotteryCc = ID_FBCQ;
				}
			}else if(super.checkBBLotteryId(lotteryChilCode)){
				if(c.contains(R)){
					lotteryCc = ID_JCLQ_RF;
				}else if(c.contains(S)){
					lotteryCc = ID_JCLQ_SF;
				}else if(c.contains(D)){
					lotteryCc = ID_JCLQ_DXF;
				}else if(c.contains(C)){
					lotteryCc = ID_JCLQ_SFC;
				}
			}
			String[] bss = new String[1];
			bss[0] = c.substring(0, c.indexOf(SymbolConstants.UNDERLINE));
			List<String> list = this.encabHit(map, lotteryCc, bss, contentMap, rc == null?null:(rc[i].split(SymbolConstants.DOUBLE_SLASH+SymbolConstants.TRAVERSE_SLASH)),rqHzjq == null?null:(rqHzjq[i].split("B")),drawCodeMap,lotteryCode);
			if(!ObjectUtil.isBlank(list)){
				hitSp.addAll(list);
			}
		}
	}

	/** 
	 * @Description: 计算奖金
	 * @param combine 计算工具类
	 * @param orderInfoWinBonus 订单中奖金额
	 * @param ticketInfoBO 票表实体对象
	 * @param passNum 过关场次数
	 * @param hitSp 命中的场次的 赔率  结合
	 * @param winPasswayZsMap 中奖的过关和注数
	 * @return 
	 * @throws NumberFormatException
	 * @author wuLong
	 * @date 2017年5月26日 下午4:24:18
	 */
	@SuppressWarnings("static-access")
	private void calcPrize(BaseLottery baseLottery, Combine combine, BonusBO bonus , TicketInfoBO ticketInfoBO, int passNum,
			List<String> hitSp,Map<String, Integer> winPasswayZsMap,String passway) throws NumberFormatException {
		@SuppressWarnings("unchecked")
		List<String> tuoCfList = combine.mn(hitSp.toArray(new String[hitSp.size()]), passNum);
		BigDecimal winAmount = BigDecimal.ZERO;
		BigDecimal multip = BigDecimal.valueOf(ticketInfoBO.getMultipleNum().longValue());
		boolean cgs = false;
		int zs = 0;
		for(String w : tuoCfList){
			String[] e = w.split(SymbolConstants.DOUBLE_SLASH + SymbolConstants.OBLIQUE_LINE);
			String[][] ex = new String[e.length][];
			int i = 0;
			for(String ey : e){
				String[] ed = ey.split(SymbolConstants.COMMA);
				ex[i] = ed;
				i++;
			}
			String ds[] = baseLottery.getCombineArrToStrabc(ex).split(SymbolConstants.DOUBLE_SLASH + SymbolConstants.OBLIQUE_LINE);
			zs += ds.length; 
			for(String b : ds){
				String[] c = b.split(SymbolConstants.DOUBLE_SLASH + SymbolConstants.VERTICAL_BAR);
				BigDecimal val = BigDecimal.ONE;
				for(String d : c){
					val = val.multiply(BigDecimal.valueOf(Double.valueOf(d)));
				}
				val = val.multiply(BigDecimal.valueOf(2L));
				double oneBet = CalculatingBonus.cauScale(2, val);
				BigDecimal oneBetBigDecimal = BigDecimal.valueOf(oneBet);
				if(!cgs&&oneBetBigDecimal.compareTo(TAX_UPPER_LIMIT_AMOUNT)>=0){
					cgs = true;
				}
				oneBetBigDecimal = oneBetBigDecimal.multiply(multip);
				winAmount = winAmount.add(oneBetBigDecimal);
			}
		}
		double preBonus = Double.valueOf(winAmount+"");
		bonus.setPreWinBonus(winAmount);
		ticketInfoBO.setWinningStatus(WIN);
		ticketInfoBO.setPreBonus(preBonus);
		double afterBonus = preBonus;
		if(cgs){
			afterBonus = preBonus - (preBonus * TAX_RATE);
		}
		bonus.setAfterWinBonus(BigDecimal.valueOf(afterBonus));
		ticketInfoBO.setAftBonus(afterBonus);
		ticketInfoBO.setAddedBonus(0d);
		ticketInfoBO.setWinningDetail(passway+SymbolConstants.UNDERLINE+zs+"注");
		if(!winPasswayZsMap.containsKey(passway)){
			winPasswayZsMap.put(passway, zs);
		}else{
			winPasswayZsMap.put(passway, winPasswayZsMap.get(passway)+zs);
		}
	}

	/** 
	 * @Description: 处理单个子彩种
	 * @param map 得到彩种的赛事赛果集合   
	 * @param lotteryChilCode 子彩种id
	 * @param buyScreens 购买赛事
	 * @param contentMap key = 赛事编号  ，value = []投注内容  ，如 12312566 ：[1@6.50,3@2.50]
	 * @param rc 返回的sp 格式如：[1.23,2.05A1.30A2.5,3.60]
	 * @param rqHzjq 返回的让球或总进球 个试试 [1,2,3]
	 * @param drawCodeMap 用于收集订单 中奖结果  map 集合
	 * @param matchRqMap 订单中有购买让球玩法的赛事的让球数  集合
	 * @return List<String> 命中的场次的sp 集合
	 * @throws ServiceRuntimeException
	 * @throws NumberFormatException
	 * @author wuLong
	 * @date 2017年5月26日 下午2:22:53
	 */
	private List<String> encabHit(Map<String, Object> map, int lotteryChilCode, String[] buyScreens,
			Map<String, String> contentMap, String[] rc,String rqHzjq[],Map<String, Map<String,String>> drawCodeMap,Integer lotteryCode) throws ServiceRuntimeException, NumberFormatException {
		List<String> hitSp = new ArrayList<>();
		for(int j = 0,len=buyScreens.length;j<len;j++){
			String c = buyScreens[j];
			String[] rca = null;
			String content = contentMap.get(c);
			if(rc!=null){
				rca = rc[j].split(A);
			}else{
				String ct = content.substring(content.indexOf(SymbolConstants.PARENTHESES_LEFT)+1, content.indexOf(SymbolConstants.PARENTHESES_RIGHT));
				String ab [] = ct.split(SymbolConstants.COMMA);
				rca = new String[ab.length];
				int i = 0 ;
				for(String bb : ab){
					String cd[] = bb.split(SymbolConstants.AT);
					rca[i] = cd[1];
					i++;
				}
			}
			ResultTypeBO rt = new ResultTypeBO();
			Map<String, String> lotteryDrawCode = new HashMap<>();
			String matchScoreString = null;
			//判断赛事是否已取消
			SportAgainstInfoBO againstInfoBO = sportAginstInfoMap.get(c+","+lotteryCode);
			boolean isQx = false;
			if(againstInfoBO.getMatchStatus() == 5 || againstInfoBO.getMatchStatus() == 14){
				isQx = true;
			}
			if(super.checkFBLotteryId(lotteryChilCode)){
				SportDrawFBBO drawFBBO = (SportDrawFBBO) map.get(c);
				String fullScore = drawFBBO.getFullScore();
				if(!isQx&&ObjectUtil.isBlank(fullScore)){
					throw new ServiceRuntimeException(c+",没有彩果");
				}
				String halfScore = drawFBBO.getHalfScore();
				matchScoreString = c+SymbolConstants.MIDDLE_PARENTHESES_LEFT+halfScore+SymbolConstants.COMMA+fullScore+SymbolConstants.MIDDLE_PARENTHESES_RIGHT;
				//如果赛事是取消的赛事，则都认为该赛事中奖，中奖sp 都为1
				if(isQx){
					rt.setCg(XH); rt.setType(S);
				}else{
					this.handleFootball(lotteryChilCode, rt, content,null, drawFBBO, fullScore);
				}
			}else if(super.checkBBLotteryId(lotteryChilCode)){
				SportDrawBBBO sportDrawBBBO = (SportDrawBBBO) map.get(c);
				String fullScore = sportDrawBBBO.getFullScore();
				if(!isQx&&ObjectUtil.isBlank(fullScore)){
					throw new ServiceRuntimeException(c+",没有彩果");
				}
				matchScoreString = c+SymbolConstants.MIDDLE_PARENTHESES_LEFT+fullScore+SymbolConstants.MIDDLE_PARENTHESES_RIGHT;
				//如果赛事是取消的赛事，则都认为该赛事中奖，中奖sp 都为1
				if(isQx){
					rt.setCg(XH); rt.setType(S);
				}else{
					this.handleBasketBall(lotteryChilCode, rt, content,rqHzjq==null?null:rqHzjq[j], sportDrawBBBO, fullScore);
				}
			}
			if(drawCodeMap.containsKey(matchScoreString)){
				lotteryDrawCode = drawCodeMap.get(matchScoreString);
			}
			if(!ObjectUtil.isBlank(rt)&&!lotteryDrawCode.containsKey(rt.getType())){
				lotteryDrawCode.put(rt.getType(), SymbolConstants.UNDERLINE+rt.getCg());
			}
			if(!isQx&&ObjectUtil.isBlank(rt.getCg())){
				throw new ServiceRuntimeException(c+",没有彩果");
			}
			this.judgeHit(contentMap, hitSp, c, rca, rt, isQx);
			drawCodeMap.put(matchScoreString, lotteryDrawCode);
		}
		return hitSp;
	}
	
	public void handleFootball(Integer lotteryChilCode,ResultTypeBO rt,String content,String rq,SportDrawFBBO drawFBBO,String fullScore){
		switch (lotteryChilCode) {
			case ID_JCZQ:
				rt.setCg(drawFBBO.getFullSpf()); rt.setType(S);
				break;
			case ID_RQS:
				double rqs = 0;
				if(ObjectUtil.isBlank(rq)||"#".equals(rq)){
					rq = content.substring(content.indexOf(SymbolConstants.MIDDLE_PARENTHESES_LEFT)+1, content.indexOf(SymbolConstants.MIDDLE_PARENTHESES_RIGHT));
				}
				if(rq.contains("+")){
					rqs = Double.valueOf(rq.substring(rq.indexOf("+")+1));
				}else{
					rqs = Double.valueOf(rq);
				}
				int cg = super.getSpfRqResult(fullScore, rqs);
				rt.setCg(cg); 
				rt.setType(R+SymbolConstants.AT+rq);
				break;
			case ID_FBF:
				rt.setCg(super.getBfResult(fullScore)); rt.setType(Q);
				break;
			case ID_FZJQ:
				rt.setCg(super.getZjqResult(fullScore)); rt.setType(Z);
				break;
			case ID_FBCQ:
				rt.setCg(drawFBBO.getHfWdf()); rt.setType(B);
				break;
			default:
				break;
		}
	}
	
	public void handleBasketBall(Integer lotteryChilCode,ResultTypeBO rt,String content,String rfdxf,SportDrawBBBO sportDrawBBBO,String fullScore){
		switch (lotteryChilCode) {
			case ID_JCLQ_SF:
				rt.setCg(sportDrawBBBO.getFullWf()); rt.setType(S);
				break;
			case ID_JCLQ_RF:
				double rf = 0;
				if(!ObjectUtil.isBlank(rfdxf)&&!"#".equals(rfdxf)){
					if(rfdxf.contains(SymbolConstants.ADD)){
						rf = Double.valueOf(rfdxf.substring(rfdxf.indexOf(SymbolConstants.ADD)+1));
					}else{
						rf = Double.valueOf(rfdxf);
					}
				}else{
					rfdxf = content.substring(content.indexOf(SymbolConstants.MIDDLE_PARENTHESES_LEFT)+1, content.indexOf(SymbolConstants.MIDDLE_PARENTHESES_RIGHT));
					if(rfdxf.contains("+")){
						rf = Double.valueOf(rfdxf.substring(rfdxf.indexOf("+")+1));
					}else{
						rf = Double.valueOf(rfdxf);
					}
				}
				//-8.5|3,-9.5|3
				String letWf = sportDrawBBBO.getLetWf();
				if(ObjectUtil.isBlank(letWf)){
					int cg = super.getRfsf(fullScore, rf);
					rt.setCg(cg);
				}else{
					String lw[] = letWf.split(SymbolConstants.COMMA);
					Map<String, String> map = new HashMap<>();
					for(String l : lw){
						String v[] = l.split(SymbolConstants.DOUBLE_SLASH_VERTICAL_BAR);
						map.put(v[0], v[1]);
					}
					rt.setCg(Integer.valueOf(map.get(String.valueOf(rf))));
				}
				rt.setType(R+SymbolConstants.AT+rfdxf);
				break;
			case ID_JCLQ_DXF:
				double dxf = 0;
				if(!ObjectUtil.isBlank(rfdxf)&&!"#".equals(rfdxf)){
					dxf = Double.valueOf(rfdxf);
				}else{
					String rfs = content.substring(content.indexOf(SymbolConstants.MIDDLE_PARENTHESES_LEFT)+1, content.indexOf(SymbolConstants.MIDDLE_PARENTHESES_RIGHT));
					dxf = Double.valueOf(rfs);
				}
				int cgi = super.getDxfResult(fullScore, dxf);
				rt.setCg(cgi); 
				rt.setType(D+SymbolConstants.AT+dxf);
				break;
			case ID_JCLQ_SFC:
				rt.setCg(sportDrawBBBO.getWinScore()); rt.setType(C);
				break;
			default:
				break;
		}
	}
	
	/** 
	 * @Description: 判断命中
	 * @param contentMap  key = 赛事编号  ，value = []投注内容  ，如 12312566 ：_R[+1](3@1.57,0@2.27)或[+1](3@1.57)或(3@1.57)
	 * @param hitSp 收集 命中的场次的sp 集合
	 * @param c 赛事编号
	 * @param rca 出票sp[]
	 * @param fullSpf 赛果
	 * @param qx 是否是取消的赛事
	 * @throws NumberFormatException
	 * @author wuLong
	 * @date 2017年5月26日 上午10:51:11
	 */
	private void judgeHit(Map<String, String> contentMap, List<String> hitSp, String c, String[] rca,
			ResultTypeBO rt,boolean qx) throws NumberFormatException {
		String con = contentMap.get(c);
		String[] y = con.substring(con.indexOf(SymbolConstants.PARENTHESES_LEFT)+1, con.indexOf(SymbolConstants.PARENTHESES_RIGHT)).split(SymbolConstants.COMMA);
		if(qx){
			String v = "1";
			StringBuffer sb = new StringBuffer();
			for (int i = 0,len=y.length;i<len; i++) {
				sb.append(v).append(SymbolConstants.COMMA);
			}
			hitSp.add(sb.deleteCharAt(sb.length()-1).toString());
		}else{
			StringBuffer sb = new StringBuffer();
			for(int i = 0,len=y.length;i<len;i++){
				String[] n = y[i].split(SymbolConstants.AT);
				if(rt.getCg() instanceof Integer){
					if(Objects.equals(Integer.valueOf(n[0]),(Integer)rt.getCg())){
						sb.append(rca[i]).append(SymbolConstants.COMMA);
					}
				}else if(rt.getCg() instanceof String){
					if(n[0].equals(rt.getCg())){
						sb.append(rca[i]).append(SymbolConstants.COMMA);
					}
				}
			}
			if(sb.length()>0){
				hitSp.add(sb.deleteCharAt(sb.length()-1).toString());
			}
		}
	}

	@Override
	protected void getDrawCode(List<OrderInfoBO> orderInfoBOs) throws ServiceRuntimeException{
		super.findBySportAgainstInfoSystemCodes(orderInfoBOs);
	}
}
