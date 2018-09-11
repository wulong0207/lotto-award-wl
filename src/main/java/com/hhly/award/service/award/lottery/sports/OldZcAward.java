package com.hhly.award.service.award.lottery.sports;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.hhly.award.base.common.CacheConstants;
import com.hhly.award.base.common.LotteryEnum;
import com.hhly.award.base.common.SymbolConstants;
import com.hhly.award.base.exception.ServiceRuntimeException;
import com.hhly.award.bo.LotteryIssueBO;
import com.hhly.award.bo.OrderInfoBO;
import com.hhly.award.bo.TicketInfoBO;
import com.hhly.award.service.award.lottery.AbstractSportAward;
import com.hhly.award.util.ObjectUtil;
import com.hhly.award.util.calcutils.CalculatingBonus;
/**
 * 老足彩
 * @ClassName: OldZcAward 
 * @author wuLong
 * @date 2017年5月28日 下午2:12:27 
 *
 */
@Service
public class OldZcAward extends AbstractSportAward{
	private Map<Integer, String> drawCodeMap = new HashMap<>();
	private static Map<Integer, Map<Integer, String>> prizeMap;
	static{
		prizeMap = new HashMap<>();
		{
			Map<Integer, String> map = new HashMap<>();
			map.put(14, "一等奖");
			map.put(13, "二等奖");
			prizeMap.put(LotteryEnum.Lottery.SFC.getName(), map);
			map = new HashMap<>();
			map.put(9, "一等奖");
			prizeMap.put(LotteryEnum.Lottery.ZC_NINE.getName(), map);
			map = new HashMap<>();
			map.put(8, "一等奖");
			prizeMap.put(LotteryEnum.Lottery.JQ4.getName(), map);
			map = new HashMap<>();
			map.put(12, "一等奖");
			prizeMap.put(LotteryEnum.Lottery.ZC6.getName(), map);
		}
	}

