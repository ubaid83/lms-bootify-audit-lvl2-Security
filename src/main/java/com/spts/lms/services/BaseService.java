 package com.spts.lms.services;

import java.util.List;
import java.util.Map;

import com.spts.lms.beans.BaseBean;
import com.spts.lms.daos.BaseDAO;
import com.spts.lms.helpers.PaginationHelper.Page;

public abstract class BaseService<T extends BaseBean> {
	protected abstract BaseDAO<T> getDAO();
	
	public T findByID(final Long id) {
		return getDAO().findOne(id);
	}
	
	public List<T> findAll() {
		return getDAO().findAll();
	}
	
	public List<T> findAllActive() {
		return getDAO().findAllActive();
	}
	
	public Page<T> findAllPaged(final int pageNo, final int pageSize) {
		return getDAO().findAll(pageNo, pageSize);
	}
	
	public Page<T> findAllActive(int pageNo, int pageSize) {
		return getDAO().findAllActive(pageNo, pageSize);
	}

	
	public int insert(T bean) {
		return getDAO().insert(bean);
	}
	
	public int upsert(T bean) {
		return getDAO().upsert(bean);
	}
	public void insertUsingMap(Map<String,Object> mapper){
		getDAO().insertUsingMap(mapper);
		}
	
	public int[] insertBatch(List<T> beans) {
		return getDAO().insertBatch(beans);
	}
	
	public int[] upsertBatch(List<T> beans) {
		return getDAO().upsertBatch(beans);
	}
	
	public int insertWithIdReturn(final T bean) {
		return getDAO().insertWithIdReturn(bean);
	}
	
	public int delete(T bean) {
		return getDAO().delete(bean);
	}
	
	public int[] deleteBatch(List<T> beans) {
		return getDAO().deleteBatch(beans);
	}
	
	public int update(T bean) {
		return getDAO().update(bean);
	}
	
	public int[] updateBatch(final List<T> beans) {
		return getDAO().updateBatch(beans);
	}
	
	public int updateSQL(T bean, String sql) {
		return getDAO().updateSQL(bean, sql);
	}
	
	public int[] updateSQLBatch(final List<T> beans, String sql) {
		return getDAO().updateSQLBatch(beans, sql);
	}
	
	public Page<T> searchActiveByExactMatch(final T bean, int pageNo, int pageSize) {
		return getDAO().searchActiveByExactMatch(bean, pageNo, pageSize);
	}
	
	public Page<T> searchByExactMatch(final T bean, int pageNo, int pageSize) {
		return getDAO().searchByExactMatch(bean, pageNo, pageSize);
	}
	
	public void deleteSoftById(final String id) {
		getDAO().deleteSoftById(id);
	}
	
	public List<T> findAllSQL(final String sql, final Object[] parameters) {
		return getDAO().findAllSQL(sql, parameters);
	}
	
	public Page<T> findAllSQL(String sql, String countSql, Object[] parameters, final int pageNo, final int pageSize) {
		return getDAO().findAllSQL(sql, countSql, parameters, pageNo, pageSize);
	}
	public void insertUsingMapBulk(List<Map<String, Object>> mapper) {
		for (Map<String, Object> map : mapper) {
			getDAO().insertUsingMap(map);
		}
	}
}
