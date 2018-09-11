package com.hhly.award.persistence.dao;

import com.hhly.award.persistence.po.OrderGroupPO;
import org.apache.ibatis.annotations.Param;

public interface OrderGroupDaoMapper {

    int deleteByPrimaryKey(Integer id);

    int insert(OrderGroupPO record);

    int insertSelective(OrderGroupPO record);

    OrderGroupPO selectByOrderCode(@Param("orderCode") String orderCode);

    int updateByPrimaryKeySelective(OrderGroupPO record);

    int updateByPrimaryKey(OrderGroupPO record);

    int updateCommissionAmount(OrderGroupPO record);

    int updateAddBonusFlag(OrderGroupPO record);
}