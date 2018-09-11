package com.hhly.award.service.award.lottery.sports;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.hhly.award.base.common.MatchStatusEnum;
import com.hhly.award.base.common.OrderEnum;
import com.hhly.award.base.common.SymbolConstants;
import com.hhly.award.base.exception.ServiceRuntimeException;
import com.hhly.award.bo.OrderInfoBO;
import com.hhly.award.bo.SportAgainstInfoBO;
import com.hhly.award.bo.TicketInfoBO;
import com.hhly.award.persistence.dao.OrderInfoDaoMapper;
import com.hhly.award.service.award.entity.Sports.BonusBO;
import com.hhly.award.service.award.lottery.AbstractSportAward;
import com.hhly.award.util.ObjectUtil;

/**
 * @author lgs on
 * @version 1.0
 * @desc 冠亚军开奖
 * @date 2018/3/21.
 * @company 益彩网络科技有限公司
 */
@Component
public class WinAward extends AbstractSportAward {

    @Autowired
    private OrderInfoDaoMapper orderInfoDaoMapper;

    /**
     * 开奖计算,对单个订单进行开奖计算
     *
     * @param orderInfoBO
     * @author jiangwei
     * @Version 1.0
     * @CreatDate 2017年5月25日 上午10:59:47
     */
    @Override
    protected void compute(OrderInfoBO orderInfoBO) {
        openPrize(orderInfoBO);
    }

    /**
     * 获取开奖号码
     *
     * @param orderInfoBOs
     * @author jiangwei
     * @Version 1.0
     * @CreatDate 2017年5月25日 上午11:27:24
     */
    @Override
    protected void getDrawCode(List<OrderInfoBO> orderInfoBOs) {
        super.findBySportAgainstInfoSystemCodes(orderInfoBOs);
    }


    /**
     * @param orderInfoBO
     * @throws ServiceRuntimeException
     * @throws NumberFormatException
     * @Description: 开奖
     * @author wuLong
     * @date 2017年10月12日 下午3:46:36
     */
    private void openPrize(OrderInfoBO orderInfoBO) throws ServiceRuntimeException, NumberFormatException {
        List<String> systemCodes = systemCodeMap.get(orderInfoBO.getLotteryCode());
        if (!systemCodes.contains(orderInfoBO.getMaxBuyScreen())) {
            throw new ServiceRuntimeException("最大购买场次编号:" + orderInfoBO.getMaxBuyScreen() + ",没有开赛结果");
        }
        String buyScreen = orderInfoBO.getBuyScreen();
        String[] a = buyScreen.split(SymbolConstants.COMMA);
        for (String b : a) {
            if (!systemCodes.contains(b)) {
                throw new ServiceRuntimeException("订单中的该场:" + b + ",赛事没有开赛结果");
            }
        }
        List<TicketInfoBO> ticketInfoBOs = orderInfoBO.getTicketInfoBOs();
        int lotteryCode = orderInfoBO.getLotteryCode();
        Map<String, Object> map = super.parseSportAgainstInfo(lotteryCode);
        stepOne(orderInfoBO, ticketInfoBOs, map);
    }