	@Override
	protected void compute(OrderInfoBO orderInfoBO) {
		if(ObjectUtil.isBlank(drawCode)){
			throw new ServiceRuntimeException("不存在开奖号码");
		}
		String[] drawCodes = drawCode.split(SymbolConstants.DOUBLE_SLASH_VERTICAL_BAR);
		for(int i = 0,len=drawCodes.length;i<len;i++){
			drawCodeMap.put(i, drawCodes[i]);
		}
		List<TicketInfoBO> ticketInfoBOs = orderInfoBO.getTicketInfoBOs();
		short orderWinStatus = NOT_WIN ;
		BigDecimal preWinBonus = BigDecimal.ZERO;
		BigDecimal afterWinBonus = BigDecimal.ZERO;
		int num = 0;
		Map<String, Integer> orderWinningDetail = new HashMap<>();
//		Combine combine = new Combine();
		for(TicketInfoBO ticketInfoBO : ticketInfoBOs){
			Map<String, Integer> orderWinningDetailbb = new HashMap<>();
			ticketInfoBO.setPreBonus(null);
			ticketInfoBO.setAftBonus(null);
			//3|_|3|3|3|_|3|3|3|_|_|3|3|_
			String[] ticketContents = ticketInfoBO.getTicketContent().split(SymbolConstants.DOUBLE_SLASH_VERTICAL_BAR);
			short winStatus = NOT_WIN;
			int hitMatchNum = 0;
			int bets = 1;
			int secondbets = 0;
			for(int j=0,lenj=ticketContents.length;j<lenj;j++){
				String tc = ticketContents[j];
				String drawValue = drawCodeMap.get(j);
				if("_".equals(tc)||(!tc.contains(drawValue)&&!"*".equals(drawValue))){
					continue;
				}
				if("*".equals(drawValue)){
					bets = bets*tc.split(SymbolConstants.COMMA).length;
				}
				
				hitMatchNum++;
			}
			if(hitMatchNum == 14){
				for(int j=0,lenj=ticketContents.length;j<lenj;j++){
					String tc = ticketContents[j];
					String drawValue = drawCodeMap.get(j);
					if("_".equals(tc)){
						continue;
					}
					if(!"*".equals(drawValue)){
						secondbets += bets*(tc.split(SymbolConstants.COMMA).length-1);
					}
				}
			}
			if(hitMatchNum == 13){
				for(int j=0,lenj=ticketContents.length;j<lenj;j++){
					String tc = ticketContents[j];
					String drawValue = drawCodeMap.get(j);
					if("_".equals(tc)){
						continue;
					}
					if((!tc.contains(drawValue)&&!"*".equals(drawValue))){
						bets = bets*(tc.split(SymbolConstants.COMMA).length);
					}
				}
			}
			Map<Integer, String> map = prizeMap.get(orderInfoBO.getLotteryCode());
			if(!ObjectUtil.isBlank(map)&&!ObjectUtil.isBlank(map.get(hitMatchNum))){
				winStatus = WIN;
			}
			if(winStatus == WIN){
				String prize = map.get(hitMatchNum);
				Object d[] = getBonus(orderInfoBO, ticketInfoBO, bets, map , prize,preWinBonus,afterWinBonus,orderWinningDetail,orderWinningDetailbb);
				double preBonus = Double.valueOf(d[0]+"");
				double afterBonus = Double.valueOf(d[1]+"");
				preWinBonus = BigDecimal.valueOf(Double.valueOf(d[2]+""));
				afterWinBonus = BigDecimal.valueOf(Double.valueOf(d[3]+""));
				if(secondbets!=0){
					for(int i = 0;i<secondbets;i++){
						String prize1 = map.get(13);
						Object d1[] = getBonus(orderInfoBO, ticketInfoBO, 1, map , prize1,preWinBonus,afterWinBonus,orderWinningDetail,orderWinningDetailbb);
						preBonus = preBonus + Double.valueOf(d1[0]+"");
						afterBonus = afterBonus +  Double.valueOf(d1[1]+"");
						preWinBonus = BigDecimal.valueOf(Double.valueOf(d1[2]+""));
						afterWinBonus = BigDecimal.valueOf(Double.valueOf(d1[3]+""));
					}
				}
				ticketInfoBO.setPreBonus(preBonus);
				ticketInfoBO.setAftBonus(afterBonus);
				StringBuffer sb = new StringBuffer();
				for(String key : orderWinningDetailbb.keySet()){
					sb.append(key+"_"+orderWinningDetailbb.get(key)+"注").append(",");
				}
				ticketInfoBO.setWinningDetail(sb.deleteCharAt(sb.length()-1).toString());
				num++;
			}
			ticketInfoBO.setWinningStatus(winStatus);
		}
		if(num>0){
			orderWinStatus = WIN;
			orderInfoBO.setPreBonus(CalculatingBonus.cauScale(2, preWinBonus));
			orderInfoBO.setAftBonus(CalculatingBonus.cauScale(2, afterWinBonus));
			StringBuffer sb = new StringBuffer();
			for(String key : orderWinningDetail.keySet()){
				sb.append(key+"_"+orderWinningDetail.get(key)+"注").append(",");
			}
			orderInfoBO.setWinningDetail(sb.deleteCharAt(sb.length()-1).toString());
		}
		orderInfoBO.setWinningStatus(orderWinStatus);
		orderInfoBO.setDrawCode(drawCode);
		orderInfoBO.setAddedBonus(0d);
		try {
			clearCacheObj(CacheConstants.SPORT_OLD_MATCH_LIST+orderInfoBO.getLotteryIssue());
		} catch (Exception e) {
			logger.error("老足彩开奖清除赛事缓存异常:"+e.getMessage(), e);
		}
	}


