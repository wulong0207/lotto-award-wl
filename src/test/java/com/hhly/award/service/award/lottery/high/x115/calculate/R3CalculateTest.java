package com.hhly.award.service.award.lottery.high.x115.calculate;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.hhly.award.service.award.lottery.high.WinInfo;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:applicationContext.xml"})
public class R3CalculateTest {

	@Autowired
	private  R3Calculate calculator;
	private String[] drawCode;
	
	@Before
	public void setUp() throws Exception {
		drawCode = ("01,02,03,04,05").split(",");
	}
	
	@Test
	public void testSimple() {
		WinInfo hitNums = calculator.simple("01,02,03;03,04,05;04,05,06;01,05,03",drawCode);
		WinInfo hitNums2 = calculator.simple("06,04,05;01,04,07;04,05,06",drawCode);
		Assert.assertTrue(hitNums.getAllCount()==3);
		Assert.assertTrue(hitNums2.getAllCount()==0);
	}
	
	@Test
	public void testComplex() {
		WinInfo hitNums = calculator.complex("04,05,06,07",drawCode);
		WinInfo hitNums2 = calculator.complex("04,05,01,02,07",drawCode);
		Assert.assertTrue(hitNums.getAllCount()==0);
		Assert.assertTrue(hitNums2.getAllCount()==4);
	}
	
	@Test
	public void testGallDrag() {
		WinInfo hitNums = calculator.gallDrag("04#05,06,07",drawCode);
		WinInfo hitNums2 = calculator.gallDrag("04,01#02,03,07",drawCode);
		Assert.assertTrue(hitNums.getAllCount()==0);
		Assert.assertTrue(hitNums2.getAllCount()==2);
	}


}
