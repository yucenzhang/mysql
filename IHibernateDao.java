package com.yczh.s2s3h4.mysql;

import java.util.List;

public interface IHibernateDao<T> {
	void insert(Object obj);

	void update(Object obj);

	void delete(Object obj);

	List<T> list(String sql, Object[] values);

	List<T> listPage(String sql, Object[] values, int firstResult, int maxResults);

	T uniqueResult(String sql, Object[] values);
	
}
