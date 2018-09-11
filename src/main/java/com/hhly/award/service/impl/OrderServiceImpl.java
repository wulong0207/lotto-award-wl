package com.hhly.award.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONObject;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hhly.award.base.common.LotteryEnum.LotIssueSaleStatus;
import com.hhly.award.base.common.LotteryEnum.Lottery;
import com.hhly.award.base.common.OrderEnum.OrderWinningStatus;
import com.hhly.award.base.common.SymbolConstants;
import com.hhly.award.base.exception.ServiceRuntimeException;
import com.hhly.award.bo.IssueContentInfoBO;
import com.hhly.award.bo.LotteryIssueBO;
import com.hhly.award.bo.LotteryOrderBO;
import com.hhly.award.bo.OrderDetailBO;
import com.hhly.award.bo.OrderInfoBO;
import com.hhly.award.bo.TicketInfoBO;
import com.hhly.award.persistence.dao.IssueContentInfoMapper;
import com.hhly.award.persistence.dao.OrderGroupContentDaoMapper;
import com.hhly.award.persistence.dao.OrderGroupDaoMapper;
import com.hhly.award.persistence.dao.OrderInfoDaoMapper;
import com.hhly.award.persistence.dao.TicketInfoDaoMapper;
import com.hhly.award.persistence.po.LotteryTypePO;
import com.hhly.award.persistence.po.OrderGroupContentPO;
import com.hhly.award.persistence.po.OrderGroupPO;
import com.hhly.award.service.IOrderService;
import com.hhly.award.service.award.SendMessage;
import com.hhly.award.util.AwardUtil;
import com.hhly.award.util.DateUtil;
import com.hhly.award.util.DrawCodeUtil;
import com.hhly.award.util.MatchUtil;
import com.hhly.award.util.ObjectUtil;

/**
 * @author wulong
 * @create 2017/5/9 14:54
 */
@Service
public class OrderServiceImpl implements IOrderService {

    private Logger logger = LoggerFactory.getLogger(OrderServiceImpl.class);
    @Autowired
    private OrderInfoDaoMapper orderInfoDaoMapper;
    @Autowired
    private TicketInfoDaoMapper ticketInfoDaoMapper;
    @Autowired
    private IssueContentInfoMapper issueContentInfoMapper;
    @Autowired
    private SendMessage sendMessage;
    @Autowired
    private OrderGroupContentDaoMapper orderGroupContentDaoMapper;
    @Autowired
    private OrderGroupDaoMapper orderGroupDaoMapper;

    /**
     * @param orderCodes 订单编号
     * @return
     * @Description: 根据订单编号查询订单信息表
     * @author wuLong
     * @date 2017年3月27日 下午4:22:07
     */
    @Override
    public List<OrderInfoBO> getOrderInfos(List<String> orderCodes) {
        // 查询订单表
        return orderInfoDaoMapper.getOrderInfoS(orderCodes);
    }

    @Override
    public void updateOrderAward(OrderInfoBO orderInfoBO) {
        orderInfoDaoMapper.updateOrderInfo(orderInfoBO);
        List<TicketInfoBO> ticketInfoBOs = orderInfoBO.getTicketInfoBOs();
        updateOrderDetail(ticketInfoBOs);
        ticketInfoDaoMapper.updateTicketInfo(ticketInfoBOs);
        updateOrderAdded(orderInfoBO);
    }

    /**
     * 修改订单追号信息
     *
     * @param orderInfoBO
     * @author jiangwei
     * @Version 1.0
     * @CreatDate 2017年9月7日 下午3:29:01
     */
    private void updateOrderAdded(OrderInfoBO orderInfoBO) {
        String orderAddCode = orderInfoBO.getOrderAddCode();
        //判断订单是否追号订单
        if (StringUtils.isBlank(orderAddCode)) {
            return;
        }
        orderInfoDaoMapper.updateOrderAddedIssue(orderInfoBO);
        //统计出票成功的
        orderInfoDaoMapper.updateOrderAddMoney(orderAddCode);
    }

