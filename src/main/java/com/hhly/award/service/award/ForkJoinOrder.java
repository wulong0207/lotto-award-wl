package com.hhly.award.service.award;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveTask;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hhly.award.base.common.HandleEnum;
import com.hhly.award.base.common.SymbolConstants;
import com.hhly.award.base.exception.ServiceRuntimeException;
import com.hhly.award.bo.IssueContentInfoBO;
import com.hhly.award.service.IOrderService;
import com.hhly.award.service.award.entity.AwardOrderBO;
import com.hhly.award.service.award.entity.AwardResultBO;
import com.hhly.award.service.award.entity.FailOrderBO;
import com.hhly.award.service.award.factory.AwardFactory;
import com.hhly.award.service.award.lottery.sports.SportsAward;
import com.hhly.award.util.JsonUtil;
import com.hhly.award.util.PropertyUtil;

/**
 * @desc 订单开奖主体服务
 * @author jiangwei
 * @date 2017年5月24日
 * @company 益彩网络科技公司
 * @version 1.0
 */
@Service
public class ForkJoinOrder implements IOrder {

	private static final Logger LOGGER = Logger.getLogger(ForkJoinOrder.class);
	// 开奖新线程池
	private static ForkJoinPool FORK_JOIN_POOL;
	// 保存开奖信息集合
	private static Map<String, OrderAwardResult> AWARD_RESULT = new ConcurrentHashMap<>();
	
	static {
		int poolSize = Integer.parseInt(PropertyUtil.getPropertyValue("application.properties", "pool_size"));
		FORK_JOIN_POOL = new ForkJoinPool(poolSize);
	}
	
	@Autowired
	private AwardFactory awardFacotry;
	
	@Autowired
	private IOrderService orderService;
	@Autowired
	private SendMessage sendMessage;

	@Override
	public AwardResultBO getResult(int lotteryCode, String issue, short handleType) {

		OrderAwardResult orderAwardResult = AWARD_RESULT.get(getResutlKey(lotteryCode, issue, handleType));
		if (orderAwardResult == null || orderAwardResult.getTotal() == 0) {
			return new AwardResultBO();
		}
		return getAwardInfo(orderAwardResult);
	}

	@Override
	public void distribute(List<String> orders, int lotteryCode, String issue, short handleType,Integer type) {
		String key = getResutlKey(lotteryCode, issue, handleType);
		OrderAwardResult orderAwardResult = AWARD_RESULT.get(key);
		if (orderAwardResult != null && orderAwardResult.isAward()) {
			String str = HandleEnum.Type.OPEN_AWARD.getValue() == handleType ? "开奖" : "派奖";
			throw new ServiceRuntimeException("该彩种{" + lotteryCode + "},期号{" + issue + "}正在"+str);
		}
		IAward award = awardFacotry.getAward(lotteryCode);
		//每个子任务处理最大订单数
		int unit = 100;
		OrderAwardResult result = new OrderAwardResult(orders.size(),lotteryCode,issue);
		AWARD_RESULT.put(key, result);
		if(HandleEnum.Type.OPEN_AWARD.getValue() == handleType){
			OrderTask orderTask = new OrderTask(0, orders.size(), unit, orders, award, result,true,type);	
			FORK_JOIN_POOL.execute(orderTask);
		}else if(HandleEnum.Type.PAYOUT_AWARD.getValue() == handleType) {
			PayoutTask payoutTask = new PayoutTask(0, orders.size(), unit, orders, award, result);	
			FORK_JOIN_POOL.execute(payoutTask);			
		}
		clearAwardResult();
	}


	/**
	 * 生成唯一key
	 * 
	 * @author jiangwei
	 * @Version 1.0
	 * @CreatDate 2017年5月24日 下午4:48:14
	 * @param lotteryCode
	 * @param issue
	 * @return
	 */
	private static String getResutlKey(int lotteryCode, String issue, short handleType) {
		if(StringUtils.isBlank(issue)||"\"\"".equals(issue)){
			issue = "";
		}
		String key =  lotteryCode + SymbolConstants.UNDERLINE + issue + SymbolConstants.UNDERLINE + handleType;
		return key;
	}

	/**
	 * 清理已经开完奖的信息
	 * 
	 * @author jiangwei
	 * @Version 1.0
	 * @CreatDate 2017年5月24日 下午6:14:21
	 */
	private void clearAwardResult() {
		for (Map.Entry<String, OrderAwardResult> entry : AWARD_RESULT.entrySet()) {
			boolean isAward = entry.getValue().isAward();
			if (!isAward) {
				LOGGER.info(JsonUtil.object2Json(entry.getValue(), "yyyy-MM-dd HH:mm:ss"));
				AWARD_RESULT.remove(entry.getKey());
			}
		}
	}

