package com.hhly.award.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hhly.award.persistence.dao.TicketInfoDaoMapper;
import com.hhly.award.service.ITicketInfoService;

/**
 * @author wulong
 * @create 2017/5/9 14:20
 */
@Service("ticketInfoService")
public class TicketInfoServiceImpl implements ITicketInfoService {
	
    @Autowired
    TicketInfoDaoMapper ticketInfoDaoMapper;

	
}