    /**
     * @param orderInfoBO
     * @param ticketInfoBOs
     * @param map
     * @throws ServiceRuntimeException
     * @throws NumberFormatException
     * @Description: 开奖兑奖
     * @author wuLong
     * @date 2017年6月5日 上午11:04:12
     */
    public void stepOne(OrderInfoBO orderInfoBO, List<TicketInfoBO> ticketInfoBOs, Map<String, Object> map)
            throws ServiceRuntimeException, NumberFormatException {
        short winStatus = NOT_WIN;
        BigDecimal preWinBonus = BigDecimal.ZERO;
        BigDecimal afterWinBonus = BigDecimal.ZERO;
        Map<String, Integer> winPasswayZsMap = new HashMap<>();
        String orderCode = orderInfoBO.getOrderCode();
        OrderEnum.OrderStatus orderStatus = OrderEnum.OrderStatus.parseOrderStatus(orderInfoBO.getOrderStatus());
        String maxBuyScreen = orderInfoBO.getMaxBuyScreen();
        int lotteryCode = orderInfoBO.getLotteryCode();
        this.checkMaxBuyScreenSportAgainstInfo(maxBuyScreen, orderCode, lotteryCode);
        orderInfoBO.setAddedBonus(null);
        orderInfoBO.setAftBonus(null);
        orderInfoBO.setPreBonus(null);
        orderInfoBO.setWebsiteBonus(null);
        orderInfoBO.setWinningDetail(null);
        for (TicketInfoBO ticketInfoBO : ticketInfoBOs) {
            ticketInfoBO.setWinningStatus((short) 1);
            ticketInfoBO.setPreBonus(null);
            ticketInfoBO.setAftBonus(null);
            BonusBO bonus = new BonusBO();
            String ticketContent = ticketInfoBO.getTicketContent();
            String[] tc = ticketContent.split(SymbolConstants.DOUBLE_SLASH + SymbolConstants.UP_CAP);
            //内容
            String content = tc[0];
            //检查是有包含延期赛事
            this.checkSportAgainstInfo(content, orderCode, lotteryCode);
            //过关方式
            String passway = tc[1];
            //最低过关场次数
            int passNum = Integer.valueOf(passway.split(SymbolConstants.DOUBLE_SLASH + SymbolConstants.UNDERLINE)[0]);
            //出票sp 9.00-3.40A15.00-8.00
            String receiptContent = ticketInfoBO.getReceiptContent();
            List<String> hitSp = new ArrayList<>();
            if (orderStatus != OrderEnum.OrderStatus.TICKETED) {
                receiptContent = null;
            }

            if (receiptContent == null) {
                throw new ServiceRuntimeException("回执SP值不存在");
            }

            //赛事
            String buyScreen = ticketInfoBO.getBuyScreen();
            SportAgainstInfoBO sportAgainstInfoBO = (SportAgainstInfoBO) map.get(buyScreen);
            MatchStatusEnum.SportMatchStatusEnum matchStatusEnum = MatchStatusEnum.SportMatchStatusEnum.parsePayStatus(sportAgainstInfoBO.getMatchStatus());
            if (matchStatusEnum == MatchStatusEnum.SportMatchStatusEnum.REVIEW || matchStatusEnum == MatchStatusEnum.SportMatchStatusEnum.OPEN_AWARD ||
                    matchStatusEnum == MatchStatusEnum.SportMatchStatusEnum.SEND_AWARD) {
                hitSp.add(buyScreen);
            }
            if (!ObjectUtil.isBlank(hitSp) && hitSp.size() >= passNum) {
                this.calcPrize(bonus, ticketInfoBO, passNum, receiptContent, hitSp, winPasswayZsMap, passway.replace(SymbolConstants.UNDERLINE, "串"));
                winStatus = WIN;
                afterWinBonus = afterWinBonus.add(bonus.getAfterWinBonus());
                preWinBonus = preWinBonus.add(bonus.getPreWinBonus());
            } else {
                ticketInfoBO.setWinningStatus(NOT_WIN);
            }
        }
        if (winStatus == WIN) {
            orderInfoBO.setPreBonus(Double.valueOf(preWinBonus + ""));
            orderInfoBO.setAftBonus(Double.valueOf(afterWinBonus + ""));
            orderInfoBO.setWinningDetail(super.getWinningDetail(winPasswayZsMap));
            orderInfoBO.setAddedBonus(0d);
        }
        orderInfoBO.setWinningStatus(winStatus);
    }

    /**
     * 获取赛事编号为key投注SP值组成的Map
     *
     * @param content
     * @return
     */
    @SuppressWarnings("unused")
	private Map<String, String> getContentMap(String content) {
        Map<String, String> contentMap = new HashMap<>();
        String[] cts = content.split(SymbolConstants.DOUBLE_SLASH + SymbolConstants.VERTICAL_BAR);
        for (int i = 0, len = cts.length; i < len; i++) {
            String d = cts[i];
            String matchNo = null;
            if (d.contains(SymbolConstants.AT)) {
                matchNo = d.substring(0, d.indexOf(SymbolConstants.AT));
                contentMap.put(matchNo, d.substring(d.indexOf(SymbolConstants.AT) + 1));
            }
        }
        return contentMap;
    }

    public void checkMaxBuyScreenSportAgainstInfo(String maxBuyScreen, String orderCode, Integer lotteryCode) {
        SportAgainstInfoBO againstInfoBO = sportAginstInfoMap.get(maxBuyScreen + "," + lotteryCode);
        if (againstInfoBO == null) {
            throw new ServiceRuntimeException("赛事编号:" + maxBuyScreen + ",不存在");
        }
        MatchStatusEnum.SportMatchStatusEnum matchStatusEnum = MatchStatusEnum.SportMatchStatusEnum.parsePayStatus(againstInfoBO.getMatchStatus());
        if (matchStatusEnum != MatchStatusEnum.SportMatchStatusEnum.REVIEW &&
                matchStatusEnum != MatchStatusEnum.SportMatchStatusEnum.CANCLE_PAY &&
                matchStatusEnum != MatchStatusEnum.SportMatchStatusEnum.OPEN_AWARD &&
                matchStatusEnum != MatchStatusEnum.SportMatchStatusEnum.SEND_AWARD
                && matchStatusEnum != MatchStatusEnum.SportMatchStatusEnum.OVER) {
            throw new ServiceRuntimeException("订单:" + orderCode + "中的最大赛事:" + maxBuyScreen + ",赛事状态为" + matchStatusEnum.getDesc() + ",不能进行开奖操作");
        }
    }

