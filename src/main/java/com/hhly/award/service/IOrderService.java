package com.hhly.award.service;

import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.hhly.award.bo.IssueContentInfoBO;
import com.hhly.award.bo.LotteryIssueBO;
import com.hhly.award.bo.LotteryOrderBO;
import com.hhly.award.bo.OrderDetailBO;
import com.hhly.award.bo.OrderInfoBO;
import com.hhly.award.persistence.po.OrderGroupPO;
import net.sf.json.JSONObject;

/**
 * @author wulong
 * @create 2017/5/9 14:13
 */
public interface IOrderService {
    /**
     *
     * @Description: 根据订单编号查询订单信息表
     * @param orderCodes 订单编号
     * @return
     * @author wuLong
     * @date 2017年3月27日 下午4:22:07
     */
    List<OrderInfoBO> getOrderInfos(List<String> orderCodes);
    /**
     * 修改订单开奖状态
     * @author jiangwei
     * @Version 1.0
     * @CreatDate 2017年5月25日 上午10:51:48
     * @param orderInfoBO
     */
	void updateOrderAward(OrderInfoBO orderInfoBO);
	/**
	 * 获取开奖号码
	 * @author jiangwei
	 * @Version 1.0
	 * @CreatDate 2017年5月25日 下午2:32:43
	 * @param lotteryCode
	 * @param lotteryIssue
	 * @return
	 */
	LotteryIssueBO getLotteryIssue(Integer lotteryCode, String lotteryIssue);
	/**
	 * 修改彩期
	 * @author jiangwei
	 * @Version 1.0
	 * @CreatDate 2017年5月27日 上午11:57:21
	 * @param lotteryCode
	 * @param lotteryIssue
	 */
	void updateAccomplishDraw(int lotteryCode, String lotteryIssue);
	/**
	 * 获取订单集合对象
	 * @param orders
	 * @return
	 * @date 2017年6月19日下午4:38:54
	 * @author cheng.chen
	 */
	Collection<LotteryOrderBO> getLotteryOrderInfo(List<String> orders);	
	/**
	 * 开奖订单信息获取
	 * @author jiangwei
	 * @Version 1.0
	 * @CreatDate 2017年6月14日 上午11:39:18
	 * @param orders
	 * @return
	 */
	Collection<LotteryOrderBO> getLotteryOrderDraw(List<String> orders,int type);
	
	/**
	 * 
	 * @param lotteryCode
	 * @param lotteryIssue
	 * @return
	 * @date 2017年6月19日下午5:10:57
	 * @author cheng.chen
	 */
	List<String> listIssuePayoutAwardOrders(Integer lotteryCode, String lotteryIssue);	
	/**
	 * 根据彩期获取开奖订单
	 * @author jiangwei
	 * @Version 1.0
	 * @CreatDate 2017年6月15日 上午8:59:17
	 * @param lotteryCode
	 * @param lotteryIssue
	 * @param type
	 * @return
	 */
	List<String> listIssueAwardOrders(Integer lotteryCode, String lotteryIssue, int type);

	/**
	 * 根据已审核场次开赛时间倒序查询订单最大场次的订单集合
	 * @param lotteryCode
	 * @param lotteryIssue
	 * @return
	 * @date 2017年6月19日下午5:11:07
	 * @author cheng.chen
	 */
	List<String> listSportPayoutAwardOrders(Integer lotteryCode);


    /**
     * 根据彩种彩期查询开奖订单集合
     *
     * @param lotteryCode
     * @param lotteryIssue
     * @return
     * @date 2017年6月19日下午5:11:07
     * @author cheng.chen
     */
    List<String> getOrderInfosByGyJc(Integer lotteryCode, String lotteryIssue);


	/**
	 * 根据比赛场次获取开奖订单号
	 * @author jiangwei
	 * @Version 1.0
	 * @CreatDate 2017年6月15日 上午8:59:17
	 * @param lotteryCode
	 * @param lotteryIssue
	 * @param type
	 * @return
	 */
	List<String> listMatchAwardOrders(Integer lotteryCode, String lotteryIssue, int type);
	/**
	 * 
	 * @Description: 查询符合重置的订单编号
	 * @param match 赛事编号
	 * @param lotteryChildCode 子彩种ID
	 * @param lotteryCode 彩种ID
	 * @return List<String>
	 * @author wuLong
	 * @date 2017年6月15日 上午11:36:53
	 */
	public List<String> getOrderCodesByParam(String match, Integer lotteryChildCode, Integer lotteryCode);
	/**
	 * 
	 * @Description:修改订单最大购买场次编号
	 * @param maxBuyScreen
	 * @param orderCode
	 * @author wuLong
	 * @date 2017年6月22日 下午5:27:38
	 */
	public void updateOrderMaxBuyScreen(String maxBuyScreen,String orderCode);
	