    /**
     * 修改订单详情
     *
     * @param ticketInfoBOs
     * @author jiangwei
     * @Version 1.0
     * @CreatDate 2017年9月7日 下午3:27:17
     */
    private void updateOrderDetail(List<TicketInfoBO> ticketInfoBOs) {
        Map<String, String> orderDetailPreBonusMap = new HashMap<>();
        List<Integer> ids = new ArrayList<>();
        for (TicketInfoBO ticketInfoBO : ticketInfoBOs) {
            if (!ids.contains(ticketInfoBO.getOrderDetailId())) {
                ids.add(ticketInfoBO.getOrderDetailId());
            }
            if (ticketInfoBO.getPreBonus() != null && ticketInfoBO.getOrderDetailId() != null && ticketInfoBO.getPreBonus() > 0) {
                StringBuilder sb = new StringBuilder("");
                BigDecimal orderDetailPreBonus = BigDecimal.ZERO;
                BigDecimal orderDetailAfterBonus = BigDecimal.ZERO;
                String windetail = null;
                String key = null;
                if (AwardUtil.isSport(ticketInfoBO.getLotteryCode())) {
                    key = ticketInfoBO.getOrderDetailId() + SymbolConstants.COMMA + "0";
                } else {
                    key = ticketInfoBO.getOrderDetailId() + SymbolConstants.COMMA + "1";
                }
                if (orderDetailPreBonusMap.containsKey(key)) {
                    String a[] = orderDetailPreBonusMap.get(key).split(SymbolConstants.DOUBLE_SLASH + SymbolConstants.NUMBER_SIGN);
                    orderDetailPreBonus = BigDecimal.valueOf(Double.valueOf(a[0])).add(BigDecimal.valueOf(ticketInfoBO.getPreBonus()));
                    orderDetailAfterBonus = BigDecimal.valueOf(Double.valueOf(a[1])).add(BigDecimal.valueOf(ticketInfoBO.getAftBonus()));
                    windetail = a[2] + SymbolConstants.AT + ticketInfoBO.getWinningDetail() + SymbolConstants.UP_CAP + ticketInfoBO.getMultipleNum();
                    sb.append(orderDetailPreBonus + SymbolConstants.NUMBER_SIGN + orderDetailAfterBonus + SymbolConstants.NUMBER_SIGN + windetail);
                } else {
                    orderDetailPreBonus = BigDecimal.valueOf(ticketInfoBO.getPreBonus());
                    orderDetailAfterBonus = BigDecimal.valueOf(ticketInfoBO.getAftBonus());
                    windetail = ticketInfoBO.getWinningDetail() + SymbolConstants.UP_CAP + ticketInfoBO.getMultipleNum();
                    sb.append(orderDetailPreBonus + SymbolConstants.NUMBER_SIGN + orderDetailAfterBonus + SymbolConstants.NUMBER_SIGN + windetail);
                }
                String abc = sb.toString();
                if (abc.length() == 0 || ObjectUtil.isBlank(abc)) {
                    continue;
                }
                orderDetailPreBonusMap.put(key, abc);
            }
        }
        orderInfoDaoMapper.resetOrderDetail(ids);
        if (!orderDetailPreBonusMap.isEmpty()) {
            orderInfoDaoMapper.updatePreBonusByOrderDetailForMap(orderDetailPreBonusMap);
        }
    }

