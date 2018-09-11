package com.hhly.award.controller;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import com.hhly.award.base.common.*;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.hhly.award.base.common.LotteryEnum.Lottery;
import com.hhly.award.base.controller.BaseController;
import com.hhly.award.base.exception.ServiceRuntimeException;
import com.hhly.award.bo.LotteryOrderBO;
import com.hhly.award.service.IOrderService;
import com.hhly.award.service.award.AbstractAward;
import com.hhly.award.service.award.IOrder;
import com.hhly.award.service.award.entity.AwardResultBO;
import com.hhly.award.service.award.factory.AwardFactory;
import com.hhly.award.util.AwardUtil;
import com.hhly.award.util.JsonUtil;
import com.hhly.award.util.ObjectUtil;
import com.hhly.award.util.RedisUtil;

/**
 * @desc 开奖派奖
 * @author jiangwei
 * @date 2017年5月15日
 * @company 益彩网络科技公司
 * @version 1.0
 */
@Controller
@RequestMapping(value = "/award")
public class AwardController extends BaseController{
	private Logger logger = LoggerFactory.getLogger(AwardController.class);
	
	@Autowired
	private IOrderService orderService;
	@Autowired
	private IOrder orderHandler;

	@Autowired
	private AwardFactory awardFacotry;
	
	@Autowired
	RedisUtil redisUtil;

	/**
	 * @author jiangwei
	 * @Version 1.0
	 * @CreatDate 2017年5月30日 上午9:26:56
	 * @param lotteryCode
	 *            彩种
	 * @param lotteryIssue
	 *            彩期
	 * @param type
	 *            0：普通开奖，1重置开奖
	 * @return
	 */
	@RequestMapping(value = "/lottery")
	@ResponseBody
	public Object award(Integer lotteryCode, String lotteryIssue, int type, String orderCodes) {
		logger.info("开奖参数【lotteryCode:"+lotteryCode+",lotteryIssue:"+lotteryIssue+",type:"+type+",orderCodes:"+orderCodes+"】");
		if (StringUtils.isNotBlank(orderCodes)) {
			List<String> orders = Arrays.asList(orderCodes.split(SymbolConstants.COMMA));
			// 按订单开奖
			Collection<LotteryOrderBO> lotteryOrder = orderService.getLotteryOrderDraw(orders, type);
			for (LotteryOrderBO lotteryOrderBO : lotteryOrder) {
				orderHandler.distribute(lotteryOrderBO.getOrders(), lotteryOrderBO.getLotteryCode(),
						lotteryOrderBO.getLotteryIssue(), HandleEnum.Type.OPEN_AWARD.getValue(),type);
				SportClearRedis(lotteryOrderBO.getLotteryCode(), lotteryIssue); 
			}
		} else {
			// 按彩种开奖
			if (lotteryCode == null) {
				throw new ServiceRuntimeException("开奖彩种不能为空");
			}
			if (!AwardUtil.isSport(lotteryCode) && StringUtils.isBlank(lotteryIssue)) {
				throw new ServiceRuntimeException("该彩种开奖彩期不能为空");
			}
			List<String> orders = null;
			if (AwardUtil.isSport(lotteryCode)) {
				if (lotteryCode.equals(Lottery.GJJC.getName()) || lotteryCode.equals(Lottery.GYJJC.getName())) {
					if (StringUtils.isBlank(lotteryIssue)) {
						throw new ServiceRuntimeException("该彩种开奖彩期不能为空");
					}
					orders = orderService.getOrderInfosByGyJc(lotteryCode, lotteryIssue);
				} else {
					orders = orderService.listMatchAwardOrders(lotteryCode, lotteryIssue, type);
				}
			} else {
				orders = orderService.listIssueAwardOrders(lotteryCode, lotteryIssue, type);
			}
			if(CollectionUtils.isEmpty(orders)){
				//改期不存在订单直接修改期状态
				orderService.updateAccomplishDraw(lotteryCode, lotteryIssue);
			}else{
				orderHandler.distribute(orders, lotteryCode, lotteryIssue, HandleEnum.Type.OPEN_AWARD.getValue(),type);
			}
			SportClearRedis(lotteryCode, lotteryIssue); 
		}
		return ResultBO.getSuccess(null);
	}
	
