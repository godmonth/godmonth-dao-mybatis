package com.godmonth.dao.mybatis.po;

import java.io.Serializable;
import java.util.Date;

/**
 * 
 * @author shenyue
 * 
 */
public interface GenericPo<KEY extends Serializable> {
	KEY getId();

	void setId(KEY id);

	Date getCreatedTime();

	void setCreatedTime(Date createdTime);
}
