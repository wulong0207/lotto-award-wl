package com.hhly.award.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hhly.award.bo.SportAgainstInfoBO;
import com.hhly.award.persistence.dao.SportAgainstInfoDaoMapper;
import com.hhly.award.service.ISportAgainstInfoService;

@Service("sportAgainstInfoService")
public class SportAgainstInfoServiceImpl implements ISportAgainstInfoService {
	@Autowired
	SportAgainstInfoDaoMapper sportAgainstInfoDaoMapper;

	@Override
	public List<SportAgainstInfoBO> findBySystemCodeSLotteryCode(List<String> systemCodes, Integer lotteryCode) {
		return sportAgainstInfoDaoMapper.findSportAgainstInfoBySystemCodeS(systemCodes, lotteryCode);
	}

	@Override
	public List<SportAgainstInfoBO> findBySystemCodes(List<String> systemCodes,Integer lotteryCode) {
		return sportAgainstInfoDaoMapper.findBySystemCodes(systemCodes,lotteryCode);
	}

	@Override
	public void updateMatchState(List<String> systemCodes, Integer lotteryCode) {
		sportAgainstInfoDaoMapper.updateMatchState(systemCodes, lotteryCode);
	}

}