    /**
     * @param lotteryCode
     * @param lotteryIssue 足球：赛事编号，其他彩种：彩期
     * @author wuLong
     * @date 2017年5月26日 下午6:34:05
     */
    private void checkLottery(Integer lotteryCode, String lotteryIssue, Short lotIssueSaleStatus) {
        if (AwardUtil.isSport(lotteryCode)) {
        } else {
            // 数字彩验证或老足彩
            LotteryIssueBO issue = orderInfoDaoMapper.getLotteryIssue(lotteryCode, lotteryIssue, lotIssueSaleStatus);
            if (issue == null) {
                throw new ServiceRuntimeException(
                        "彩期状态必须是" + LotIssueSaleStatus.getLotIssueSaleStatus(lotIssueSaleStatus).getDesc());
            }
            if (StringUtils.isBlank(issue.getDrawCode())) {
                throw new ServiceRuntimeException("开奖错误，开奖号码不能为空");
            }
            if (!DrawCodeUtil.isCorrect(issue.getDrawCode(), lotteryCode)) {
                throw new ServiceRuntimeException("开奖错误，开奖号码错误");
            }
            if (StringUtils.isBlank(issue.getDrawDetail())) {
                throw new ServiceRuntimeException("开奖错误，开奖详情不能为空");
            }
            logger.info("彩期开奖信息{彩种：" + lotteryCode + "，彩期：" + lotteryIssue + "，开奖号码：" + issue.getDrawCode() + "，开奖详情："
                    + issue.getDrawDetail() + "}");
        }
    }

    @Override
    public LotteryIssueBO getLotteryIssue(Integer lotteryCode, String lotteryIssue) {
        return orderInfoDaoMapper.getLotteryIssue(lotteryCode, lotteryIssue, null);
    }

    @Override
    public void updateAccomplishDraw(int lotteryCode, String lotteryIssue) {
        if (AwardUtil.isSport(lotteryCode)) {
            orderInfoDaoMapper.updateSportAgnistInfoByMatchStatus(lotteryCode);
        } else {
            // 查询是否存在未开奖的订单
            int num = ticketInfoDaoMapper.countTicketNotDraw(lotteryCode, lotteryIssue);
            if (num == 0) {
                orderInfoDaoMapper.updateLotteryIssueStatus(lotteryCode, lotteryIssue,
                        LotIssueSaleStatus.OPEN_AWARD.getValue());
                sendMessage.sendUpdateNotice(lotteryCode, 2);
            } else {
                logger.info("彩种:" + lotteryCode + ",彩期:" + lotteryIssue + ",还存在未开完奖的订单，不能修改为彩期为已开奖状态");
            }
            sendMessage.sendChaseOrder(lotteryCode, lotteryIssue);
        }
    }

    @Override
    public void updateAccomplishPay(Integer lotteryCode, String lotteryIssue) {
        if (AwardUtil.isSport(lotteryCode)) {
            // 查询彩种所有已开奖的并且没有未派奖订单的赛事主键id
            List<Integer> ids = orderInfoDaoMapper.getPayoutMatchId(lotteryCode);
            if (CollectionUtils.isNotEmpty(ids)) {
                orderInfoDaoMapper.updateSportAgnistInfoPayout(ids);
            }
        } else {
            // 根据彩期菜彩种查询是否改期还有未派奖的订单
            int count = orderInfoDaoMapper.getPayoutAwardOrderCount(lotteryCode, lotteryIssue);
            if (count == 0) {
                orderInfoDaoMapper.updateLotteryIssueStatus(lotteryCode, lotteryIssue,
                        LotIssueSaleStatus.SEND_PRIZE.getValue());
            }
        }
    }

    @Override
    public Collection<LotteryOrderBO> getLotteryOrderDraw(List<String> orders, int type) {
        if (CollectionUtils.isEmpty(orders)) {
            return new ArrayList<>();
        }
        List<Short> status = getDrawOrderStatus(type);
        //过滤掉跟单的订单编号
        orders = orderInfoDaoMapper.getByOrderType(orders, Arrays.asList(1, 2));
        if (orders.isEmpty()) {
            throw new ServiceRuntimeException("订单不存在或这些订单都是跟单的订单不能做处理");
        }
        // 调用重载的获取订单集合对象
        List<String> list = orderInfoDaoMapper.getOrderWinStatus(orders, status);
        if (list.size() != orders.size()) {
            StringBuffer sb = new StringBuffer();
            for (String order : orders) {
                if (!list.contains(order)) {
                    if (sb.length() > 0) {
                        sb.append(",");
                    }
                    sb.append(order);
                }
            }
            sb.append("订单中奖状态错误！不能执行");
            sb.append(type == 1 ? "重置开奖" : "开奖");
            throw new ServiceRuntimeException(sb.toString());
        }
        return getLotteryOrderInfo(orders);
    }

