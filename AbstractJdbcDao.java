package com.yczh.s2s3h4.mysql;

import java.sql.Connection;
import java.sql.SQLException;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.jdbc.ReturningWork;
import org.springframework.beans.factory.annotation.Autowired;

public abstract class AbstractJdbcDao {
	@Autowired
	private SessionFactory sessionFactory;
	
	public Session getSession() {
		return this.sessionFactory.getCurrentSession();
	}
	<T> T doJdbcExecute(final JdbcExecuteCallBack<T> action) {
		getSession().doReturningWork(new ReturningWork<T>() {

			@Override
			public T execute(Connection conn) throws SQLException {
				return null;
			}
			
		});
		
		return null;
	}
}	
