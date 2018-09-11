package com.hhly.award.persistence.dao;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.hhly.award.DefaultDao;
import com.hhly.award.bo.TicketInfoBO;

public class TicketInfoDaoMapperTest extends DefaultDao {
    @Autowired
    TicketInfoDaoMapper mapper;
    
	@Test
	public void testUpdateTicketInfo() {
		List<TicketInfoBO> ticketInfoBOs = new ArrayList<>();
		for (int i = 1; i < 3; i++) {
			TicketInfoBO bo = new TicketInfoBO();
			ticketInfoBOs.add(bo);
			bo.setWinningDetail("1");
			bo.setPreBonus(0.0);
			bo.setAftBonus(0.0);
			bo.setAddedBonus(0.0);
			bo.setWinningDetail("123");
			bo.setId(Long.valueOf(i));
		}
		mapper.updateTicketInfo(ticketInfoBOs);
	}

}
