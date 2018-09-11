package com.hhly.award.service.award;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicLong;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.CollectionUtils;

import com.hhly.award.base.common.LotteryEnum.Lottery;
import com.hhly.award.base.common.OrderEnum.OrderStatus;
import com.hhly.award.base.common.OrderEnum.OrderWinningStatus;
import com.hhly.award.base.common.ResultBO;
import com.hhly.award.base.common.SymbolConstants;
import com.hhly.award.base.exception.ServiceRuntimeException;
import com.hhly.award.bo.OrderInfoBO;
import com.hhly.award.bo.TicketInfoBO;
import com.hhly.award.persistence.po.OrderGroupPO;
import com.hhly.award.service.IOrderService;
import com.hhly.award.service.ISportAgainstInfoService;
import com.hhly.award.service.ITicketInfoService;
import com.hhly.award.service.award.entity.AwardOrderBO;
import com.hhly.award.service.award.entity.FailOrderBO;
import com.hhly.award.util.HttpUtil;
import com.hhly.award.util.MatchUtil;
import com.hhly.award.util.ObjectUtil;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * @desc 公共处理类 实现cloneable 使用原型设计模式，对对象进行深度复制
 * @author jiangwei
 * @date 2017年5月24日
 * @company 益彩网络科技公司
 * @version 1.0
 */
public abstract class AbstractAward implements IAward, Cloneable {

	protected static Logger LOGGER = LoggerFactory.getLogger(AbstractAward.class);
	@Autowired
	protected IOrderService orderService;
	@Autowired
	protected ISportAgainstInfoService sportAgainstInfoService;
	@Autowired
	protected ITicketInfoService ticketInfoService;
	@Autowired
	private SendMessage message;
	// 已中奖状态
	protected static short NOT_WIN = (short) 2;
	// 未中奖装态
	protected static short WIN = (short) 3;
	// 执行总时间cpu
	private static AtomicLong TIME_CPU = new AtomicLong(0);
	// 执行总时间IO
	private static AtomicLong TIME_IO = new AtomicLong(0);

	protected String drawCode;

	@Value("${payout_award_url}")
	private  String PAYOUT_AWARD_URL;
	@Value("${payout_deduct_money}")
	private  String PAYOUT_DEDUCT_MONEY ;
	@Value("${all_bonus_url}")
	private  String ALL_BONUS_URL;
	
	@Value("${payout}")
	private  String PAYOUT;
	
	@Value("${bonus_url}")
	private  String BONUS_URL;

	public static final Map<Integer, List<String>> FLUCTUATE;

	static {
		// 浮动奖
		FLUCTUATE = new HashMap<>();
		FLUCTUATE.put(Lottery.SSQ.getName(), Arrays.asList("一等奖", "二等奖"));
		FLUCTUATE.put(Lottery.DLT.getName(), Arrays.asList("一等奖", "二等奖", "三等奖"));
		FLUCTUATE.put(Lottery.QLC.getName(), Arrays.asList("一等奖", "二等奖", "三等奖"));
		FLUCTUATE.put(Lottery.QXC.getName(), Arrays.asList("一等奖", "二等奖"));
		FLUCTUATE.put(Lottery.ZC6.getName(), Arrays.asList("一等奖"));
		FLUCTUATE.put(Lottery.JQ4.getName(), Arrays.asList("一等奖"));
		FLUCTUATE.put(Lottery.ZC_NINE.getName(), Arrays.asList("一等奖"));
		FLUCTUATE.put(Lottery.SFC.getName(), Arrays.asList("一等奖", "二等奖"));
	}

	@Override
	public String getDrawCode() {
		return this.drawCode;
	}

