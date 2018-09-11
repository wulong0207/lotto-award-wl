package com.hhly.award.service.award.lottery;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.hhly.award.DefaultDao;
import com.hhly.award.service.award.IOrder;

public class YbfAwardTest extends DefaultDao{
	@Autowired
	private IOrder orderHandler;
	
	@Test
	public void openYbf(){
		orderHandler.RecommendDistribute(300, null,1,0,null);
	}

}
