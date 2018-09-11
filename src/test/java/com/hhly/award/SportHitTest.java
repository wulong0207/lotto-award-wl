package com.hhly.award;

import java.util.Arrays;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.hhly.award.service.award.lottery.sports.SportsAward;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath*:applicationContext.xml"})
public class SportHitTest {
	@Autowired
	SportsAward sportsAward;
	
	@Test
	public void hitTest() throws Exception {
		sportsAward.handle(Arrays.asList("D1705231547210100029"),false,0);
	}
}
