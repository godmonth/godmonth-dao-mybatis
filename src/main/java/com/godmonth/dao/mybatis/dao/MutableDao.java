package com.godmonth.dao.mybatis.dao;

import java.io.Serializable;

import com.godmonth.dao.mybatis.po.MutablePo;

/**
 * 
 * @author shenyue
 * 
 */
public class MutableDao<KEY extends Serializable, EXAMPLE, PO extends MutablePo<KEY>, MAPPER extends GenericMapper<KEY, PO, EXAMPLE>>
		extends GenericDao<KEY, EXAMPLE, PO, MAPPER> {
	private boolean enforceLastUpdatedTime = true;

	public int updateByPrimaryKey(PO po, boolean selective) {
		return super.updateByPrimaryKey(enforceLastUpdatedTime(po, selective),
				selective);
	}

	@Override
	public int insert(PO po, boolean selective) {
		return super.insert(enforceLastUpdatedTime(po, selective), selective);
	}

	protected PO enforceLastUpdatedTime(PO po, boolean selective) {
		if (enforceLastUpdatedTime) {
			po.setLastUpdatedTime(getCurrentDate());
		}
		return po;
	}

	public boolean isEnforceLastUpdatedTime() {
		return enforceLastUpdatedTime;
	}

	public void setEnforceLastUpdatedTime(boolean enforceLastUpdatedTime) {
		this.enforceLastUpdatedTime = enforceLastUpdatedTime;
	}

}
