package com.godmonth.dao.mybatis.dao;

import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import com.godmonth.dao.mybatis.po.StatusPo;

public class StatusTransition<T> {
	private final T previousStatus;
	private final T nextStatus;

	public static StatusTransition<String> nextStatus(StatusPo<?> po,
			Enum<?> nextStatus) {
		return new StatusTransition<String>(po.getCurrentStatus(),
				nextStatus.name());
	}

	public StatusTransition(T previousStatus, T nextStatus) {
		this.previousStatus = previousStatus;
		this.nextStatus = nextStatus;
	}

	public T getPreviousStatus() {
		return previousStatus;
	}

	public T getNextStatus() {
		return nextStatus;
	}

	public boolean isChanged() {
		return !ObjectUtils.equals(nextStatus, previousStatus);
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
				.append("previousState", this.previousStatus)
				.append("nextState", this.nextStatus).toString();
	}

}
