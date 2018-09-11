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
public class Q3CalculateTest {

	@Autowired
	private  Q3Calculate calculator;
	private String[] drawCode;
	
	@Before
	public void setUp() throws Exception {
		drawCode = ("01,02,03,04,05").split(",");
	}
	
	@Test
	public void testSimple() {
		WinInfo hitNums = calculator.simple("01|02|03;03|04|05;05|06|07",drawCode);
		WinInfo hitNums2 = calculator.simple("02|01|03;03|04|05;05|06|07",drawCode);
		Assert.assertTrue(hitNums.getAllCount()==1);
		Assert.assertTrue(hitNums2.getAllCount()==0);
	}
	
	@Test
	public void testComplex() {
		WinInfo hitNums = calculator.complex("04,05,01|02|08,03,10",drawCode);
		WinInfo hitNums2 = calculator.complex("03,06|05,02|05,01",drawCode);
		Assert.assertTrue(hitNums.getAllCount()==1);
		Assert.assertTrue(hitNums2.getAllCount()==0);
	}
}