	/**
	 * 计算实时开奖信息
	 * @author jiangwei
	 * @Version 1.0
	 * @CreatDate 2017年5月24日 下午6:22:02
	 * @param orderAwardResult
	 * @return
	 */
	private static AwardResultBO getAwardInfo(OrderAwardResult orderAwardResult) {
		AwardResultBO bo = new AwardResultBO();
		Date startTime = orderAwardResult.getStartTime();
		int total = orderAwardResult.getTotal();
		int success = orderAwardResult.getSuccess();
		List<String> fail = orderAwardResult.getFailOrder();
		int failNum = fail.size();
		bo.setFail(failNum);
		bo.setSuccess(success);
		bo.setTotal(total);
		bo.setFailOrder(fail);
		bo.setDrawCode(orderAwardResult.getDrawCode());
		bo.setFailMessage(orderAwardResult.getFailMessage());
		bo.setWinCount(orderAwardResult.getWinCount());
		// 开奖订单数
		int awardNum = success + failNum;
		long now = System.currentTimeMillis();
		if(orderAwardResult.getEndTime() != null){
			now = orderAwardResult.getEndTime().getTime();
		}
		// 已开奖时间
		long awardSecond = now - startTime.getTime();
		bo.setAwardSecond(awardSecond / 1000);
		// 剩余开奖时间
		if (awardNum > 0) {
			double plan = awardSecond / (double) awardNum * (total - awardNum);
			bo.setPlanSecond((long) (plan / 1000));
		} else {
			bo.setPlanSecond(-1);
		}
		return bo;
	}

	/**
	 * @desc 订单开奖task
	 * @author jiangwei
	 * @date 2017年5月24日
	 * @company 益彩网络科技公司
	 * @version 1.0
	 */
	@SuppressWarnings("serial")
	private class OrderTask extends RecursiveTask<Integer> {
		// list 开始位置
		private int start;
		// list 结束位置
		private int end;
		// 处理单元（一次处理订单数）
		private int unit;
		// 开奖服务
		private IAward award;
		// 开奖订单
		private List<String> orders;
		// 开奖结果
		private OrderAwardResult result;
		//是否要请求嘉奖接口
		private boolean isBonus;
		//是否重置开奖
		private int type;

		public OrderTask(int start, int end, int unit, List<String> orders, IAward award, OrderAwardResult result,boolean isBonus,int type) {
			this.start = start;
			this.end = end;
			this.orders = orders;
			this.award = award;
			this.unit = unit;
			this.result = result;
			this.isBonus = isBonus;
			this.type = type;
		}

		@Override
		protected Integer compute() {
			int invertal = end - start;
			if (invertal <= unit) {
				List<String> comList = orders.subList(start, end);
				try {
					AwardOrderBO awardOrder = award.handle(comList,isBonus,type);
					result.add(awardOrder.getWinCount(),comList.size() - awardOrder.getFail().size(), awardOrder.getFail());
					result.setDrawCode(award.getDrawCode());
					if(result.isAllSuccess()){
						LOGGER.info(result.getLotteryCode()+"|"+result.getLotteryIssue()+"|开奖完成|所有订单开奖成功，修改竞彩数字彩状态");
						//全部订单开奖成功
						result.setEndTime(new Date());
						orderService.updateAccomplishDraw(result.getLotteryCode(),result.getLotteryIssue());
					}
					if(result.isAccomplish()){
						if(!result.getFailOrder().isEmpty()){
							sendMessage.sendDrawFail(result.getFailOrder(),result.getLotteryCode(),result.getLotteryIssue());
							orderService.updateOrderDrawFail(result.getFailOrder());
						}
						LOGGER.info(result.getLotteryCode()+"|"+result.getLotteryIssue()+"|开奖完成|"+getAwardInfo(result));
						LOGGER.info("所有彩种开奖执行时间（毫秒）"+ award.getExecuteTime());
					}
				} catch (Exception e) {
					LOGGER.error("订单开奖失败" + comList.toString(), e);
					result.addFailOrder(comList);
				}
			} else {
				int middle = (start + end) / 2;
				OrderTask left = new OrderTask(start, middle, unit, orders, award, result,isBonus,type);
				OrderTask right = new OrderTask(middle, end, unit, orders, award, result,isBonus,type);
				left.fork();
				right.fork();
			}
			return 0;
		}

	}
	