    public void checkSportAgainstInfo(String content, String orderCode, Integer lotteryCode) throws ServiceRuntimeException {
        String maxBuyScreen = null;
        String errorMsg = null;

        Map<String, String> contentMap = new HashMap<>();
        String[] cts = content.split(SymbolConstants.DOUBLE_SLASH + SymbolConstants.VERTICAL_BAR);
        for (int i = 0, len = cts.length; i < len; i++) {
            String d = cts[i];
            String matchNo = null;
            if (d.contains(SymbolConstants.AT)) {
                matchNo = d.substring(0, d.indexOf(SymbolConstants.AT));
                contentMap.put(matchNo, d.substring(d.indexOf(SymbolConstants.AT) + 1));
            }
        }
        for (String key : contentMap.keySet()) {
            SportAgainstInfoBO againstInfoBO = sportAginstInfoMap.get(key + "," + lotteryCode);
            if (againstInfoBO == null) {
                throw new ServiceRuntimeException("赛事编号:" + key + ",不存在");
            }
            MatchStatusEnum.SportMatchStatusEnum matchStatusEnum = MatchStatusEnum.SportMatchStatusEnum.parsePayStatus(againstInfoBO.getMatchStatus());
            if (matchStatusEnum != MatchStatusEnum.SportMatchStatusEnum.REVIEW &&
                    matchStatusEnum != MatchStatusEnum.SportMatchStatusEnum.CANCLE_PAY &&
                    matchStatusEnum != MatchStatusEnum.SportMatchStatusEnum.OPEN_AWARD &&
                    matchStatusEnum != MatchStatusEnum.SportMatchStatusEnum.SEND_AWARD
                    &&
                    matchStatusEnum != MatchStatusEnum.SportMatchStatusEnum.OVER) {
                //9：销售中；10：暂停销售；11：销售截止；12：比赛进行中；13：延期;18 已淘汰
                if (matchStatusEnum == MatchStatusEnum.SportMatchStatusEnum.IN_SALE || matchStatusEnum == MatchStatusEnum.SportMatchStatusEnum.SUSPENDED_SALE
                        || matchStatusEnum == MatchStatusEnum.SportMatchStatusEnum.SALE_DEAD || matchStatusEnum == MatchStatusEnum.SportMatchStatusEnum.PAYING
                        || matchStatusEnum == MatchStatusEnum.SportMatchStatusEnum.DELAY_PAY) {
                    maxBuyScreen = key;
                }
                errorMsg = "订单:" + orderCode + "中的赛事:" + key + ",赛事状态为" + matchStatusEnum.getDesc() + ",不能进行开奖操作";
            }
        }
        if (errorMsg != null) {
            if (maxBuyScreen != null) {
                orderInfoDaoMapper.updateOrderMaxBuyScreen(maxBuyScreen, orderCode);
            }
            throw new ServiceRuntimeException(errorMsg);
        }
    }


    /**
     * @param ticketInfoBO    票表实体对象
     * @param passNum         过关场次数
     * @param hitSp           命中的场次的 赔率  结合
     * @param winPasswayZsMap 中奖的过关和注数
     * @return
     * @throws NumberFormatException
     * @Description: 计算奖金
     * @author wuLong
     * @date 2017年5月26日 下午4:24:18
     */
    private void calcPrize(BonusBO bonus, TicketInfoBO ticketInfoBO, int passNum, String receiptContent,
                           List<String> hitSp, Map<String, Integer> winPasswayZsMap, String passway) throws NumberFormatException {
        BigDecimal winAmount = BigDecimal.ZERO;
        boolean cgs = false;
        int zs = 1;

        BigDecimal oneBetBigDecimal = BigDecimal.valueOf(Double.valueOf(receiptContent));
        if (!cgs && oneBetBigDecimal.compareTo(TAX_UPPER_LIMIT_AMOUNT) >= 0) {
            cgs = true;
        }
        oneBetBigDecimal = oneBetBigDecimal.multiply(BigDecimal.valueOf(ticketInfoBO.getTicketMoney()));
        winAmount = winAmount.add(oneBetBigDecimal);
        double preBonus = Double.valueOf(winAmount + "");
        bonus.setPreWinBonus(winAmount);
        ticketInfoBO.setWinningStatus(WIN);
        ticketInfoBO.setPreBonus(preBonus);
        double afterBonus = preBonus;
        if (cgs) {
            afterBonus = preBonus - (preBonus * TAX_RATE);
        }
        bonus.setAfterWinBonus(BigDecimal.valueOf(afterBonus));
        ticketInfoBO.setAftBonus(afterBonus);
        ticketInfoBO.setAddedBonus(0d);
        ticketInfoBO.setWinningDetail(passway + SymbolConstants.UNDERLINE + zs + "注");
        if (!winPasswayZsMap.containsKey(passway)) {
            winPasswayZsMap.put(passway, zs);
        } else {
            winPasswayZsMap.put(passway, winPasswayZsMap.get(passway) + zs);
        }
    }

}
