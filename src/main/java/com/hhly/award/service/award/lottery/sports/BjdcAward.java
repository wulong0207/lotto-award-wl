package com.hhly.award.service.award.lottery.sports;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hhly.award.base.common.LotteryChildEnum;
import com.hhly.award.base.common.MatchStatusEnum;
import com.hhly.award.base.common.SymbolConstants;
import com.hhly.award.base.exception.ServiceRuntimeException;
import com.hhly.award.bo.OrderInfoBO;
import com.hhly.award.bo.SportAgainstInfoBO;
import com.hhly.award.bo.SportDrawBJBO;
import com.hhly.award.bo.SportDrawWFBO;
import com.hhly.award.bo.TicketInfoBO;
import com.hhly.award.persistence.dao.OrderInfoDaoMapper;
import com.hhly.award.service.award.entity.Sports.BonusBO;
import com.hhly.award.service.award.entity.Sports.ResultTypeBO;
import com.hhly.award.service.award.lottery.AbstractSportAward;
import com.hhly.award.util.ObjectUtil;
import com.hhly.award.util.calcutils.BaseLottery;
import com.hhly.award.util.calcutils.Combine;
/**
 * 北京单场
 * @ClassName: BjdcAward 
 * @author wuLong
 * @date 2017年5月28日 下午2:12:36 
 *
 */
@Service
public class BjdcAward extends AbstractSportAward{
	
	@Autowired
	OrderInfoDaoMapper orderInfoDaoMapper;

