package com.godmonth.dao.mybatis.po;

import java.io.Serializable;

/**
 * 
 * @author shenyue
 * 
 */
public interface OptimisticLockPo<KEY extends Serializable> extends
		MutablePo<KEY>, OptimisticLock {
	void setOptimisticLock(Integer optimisticLock);
}
