package com.hhly.award.service.award;

import java.util.Collection;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.hhly.award.base.common.LotteryEnum.Lottery;
import com.hhly.award.base.common.SymbolConstants;
import com.hhly.award.service.rabbitmq.produce.MQProducer;
import com.hhly.award.util.DateUtil;

import net.sf.json.JSONObject;
/**
 * @desc 发送消息
 * @author jiangwei
 * @date 2017年6月5日
 * @company 益彩网络科技公司
 * @version 1.0
 */

@Service
public class SendMessage {
	//消息列队
	private  static final String TICKET_EXCHANGE = "ticket";
	//短信消息列队
	private static  final String  MSG = "msg_queue";
	//开奖后通知生成追号订单
	private static final String CHASE_ORDER_QUEUE = "chase_order_create_afterdraw_queue";
	//派奖后发送活动通知
	private static final String ACTIVITY_CHANNEL_QUEUE = "activity_channel_queue";
	//抄单队列列名
	private static final String QUEUE_NAME_FOR_ORDER_COPY = "copy_order_queue";
	//添加报警信息
    private static  final String ALARM_INFO="alarm_info";
    //合买中奖统计队列列名
    private static final String ORDER_GROUP_RESULT_STATIS_QUEUE = "order_group_result_statis_queue";
    //已中奖
	private static final int WIN = 9;
	//未中奖
	private static final int NOT_WIN = 10;
	//派奖成功
	private static final int SUCC = 11;
	
	@Autowired
	private MQProducer producer;
	
	public void sendWin(List<String> orders){
		producer.sendDataToRouting(TICKET_EXCHANGE,"ticket.award.open.win",getMessageStr(orders, WIN));
		sendActivity(orders,2);
	}
	
	public void sendNotWin(List<String> orders){
		producer.sendDataToRouting(TICKET_EXCHANGE,"ticket.award.open.notwin",getMessageStr(orders, NOT_WIN));
		sendActivity(orders,2);
	}
	
	public void sendSucc(List<String> orders){
		producer.sendDataToRouting(TICKET_EXCHANGE,"ticket.award.send.success",getMessageStr(orders, SUCC));
	}
	/**
	 * 开奖失败
	 * @author jiangwei
	 * @Version 1.0
	 * @CreatDate 2017年8月9日 下午12:14:34
	 * @param list
	 * @param lotteryCode
	 * @param issueCode
	 */
	public  void sendDrawFail(List<String> list, int lotteryCode, String issueCode){
		String orders = "[超过5个订单]";
		if(list.size() <= 5){
			orders = list.toString();
		}
		String lotteryName = Lottery.getLottery(lotteryCode).getDesc();
		sendAlarmInfo(1,31,2,String.format("%s%s%s开奖失败", lotteryName,issueCode,orders), "lotto-award");
	}


    /**
	 * 重置开奖扣款失败
	 * @author jiangwei
	 * @Version 1.0
	 * @CreatDate 2017年9月8日 下午4:45:53
	 * @param order
	 * @param lotteryCode
	 * @param issueCode
	 */
	public  void deductMoneyFail(String order, int lotteryCode, String issueCode){
		String lotteryName = Lottery.getLottery(lotteryCode).getDesc();
		sendAlarmInfo(1,31,2,String.format("%s%s%s重置开奖失败(扣款失败)", lotteryName,issueCode,order), "lotto-award");
	}
	/**
	 * 派奖失败
	 * @author jiangwei
	 * @Version 1.0
	 * @CreatDate 2017年8月9日 下午12:16:53
	 * @param failOrder
	 * @param lotteryCode
	 * @param lotteryIssue
	 */
	public void sendPayoutFail(List<String> list, int lotteryCode, String issueCode) {
		String orders = "[超过5个订单]";
		if(list.size() <= 5){
			orders = list.toString();
		}
		String lotteryName = Lottery.getLottery(lotteryCode).getDesc();
		sendAlarmInfo(1,34,2,String.format("%s%s%s派奖失败", lotteryName,issueCode,orders), "lotto-award");
	}
	/**
	 * 订单中奖金额与票总金额不相等
	 * @author jiangwei
	 * @Version 1.0
	 * @CreatDate 2017年8月9日 下午2:43:12
	 * @param lotteryCode
	 * @param lotteryIssue
	 * @param orderCode
	 */
	public void sendPayoutMoneyFail(Integer lotteryCode, String lotteryIssue, String orderCode) {
		String lotteryName = Lottery.getLottery(lotteryCode).getDesc();
		sendAlarmInfo(1,34,2,String.format("%s%s%s订单奖金不符", lotteryName,lotteryIssue,orderCode), "lotto-award");
	}
	/**
	 * 派奖中浮动奖通知
	 * @author jiangwei
	 * @Version 1.0
	 * @CreatDate 2017年8月9日 下午4:04:26
	 * @param lotteryCode
	 * @param lotteryIssue
	 * @param orderCode
	 * @param str
	 */
	public void sendFluctuate(Integer lotteryCode, String lotteryIssue, String orderCode, String drowLevel) {
		String lotteryName = Lottery.getLottery(lotteryCode).getDesc();
		sendAlarmInfo(1,35,0,String.format("%s%s[%s]中%s", lotteryName,lotteryIssue,orderCode,drowLevel), "lotto-award");
	}
	
