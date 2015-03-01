package com.godmonth.dao.mybatis.dao;

import org.springframework.jdbc.core.support.JdbcDaoSupport;

/**
 * this is not A interface even its name start with an I.
 * 
 * @author shenyue
 * 
 */
public class IWantSequence extends JdbcDaoSupport {

	private String sequenceSql;

	public String getNext() {
		return getJdbcTemplate().queryForObject(sequenceSql, String.class);
	}

	public void setSequenceSql(String sequenceSql) {
		this.sequenceSql = sequenceSql;
	}

}
