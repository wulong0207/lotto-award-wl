package com.hhly.award.service.rabbitmq.produce;

import java.util.Arrays;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;

import com.hhly.award.DefaultDao;
import com.hhly.award.base.common.SymbolConstants;
import com.hhly.award.service.award.SendMessage;

import net.sf.json.JSONObject;

public class MQProducerTest extends DefaultDao {
	@Autowired
	MQProducer producer;
	
	@Test
	public void testSendDataToQueue() {
		producer.sendDataToQueue("test_queue", "1");
	}

	@Test
	public void testSendDataToFanout() {
		
	}

	@Test
	public void testSendCopyOrderPrize(){
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("type", 2);
		jsonObject.put("orderCodeStr", StringUtils.collectionToDelimitedString(Arrays.asList("D1707151602470100004"),SymbolConstants.COMMA));
		producer.sendDataToQueue("copy_order_queue_test", jsonObject.toString());
	}
}
