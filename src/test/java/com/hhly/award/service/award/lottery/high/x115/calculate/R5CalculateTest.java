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
public class R5CalculateTest {

	@Autowired
	private  R5Calculate calculator;
	private String[] drawCode;
	
	@Before
	public void setUp() throws Exception {
		drawCode = ("01,02,03,04,05").split(",");
	}
	
	@Test
	public void testSimple() {
		WinInfo hitNums = calculator.simple("01,02,03,04,05;03,04,05,06,07;04,05,06,07,08;04,06,08,09,10",drawCode);
		WinInfo hitNums2 = calculator.simple("01,02,03,04,05;03,04,05,02,01;04,05,06,07,08;04,06,08,09,10",drawCode);
		Assert.assertTrue(hitNums.getAllCount()==1);
		Assert.assertTrue(hitNums2.getAllCount()==2);
	}
	
	@Test
	public void testComplex() {
		WinInfo hitNums = calculator.complex("04,05,06,07,08,10",drawCode);
		WinInfo hitNums2 = calculator.complex("04,05,11,01,03,02",drawCode);
		Assert.assertTrue(hitNums.getAllCount()==0);
		Assert.assertTrue(hitNums2.getAllCount()==1);
	}
	
	@Test
	public void testGallDrag() {
		WinInfo hitNums = calculator.gallDrag("04#05,06,07,08,09",drawCode);
		WinInfo hitNums2 = calculator.gallDrag("01,02#05,04,07,08,09,03",drawCode);
		Assert.assertTrue(hitNums.getAllCount()==0);
		Assert.assertTrue(hitNums2.getAllCount()==1);
	}


}