	/** 
	 * @Description: TODO(这里用一句话描述这个方法的作用) 
	 * @param orderInfoBO
	 * @param ticketInfoBO
	 * @param hitMatchNum
	 * @param bets
	 * @param map
	 * @throws ServiceRuntimeException
	 * @throws NumberFormatException
	 * @author wuLong
	 * @date 2018年3月9日 下午4:58:28
	 */
	private Object[] getBonus(OrderInfoBO orderInfoBO, TicketInfoBO ticketInfoBO, int bets,
			Map<Integer, String> map,String prize,BigDecimal preWinBonus,BigDecimal afterWinBonus,Map<String, Integer> orderWinningDetail,Map<String, Integer> orderWinningDetailbb) throws ServiceRuntimeException, NumberFormatException {
		String detail = draw.get(prize);
		String[] str = detail.split(SymbolConstants.DOUBLE_SLASH_VERTICAL_BAR);
		if(ObjectUtil.isBlank(str[2])){
			throw new ServiceRuntimeException(LotteryEnum.Lottery.getLottery(orderInfoBO.getLotteryCode()).getDesc()+"的彩期:"+orderInfoBO.getLotteryIssue()+",对应的"+prize+"未配置奖金");
		}
		double singleZsAmount = Double.valueOf(str[2]);
		double preBonus = singleZsAmount*ticketInfoBO.getMultipleNum();
		double afterBonus = preBonus;
		if(BigDecimal.valueOf(singleZsAmount).compareTo(TAX_UPPER_LIMIT_AMOUNT)>=0){
			afterBonus = preBonus-preBonus*TAX_RATE ;
		}
		ticketInfoBO.setAddedBonus(0d);
		//加奖奖金处理
		String addjz = str[3];
		if(!"0".equals(addjz)&&!ObjectUtil.isBlank(addjz)){
			double addAmount = Double.valueOf(addjz);
			double addPreBonus = addAmount*ticketInfoBO.getMultipleNum();
			double addAfterBonus = addPreBonus;
			if(BigDecimal.valueOf(addAmount).compareTo(TAX_UPPER_LIMIT_AMOUNT)>0){
				addAfterBonus = addPreBonus-addPreBonus*TAX_RATE ;
			}
			preBonus = preBonus+addPreBonus;
			afterBonus = afterBonus+addAfterBonus;
			ticketInfoBO.setAddedBonus(addAmount*bets);
		}
		if(orderWinningDetail.containsKey(prize)){
			orderWinningDetail.put(prize, orderWinningDetail.get(prize)+bets);
		}else{
			orderWinningDetail.put(prize, bets);
		}
		if(orderWinningDetailbb.containsKey(prize)){
			orderWinningDetailbb.put(prize, orderWinningDetailbb.get(prize)+bets);
		}else{
			orderWinningDetailbb.put(prize, bets);
		}
		preBonus = preBonus*bets;
		afterBonus = afterBonus*bets;
		preWinBonus = preWinBonus.add(BigDecimal.valueOf(preBonus));
		afterWinBonus = afterWinBonus.add(BigDecimal.valueOf(afterBonus));
		Object[] d = new Object[4];
		d[0] = preBonus;
		d[1] = afterBonus;
		d[2] = preWinBonus;
		d[3] = afterWinBonus;
		return d;
	}
	

	@Override
	protected void getDrawCode(List<OrderInfoBO> orderInfoBOs) {
		OrderInfoBO bo = null;
		if(!ObjectUtil.isBlank(orderInfoBOs)&&(bo = orderInfoBOs.get(0))!=null){
			LotteryIssueBO issue = orderService.getLotteryIssue(bo.getLotteryCode(), bo.getLotteryIssue());
			if ((drawCode = issue.getDrawCode()) == null) {
				throw new ServiceRuntimeException("不存在开奖号码");
			}
			handleDrawDetail(issue.getDrawDetail());
		}
	}
	
	private void handleDrawDetail(String drawDetail){
		//格式例如:一等奖|2|10000000 ,二等奖|5|200000,用 '|' 隔开；代表 奖项，注数，每注中奖金额 ，奖项与奖项之间用','号隔开。
		if(!ObjectUtil.isBlank(drawDetail)){
			String[] details = drawDetail.split(SymbolConstants.COMMA);
			for (String detail : details) {
				String[] str = detail.split(SymbolConstants.DOUBLE_SLASH_VERTICAL_BAR);
				if (str.length < 3) {
					throw new ServiceRuntimeException("彩期开奖详情错误");
				}
				draw.put(str[0].trim(), detail);
			}
		}
	}
	
}
