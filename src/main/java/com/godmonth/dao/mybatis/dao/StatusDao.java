package com.godmonth.dao.mybatis.dao;

import java.io.Serializable;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.tuple.Pair;

import com.godmonth.dao.mybatis.po.StatusPo;

/**
 * 
 * @author shenyue
 * 
 */
public class StatusDao<KEY extends Serializable, EXAMPLE, PO extends StatusPo<KEY>, MAPPER extends GenericMapper<KEY, PO, EXAMPLE>>
		extends OptimisticLockDao<KEY, EXAMPLE, PO, MAPPER> {

	public PO getSpecifiedStatusPo(KEY id, String status, Integer optimisticLock) {
		List<Pair<String, ?>> params = new ArrayList<Pair<String, ?>>();
		params.add(Pair.of("id", id));
		params.add(Pair.of("currentStatus", status));
		if (optimisticLock != null) {
			params.add(Pair.of("optimisticLock", optimisticLock));
		}
		Pair<String, ?>[] array = (Pair<String, ?>[]) Array.newInstance(
				Pair.class, params.size());
		PO selectOneByCompositeConditions = selectOneByCompositeConditions(params
				.toArray(array));
		return selectOneByCompositeConditions;

	}

	public PO justChangeStatus(KEY id, StatusTransition<String> statusTransition) {
		return justChangeStatus(id, statusTransition, null, null);
	}

	/**
	 * without transaction, may be dirty read
	 * 
	 * @param id
	 * @param statusTransition
	 * @param optimisticLock
	 *            这个参数是因为更新的入参只有id,不查数据库,找不到乐观锁,所以要带进来,带进来的应该是该对象从数据库读出来时候的乐观锁,
	 *            而不是变更后的乐观锁
	 */
	public PO justChangeStatus(KEY id,
			StatusTransition<String> statusTransition, Integer optimisticLock,
			StatusTransitionCallback<PO, Void> callbackBeforeStatusChange) {
		PO po = getSpecifiedStatusPo(id, statusTransition.getPreviousStatus(),
				optimisticLock);
		if (po != null) {
			changeStatus(po, statusTransition, callbackBeforeStatusChange);
		}
		return po;
	}

	public <R> R changeStatus(PO po, String nextState) {
		return changeStatus(po,
				new StatusTransition<String>(po.getCurrentStatus(), nextState),
				null);
	}

	public <R> R changeStatus(PO po, StatusTransition<String> statusTransition) {
		return changeStatus(po, statusTransition, null);
	}

	public <R> R changeStatus(PO po, StatusTransition<String> statusTransition,
			StatusTransitionCallback<PO, R> statusTransitionCallback) {
		daoLogger.trace("{}, {}", po.getId(), statusTransition);
		R r = null;
		if (po.getCurrentStatus().equals(statusTransition.getPreviousStatus())) {
			if (statusTransitionCallback != null) {
				r = statusTransitionCallback.beforeChanged(po,
						statusTransition.getNextStatus());
			}
			po.setCurrentStatus(statusTransition.getNextStatus());
			updateByPrimaryKey(po, true);
		} else {
			String message = String.format(
					"po.id:%s, exptected status:%s,actual status:%s",
					po.getId(), statusTransition.getPreviousStatus(),
					po.getCurrentStatus());
			throw new IllegalStateException(message);
		}
		return r;
	}

	public static interface StatusTransitionCallback<PO, R> {
		R beforeChanged(PO po, String currentStatus);
	}
}
