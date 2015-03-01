package com.godmonth.dao.mybatis.dao;

import java.io.Serializable;

import org.apache.commons.lang3.Validate;
import org.apache.commons.lang3.exception.ContextedRuntimeException;
import org.springframework.dao.OptimisticLockingFailureException;

import com.godmonth.dao.mybatis.po.OptimisticLockPo;

/**
 * 
 * @author shenyue
 * 
 */
public class OptimisticLockDao<KEY extends Serializable, EXAMPLE, PO extends OptimisticLockPo<KEY>, MAPPER extends GenericMapper<KEY, PO, EXAMPLE>>
		extends MutableDao<KEY, EXAMPLE, PO, MAPPER> {

	public static final int INITIAL_OPTIMISTIC_LOCK_VALUE = 1;
	private boolean enforceOptimisticLock = true;

	public int updateByPrimaryKey(PO po, boolean selective) {
		Validate.notNull(po, "po is null");
		Integer beforeOptimisticLock = po.getOptimisticLock();
		try {
			enforceLastUpdatedTime(po, selective);
			EXAMPLE example = exampleClass.newInstance();
			Object criteria = createCriteria.invoke(example);
			Class<?> c2 = criteria.getClass();

			c2.getMethod("andIdEqualTo", po.getId().getClass()).invoke(
					criteria, po.getId());
			if (enforceOptimisticLock) {
				if (beforeOptimisticLock != null && beforeOptimisticLock > 0) {
					Integer afterOptimisticLock = beforeOptimisticLock + 1;
					daoLogger.trace("{},optimisticLock:{}->{}", po.getId(),
							beforeOptimisticLock, afterOptimisticLock);
					c2.getMethod("andOptimisticLockEqualTo", Integer.class)
							.invoke(criteria, beforeOptimisticLock);
					po.setOptimisticLock(afterOptimisticLock);

				} else {
					throw new OptimisticLockingFailureException(String.format(
							"class:%s,id:%s,optimisticLock can not be:%d", po
									.getClass().getName(), po.getId(),
							beforeOptimisticLock));
				}
			}
			int updateByExampleSelective = getMapper()
					.updateByExampleSelective(po, example);
			logger.trace("updateByExampleSelective:{}",
					updateByExampleSelective);
			if (updateByExampleSelective == 0) {
				throw new OptimisticLockingFailureException(String.format(
						"class:%s,id:%s,expected optimisticLock:%d", po
								.getClass().getName(), po.getId(),
						beforeOptimisticLock));
			} else {
				return updateByExampleSelective;
			}
		} catch (OptimisticLockingFailureException ex) {
			throw ex;
		} catch (Exception ex) {
			throw new ContextedRuntimeException(ex);
		}
	}

	@Override
	public int insert(PO po, boolean selective) {
		if (enforceOptimisticLock) {
			po.setOptimisticLock(INITIAL_OPTIMISTIC_LOCK_VALUE);
		}
		return super.insert(po, selective);
	}

	public boolean isEnforceOptimisticLock() {
		return enforceOptimisticLock;
	}

	public void setEnforceOptimisticLock(boolean enforceOptimisticLock) {
		this.enforceOptimisticLock = enforceOptimisticLock;
	}

}