    /**
     * 获取需要开奖订单状态
     *
     * @param type
     * @return
     * @author jiangwei
     * @Version 1.0
     * @CreatDate 2017年9月8日 下午3:41:53
     */
    private List<Short> getDrawOrderStatus(int type) {
        List<Short> status = new ArrayList<>();
        if (type == 1) {
            // 重置开奖
            status.add(OrderWinningStatus.NOT_WINNING.getValue());
            status.add(OrderWinningStatus.WINNING.getValue());
            status.add(OrderWinningStatus.GET_WINNING.getValue());
        }
        status.add(OrderWinningStatus.NOT_DRAW_WINNING.getValue());
        return status;
    }

    @Override
    public Collection<LotteryOrderBO> getLotteryOrderInfo(List<String> orders) {
        List<OrderInfoBO> list = orderInfoDaoMapper.listOrderInfo(orders);
        Map<String, LotteryOrderBO> result = new HashMap<>();
        for (OrderInfoBO orderInfoBO : list) {
            String key = orderInfoBO.getLotteryCode() + orderInfoBO.getLotteryIssue();
            if (!result.containsKey(key)) {
                LotteryOrderBO bo = new LotteryOrderBO(orderInfoBO.getLotteryCode(), orderInfoBO.getLotteryIssue());
                result.put(key, bo);
            }
            result.get(key).getOrders().add(orderInfoBO.getOrderCode());
        }
        return result.values();
    }

    @Override
    public List<String> listIssuePayoutAwardOrders(Integer lotteryCode, String lotteryIssue) {
        checkLottery(lotteryCode, lotteryIssue, LotIssueSaleStatus.OPEN_AWARD.getValue());
        return orderInfoDaoMapper.getWinOrderCodes(lotteryCode, lotteryIssue);
    }

    @Override
    public List<String> listIssueAwardOrders(Integer lotteryCode, String lotteryIssue, int type) {
        if (type == 1) {
            orderInfoDaoMapper.updateLotteryIssueStatus(lotteryCode, lotteryIssue, LotIssueSaleStatus.APPROVED.getValue());
        }
        checkLottery(lotteryCode, lotteryIssue, LotIssueSaleStatus.APPROVED.getValue());
        return orderInfoDaoMapper.getDrawOrderCodes(lotteryCode, lotteryIssue, getDrawOrderStatus(type));
    }

    @Override
    public List<String> listSportPayoutAwardOrders(Integer lotteryCode) {
    	List<String> issue = null;
    	if(lotteryCode == Lottery.FB.getName() || lotteryCode == Lottery.BB.getName()){
    		issue= getIssue();
    	}
        return orderInfoDaoMapper.getOrderInfosWinByMaxBuyScreen(lotteryCode,issue);
    }
    
    private List<String> getIssue() {
		Date now = new Date();
		List<String> issue = new ArrayList<>();
		for (int i = 0; i > -10; i--) {
			issue.add(DateUtil.convertDateToStr(DateUtil.addDay(now, i), DateUtil.FORMAT_YYMMDD));
		}
		return issue;
	}

    /**
     * 根据彩种彩期查询开奖订单集合
     *
     * @param lotteryCode
     * @param lotteryIssue
     * @return
     * @date 2017年6月19日下午5:11:07
     * @author cheng.chen
     */
    @Override
    public List<String> getOrderInfosByGyJc(Integer lotteryCode, String lotteryIssue) {
        return orderInfoDaoMapper.getOrderInfosByGyJc(lotteryCode, lotteryIssue);
    }

    @Override
    public List<String> listMatchAwardOrders(Integer lotteryCode, String match, int type) {
        List<String> orders = orderInfoDaoMapper.getOrderInfosByMaxBuyScreen(lotteryCode);
        if (type == 1) {
            List<String> orderCodes = getOrderCodesByParam(match, null, lotteryCode);
            orders.addAll(orderCodes);
        }
        if (!CollectionUtils.isEmpty(orders)) {
            orders = orderInfoDaoMapper.getByOrderType(orders, Arrays.asList(1, 2));
        }
        return orders;
    }