	/**
	 * 派奖管理
	 * @author jiangwei
	 * @Version 1.0
	 * @CreatDate 2017年7月22日 上午9:34:47
	 * @param lotteryCode
	 * @param lotteryIssue
	 * @param orderCodes
	 * @return
	 */
	@RequestMapping(value = "/payout")
	@ResponseBody
	public Object payout(Integer lotteryCode, String lotteryIssue, String orderCodes){
		logger.info("派奖参数【lotteryCode:"+lotteryCode+",lotteryIssue:"+lotteryIssue+",orderCodes:"+orderCodes+"】");
		if (StringUtils.isNotBlank(orderCodes)) {
			List<String> orders = Arrays.asList(orderCodes.split(SymbolConstants.COMMA));
			orders = orderService.getByOrderType(orders, Arrays.asList(1,2));
			if(orders.isEmpty()){
				throw new ServiceRuntimeException("订单不存在或这些订单都是跟单的订单不能单独做派奖");
			}
			//验证订单是否合法能派奖
			orderService.checkPayout(orders);
			// 按订单开奖
			Collection<LotteryOrderBO> lotteryOrder = orderService.getLotteryOrderInfo(orders);
			for (LotteryOrderBO lotteryOrderBO : lotteryOrder) {
				orderHandler.distribute(lotteryOrderBO.getOrders(), lotteryOrderBO.getLotteryCode(),
						lotteryOrderBO.getLotteryIssue(), HandleEnum.Type.PAYOUT_AWARD.getValue(),null);
				SportClearRedis(lotteryOrderBO.getLotteryCode(), lotteryIssue); 
			}			
		} else {
			// 按彩种开奖
			if (lotteryCode == null) {
				throw new ServiceRuntimeException("派奖彩种不能为空");
			}
			if (!AwardUtil.isSport(lotteryCode) && StringUtils.isBlank(lotteryIssue)) {
				throw new ServiceRuntimeException("该彩种派奖彩期不能为空");
			}
			List<String> orders = null;
			if (AwardUtil.isSport(lotteryCode)) {
//				if (lotteryCode.equals(Lottery.GJJC.getName()) || lotteryCode.equals(Lottery.GYJJC.getName())) {
//					orders = orderService.getOrderInfosByGyJc(lotteryCode, lotteryIssue);
//				} else {
                orders = orderService.listSportPayoutAwardOrders(lotteryCode);
					if (!CollectionUtils.isEmpty(orders)) {
						orders = orderService.getByOrderType(orders, Arrays.asList(1, 2));
					}
//				}
            } else {
				orders = orderService.listIssuePayoutAwardOrders(lotteryCode, lotteryIssue);
			}
			if(CollectionUtils.isEmpty(orders)){
				//改期不存在订单直接修改期状态
				orderService.updateAccomplishPay(lotteryCode, lotteryIssue);
			}else{
				orderHandler.distribute(orders, lotteryCode, lotteryIssue, HandleEnum.Type.PAYOUT_AWARD.getValue(),null);
			}
			SportClearRedis(lotteryCode, lotteryIssue); 
		}
		return ResultBO.getSuccess(null);
	}
	
	
	/**
	 * 竞技彩重置开奖
	 * @author jiangwei
	 * @Version 1.0
	 * @CreatDate 2017年6月15日 下午4:54:49
	 * @param lotteryChild
	 * @param code
	 * @param lotteryCode
	 * @return
	 */
	@RequestMapping(value = "/restart/sport")
	@ResponseBody
	public Object awardRestart(Integer lotteryChild,String code,Integer lotteryCode){
		logger.info("竞彩重置开奖参数lotteryChild:"+lotteryChild+",code:"+code+",lotteryCode:"+lotteryCode);
		if(lotteryCode == null){
			return ResultBO.getFail("参数有误", null);
		}
		List<String> orders = orderService.getOrderCodesByParam(code, lotteryChild, lotteryCode);
		if(!ObjectUtil.isBlank(orders)){
			String key ="";
			if(lotteryChild != null){
				key +=lotteryChild;
			}
			if(code != null){
				key +=code;
			}
			orders = orderService.getByOrderType(orders, Arrays.asList(1,2));
			if(orders.isEmpty()){
				return ResultBO.getFail("已跟单订单不能单独处理", null);
			}
			orderHandler.distribute(orders, lotteryCode, key, HandleEnum.Type.OPEN_AWARD.getValue(),1);
			SportClearRedis(lotteryCode, null); 
		}
//		try {
//			/****一比分推荐订单开奖*****/
//			orderHandler.RecommendDistribute(lotteryCode, lotteryChild,null,1,code);
//		} catch (Exception e) {
//			logger.error(e.getMessage(),e);
//		}
		return ResultBO.getSuccess(null);
	}
	/**
	 * 获取实时开奖进度
	 * 
	 * @author jiangwei
	 * @Version 1.0
	 * @CreatDate 2017年6月9日 上午10:01:25
	 * @param lotteryCode
	 * @param lotteryIssue
	 * @return
	 */
	@RequestMapping(value = "/schedule")
	@ResponseBody
	public Object awardTime(Integer lotteryCode, String lotteryIssue, short handleType) {
		AwardResultBO bo = orderHandler.getResult(lotteryCode, lotteryIssue, handleType);
		bo.setFailOrder(null);
		return ResultBO.getSuccess(JsonUtil.object2Json(bo));
	}

	/**
	 * 获取彩种执行时间
	 * 
	 * @author jiangwei
	 * @Version 1.0
	 * @CreatDate 2017年5月30日 上午10:10:17
	 * @param lotteryCode
	 * @return
	 */
	@RequestMapping(value = "/lottery/time")
	@ResponseBody
	public Object executeTime(Integer lotteryCode) {
		AbstractAward award = awardFacotry.getAward(lotteryCode);
		return award.getExecuteTime();
	}
	/**
	 * 竞技彩清除缓存
	 * @param lotteryCode
	 * @param lotteryIssue
	 * @author wul687
	 * @date 2018-07-24
	 */
	private void SportClearRedis(Integer lotteryCode, String lotteryIssue) {
		if (AwardUtil.isSport(lotteryCode)) {
			try {
				redisUtil.delAllString(CacheConstants.SPORT_FB_MATCH_LIST);
				redisUtil.delAllString(CacheConstants.SPORT_BB_MATCH_LIST);
				redisUtil.delAllString(CacheConstants.SPORT_FB_SYSTEM_CODE_MATCH_LIST);
				redisUtil.delAllString(CacheConstants.SPORT_BB_SYSTEM_CODE_MATCH_LIST_);
			} catch (Exception e) {
				logger.error("竞彩派奖清除赛事缓存异常:"+e.getMessage(), e);
			}
		}else{
			if(lotteryCode == Lottery.ZC6.getName()||lotteryCode == Lottery.JQ4.getName()||lotteryCode == Lottery.SFC.getName()||lotteryCode == Lottery.ZC_NINE.getName()){
				try {
					redisUtil.delObj(CacheConstants.SPORT_OLD_MATCH_LIST+lotteryIssue);
				} catch (Exception e) {
					logger.error("老足彩派奖清除赛事缓存异常:"+e.getMessage(), e);
				}
			}
		}
	}
}
