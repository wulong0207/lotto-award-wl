package com.hhly.award.service.rabbitmq.consumer;

import java.io.UnsupportedEncodingException;

import org.apache.log4j.Logger;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.hhly.award.base.common.HandleEnum;
import com.hhly.award.base.exception.ServiceRuntimeException;
import com.hhly.award.base.thread.ThreadPoolManager;
import com.hhly.award.controller.AwardController;
import com.hhly.award.service.IOrderService;
import com.hhly.award.service.award.IOrder;
import com.hhly.award.service.award.entity.AwardResultBO;

import net.sf.json.JSONObject;
/**
 * @desc 自动开奖列队监听
 * @author jiangwei
 * @date 2017年8月14日
 * @company 益彩网络科技公司
 * @version 1.0
 */
@Component
public class DrawListenter{
	
	private static final Logger LOGGER = Logger.getLogger(DrawListenter.class);
	
	@Autowired
	private IOrderService orderService;
	@Autowired
	private AwardController award;
	@Autowired
	private IOrder orderHandler;

	@RabbitListener(queues="auto_draw",containerFactory ="rabbitmqContainerFactory")
	public void onMessage(Message message) {
		String result = "";
		try {
			result = new String(message.getBody(), "UTF-8");
		} catch (UnsupportedEncodingException e) {
			LOGGER.error(message.toString(),e);
		}
		if(StringUtils.isEmpty(result)){
			return;
		}
		try {
			JSONObject object = JSONObject.fromObject(result);
			final int lotteryCode = object.getInt("lotteryCode");
			final String lotteryIssue =object.getString("lotteryIssue");
			LOGGER.info(lotteryCode+"|"+lotteryIssue+"进行自动开奖");
			orderService.updateLotteryIssueAutoDraw(lotteryCode,lotteryIssue);
			//开奖
			award.award(lotteryCode, lotteryIssue, 0, null);
			//添加派奖任务
			ThreadPoolManager.executeCachedThread(new Runnable() {
				@Override
				public void run() {
					try {
						for(;;){
							//每隔5秒获取开奖进度，进行判断
							try {
								Thread.sleep(5000);
							} catch (InterruptedException e) {
								e.printStackTrace();
							}
							AwardResultBO bo = orderHandler.getResult(lotteryCode, lotteryIssue, HandleEnum.Type.OPEN_AWARD.getValue());
							//如果存在开奖失败的订单，不进行自动开奖
							if(bo.getFail() >0 ){
								LOGGER.info("彩种存在开奖失败订单，不能进行自动派奖：lotteryCode:"+lotteryCode+",lotteryIssue:"+lotteryIssue);
								break;
							}
							//判断是否全部订单开奖成功，如果是（延迟5秒）进行派奖
							if(bo.getTotal() == bo.getSuccess()){
								Thread.sleep(5000);
								award.payout(lotteryCode, lotteryIssue, null);
								break;
							}
							
						}
					} catch (Exception e) {
						LOGGER.error("彩种自动派奖失败：lotteryCode:"+lotteryCode+",lotteryIssue:"+lotteryIssue,e);
					}
					
					
				}
			});
		}catch(ServiceRuntimeException se){
			LOGGER.info(se.getMessage());
		} catch (Exception e) {
			LOGGER.error("自动开奖异常:"+result,e);
		}
		
	}

}