	@Override
	public AwardOrderBO handle(List<String> orders,boolean isBonus,int type) {
		AwardOrderBO result = new AwardOrderBO();
		List<FailOrderBO> fail = new ArrayList<>();
		result.setFail(fail);
		if (CollectionUtils.isEmpty(orders)) {
			return result;
		}
		LOGGER.info("进行开奖订单：" + orders);
		List<OrderInfoBO> orderInfoBOs = getOrderInfoBos(orders);
		if (ObjectUtil.isBlank(orderInfoBOs)) {
			LOGGER.info("数据库不存在这些订单的票信息:" + StringUtils.join(orders, SymbolConstants.COMMA));
			return result;
		}
		getDrawCode(orderInfoBOs);
		List<String> win = new ArrayList<>();
		List<String> notWin = new ArrayList<>();
		List<String> copyOrderList = new ArrayList<>();
		Short winingStatus = OrderWinningStatus.WINNING.getValue();
		for (OrderInfoBO orderInfoBO : orderInfoBOs) {
			long start = System.currentTimeMillis();
			try {
				deductMoney(orderInfoBO,type);
				if (CollectionUtils.isEmpty(orderInfoBO.getTicketInfoBOs())) {
					continue;
				}
				compute(orderInfoBO);
				long end = System.currentTimeMillis();
				orderInfoBO.setAddedBonus(null);//add wuLong 2017-12-23重置官方嘉奖字段为null
				orderService.updateOrderAward(orderInfoBO);
				TIME_CPU.addAndGet(end - start);
				TIME_IO.addAndGet(System.currentTimeMillis() - end);
				// 判断是否中奖
				if (Objects.equals(winingStatus, orderInfoBO.getWinningStatus())) {
					LOGGER.info(String.format("开奖-订单%s中%s,中奖金额（税前）：%s", orderInfoBO.getOrderCode(),
							orderInfoBO.getWinningDetail(), orderInfoBO.getPreBonus()));
					//合买
					if (orderInfoBO.getBuyType() == 3) {
						orderService.updateOrderGroup(orderInfoBO);
					}
					if(isBonus&&orderInfoBO.getOrderStatus() == OrderStatus.TICKETED.getValue()){
						getAllBonus(orderInfoBO);
					}
					win.add(orderInfoBO.getOrderCode());
				} else {
					notWin.add(orderInfoBO.getOrderCode());
				}
				if(orderInfoBO.getLotteryCode()==300||orderInfoBO.getLotteryCode()==301){
					copyOrderList.add(orderInfoBO.getOrderCode());
				}
			} catch (ServiceRuntimeException e) {
				fail.add(new FailOrderBO(orderInfoBO.getOrderCode(), e.getMessage()));
				LOGGER.error("订单开奖失败" + orderInfoBO.getOrderCode(), e);
			} catch (Exception e) {
				fail.add(new FailOrderBO(orderInfoBO.getOrderCode(), "服务器异常"));
				LOGGER.error("订单开奖失败" + orderInfoBO.getOrderCode(), e);
			}
		}
		// 发送中奖和不中奖通知
		if (!win.isEmpty()) {
			result.setWinCount(win.size());
			message.sendWin(win);
			// 数字彩开发发送中奖通知
			int lotteryCode = orderInfoBOs.get(0).getLotteryCode();
			if (lotteryCode < 200) {
				message.sendWinMesssage(win);
			}
		}
		if (!notWin.isEmpty()) {
			message.sendNotWin(notWin);
		}
		if(!copyOrderList.isEmpty()){
			message.sendCopyOrderPrize(copyOrderList);
		}
		return result;
	}
	/**
	 * 
	 * @Description: 获取到所有的嘉奖
	 * @param orderInfoBO
	 * @author wuLong
	 * @date 2018年1月6日 上午10:13:57
	 */
	private void getAllBonus(OrderInfoBO orderInfoBO){
		List<TicketInfoBO> detailBOs = orderInfoBO.getTicketInfoBOs();
		String result = "";
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("orderId", orderInfoBO.getOrderCode());
		jsonObject.put("aftBonus", orderInfoBO.getAftBonus());
		JSONArray jsonArray = new JSONArray();
		for(TicketInfoBO detailBO : detailBOs){
			if(detailBO.getWinningStatus() == (short)3){
				JSONObject jsonObjectDetailBO = new JSONObject();
				jsonObjectDetailBO.put("id", detailBO.getId());
				jsonObjectDetailBO.put("aftBonus", detailBO.getAftBonus());
				jsonObjectDetailBO.put("winningDetail", detailBO.getWinningDetail());
				jsonArray.add(jsonObjectDetailBO);
			}
		}
		jsonObject.put("orderDetailBO", jsonArray);
		Map<String, Object> param = new HashMap<>();
		param.put("orderStr", jsonObject.toString());
		try {
			LOGGER.info("订单编号："+orderInfoBO.getOrderCode()+",获取到所有的嘉奖传参数据:"+param.toString());
			result = HttpUtil.doPost(BONUS_URL.trim()+ALL_BONUS_URL.trim(), param);
			LOGGER.info("订单编号："+orderInfoBO.getOrderCode()+",获取到所有的嘉奖返回数据:"+result);
		} catch (IOException | URISyntaxException e) {
			message.sendFailBonus(orderInfoBO.getLotteryCode(), orderInfoBO.getLotteryIssue(),orderInfoBO.getOrderCode());
			throw new ServiceRuntimeException("请求获取到所有的嘉奖失败", e);
		}
//		{"success":1,"errorCode":"10001","message":"正确","data":{officialBonus:"官方加奖金额",localBonus:"本公司活动加奖金额",redCode:"红包编号"},"serviceTime":1515134071139}
		ResultBO<?> bo = (ResultBO<?>) JSONObject.toBean(JSONObject.fromObject(result), ResultBO.class);
		if (bo.getErrorCode().equals(ResultBO.SUCCESS_CODE)) {
			JSONObject jsobject = JSONObject.fromObject(bo.getData());
			Double officialBonus = null;
			Double localBonus = null;
			String redCode = null;
			if(jsobject.get("officialBonus")!=null){
				officialBonus = jsobject.getDouble("officialBonus");
			}
			if(jsobject.get("localBonus")!=null){
				localBonus = jsobject.getDouble("localBonus");
			}
			if(jsobject.get("redCode")!=null){
				redCode = jsobject.getString("redCode");
			}
			if (officialBonus == null && localBonus == null && redCode == null && jsobject.get("groupList") == null) {
				LOGGER.info("订单编号："+orderInfoBO.getOrderCode()+",获取到所有的嘉奖返回数据为空");
				return;
			}
			//合买加奖处理
			if (orderInfoBO.getBuyType() == 3) {
				if (jsobject.get("groupList") != null) {
					JSONArray groupList = jsobject.getJSONArray("groupList");
					List<Map> jsons = new ArrayList<>();
					for (Object o : groupList) {
						JSONObject object = (JSONObject) o;
						Map map = new HashMap();
						map.put("id", object.get("id"));
						map.put("addedBonus", object.get("addedBonus"));
						map.put("siteAddedBonus", object.get("siteAddedBonus"));
						map.put("redCode", object.get("redCode"));
						jsons.add(map);
					}
					OrderGroupPO po = new OrderGroupPO();
					po.setBonusFlag(2);
					po.setOrderCode(orderInfoBO.getOrderCode());
					orderService.updateOrderGroupAddBonusType(po);
					orderService.updateOrderGroupAddBonus(jsons);
				} else {
					//groupList 为null 返还发起人
					OrderGroupPO po = new OrderGroupPO();
					po.setBonusFlag(1);
					po.setOrderCode(orderInfoBO.getOrderCode());
					orderService.updateOrderGroupAddBonusType(po);
					orderService.updateOrderInfoBonus(officialBonus, localBonus, redCode, orderInfoBO.getId());
				}
			} else {
				orderService.updateOrderInfoBonus(officialBonus, localBonus, redCode, orderInfoBO.getId());
			}
		}else{
			message.sendFailBonus(orderInfoBO.getLotteryCode(), orderInfoBO.getLotteryIssue(),orderInfoBO.getOrderCode());
			throw new ServiceRuntimeException("获取到所有的嘉奖失败");
		}
	}

