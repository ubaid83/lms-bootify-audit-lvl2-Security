package com.spts.lms.daos;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.core.GenericTypeResolver;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.SingleColumnRowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcDaoSupport;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import com.spts.lms.beans.BaseBean;
import com.spts.lms.helpers.PaginationHelper;
import com.spts.lms.helpers.PaginationHelper.Page;
import com.spts.lms.web.utils.Utils;

public abstract class BaseDAO<T extends BaseBean> extends
		NamedParameterJdbcDaoSupport {

	@Autowired
	protected PaginationHelper<T> paginationHelper;

	protected Class<T> genericType;

	@SuppressWarnings("unchecked")
	public BaseDAO() {
		super();
		// Get the generic type class
		this.genericType = (Class<T>) GenericTypeResolver.resolveTypeArgument(
				getClass(), BaseDAO.class);
	}

	@Autowired
	public void setDS(DataSource dataSource) {
		setDataSource(dataSource);
	}

	protected abstract String getTableName();

	protected abstract String getInsertSql();

	protected abstract String getUpdateSql();

	protected abstract String getUpsertSql();

	private void initInsert(final T bean) {
		Date dt = Utils.getInIST();
		bean.setCreatedDate(dt);
		bean.setLastModifiedDate(dt);
	}

	private void initUpdate(final T bean) {
		Date dt = Utils.getInIST();
		bean.setLastModifiedDate(dt);
	}

	public T findOneSQL(final String sql, final Object[] parameters) {
		// queryForObject returns EmptyResultDataAccessException when object is
		// not found
		// overwriting the behavior to return a standard null instead
		try {
			if (null == parameters) {
				return getJdbcTemplate().queryForObject(sql,
						BeanPropertyRowMapper.newInstance(genericType));
			}
			return getJdbcTemplate().queryForObject(sql,
					BeanPropertyRowMapper.newInstance(genericType), parameters);
		} catch (EmptyResultDataAccessException e) {
			return null;
		}
	}

	public void updateUsingMaps(final String sql,
			Map<String, Object> mapOfParameters) {
		mapOfParameters.put("createdDate",
				String.valueOf(new Timestamp(System.currentTimeMillis())));
		mapOfParameters.put("lastModifiedDate",
				String.valueOf(new Timestamp(System.currentTimeMillis())));

		getNamedParameterJdbcTemplate().update(sql, mapOfParameters);
	}

	public List<T> queryInClause(String sql, MapSqlParameterSource mapSource) {
		return getNamedParameterJdbcTemplate().query(sql, mapSource,
				BeanPropertyRowMapper.newInstance(genericType));
	}

	public T findOne(final Long id) {
		final String sql = "SELECT * FROM " + getTableName() + " WHERE ID = ?";
		return findOneSQL(sql, new Object[] { id });
	}

	public void insertUsingMap(Map<String, Object> mapper) {
		insertUsingMaps(mapper, getInsertSql());
	}
	
	

	public void insertUsingMaps(Map<String, Object> mapOfInputs, String sql) {
		getNamedParameterJdbcTemplate().update(sql, mapOfInputs);
	}

	public List<T> findAllSQL(final String sql, final Object[] parameters) {
		if (null == parameters) {
			return getJdbcTemplate().query(sql,
					BeanPropertyRowMapper.newInstance(genericType));
		}
		return getJdbcTemplate().query(sql,
				BeanPropertyRowMapper.newInstance(genericType), parameters);
	}

	public void deleteSoftById(final String id) {
		final String sql = "Update " + getTableName()
				+ " set active = 'N' WHERE ID = ?";
		getJdbcTemplate().update(sql, new Object[] { id });
	}

	public List<T> findAll() {
		final String sql = "SELECT * FROM " + getTableName();
		return findAllSQL(sql, new Object[] {});
	}

	public List<T> findAllActive() {
		final String sql = "SELECT * FROM " + getTableName()
				+ " where active = 'Y' ";
		return findAllSQL(sql, new Object[] {});
	}

	public Page<T> findAllActive(final int pageNo, final int pageSize) {
		return paginationHelper.fetchPage(getJdbcTemplate(),
				"SELECT COUNT(*) FROM " + getTableName()
						+ " where active = 'Y' ", "SELECT * FROM "
						+ getTableName() + " where active = 'Y' ",
				new Object[] {}, pageNo, pageSize,
				BeanPropertyRowMapper.newInstance(genericType));
	}

	public Page<T> findAll(final int pageNo, final int pageSize) {
		return paginationHelper.fetchPage(getJdbcTemplate(),
				"SELECT COUNT(*) FROM " + getTableName(), "SELECT * FROM "
						+ getTableName(), new Object[] {}, pageNo, pageSize,
				BeanPropertyRowMapper.newInstance(genericType));
	}

	public Page<T> findAllSQL(String sql, String countSql, Object[] parameters,
			final int pageNo, final int pageSize) {
		return paginationHelper.fetchPage(getJdbcTemplate(), countSql, sql,
				parameters, pageNo, pageSize,
				BeanPropertyRowMapper.newInstance(genericType));
	}

	public Long countAll() {
		final String sql = "SELECT COUNT(*) FROM " + getTableName();
		return getJdbcTemplate().queryForObject(sql, Long.class);
	}

	private int executeSQL(final T bean, final String sql) {

		SqlParameterSource parameterSource = getParameterSource(bean);
		int updated = getNamedParameterJdbcTemplate().update(sql,
				parameterSource);
		return updated;
	}

	public void executeUpdateSql(final String sql, Object[] parameters) {
		getJdbcTemplate().update(sql, parameters);
	}

	protected BeanPropertySqlParameterSource getParameterSource(T bean) {
		BeanPropertySqlParameterSource parameterSource = new BeanPropertySqlParameterSource(
				bean);
		return parameterSource;
	}

	public int updateSQL(final T bean, final String sql) {
		initUpdate(bean);
		return executeSQL(bean, sql);
	}

	private int[] executeSQLBatch(final List<T> beans, final String sql,
			final boolean isInsert) {
		List<SqlParameterSource> parameters = new ArrayList<SqlParameterSource>(
				beans.size());
		for (T bean : beans) {
			if (isInsert) {
				initInsert(bean);
			} else {
				initUpdate(bean);
			}
			parameters.add(getParameterSource(bean));
		}
		SqlParameterSource[] param = parameters
				.toArray(new SqlParameterSource[parameters.size()]);

		return getNamedParameterJdbcTemplate().batchUpdate(sql, param);
	}

	public String returnSingleColumn(String sql, Object[] parameters) {
		return (String) getJdbcTemplate().queryForObject(sql, parameters,
				new SingleColumnRowMapper(String.class));
	}

	public List<String> listOfStringParameter(final String sql,
			Object[] parameters) {
		return getJdbcTemplate().query(sql, parameters,
				new SingleColumnRowMapper<String>(String.class));

	}

	public int[] updateSQLBatch(final List<T> beans, final String sql) {
		return executeSQLBatch(beans, sql, false);
	}

	public int insert(final T bean) {
		initInsert(bean);
		return executeSQL(bean, getInsertSql());
	}

	public int upsert(final T bean) {
		initInsert(bean);
		return executeSQL(bean, getUpsertSql());
	}

	public int insertWithIdReturn(final T bean) {
		initInsert(bean);
		SqlParameterSource parameterSource = getParameterSource(bean);
		KeyHolder keyHolder = new GeneratedKeyHolder();

		int affected = getNamedParameterJdbcTemplate().update(getInsertSql(),
				parameterSource, keyHolder);
		bean.setId(keyHolder.getKey().longValue());
		return affected;
	}

	public int[] insertBatch(final List<T> beans) {
		return executeSQLBatch(beans, getInsertSql(), true);
	}

	public int[] upsertBatch(final List<T> beans) {
		return executeSQLBatch(beans, getUpsertSql(), true);
	}

	public int update(final T bean) {
		return updateSQL(bean, getUpdateSql());
	}

	public int[] updateBatch(final List<T> beans) {
		return updateSQLBatch(beans, getUpdateSql());
	}

	public int delete(final T bean) {
		final String sql = "Delete from " + getTableName() + " where id = :id";
		return deleteSQL(bean, sql);
	}

	public int[] deleteBatch(final List<T> beans) {
		final String sql = "Delete from " + getTableName() + " where id = :id";
		return deleteSQLBatch(beans, sql);
	}

	public int deleteSQL(final T bean, final String sql) {
		return executeSQL(bean, sql);
	}

	public int[] deleteSQLBatch(final List<T> beans, final String sql) {
		return executeSQLBatch(beans, sql, false);
	}

	public Page<T> searchActiveByExactMatch(final T bean, int pageNo,
			int pageSize) {
		return searchByExactMatch(bean, pageNo, pageSize, true);
	}

	public Page<T> searchByExactMatch(final T bean, int pageNo, int pageSize) {
		return searchByExactMatch(bean, pageNo, pageSize, false);
	}

	protected ArrayList<Object> buildParameters(final T bean,
			boolean includeActive) {
		StringBuilder criteria = new StringBuilder();
		criteria.append(" where 1=1 ");
		if (includeActive) {
			criteria.append(" and ").append(getTableName())
					.append(".active='Y' ");
		}

		ArrayList<Object> parameters = new ArrayList<Object>();
		// Add the criteria String as the first object
		parameters.add(criteria);

		try {
			BeanInfo beanInfo = Introspector.getBeanInfo(bean.getClass());
			PropertyDescriptor[] pdList = beanInfo.getPropertyDescriptors();
			for (PropertyDescriptor pd : pdList) {
				Method writeMethod = null;
				Method readMethod = null;
				try {
					writeMethod = pd.getWriteMethod();
					readMethod = pd.getReadMethod();
				} catch (Exception e) {
				}

				if (readMethod == null || writeMethod == null) {
					continue;
				}

				Object val = readMethod.invoke(bean);

				if (val instanceof String || val instanceof Short
						|| val instanceof Integer || val instanceof Long
						|| val instanceof Double) {

					if (val == null || val.equals(readMethod.getDefaultValue())) {
						continue;
					}
					String propertyName = pd.getName();

					if (val instanceof String && ((String) val).equals("")) {
						continue;
					}
					if (val instanceof Short
							&& ((Short) val).equals(Short.valueOf("0"))) {
						continue;
					}
					if (val instanceof Integer
							&& ((Integer) val).equals(Integer.valueOf("0"))) {
						continue;
					}
					if (val instanceof Long
							&& ((Long) val).equals(Long.valueOf("0"))) {
						continue;
					}
					if (val instanceof Double
							&& ((Double) val).equals(Double.valueOf("0"))) {
						continue;
					}

					criteria.append(" and ").append(getTableName()).append('.')
							.append(propertyName.toUpperCase()).append(" = ? ");
					parameters.add(val);
				}

			}
		} catch (IntrospectionException ite) {
			logger.error("Exception", ite);
		} catch (IllegalAccessException e) {
			logger.error("Exception", e);
		} catch (IllegalArgumentException e) {
			logger.error("Exception", e);
		} catch (InvocationTargetException e) {
			logger.error("Exception", e);
		}

		return parameters;
	}

	protected Page<T> searchByExactMatch(final T bean, int pageNo,
			int pageSize, boolean includeActive) {
		ArrayList<Object> parameters = buildParameters(bean, includeActive);
		// The first object will always be the criteria String
		StringBuilder criteria = (StringBuilder) parameters.remove(0);

		return paginationHelper.fetchPage(getJdbcTemplate(),
				"SELECT COUNT(*) FROM " + getTableName() + criteria.toString() +"order by createdDate desc",
				"SELECT * FROM " + getTableName() + criteria.toString() +"order by createdDate desc",
				parameters.toArray(), pageNo, pageSize,
				BeanPropertyRowMapper.newInstance(genericType));
	}

	@Cacheable(value = "acad_cycle", key = "#root.methodName")
	public String findLiveAcadMonth(String programId) {
		final String sql = "Select month from instituteCycle where live = 'Y' ";
		return getJdbcTemplate().queryForObject(sql,
				new SingleColumnRowMapper<String>(String.class));

	}

	@Cacheable(value = "acad_cycle", key = "#root.methodName")
	public Integer findLiveAcadYear(String programId) {
		final String sql = "Select year from instituteCycle where live = 'Y' ";
		return getJdbcTemplate().queryForObject(sql,
				new SingleColumnRowMapper<Integer>(Integer.class));
	}

	public List<String> getListOfString(final String sql, Object[] parameters) {
		return getJdbcTemplate().query(sql, parameters,
				new SingleColumnRowMapper(String.class));
	}
}
