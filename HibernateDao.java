package com.yczh.s2s3h4.mysql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.jdbc.ReturningWork;
import org.springframework.beans.factory.annotation.Autowired;

public class HibernateDao<T> implements IHibernateDao<T>{
	@Autowired
	private SessionFactory sessionFactory;
	
	public Session getSession() {
		return sessionFactory.getCurrentSession();
	}
	
	@Override
	public void insert(Object obj) {
		getSession().save(obj);
	}

	@Override
	public void update(Object obj) {
		getSession().update(obj);
	}

	@Override
	public void delete(Object obj) {
		getSession().delete(obj);
	}

	@Override
	public List<T> list(final String sql, final Object[] values) {
		getSession().doReturningWork(new ReturningWork<List<T>>() {

			@Override
			public List<T> execute(Connection con) throws SQLException {
				PreparedStatement ps = con.prepareStatement(sql);
				if (values != null && values.length > 0) {
					int parameterIndex = 1;
					for (Object value : values) {
						ps.setObject(parameterIndex++, value);
					}
				}
				ResultSet rs = ps.executeQuery();
				int columnCount = rs.getMetaData().getColumnCount();
				List<T> result = new ArrayList<T>();
				for (int i = 0; i < columnCount; i++) {
					
				}
				return null;
			}
			
		});
		return null;
	}

	@Override
	public List<T> listPage(String sql, Object[] values, int firstResult,
			int maxResults) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public T uniqueResult(String sql, Object[] values) {
		// TODO Auto-generated method stub
		return null;
	}
	
}