	/**
	 * 派奖订单扣款
	 * @author jiangwei
	 * @Version 1.0
	 * @CreatDate 2017年9月8日 下午4:29:05
	 * @param orderInfoBO
	 */
	private void deductMoney(OrderInfoBO orderInfoBO,int type) {
		//如果是推单的，则查询出所有的跟单订单并处理扣款
		if(orderInfoBO.getOrderType() == 2){
			noticePay(orderInfoBO,type);
			List<String> followOrders = orderService.getFollowOrderInfo(orderInfoBO.getOrderCode());
			if(!ObjectUtil.isBlank(followOrders)){
				List<OrderInfoBO>  infoBOs = orderService.getOrderInfos(followOrders);
				for(OrderInfoBO infoBO:infoBOs){
					noticePay(infoBO,type);
				}
			}
		}else{
			noticePay(orderInfoBO,type);
		}
	}

	/** 
	 * @Description: 通知pay工程扣款 
	 * @param orderInfoBO
	 * @param type 0正常开奖 1重置开奖  （开奖专用）
	 * @throws ServiceRuntimeException
	 * @author wuLong
	 * @date 2017年10月12日 下午3:43:27
	 */
	private void noticePay(OrderInfoBO orderInfoBO,int type) throws ServiceRuntimeException {
		if (orderInfoBO.getWinningStatus().shortValue() == OrderWinningStatus.GET_WINNING.getValue()
				|| StringUtils.isNotBlank(orderInfoBO.getRedCodeGet())) {
			Map<String, Object> param = new HashMap<>();
			param.put("orderCode", orderInfoBO.getOrderCode());
			param.put("type", String.valueOf(type));
			String result = "";
			try {
				result = HttpUtil.doPost(PAYOUT+PAYOUT_DEDUCT_MONEY, param);
			} catch (IOException | URISyntaxException e) {
				message.deductMoneyFail(orderInfoBO.getOrderCode(), orderInfoBO.getLotteryCode(), orderInfoBO.getLotteryIssue());
				throw new ServiceRuntimeException("请求退款网络错误", e);
			}
			
			ResultBO<?> bo = (ResultBO<?>) JSONObject.toBean(JSONObject.fromObject(result), ResultBO.class);
			if (!bo.getErrorCode().equals(ResultBO.SUCCESS_CODE)) {
				message.deductMoneyFail(orderInfoBO.getOrderCode(), orderInfoBO.getLotteryCode(), orderInfoBO.getLotteryIssue());
				throw new ServiceRuntimeException("退款失败");
			}
		}else{
			LOGGER.info("订单编号："+orderInfoBO.getOrderCode()+",状态不是已派奖，不能远程调用支付工程，进行交易信息操作");
		}
	}