	/**
	 * 
	 * @Description: 获取嘉奖失败
	 * @param lotteryCode
	 * @param lotteryIssue
	 * @param orderCode
	 * @param drowLevel
	 * @author wuLong
	 * @date 2018年1月6日 上午10:22:35
	 */
	public void sendFailBonus(Integer lotteryCode, String lotteryIssue, String orderCode) {
		String lotteryName = Lottery.getLottery(lotteryCode).getDesc();
		sendAlarmInfo(1,34,2,String.format("%s%s%s获取嘉奖失败", lotteryName,lotteryIssue,orderCode), "lotto-award");
	}
	
	
	/**
	 * 发送彩期状态更新
	 * @author jiangwei
	 * @Version 1.0
	 * @CreatDate 2017年8月15日 上午11:14:00
	 * @param lotteryCode
	 * @param updateDataType
	 */
	public void sendUpdateNotice(int lotteryCode,int updateDataType){
		JSONObject jsonObject = new JSONObject();
        jsonObject.put("lotteryCode",lotteryCode);
        jsonObject.put("updateDataType",updateDataType);
        producer.sendDataToQueue("update_notice", jsonObject.toString());
	}
	/**
	 * 中奖通知发短信
	 * @author jiangwei
	 * @Version 1.0
	 * @CreatDate 2017年7月6日 下午3:21:15
	 * @param orders
	 */
	public void sendWinMesssage(Collection<String> orders){
		if(CollectionUtils.isEmpty(orders)){
			return;
		}
		JSONObject message = new JSONObject();
		message.put("nodeId", 7);
		message.put("nodeData", StringUtils.collectionToDelimitedString(orders, SymbolConstants.COMMA));
		JSONObject json = new JSONObject();
		json.put("message", message);
		json.put("key", "nodeMsgSend");
		json.put("messageSource", "award");
		producer.sendDataToQueue(MSG, json.toString());
	}
	/**
	 * 发送添加追号订单通知
	 * @author jiangwei
	 * @Version 1.0
	 * @CreatDate 2017年7月20日 下午2:17:51
	 * @param lotteryCode
	 * @param issueCode
	 */
	public void sendChaseOrder(int lotteryCode,String issueCode){
		JSONObject message = new JSONObject();
		message.put("lotteryCode", lotteryCode);
		message.put("issueCode", issueCode);
		producer.sendDataToQueue(CHASE_ORDER_QUEUE, message.toString());
	}
	/**
	 * 派奖发送活动通知
	 * @author jiangwei
	 * @Version 1.0
	 * @CreatDate 2017年7月27日 上午11:43:30
	 * @param orders
	 */
	public void sendActivity(Collection<String> orders,int type){
		if(CollectionUtils.isEmpty(orders)){
			return;
		}
		JSONObject message = new JSONObject();
		message.put("transId", StringUtils.collectionToDelimitedString(orders, SymbolConstants.COMMA));
		JSONObject json = new JSONObject();
		json.put("message", message);
		json.put("type", type);
		json.put("messageSource", "award");
		producer.sendDataToQueue(ACTIVITY_CHANNEL_QUEUE, json.toString());
	}
	/**
	 * 组装数据
	 * @author jiangwei
	 * @Version 1.0
	 * @CreatDate 2017年6月5日 下午4:04:43
	 * @param orders
	 * @param status
	 * @return
	 */
	private String getMessageStr(List<String> orders,int status){
		JSONObject jsonObject = new JSONObject();
        jsonObject.put("orderCode",StringUtils.collectionToDelimitedString(orders,SymbolConstants.COMMA));
        jsonObject.put("createTime", DateUtil.getNow());
        jsonObject.put("status",status);
        jsonObject.put("buyType", "1");
        return jsonObject.toString();
	}
	/**
	 * 添加报警信息
	 * @author jiangwei
	 * @Version 1.0
	 * @CreatDate 2017年8月8日 下午12:18:05
	 * @param alarmType 报警类型
	 * @param alarmChild 报警子类型
	 * @param alarmLevel 报警等级
	 * @param alarmInfo 报警信息
	 * @param remark 备注
	 */
	private void sendAlarmInfo(int alarmType,int alarmChild,int alarmLevel,String alarmInfo,String remark){
		JSONObject jsonObject = new JSONObject();
        jsonObject.put("alarmChild",alarmChild);
        jsonObject.put("alarmInfo",alarmInfo);
        jsonObject.put("alarmLevel",alarmLevel);
        jsonObject.put("alarmType",alarmType);
        jsonObject.put("remark",remark);
        jsonObject.put("alarmTime", DateUtil.getNow());
        producer.sendDataToQueue(ALARM_INFO, jsonObject.toString());
	}
	
	
	/**
	 * @Description: 发送抄单派奖成功通知
	 * @param orders
	 * @author wuLong
	 * @date 2017年10月18日 上午11:54:05
	 */
	public void sendCopyOrderPrize(Collection<String> orders) {
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("type", 2);
		jsonObject.put("orderCodeStr", StringUtils.collectionToDelimitedString(orders,SymbolConstants.COMMA));
		producer.sendDataToQueue(QUEUE_NAME_FOR_ORDER_COPY, jsonObject.toString());
	}


    /**
     * 合买中奖统计
     *
     * @param jsonObject
     * @author lgs
     * @Version 1.0
     */
    public void sendOrderGroupCount(JSONObject jsonObject) {
        producer.sendDataToQueue(ORDER_GROUP_RESULT_STATIS_QUEUE, jsonObject.toString());
    }

}
