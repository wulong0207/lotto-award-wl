package com.hhly.award.persistence.dao;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.SelectProvider;

import com.hhly.award.bo.LotteryIssueBO;
import com.hhly.award.bo.OrderDetailBO;
import com.hhly.award.bo.OrderInfoBO;
import com.hhly.award.persistence.po.LotteryTypePO;

/**
 * @desc
 * @author jiangwei
 * @date 2017年5月26日
 * @company 益彩网络科技公司
 * @version 1.0
 */
public interface OrderInfoDaoMapper {
	/**
	 * 
	 * @Description: 根据订单编号查询订单信息表
	 * @param orderCode 订单编号
	 * @return
	 * @author wuLong
	 * @date 2017年3月27日 下午4:22:07
	 */
	List<OrderInfoBO> getOrderInfoS(@Param("orderCodes")List<String> orderCodes);
	/**
	 * 
	 * @Description: 匹配订单表中的  最大购买场次编号
	 * @param lotteryCode 彩种ID
	 * @return  List<OrderInfoBO>
	 * @author wuLong
	 * @date 2017年5月27日 上午9:20:11
	 */
	List<String> getOrderInfosByMaxBuyScreen(@Param("lotteryCode")Integer lotteryCode);


	/**
	 * @param lotteryCode 彩种ID
	 * @return List<OrderInfoBO>
	 * @Description: 匹配订单表中的冠亚军订单编号
	 * @author wuLong
	 * @date 2017年5月27日 上午9:20:11
	 */
	List<String> getOrderInfosByGyJc(@Param("lotteryCode") Integer lotteryCode, @Param("issueCode") String issueCode);

	/**
	 * 匹配订单表中最大购买场次编号并且赛事状态为已开奖
	 * @param lotteryCode
	 * @param winStatus
	 * @return
	 * @date 2017年7月4日下午4:48:23
	 * @author cheng.chen
	 */
	List<String> getOrderInfosWinByMaxBuyScreen(@Param("lotteryCode")Integer lotteryCode,@Param("issues")List<String> issues);
	/**
	 * 
	 * @param vo
	 * @return
	 */
	List<OrderDetailBO> findOrderDetail(@Param("orderCode")String orderCode);
	/**
	 * 
	 * @Description: 修改订单信息 
	 * @param orderInfoBO
	 * @return
	 * @author wuLong
	 * @date 2017年5月26日 下午4:52:04
	 */
	int updateOrderInfo(OrderInfoBO orderInfoBO);
    /**
     * 获取彩种开奖号码
     * @author jiangwei
     * @Version 1.0
     * @CreatDate 2017年5月25日 上午11:36:21
     * @param lotteryCode
     * @param lotteryIssue
     * @return
     */
	LotteryIssueBO getLotteryIssue(@Param("lotteryCode")Integer lotteryCode,@Param("lotteryIssue")String lotteryIssue,@Param("status")Short status);
	/**
	 * 修改彩期状态
	 * @author jiangwei
	 * @Version 1.0
	 * @CreatDate 2017年5月27日 下午12:01:01
	 * @param lotteryCode 彩种
	 * @param lotteryIssue 彩期
	 * @param status 状态
	 */
	int updateLotteryIssueStatus(@Param("lotteryCode")int lotteryCode, @Param("lotteryIssue")String lotteryIssue,@Param("status")short status);
	/**
	 * 获取需要开奖的订单好
	 * 根据彩种彩期
	 * @author jiangwei
	 * @Version 1.0
	 * @CreatDate 2017年5月27日 下午2:28:10
	 * @param lotteryCode 彩种编号
	 * @param lotteryIssue 彩期
	 * @param winStatus 中奖状态
	 * @return
	 */
	List<String> getDrawOrderCodes(@Param("lotteryCode")Integer lotteryCode, @Param("lotteryIssue")String lotteryIssue,@Param("status")List<Short> statu);
	
	/**
	 * 
	 * @param lotteryCode
	 * @param lotteryIssue
	 * @return
	 * @date 2017年7月5日下午12:03:58
	 * @author cheng.chen
	 */
	List<String> getWinOrderCodes(@Param("lotteryCode")Integer lotteryCode, @Param("lotteryIssue")String lotteryIssue);	
	/**
	 * 修改订单状态
	 * @author jiangwei
	 * @Version 1.0
	 * @CreatDate 2017年5月30日 上午9:31:57
	 * @param lotteryCode
	 * @param lotteryIssue
	 */
	void updateOrderRestart(@Param("orders")List<String> orders);
	
	/**
	 * 
	 * @Description: 查询所有包含这场赛事并且状态为 未中奖或已中奖的 订单  
	 * @param systemCode 赛事编号
	 * @param lotteryChildCode 子彩种id
	 * @param lotteryCode 彩种id
	 * @return List<String>
	 * @author wuLong
	 * @date 2017年6月15日 上午10:53:57
	 */
	List<String> getContainSystemCode(@Param("systemCode") String systemCode,@Param("lotteryChildCode")Integer lotteryChildCode,@Param("lotteryCode")Integer lotteryCode);
	/**
	 * 获取订单信息
	 * @author jiangwei
	 * @Version 1.0
	 * @CreatDate 2017年6月14日 上午11:40:23
	 * @param orders
	 * @return
	 */
	List<OrderInfoBO> listOrderInfo(@Param("orders")List<String> orders);
	/**
	 * 
	 * @Description: 修改订单最大购买场次编号
	 * @param maxBuyScreen
	 * @param orderCode
	 * @author wuLong
	 * @date 2017年6月22日 下午5:28:33
	 */
	void updateOrderMaxBuyScreen(@Param("maxBuyScreen")String maxBuyScreen, @Param("orderCode")String orderCode);
	