	@SuppressWarnings("rawtypes")
	@Override
	public List<FailOrderBO> payoutHandle(List<String> orders) {
		List<FailOrderBO> fail = new ArrayList<>();
		if (CollectionUtils.isEmpty(orders)) {
			return fail;
		}
		LOGGER.info("执行派奖订单：" + orders);
		List<String> suss = new ArrayList<>();
		List<OrderInfoBO> orderInfoBOs = getOrderInfoBos(orders);
		if (ObjectUtil.isBlank(orderInfoBOs)) {
			LOGGER.info("数据库不存在这些订单的票信息:" + StringUtils.join(orders, SymbolConstants.COMMA));
			return fail;
		}

		List<String> copyOrderList = new ArrayList<>();
		for (OrderInfoBO orderInfoBO : orderInfoBOs) {
			if (!CollectionUtils.isEmpty(orderInfoBO.getTicketInfoBOs())) {
				try {
					payoutCompute(orderInfoBO);
					// 发送浮动奖通知
					fluctuateDrawMessage(orderInfoBO);
					// 发送请求进行派奖
					Map<String, Object> param = new HashMap<>();
					param.put("orderCode", orderInfoBO.getOrderCode());
					String result = HttpUtil.doPost(PAYOUT+PAYOUT_AWARD_URL, param);
					ResultBO bo = (ResultBO) JSONObject.toBean(JSONObject.fromObject(result), ResultBO.class);
					if (bo.getErrorCode().equals(ResultBO.SUCCESS_CODE)) {
						if(orderInfoBO.getLotteryCode() == 300 || orderInfoBO.getLotteryCode() == 301){
							copyOrderList.add(orderInfoBO.getOrderCode());
						}
						List<String> orderCodes = new ArrayList<>();
						orderCodes.add(orderInfoBO.getOrderCode());
						if(orderInfoBO.getOrderType() == 2){//推单
							List<String> follows = orderService.getFollowOrderInfo(orderInfoBO.getOrderCode());
							orderCodes.addAll(follows);
						}
						orderService.updateOrderWinStatus(orderCodes);
						suss.add(orderInfoBO.getOrderCode());
					} else {
						LOGGER.info("http请求 支付端接口, 为用户派发失败 中奖金额     result  : " + result + "ordercode : "
								+ orderInfoBO.getOrderCode());
						fail.add(new FailOrderBO(orderInfoBO.getOrderCode(), bo.getMessage()));
					}
				} catch (ServiceRuntimeException e) {
					fail.add(new FailOrderBO(orderInfoBO.getOrderCode(), e.getMessage()));
					LOGGER.info("订单派奖失败" + orderInfoBO.getOrderCode(), e);
				} catch (Exception e) {
					fail.add(new FailOrderBO(orderInfoBO.getOrderCode(), "服务器异常"));
					LOGGER.error("订单派奖失败" + orderInfoBO.getOrderCode(), e);
				}
			}
		}
		// 发送派奖和不派奖通知
		if (!suss.isEmpty()) {
			message.sendSucc(suss);
			message.sendActivity(suss,1);
			// 高频彩竞技彩派奖发送通知
			int lotteryCode = orderInfoBOs.get(0).getLotteryCode();
			if (lotteryCode >= 200) {
				message.sendWinMesssage(suss);
			}
			if(!copyOrderList.isEmpty()){
				message.sendCopyOrderPrize(suss);
			}
		}
		return fail;
	}

