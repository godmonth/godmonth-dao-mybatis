package com.godmonth.dao.mybatis.dao;

import org.springframework.beans.factory.annotation.Required;
import org.springframework.jdbc.core.support.JdbcDaoSupport;

/**
 * 
 * @author shenyue
 * 
 */
public class SequenceGetter extends JdbcDaoSupport {

	private String sequenceSql;

	public String getNext() {
		return getJdbcTemplate().queryForObject(sequenceSql, String.class);
	}

	@Required
	public void setSequenceSql(String sequenceSql) {
		this.sequenceSql = sequenceSql;
	}

}
