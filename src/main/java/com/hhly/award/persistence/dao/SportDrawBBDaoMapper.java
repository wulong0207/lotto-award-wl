package com.hhly.award.persistence.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.hhly.award.bo.SportDrawBBBO;

public interface SportDrawBBDaoMapper {
	/**
	 * 
	 * @Description:根据赛事ID集合查询赛事彩果
	 * @param SportAgainstInfoIdS
	 * @return List<SportDrawBBBO>
	 * @author wuLong
	 * @date 2017年5月25日 下午5:27:25
	 */
    List<SportDrawBBBO> findBySportAgainstInfoIdS(@Param("sportAgainstInfoIdS")List<Long> sportAgainstInfoIdS);
    /**
     * 
     * @Description: 通过赛事编号查询赛事开奖结果 
     * @param systemCode 赛事编号
     * @return SportDrawBBBO
     * @author wuLong
     * @date 2017年5月27日 下午3:37:30
     */
    SportDrawBBBO findBySystemCode(@Param("systemCode")String systemCode);
}