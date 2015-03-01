package com.godmonth.dao.mybatis.po;

import java.io.Serializable;

/**
 * 
 * @author shenyue
 * 
 */
public interface StatusPo<KEY extends Serializable> extends
		OptimisticLockPo<KEY>, StatusEnable {

}