	/**
	 * 修改订单中奖状态
	 * @param orderCode
	 * @return
	 * @date 2017年6月21日下午6:11:56
	 * @author cheng.chen
	 */
	int updateOrderWinStatus(List<String> orderCodes);
	/**
	 * 
	 * @Description: 跟新跟单的订单为派奖状态
	 * @param orderCode 推单订单编号
	 * @return
	 * @author wuLong
	 * @date 2017年11月20日 下午3:38:58
	 */
	int updateCopyOrderWinStatus(String orderCode);
	/**
	 * 完成派奖
	 * @author jiangwei
	 * @Version 1.0
	 * @CreatDate 2017年7月22日 上午8:57:12
	 * @param lotteryCode
	 * @param lotteryIssue
	 */
	void updateAccomplishPay(Integer lotteryCode, String lotteryIssue);
	/**
	 * 检查订单状态，是否符合派奖（状态，已中奖，已支付，已出票）
	 * @author jiangwei
	 * @Version 1.0
	 * @CreatDate 2017年7月22日 上午9:41:58
	 * @param orders
	 */
	void checkPayout(List<String> orders);
	/**
	 * 修改彩种彩期状态（已审核）自动开奖
	 * @author jiangwei
	 * @Version 1.0
	 * @CreatDate 2017年8月14日 下午5:42:43
	 * @param lotteryCode
	 * @param lotteryIssue
	 */
	void updateLotteryIssueAutoDraw(int lotteryCode, String lotteryIssue);
	/**
	 * 修改开奖失败的订单为未开奖
	 * @author jiangwei
	 * @Version 1.0
	 * @CreatDate 2017年9月8日 下午6:26:17
	 * @param failOrder
	 */
	void updateOrderDrawFail(List<String> order);
	/**
	 * @Description:根据抄单状态返回订单编号  
	 * @param orders
	 * @param orderTypes
	 * @return
	 * @author wuLong
	 * @date 2017年10月12日 下午3:09:08
	 */
	List<String> getByOrderType(List<String> orders,List<Integer> orderTypes);
	/**
	 * 
	 * @Description: 根据推单的订单编号，查询跟单的订单编号 
	 * @param orderCode
	 * @return
	 * @author wuLong
	 * @date 2017年10月12日 下午3:35:00
	 */
	List<String> getFollowOrderInfo(String orderCode);
	/**
	 * 
	 * @Description: 获取订单的详情
	 * @param orderCode
	 * @return
	 * @author wuLong
	 * @date 2018年1月6日 上午10:08:00
	 */
	List<OrderDetailBO> getOrderDetailBO(String orderCode);
	
	/**
	 * @Description: 更新嘉奖
	 * @param officialBonus
	 * @param localBonus
	 * @param redCode
	 * @param id
	 * @author wuLong
	 * @date 2018年1月6日 上午10:53:00
	 */
	void updateOrderInfoBonus(Double officialBonus,
			Double localBonus,String redCode,long id);
	
	/**
	 * @Description: 查询推荐内容表
	 * @param lotteryCode 彩种ID
	 * @param orderStatus 开奖状态
	 * @param lotteryChild 子彩种ID
	 * @param cancelSystemCode 重置开奖的赛事编号
	 * @return List<IssueContentInfoBO>
	 * @author wuLong
	 * @date 2018年1月6日 下午4:01:00
	 */
	public List<IssueContentInfoBO> getIssueContentInfos(Integer lotteryCode,Integer orderStatus,Integer lotteryChild,String cancelSystemCode);


	/**
	 * @Description: 合买派奖
	 * @author liguisheng
	 * @date 2018年5月8日
	 */
	void updateOrderGroup(OrderInfoBO bo);

	/**
	 * 查询订单的购买时间
	 * @param orderCodes
	 * @author wul687
	 * @date 2018-07-19
	 * @return
	 */
	List<Date> getOrderInfoBuyTime(@Param("orderCodes")List<String> orderCodes);

	/**
	 * @Description: 合买加奖
	 * @author liguisheng
	 * @date 2018年7月20日
	 */
	int updateOrderGroupAddBonus(List<Map> list);


	/**
	 * @Description: 更新合买加奖类别
	 * @author liguisheng
	 * @date 2018年7月20日
	 */
	int updateOrderGroupAddBonusType(OrderGroupPO po);
}