	/**
	 * 修改订单派奖状态
	 * @param orderCode
	 * @return
	 * @date 2017年6月21日下午5:58:36
	 * @author cheng.chen
	 */
	int updateOrderWinStatus(@Param("orderCodes")List<String> orderCodes);
	/**
	 * 
	 * @Description: 跟新跟单的订单为派奖状态
	 * @param orderCode 推单订单编号
	 * @return
	 * @author wuLong
	 * @date 2017年11月20日 下午3:38:58
	 */
	int updateCopyOrderWinStatus(@Param("orderCode")String orderCode);

	/**
	 * 查询订单开奖状态订单数
	 * @param lotteryCode
	 * @param lotteryIssue
	 * @return
	 * @date 2017年6月23日上午9:46:29
	 * @author cheng.chen
	 */
	int getPayoutAwardOrderCount(@Param("lotteryCode")Integer lotteryCode,@Param("lotteryIssue")String lotteryIssue);
	
	/**
	 * 
	 * @Description: 批量更新对应的订单详情中奖的税前金额
	 * @param orderDetailPreBonusMap
	 * @return
	 * @author wuLong
	 * @date 2017年7月13日 上午11:44:03
	 */
	@SelectProvider(type = OrderInfoDaoProvider.class,method="updatePreBonusByOrderDetailForMap")
    void updatePreBonusByOrderDetailForMap(Map<String,String> orderDetailPreBonusMap);
	/**
	 * 
	 * @Description: 修改状态为已审核的改为已开奖 
	 * @param lotteryCode
	 * @return
	 * @author wuLong
	 * @date 2017年7月20日 上午10:15:25
	 */
	void updateSportAgnistInfoByMatchStatus(Integer lotteryCode);
	/**
	 * 查询能够进行派奖的订单
	 * @author jiangwei
	 * @Version 1.0
	 * @CreatDate 2017年7月22日 上午9:44:32
	 * @param orders
	 * @return
	 */
	List<String> listCanPayout(@Param("orders")List<String> orders);
	/**
	 * 获取已经开奖完成赛事主键ID
	 * @author jiangwei
	 * @Version 1.0
	 * @CreatDate 2017年7月27日 下午2:13:30
	 * @param lotteryCode
	 * @return
	 */
	List<Integer> getPayoutMatchId(@Param("lotteryCode")Integer lotteryCode);
	/**
	 * 修改赛事状态已派奖
	 * @author jiangwei
	 * @Version 1.0
	 * @CreatDate 2017年7月27日 下午2:18:29
	 * @param ids
	 */
	void updateSportAgnistInfoPayout(@Param("ids")List<Integer> ids);
	/**
	 * 获取彩种信息
	 * @author jiangwei
	 * @Version 1.0
	 * @CreatDate 2017年8月15日 上午9:08:54
	 * @param lotteryCode
	 * @return
	 */
	LotteryTypePO getLotteryType(@Param("lotteryCode")int lotteryCode);
	/**
	 * 检查订单状态
	 * @author jiangwei
	 * @Version 1.0
	 * @CreatDate 2017年9月8日 下午3:51:22
	 * @param orders
	 * @param status
	 * @return
	 */
	List<String> getOrderWinStatus(@Param("order")List<String> orders,@Param("status")List<Short> status);
	/**
	 * 修改追号计划订单表信息
	 * @author jiangwei
	 * @Version 1.0
	 * @CreatDate 2017年9月12日 下午3:11:31
	 * @param orderInfoBO
	 */
	void updateOrderAddedIssue(OrderInfoBO orderInfoBO);
	/**
	 * 更新追号计划订单中奖金额
	 * @author jiangwei
	 * @Version 1.0
	 * @CreatDate 2017年9月12日 下午3:30:54
	 * @param orderAddCode
	 */
	void updateOrderAddMoney(@Param("orderAddCode")String orderAddCode);
	/**
	 * @Description:检查抄单状态
	 * @param orders
	 * @param orderTypes
	 * @return
	 * @author wuLong
	 * @date 2017年10月12日 下午3:09:08
	 */
	List<String> getByOrderType(@Param("orders")List<String> orders,@Param("orderTypes")List<Integer> orderTypes);
	/**
	 * 
	 * @Description: 根据推单的订单编号，查询跟单的订单编号 
	 * @param orderCode
	 * @return
	 * @author wuLong
	 * @date 2017年10月12日 下午3:35:00
	 */
	List<String> getFollowOrderInfo(@Param("orderCode") String orderCode);
	/**
	 * @Description: 重置订单详情税前奖金、税后奖金、中奖详情设置为NULL 
	 * @param ids
	 * @author wuLong
	 * @date 2017年10月16日 上午9:01:32
	 */
	void resetOrderDetail(@Param("ids") List<Integer> ids);
	/**
	 * @Description: 批量更新订单详情
	 * @param orderDetails
	 * @author wuLong
	 * @date 2018年1月6日 上午10:35:27
	 */
	void updateOrderDetail(@Param("orderDetails") List<OrderDetailBO> orderDetails);
	/**
	 * @Description: 更新嘉奖
	 * @param officialBonus
	 * @param localBonus
	 * @param redCode
	 * @param id
	 * @author wuLong
	 * @date 2018年1月6日 上午10:53:00
	 */
	void updateOrderInfoBonus(@Param("officialBonus") Double officialBonus,
			@Param("localBonus") Double localBonus,@Param("redCode") String redCode,@Param("id") long id);
	/**
	 * 查询订单的购买时间
	 * @param orderCodes
	 * @author wul687 
	 * @date 2018-07-19
	 * @return
	 */
	List<Date> getOrderInfoBuyTime(@Param("orderCodes")List<String> orderCodes);

}