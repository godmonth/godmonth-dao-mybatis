package com.godmonth.dao.mybatis.dao;

import java.io.Serializable;
import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.godmonth.dao.mybatis.po.GenericPo;

/**
 * 
 * @author shenyue
 * 
 */
public interface GenericMapper<KEY extends Serializable, PO extends GenericPo<KEY>, EXAMPLE> {
	int insert(PO p);

	int countByExample(EXAMPLE example);

	int deleteByExample(EXAMPLE example);

	int deleteByPrimaryKey(KEY id);

	int insertSelective(PO record);

	List<PO> selectByExample(EXAMPLE example);

	PO selectByPrimaryKey(KEY id);

	int updateByExampleSelective(@Param("record") PO record,
			@Param("example") EXAMPLE example);

	int updateByExample(@Param("record") PO record,
			@Param("example") EXAMPLE example);

	int updateByPrimaryKeySelective(PO record);

	int updateByPrimaryKey(PO record);

}
