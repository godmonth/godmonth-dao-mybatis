package com.godmonth.dao.mybatis.dao;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;
import org.apache.commons.lang3.exception.ContextedRuntimeException;
import org.apache.commons.lang3.tuple.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;

import com.godmonth.dao.mybatis.po.GenericPo;

/**
 * 
 * @author shenyue
 * 
 */
public class GenericDao<KEY extends Serializable, EXAMPLE, PO extends GenericPo<KEY>, MAPPER extends GenericMapper<KEY, PO, EXAMPLE>>
		implements InitializingBean {

	protected Logger logger = LoggerFactory.getLogger(getClass());
	protected static final Logger daoLogger = LoggerFactory
			.getLogger("dao.stream");
	private MAPPER mapper;

	protected Class<EXAMPLE> exampleClass;
	protected Method createCriteria;
	private boolean enforceCreatedTimeWhenInsert = true;

	@Override
	public void afterPropertiesSet() throws Exception {
		if (exampleClass != null) {
			createCriteria = exampleClass.getDeclaredMethod("createCriteria");
		}
	}

	public int updateByPrimaryKey(PO po, boolean selective) {
		return updateByPrimaryKeyDelegately(po, selective);
	}

	protected final int updateByPrimaryKeyDelegately(PO po, boolean selective) {
		if (selective) {
			return getMapper().updateByPrimaryKeySelective(po);
		} else {
			return getMapper().updateByPrimaryKey(po);
		}
	}

	public int insert(PO po, boolean selective) {
		return insertDelegately(enforceCreatedTime(po), selective);
	}

	protected PO enforceCreatedTime(PO po) {
		if (enforceCreatedTimeWhenInsert && po.getCreatedTime() == null) {
			po.setCreatedTime(getCurrentDate());
		}
		return po;
	}

	protected final int insertDelegately(PO po, boolean selective) {
		if (selective) {
			return getMapper().insertSelective(po);
		} else {
			return getMapper().insert(po);
		}
	}

	public PO selectByPrimaryKey(KEY key) {
		return getMapper().selectByPrimaryKey(key);
	}

	@SuppressWarnings("unchecked")
	public PO selectByUniqueProperty(String propertyName, Object propertyValue) {
		Validate.notEmpty(propertyName);
		Validate.notNull(propertyValue);
		return selectOneByCompositeConditions(Pair.of(propertyName,
				propertyValue));
	}

	public PO selectOneByCompositeConditions(Pair<String, ?>... condition) {
		try {
			EXAMPLE example = exampleClass.newInstance();

			Object criteria = createCriteria.invoke(example);
			Class<?> c2 = criteria.getClass();
			for (Pair<String, ?> pair : condition) {
				if (pair.getValue() != null) {
					Method declaredMethod = c2.getMethod(
							String.format("and%sEqualTo",
									StringUtils.capitalize(pair.getKey())),
							pair.getValue().getClass());
					declaredMethod.invoke(criteria, pair.getValue());
				}
			}

			List<PO> selectByExample = getMapper().selectByExample(example);
			if (selectByExample != null && selectByExample.size() == 1) {
				return selectByExample.get(0);
			} else {
				return null;
			}
		} catch (Exception ex) {
			throw new ContextedRuntimeException(ex);
		}

	}

	public List<PO> selectListByCompositeConditions(
			Pair<String, ?>... condition) {
		try {
			EXAMPLE example = exampleClass.newInstance();

			Object criteria = createCriteria.invoke(example);
			Class<?> c2 = criteria.getClass();
			for (Pair<String, ?> pair : condition) {
				if (pair.getValue() != null) {
					Method declaredMethod = c2.getMethod(
							String.format("and%sEqualTo",
									StringUtils.capitalize(pair.getKey())),
							pair.getValue().getClass());
					declaredMethod.invoke(criteria, pair.getValue());
				}
			}

			List<PO> selectByExample = getMapper().selectByExample(example);
			return selectByExample;
		} catch (Exception ex) {
			throw new ContextedRuntimeException(ex);
		}

	}

	public int countByCompositeConditions(Pair<String, ?>... condition) {
		try {
			EXAMPLE example = exampleClass.newInstance();

			Object criteria = createCriteria.invoke(example);
			Class<?> c2 = criteria.getClass();
			for (Pair<String, ?> pair : condition) {
				if (pair.getValue() != null) {
					Method declaredMethod = c2.getMethod(
							String.format("and%sEqualTo",
									StringUtils.capitalize(pair.getKey())),
							pair.getValue().getClass());
					declaredMethod.invoke(criteria, pair.getValue());
				}
			}

			int num = getMapper().countByExample(example);
			return num;
		} catch (Exception ex) {
			throw new ContextedRuntimeException(ex);
		}

	}

	protected Date getCurrentDate() {
		return new Date();
	}

	public void setExampleClass(Class<EXAMPLE> exampleClass) {
		this.exampleClass = exampleClass;
	}

	public MAPPER getMapper() {
		return mapper;
	}

	public void setMapper(MAPPER mapper) {
		this.mapper = mapper;
	}

	public boolean isEnforceCreatedTimeWhenInsert() {
		return enforceCreatedTimeWhenInsert;
	}

	public void setEnforceCreatedTimeWhenInsert(
			boolean enforceCreatedTimeWhenInsert) {
		this.enforceCreatedTimeWhenInsert = enforceCreatedTimeWhenInsert;
	}

}