	@Override
	public Map<String, Long> getExecuteTime() {
		Map<String, Long> map = new HashMap<>();
		map.put("cpu", TIME_CPU.get());
		map.put("io", TIME_IO.get());
		return map;
	}

	@Override
	public AbstractAward clone() {
		try {
			AbstractAward award = (AbstractAward) super.clone();
			depthClone(award);
			return award;
		} catch (Exception e) {
			throw new ServiceRuntimeException("原型模式创建对象失败");
		}
	}

	/**
	 * 进行深度复制,对除基础数据类型进行处理 有需要请进行实现
	 * 
	 * @author jiangwei
	 * @Version 1.0
	 * @CreatDate 2017年5月27日 上午11:22:17
	 */
	protected void depthClone(AbstractAward award) {

	};

	/**
	 * 开奖计算,对单个订单进行开奖计算
	 * 
	 * @author jiangwei
	 * @Version 1.0
	 * @CreatDate 2017年5月25日 上午10:59:47
	 * @param orderInfoBO
	 */
	protected abstract void compute(OrderInfoBO orderInfoBO);

	/**
	 * 派奖计算,对单个订单进行派奖计算
	 * 
	 * @param orderInfoBO
	 * @date 2017年6月19日下午6:43:41
	 * @author cheng.chen
	 */
	protected void payoutCompute(OrderInfoBO orderInfoBO) {
		List<TicketInfoBO> details = orderInfoBO.getTicketInfoBOs();
		// 统计订单详情票
		double ticketMoney = 0.0;
		double preBonus = 0.0;
		for (TicketInfoBO detail : details) {
			ticketMoney = MatchUtil.sum(ticketMoney, detail.getTicketMoney());
			if (detail.getWinningStatus().equals(OrderWinningStatus.WINNING.getValue())) {
				preBonus = MatchUtil.sum(preBonus, detail.getPreBonus());
			}
		}
		if (!Objects.equals(orderInfoBO.getOrderAmount(), ticketMoney)) {
			throw new ServiceRuntimeException("订单投注金额与票总金额不相等");
		}

		if (!Objects.equals(orderInfoBO.getPreBonus(), preBonus)) {
			message.sendPayoutMoneyFail(orderInfoBO.getLotteryCode(), orderInfoBO.getLotteryIssue(),
					orderInfoBO.getOrderCode());
			throw new ServiceRuntimeException("订单中奖金额与票总中奖金额不相等");
		}
	}

	/**
	 * 判断是否存在浮动奖，如果存在发送报警信息
	 * 
	 * @author jiangwei
	 * @Version 1.0
	 * @CreatDate 2017年8月9日 下午4:07:00
	 * @param orderInfoBO
	 */
	private void fluctuateDrawMessage(OrderInfoBO orderInfoBO) {
		String detail = orderInfoBO.getWinningDetail();
		List<String> fluctuate = FLUCTUATE.get(orderInfoBO.getLotteryCode());
		if (StringUtils.isBlank(detail) || CollectionUtils.isEmpty(fluctuate)) {
			return;
		}
		// 判断是否中浮动奖
		for (String str : fluctuate) {
			if (detail.indexOf(str) != -1) {
				message.sendFluctuate(orderInfoBO.getLotteryCode(), orderInfoBO.getLotteryIssue(),
						orderInfoBO.getOrderCode(), str);
			}
		}
	}

	/**
	 * 获取开奖号码
	 * 
	 * @author jiangwei
	 * @Version 1.0
	 * @CreatDate 2017年5月25日 上午11:27:24
	 * @param orderInfoBOs
	 */
	protected abstract void getDrawCode(List<OrderInfoBO> orderInfoBOs);

	private List<OrderInfoBO> getOrderInfoBos(List<String> list) {
		return orderService.getOrderInfos(list);
	}
}