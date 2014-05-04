package com.yczh.s2s3h4.mysql;

public interface JdbcExecuteCallBack<T> {
	T doJdbcExecute(String sql);
	
	T doJdbcExecute(String sql, Object[] values);
}