    @Override
    public List<String> getOrderCodesByParam(String match, Integer lotteryChildCode, Integer lotteryCode) {
        return orderInfoDaoMapper.getContainSystemCode(match, lotteryChildCode, lotteryCode);
    }

    @Override
    public void updateOrderMaxBuyScreen(String maxBuyScreen, String orderCode) {
        orderInfoDaoMapper.updateOrderMaxBuyScreen(maxBuyScreen, orderCode);
    }

    @Override
    public int updateOrderWinStatus(List<String> orderCodes) {
        return orderInfoDaoMapper.updateOrderWinStatus(orderCodes);
    }

    @Override
    public void checkPayout(List<String> orders) {
        List<String> list = orderInfoDaoMapper.listCanPayout(orders);
        if (list.size() != orders.size()) {
            StringBuilder sb = new StringBuilder("派奖失败，订单状态必须：已中奖，已支付，已出票。错误订单:");
            for (String order : orders) {
                if (!list.contains(order)) {
                    sb.append(order);
                    sb.append(",");
                }
            }
            throw new ServiceRuntimeException(sb.toString());
        }
    }

    @Override
    public void updateLotteryIssueAutoDraw(int lotteryCode, String lotteryIssue) {
        if (lotteryCode < 200 || lotteryCode >= 300) {
            throw new ServiceRuntimeException(
                    "自动开奖失败（必须是高频彩）：lotteryCode:" + lotteryCode + "|lotteryIssue:" + lotteryIssue);
        }
        // 验证彩种是否配置自动开奖
        LotteryTypePO po = orderInfoDaoMapper.getLotteryType(lotteryCode);
        if (po == null || po.getAutoType() == null || po.getAutoType() != 2) {
            throw new ServiceRuntimeException("自动开奖失败（该彩种没有配置自动开奖）：lotteryCode:" + lotteryCode + "|lotteryIssue:" + lotteryIssue);
        }
        LotteryIssueBO issue = orderInfoDaoMapper.getLotteryIssue(lotteryCode, lotteryIssue,
                LotIssueSaleStatus.STOP_SALE.getValue());
        if (issue == null) {
            throw new ServiceRuntimeException(
                    "自动开奖失败（彩期状态必须是销售截止或已开奖）：lotteryCode:" + lotteryCode + "|lotteryIssue:" + lotteryIssue);
        }
        if (StringUtils.isBlank(issue.getDrawCode())) {
            throw new ServiceRuntimeException(
                    "自动开奖失败（不存在开奖号码）：lotteryCode:" + lotteryCode + "|lotteryIssue:" + lotteryIssue);
        }
        if (!DrawCodeUtil.isCorrect(issue.getDrawCode(), lotteryCode)) {
            throw new ServiceRuntimeException(
                    "自动开奖失败（开奖号码格式错误）：lotteryCode:" + lotteryCode + "|lotteryIssue:" + lotteryIssue);
        }
        orderInfoDaoMapper.updateLotteryIssueStatus(lotteryCode, lotteryIssue, LotIssueSaleStatus.APPROVED.getValue());
    }

    @Override
    public void updateOrderDrawFail(List<String> fialOrders) {
        List<String> orders = new ArrayList<>(fialOrders);
        List<String> getWinningOrders = orderInfoDaoMapper.getOrderWinStatus(orders, Arrays.asList(OrderWinningStatus.GET_WINNING.getValue()));
        for (String order : getWinningOrders) {
            orders.remove(order);
        }
        if (orders.isEmpty()) {
            return;
        }
        orderInfoDaoMapper.updateOrderRestart(orders);
        ticketInfoDaoMapper.updateTicketRestart(orders);
    }

    @Override
    public List<String> getByOrderType(List<String> orders, List<Integer> orderTypes) {
        return orderInfoDaoMapper.getByOrderType(orders, orderTypes);
    }