	@SuppressWarnings("serial")
	private class PayoutTask extends RecursiveTask<Integer> {
		// list 开始位置
		private int start;
		// list 结束位置
		private int end;
		// 处理单元（一次处理订单数）
		private int unit;
		// 开奖服务
		private IAward award;
		// 开奖订单
		private List<String> orders;
		// 开奖结果
		private OrderAwardResult result;

		public PayoutTask(int start, int end, int unit, List<String> orders, IAward award, OrderAwardResult result) {
			this.start = start;
			this.end = end;
			this.orders = orders;
			this.award = award;
			this.unit = unit;
			this.result = result;
		}

		@Override
		protected Integer compute() {
			int invertal = end - start;
			if (invertal <= unit) {
				List<String> comList = orders.subList(start, end);
				try {
					List<FailOrderBO> fail = award.payoutHandle(comList);
					result.add(comList.size() - fail.size(), fail);
					if(result.isAccomplish()){
						result.setEndTime(new Date());
						orderService.updateAccomplishPay(result.getLotteryCode(),result.getLotteryIssue());
						LOGGER.info(result.getLotteryCode()+"|"+result.getLotteryIssue()+"|派奖完成|");
						if(result.getFailOrder().size() > 0){
							sendMessage.sendPayoutFail(result.getFailOrder(),result.getLotteryCode(),result.getLotteryIssue());	
						}
					}
				} catch (Exception e) {
					LOGGER.error("订单派奖失败" + comList.toString(), e);
					result.addFailOrder(comList);
				}
			} else {
				int middle = (start + end) / 2;
				PayoutTask left = new PayoutTask(start, middle, unit, orders, award, result);
				PayoutTask right = new PayoutTask(middle, end, unit, orders, award, result);
				left.fork();
				right.fork();
			}
			return 0;
		}

	}	

	public static void main(String[] args) throws InterruptedException {
		IOrder order = new ForkJoinOrder();
		List<String> list = new ArrayList<>();
		for (int i = 0; i < 1000000; i++) {
			list.add("" + i);
		}
		order.distribute(list, 100, "10012", HandleEnum.Type.OPEN_AWARD.getValue(),0);
		for (;;) {
			Thread.sleep(2000);
			AwardResultBO bo1 = order.getResult(100, "10012", HandleEnum.Type.OPEN_AWARD.getValue());
			System.out.println(JsonUtil.object2Json(bo1, "yyyy-MM-dd HH:mm:ss"));
		}
	}

	@Override
	public void RecommendDistribute(Integer lotteryCode,Integer lotteryChild, Integer orderStatus,int type,String systemCode) {
		List<IssueContentInfoBO> contentInfoBOs = null;
		if(type==0){
			contentInfoBOs = orderService.getIssueContentInfos(lotteryCode, orderStatus, lotteryChild,null);
		}else{
			contentInfoBOs = orderService.getIssueContentInfos(lotteryCode, orderStatus, lotteryChild,systemCode);
		}
		if(contentInfoBOs.isEmpty()){
			return;
		}
		IAward award = awardFacotry.getAward(lotteryCode);
		SportsAward sportsAward = (SportsAward)award;
		RecommendTask orderTask = new RecommendTask(0, contentInfoBOs.size(), 1, contentInfoBOs, sportsAward);	
		FORK_JOIN_POOL.execute(orderTask);
	}
	
	@SuppressWarnings("serial")
	private class RecommendTask extends RecursiveTask<Integer> {
		// list 开始位置
		private int start;
		// list 结束位置
		private int end;
		// 处理单元（一次处理订单数）
		private int unit;
		// 开奖服务
		private SportsAward award;
		// 开奖订单
		private List<IssueContentInfoBO> contentInfoBOs;
		
		public RecommendTask(int start, int end, int unit, List<IssueContentInfoBO> contentInfoBOs, SportsAward award) {
			this.start = start;
			this.end = end;
			this.contentInfoBOs = contentInfoBOs;
			this.award = award;
			this.unit = unit;
		}

		@Override
		protected Integer compute() {
			int invertal = end - start;
			if (invertal <= unit) {
				List<IssueContentInfoBO> comList = contentInfoBOs.subList(start, end);
				try {
					award.recommendCompute(comList.get(0));
				} catch (Exception e) {
					LOGGER.error("推荐订单开奖失败" + comList.get(0).toString(), e);
				}
			} else {
				int middle = (start + end) / 2;
				RecommendTask left = new RecommendTask(start, middle, unit, contentInfoBOs, award);
				RecommendTask right = new RecommendTask(middle, end, unit, contentInfoBOs, award);
				left.fork();
				right.fork();
			}
			return 0;
		}

	}
}
