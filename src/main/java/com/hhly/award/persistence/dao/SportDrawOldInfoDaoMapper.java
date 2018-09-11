package com.hhly.award.persistence.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.hhly.award.bo.SportDrawOldInfoBO;


public interface SportDrawOldInfoDaoMapper {
	/**
	 * 
	 * @Description:根据赛事ID集合查询赛事彩果
	 * @param SportAgainstInfoIdS
	 * @return List<SportDrawBBBO>
	 * @author wuLong
	 * @date 2017年5月25日 下午5:27:25
	 */
    List<SportDrawOldInfoBO> findBySportAgainstInfoIdS(@Param("sportAgainstInfoIdS")List<Long> sportAgainstInfoIdS);
    
}