package com.godmonth.dao.mybatis.po;

import java.io.Serializable;
import java.util.Date;

/**
 * 
 * @author shenyue
 * 
 */
public interface MutablePo<KEY extends Serializable> extends GenericPo<KEY> {
	Date getLastUpdatedTime();

	void setLastUpdatedTime(Date lastUpdatedTime);
}
