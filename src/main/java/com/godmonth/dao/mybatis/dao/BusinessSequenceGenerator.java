package com.godmonth.dao.mybatis.dao;

import java.util.Date;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.FastDateFormat;

public class BusinessSequenceGenerator extends IWantSequence {
	private String prefix = "";
	private String datePattern = "yyyyMMdd";
	private FastDateFormat fastDateFormat;
	private int size = 10;

	@Override
	protected void initTemplateConfig() {
		fastDateFormat = FastDateFormat.getInstance(datePattern);
	}

	@Override
	public String getNext() {
		String next = super.getNext();
		return prefix + fastDateFormat.format(new Date())
				+ StringUtils.leftPad(next, size, '0');
	}

	public void setDatePattern(String datePattern) {
		this.datePattern = datePattern;
	}

	public void setPrefix(String prefix) {
		this.prefix = prefix;
	}

	public void setSize(int size) {
		this.size = size;
	}

}