    @Override
    public List<String> getFollowOrderInfo(String orderCode) {
        return orderInfoDaoMapper.getFollowOrderInfo(orderCode);
    }

    @Override
    public int updateCopyOrderWinStatus(String orderCode) {
        return orderInfoDaoMapper.updateCopyOrderWinStatus(orderCode);
    }

    @Override
    public List<OrderDetailBO> getOrderDetailBO(String orderCode) {
        return orderInfoDaoMapper.findOrderDetail(orderCode);
    }

    @Override
    public void updateOrderInfoBonus(Double officialBonus, Double localBonus, String redCode, long id) {
        orderInfoDaoMapper.updateOrderInfoBonus(officialBonus, localBonus, redCode, id);
    }

    @Override
    public List<IssueContentInfoBO> getIssueContentInfos(Integer lotteryCode, Integer orderStatus, Integer lotteryChild,
                                                         String cancelSystemCode) {

        List<IssueContentInfoBO> contentInfoBOs = issueContentInfoMapper.getIssueContentInfos(lotteryCode, lotteryChild, orderStatus, cancelSystemCode);
        return contentInfoBOs;
    }

    /**
     * @param bo
     * @Description: 合买派奖
     * @author liguisheng
     * @date 2018年5月8日
     */
    @Override
    public void updateOrderGroup(OrderInfoBO bo) {
        if (ObjectUtil.isBlank(bo)) {
            return;
        }

        if (!ObjectUtil.isBlank(bo.getWinningStatus()) && (bo.getWinningStatus().shortValue() == OrderWinningStatus.WINNING.getValue() || bo.getWinningStatus().shortValue() == OrderWinningStatus.GET_WINNING.getValue())) {
            OrderGroupPO orderGroupPO = orderGroupDaoMapper.selectByOrderCode(bo.getOrderCode());
            if (orderGroupPO.getGrpbuyStatus() != 2) {//满员才算奖金
                return;
            }
            double orderGroupMoney = orderGroupContentDaoMapper.selectBuyAmount(bo.getOrderCode());

            if (orderGroupMoney != bo.getOrderAmount().doubleValue()) {
                JSONObject messageJson = new JSONObject();
                messageJson.put("orderCode", bo.getOrderCode());
                messageJson.put("type", 3);
                sendMessage.sendOrderGroupCount(messageJson);
                throw new ServiceRuntimeException("合买金额与订单金额不匹配暂不开奖");
            }

            //提成佣金比例 数据库存百分比 需要除100 保留2位小数
            Double commissionRatio = MatchUtil.div(orderGroupPO.getCommissionRatio(), 100, 2);

            //合买进度 + 保底进度 小于90%不更新
            double progress = MatchUtil.sum(orderGroupPO.getProgress(), orderGroupPO.getGuaranteeRatio());
            if (progress < 90) {
                return;
            }

            List<OrderGroupContentPO> list = orderGroupContentDaoMapper.selectByOrderCode(bo.getOrderCode());

            Double orderAmount = bo.getOrderAmount();
            Double aftBonus = bo.getAftBonus();
            Double commissionAmountTotal = 0d;
            if (bo.getWinningStatus().equals(OrderWinningStatus.WINNING.getValue()) || bo.getWinningStatus().equals(OrderWinningStatus.GET_WINNING.getValue())) {
                double profit = MatchUtil.sub(aftBonus, orderAmount);

                OrderGroupPO updatePO = new OrderGroupPO();
                updatePO.setId(orderGroupPO.getId());
                if (!ObjectUtil.isBlank(commissionRatio) && aftBonus.compareTo(orderAmount) == 1) {
                    commissionAmountTotal = MatchUtil.mul(profit, commissionRatio, 2, BigDecimal.ROUND_DOWN);
                    //总佣金 =  盈利 * 佣金比例
                    updatePO.setCommissionAmount(commissionAmountTotal);
                }
                updatePO.setModifyBy("lotto-award");
                orderGroupDaoMapper.updateCommissionAmount(updatePO);

                //实际分成金额 = 盈利 - 佣金
                double profi = MatchUtil.sub(aftBonus, commissionAmountTotal);
                double total = 0;
                double aftTotal = 0;
                for (int i = 0; list != null && i < list.size(); i++) {
                    OrderGroupContentPO v = list.get(i);
                    double commissionAmount = 0;
                    double sendBonus = 0;
                    //税后奖金 = 1/订单金额*中奖金额*购买金额
                    v.setAftBonus(MatchUtil.mul(MatchUtil.mul(MatchUtil.div(1d,orderAmount,55),aftBonus),v.getBuyAmount(),2,BigDecimal.ROUND_DOWN));
                    //如果大于方案金额 需要计算佣金
                    if (aftBonus.compareTo(orderAmount) == 1) {
                        if(i < list.size()-1) {
                            //派发奖金 = 1/订单金额*(中奖金额-佣金)*购买金额
                            sendBonus = MatchUtil.mul(MatchUtil.mul(MatchUtil.div(1d,orderAmount,55),profi),v.getBuyAmount(),2,BigDecimal.ROUND_DOWN);
                            v.setCommissionAmount(MatchUtil.sub(v.getAftBonus(), sendBonus));
                        }else{
                            //最后一个人 是发起人 发起人奖金 =  中奖金额-佣金-其他合买人总派奖金额
                            sendBonus = MatchUtil.sub(profi,total);
                            v.setAftBonus(MatchUtil.sub(aftBonus, aftTotal));
                        }
                    } else {
                        if(i < list.size()-1) {
                            v.setCommissionAmount(commissionAmount);
                            sendBonus = v.getAftBonus();
                        }else{
                            //最后一个人 是发起人 发起人奖金 =  中奖金额-佣金-其他合买人总派奖金额
                            sendBonus = MatchUtil.sub(aftBonus,total);
                            v.setAftBonus(MatchUtil.sub(aftBonus, aftTotal));
                        }
                    }
                    if(sendBonus < 0.01){
                        sendBonus = 0;
                    }
                    v.setSendBonus(sendBonus);
                    total = MatchUtil.sum(sendBonus,total);
                    aftTotal = MatchUtil.sum(v.getAftBonus(), aftTotal);
                }
                orderGroupContentDaoMapper.updatePer(list);
            }

            JSONObject messageJson = new JSONObject();
            messageJson.put("userId", bo.getUserId());
            messageJson.put("lotteryCode", bo.getLotteryCode());
            messageJson.put("orderCode", bo.getOrderCode());
            messageJson.put("orderAmount", bo.getOrderAmount());
            messageJson.put("winAmount", bo.getAftBonus());
            messageJson.put("commissionAmount", commissionAmountTotal);
            messageJson.put("type", 2);
            messageJson.put("winStatus", 1);
            sendMessage.sendOrderGroupCount(messageJson);
        } else {
            JSONObject messageJson = new JSONObject();
            messageJson.put("userId", bo.getUserId());
            messageJson.put("lotteryCode", bo.getLotteryCode());
            messageJson.put("orderCode", bo.getOrderCode());
            messageJson.put("orderAmount", bo.getOrderAmount());
            messageJson.put("winAmount", bo.getAftBonus());
            messageJson.put("type", 2);
            messageJson.put("winStatus", 0);
            sendMessage.sendOrderGroupCount(messageJson);
        }
    }

	@Override
	public List<Date> getOrderInfoBuyTime(List<String> orderCodes) {
		return orderInfoDaoMapper.getOrderInfoBuyTime(orderCodes);
	}

    /**
     * @param list
     * @Description: 合买加奖
     * @author liguisheng
     * @date 2018年5月8日
     */
    @Override
    public int updateOrderGroupAddBonus(List<Map> list) {
        return orderGroupContentDaoMapper.updateAddBonus(list);
    }


    /**
     * @param po
     * @Description: 更新合买加奖类别
     * @author liguisheng
     * @date 2018年7月20日
     */
    @Override
    public int updateOrderGroupAddBonusType(OrderGroupPO po) {
        return orderGroupDaoMapper.updateAddBonusFlag(po);
    }
}