	@Override
	protected void compute(OrderInfoBO orderInfoBO) {
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
		String maxBuyScreen = orderInfoBO.getMaxBuyScreen();
		this.checkMaxBuyScreenSportAgainstInfo(maxBuyScreen, orderCode,orderInfoBO.getLotteryCode());
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
			this.checkSportAgainstInfo(content, orderCode,orderInfoBO.getLotteryCode());
			//过关方式
			String passway = tc[1];
			//最低过关场次数
			int passNum = Integer.valueOf(passway.split(SymbolConstants.DOUBLE_SLASH+SymbolConstants.UNDERLINE)[0]);
			//子彩种id
			int lotteryChilCode = ticketInfoBO.getLotteryChildCode();
			List<String> hitSp = new ArrayList<>();
			//赛事
			String[] buyScreens = ticketInfoBO.getBuyScreen().split(SymbolConstants.COMMA);
			Map<String, String> contentMap = super.pakegeContenMap(content);
			hitSp = this.encabHit(map, lotteryChilCode, buyScreens, contentMap, drawCodeMap,orderInfoBO.getLotteryCode());
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
			if(maxBuyScreen != null){
				orderInfoDaoMapper.updateOrderMaxBuyScreen(maxBuyScreen, orderCode);
			}
			throw new ServiceRuntimeException(errorMsg);
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
				int clen = c.length;
				int isOne = 0;
				for(String d : c){
					double dc = Double.valueOf(d);
					if(dc==Double.valueOf(1)){
						isOne++;
					}
					val = val.multiply(BigDecimal.valueOf(dc));
				}
				val = val.multiply(BigDecimal.valueOf(2L));
				BigDecimal fjl = new BigDecimal("0.65");
				if(clen == isOne){
					fjl= new BigDecimal("1");
				}
				BigDecimal oneBetBigDecimal = val.multiply(fjl).setScale(2, BigDecimal.ROUND_DOWN);
				//单关，单注奖金小于2元，则把单注奖金改成2元
				if(passNum==1&&oneBetBigDecimal.compareTo(new BigDecimal("2"))<0){
					oneBetBigDecimal = new BigDecimal("2");
				}
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
			Map<String, String> contentMap,Map<String, Map<String,String>> drawCodeMap,Integer lotteryCode) throws ServiceRuntimeException, NumberFormatException {
		List<String> hitSp = new ArrayList<>();
		for(int j = 0,len=buyScreens.length;j<len;j++){
			String c = buyScreens[j];
			String content = contentMap.get(c);
			ResultTypeBO rt = new ResultTypeBO();
			Map<String, String> lotteryDrawCode = new HashMap<>();
			String matchScoreString = null;
			//判断赛事是否已取消
			SportAgainstInfoBO againstInfoBO = sportAginstInfoMap.get(c+","+lotteryCode);
			boolean isQx = false;
			if(againstInfoBO.getMatchStatus() == 5 || againstInfoBO.getMatchStatus() == 14){
				isQx = true;
			}
			String fullScore = null;String halfScore = null;
			if(lotteryChilCode == LotteryChildEnum.LotteryChild.ID_SFGG_PT.getValue()){
				SportDrawWFBO drawWFBO = (SportDrawWFBO) map.get(c);
				fullScore = drawWFBO.getFullScore();
				matchScoreString = c+SymbolConstants.MIDDLE_PARENTHESES_LEFT+fullScore+SymbolConstants.MIDDLE_PARENTHESES_RIGHT;
			}else{
				SportDrawBJBO drawFBBO = (SportDrawBJBO) map.get(c);
				fullScore = drawFBBO.getFullScore();
				halfScore = drawFBBO.getHalfScore();
				matchScoreString = c+SymbolConstants.MIDDLE_PARENTHESES_LEFT+halfScore+SymbolConstants.COMMA+fullScore+SymbolConstants.MIDDLE_PARENTHESES_RIGHT;
			}
			if(!isQx&&ObjectUtil.isBlank(fullScore)){
				throw new ServiceRuntimeException(c+",没有彩果");
			}
			//如果赛事是取消的赛事，则都认为该赛事中奖，中奖sp 都为1
			if(isQx){
				rt.setCg(XH); rt.setType(S);
			}else{
				this.handleFootball(lotteryChilCode, rt, content,null, map.get(c), fullScore);
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
			this.judgeHit(contentMap, hitSp, c, map.get(c),lotteryChilCode,rt, isQx);
			drawCodeMap.put(matchScoreString, lotteryDrawCode);
		}
		return hitSp;
	}
	
	public void handleFootball(Integer lotteryChilCode,ResultTypeBO rt,String content,String rq,Object bo,String fullScore){
		LotteryChildEnum.LotteryChild child = LotteryChildEnum.LotteryChild.valueOf(lotteryChilCode);
		switch (child) {
			case ID_BD_SXDX:
				rt.setCg(super.getSdxs(fullScore)); rt.setType(S);
				break;
			case ID_BD_RQS:
			case ID_SFGG_PT:
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
				if(child == LotteryChildEnum.LotteryChild.ID_BD_RQS){
					rt.setType(R+SymbolConstants.AT+rq);
				}else if(child == LotteryChildEnum.LotteryChild.ID_SFGG_PT){
					rt.setType(R+SymbolConstants.AT+rq);
				}
				break;
			case ID_BD_FBF:
				rt.setCg(super.getBjdcBfResult(fullScore)); rt.setType(Q);
				break;
			case ID_BD_FZJQ:
				rt.setCg(super.getZjqResult(fullScore)); rt.setType(Z);
				break;
			case ID_BD_FBCQ:
				SportDrawBJBO drawFBBO = (SportDrawBJBO)bo;
				rt.setCg(drawFBBO.getHfWdf()); rt.setType(B);
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
	private void judgeHit(Map<String, String> contentMap, List<String> hitSp, String c,Object bo,Integer lotteryChildCode,
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
						String bjHitSp = getBjHitSp(lotteryChildCode, bo);
						if(bjHitSp==null){
							continue;
						}
						sb.append(bjHitSp).append(SymbolConstants.COMMA);
					}
				}else if(rt.getCg() instanceof String){
					if(n[0].equals(rt.getCg())){
						String bjHitSp = getBjHitSp(lotteryChildCode, bo);
						if(bjHitSp==null){
							continue;
						}
						sb.append(bjHitSp).append(SymbolConstants.COMMA);
					}
				}
			}
			if(sb.length()>0){
				hitSp.add(sb.deleteCharAt(sb.length()-1).toString());
			}
		}
	}
	
	public String getBjHitSp(Integer lotteryChildCode,Object bo){
		LotteryChildEnum.LotteryChild child = LotteryChildEnum.LotteryChild.valueOf(lotteryChildCode);
		String bjHitSp = null;
		switch (child) {
			case ID_BD_SXDX:
				SportDrawBJBO drawFBBO = (SportDrawBJBO)bo;
				if(drawFBBO.getSpLetWdf()==0){
					throw new ServiceRuntimeException("sportAgainstInfoId:"+drawFBBO.getSportAgainstInfoId()+"子彩种名:"+child.getDesc()+"开奖sp有误");
				}
				bjHitSp = drawFBBO.getSpUdSd().toString();
				break;
			case ID_BD_RQS:
				drawFBBO = (SportDrawBJBO)bo;
				if(drawFBBO.getSpLetWdf()==0){
					throw new ServiceRuntimeException("sportAgainstInfoId:"+drawFBBO.getSportAgainstInfoId()+"子彩种名:"+child.getDesc()+"开奖sp有误");
				}
				bjHitSp = drawFBBO.getSpLetWdf().toString();
				break;
			case ID_SFGG_PT:
				SportDrawWFBO drawWFBO = (SportDrawWFBO)bo;
				if(drawWFBO.getSpLetWf()==0){
					drawWFBO.setSpLetWf(1d);
				}
				bjHitSp = drawWFBO.getSpLetWf().toString();
				break;
			case ID_BD_FBF:
				drawFBBO = (SportDrawBJBO)bo;
				if(drawFBBO.getSpLetWdf()==0){
					throw new ServiceRuntimeException("sportAgainstInfoId:"+drawFBBO.getSportAgainstInfoId()+"子彩种名:"+child.getDesc()+"开奖sp有误");
				}
				bjHitSp = drawFBBO.getSpScore().toString();
				break;
			case ID_BD_FZJQ:
				drawFBBO = (SportDrawBJBO)bo;
				if(drawFBBO.getSpLetWdf()==0){
					throw new ServiceRuntimeException("sportAgainstInfoId:"+drawFBBO.getSportAgainstInfoId()+"子彩种名:"+child.getDesc()+"开奖sp有误");
				}
				bjHitSp = drawFBBO.getSpGoalNum().toString();
				break;
			case ID_BD_FBCQ:
				drawFBBO = (SportDrawBJBO)bo;
				if(drawFBBO.getSpLetWdf()==0){
					throw new ServiceRuntimeException("sportAgainstInfoId:"+drawFBBO.getSportAgainstInfoId()+"子彩种名:"+child.getDesc()+"开奖sp有误");
				}
				bjHitSp = drawFBBO.getSpHfWdf().toString();
				break;
			default:
				break;
		}
		return bjHitSp;
	}

	@Override
	protected void getDrawCode(List<OrderInfoBO> orderInfoBOs) throws ServiceRuntimeException{
		super.findBySportAgainstInfoSystemCodes(orderInfoBOs);
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
}
