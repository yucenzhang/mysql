package com.yczh.s2s3h4.mysql;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;


public class SqlProperty implements Serializable {

	/**
	 * Version
	 */
	private static final long serialVersionUID = 1L;

	private String entityClassName;
	private Class<?> entityClass;
	private Map<String, String> columns;
	private String table;
	private SqlCondition pk;
	private Map<String, SqlCondition> sqlConditions = new HashMap<String, SqlCondition>();
	
	private static Map<SqlCommandType, String> sqlCommand = new HashMap<SqlCommandType, String>();

	/**
	 * 
	 * @param entityClassName
	 * @see #SqlProperty(Class entityClass)
	 */
	public SqlProperty(String entityClassName) {
		this.entityClassName = entityClassName;
		try {
			getTableColumns();
		} catch (ClassNotFoundException | IntrospectionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	/**
	 * 
	 * @param entityClass
	 * @see #SqlProperty(String entityClassName)
	 */
	public SqlProperty(Class<?> entityClass) {
		this.entityClass = entityClass;
		try {
			getTableColumns();
		} catch (ClassNotFoundException | IntrospectionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@SuppressWarnings("unused")
	private SqlProperty() {

	}

	/**
	 * 获取实体类对应表的有效列信息
	 * <li>key:实体类属性名
	 * <li>value:物理表列名
	 * <br>延迟加载策略，当实例被创建时对象为空，当且仅当方法被初次调用后，完成对象的初始化。
	 * @return 实体类对应表的有效列信息
	 * @throws IntrospectionException
	 * @throws ClassNotFoundException
	 */
	public Map<String, String> getTableColumns() throws IntrospectionException, ClassNotFoundException {
		if (columns == null) {
			columns = new HashMap<String, String>();
			if (entityClass == null) {
				entityClass = Class.forName(entityClassName);
			}
			Field[] fields = entityClass.getDeclaredFields();
			if (entityClass.getAnnotation(Table.class) == null) {
				throw new NullPointerException("Table is null");
			}
			table = entityClass.getAnnotation(Table.class).name();
			for (Field field : fields) {
				if (!Modifier.isStatic(field.getModifiers())) {
					PropertyDescriptor pd = new PropertyDescriptor(field.getName(), entityClass);
					Method readMethod = pd.getReadMethod();
					if (readMethod.isAnnotationPresent(Column.class)) {
						columns.put(field.getName(), readMethod.getAnnotation(Column.class).name());
						if (readMethod.isAnnotationPresent(Id.class)) {
							pk = new SqlCondition(field.getName(), readMethod.getAnnotation(Column.class).name());
						}
					}
				}
			}
		}
		return columns;
	}
	/**
	 * 获取物理表名
	 * @return 物理表名
	 */
	public String getTable() {
		return this.table;
	}
	/**
	 * 获取主键列名
	 * @return 主键列名
	 */
	public String getPkName() {
		return this.pk.getName();
	}
	/**
	 * 获取主键值
	 * @return 主键值
	 */
	public Object getPkValue() {
		return this.pk.getValue();
	}
	
	public String getSelectSqlCommand() {
		if (!sqlCommand.containsKey("select")) {
			
		}
		return sqlCommand.get("select");
	}

	public String getSqlCommand(SqlCommandType sqlCommandType) {
		switch (sqlCommandType) {
		case INSERT:
			if (!sqlCommand.containsKey(SqlCommandType.INSERT)) {
				StringBuilder insertSb = new StringBuilder();
				StringBuilder valueSb = new StringBuilder();
				try {
					Set<String> keys = getTableColumns().keySet();
					for (String key : keys) {
						if (insertSb.length() == 0) {
							insertSb.append("INSERT INTO ").append(table).append(" (").append(getTableColumns().get(key));
							valueSb.append(" VALUES (").append("?");
						} else {
							insertSb.append(",").append(getTableColumns().get(key));
							valueSb.append(",").append("?");
						}
					}
					insertSb.append(")").append(valueSb).append(")");
					sqlCommand.put(SqlCommandType.INSERT, insertSb.toString());
				} catch (ClassNotFoundException | IntrospectionException e) {
					e.printStackTrace();
				}
			}
			return sqlCommand.get(SqlCommandType.INSERT).toUpperCase();
		case UPDATE:
			if (!sqlCommand.containsKey(SqlCommandType.UPDATE)) {
				StringBuilder updateSb = new StringBuilder();
				try {
					Set<String> keys = getTableColumns().keySet();
					for (String key : keys) {
						if (updateSb.length() == 0) {
							updateSb.append("UPDATE ").append(table).append(" SET ").append(getTableColumns().get(key)).append("=?");
						} else {
							updateSb.append(",").append(getTableColumns().get(key)).append("=?");
						}
					}
					sqlCommand.put(SqlCommandType.UPDATE, updateSb.toString());
				} catch (ClassNotFoundException | IntrospectionException e) {
					e.printStackTrace();
				}
			}
			return sqlCommand.get(SqlCommandType.UPDATE).toUpperCase() + getConditionString();
		case DELETE:
			if (!sqlCommand.containsKey(SqlCommandType.DELETE)) {
				sqlCommand.put(SqlCommandType.DELETE, "DELETE ".concat(table));
			}
			return sqlCommand.get(SqlCommandType.DELETE).toUpperCase() + getConditionString();
		case SELECT:
			if (!sqlCommand.containsKey(SqlCommandType.SELECT)) {
				StringBuilder selectSb = new StringBuilder();
				try {
					Set<String> keys = getTableColumns().keySet();
					for (String key : keys) {
						if (selectSb.length() == 0) {
							selectSb.append("SELECT ").append(getTableColumns().get(key));
						} else {
							selectSb.append(",").append(getTableColumns().get(key));
						}
					}
					selectSb.append(" FROM ").append(table);
					sqlCommand.put(SqlCommandType.SELECT, selectSb.toString());
				} catch (ClassNotFoundException | IntrospectionException e) {
					e.printStackTrace();
				}
			}
			return sqlCommand.get(SqlCommandType.SELECT).toUpperCase() + getConditionString();
		default:
			return "";
		}
	}
	
	public String getConditionString() {
		StringBuilder whereSb = new StringBuilder();
		Set<String> keys = sqlConditions.keySet();
		for (String key : keys) {
			SqlCondition condition = sqlConditions.get(key);
			if (whereSb.length() == 0) {
				whereSb.append(" WHERE ").append(condition.toString());
			} else {
				whereSb.append("AND ").append(condition.toString());
			}
		}
		return whereSb.toString();
	}
	
	public Object[] getValues(SqlCommandType sqlCommandType) {
		Object[] values = new Object[sqlConditions.size()];
		Set<String> keys = sqlConditions.keySet();
		switch (sqlCommandType) {
		case INSERT:
		case UPDATE:
		case DELETE:
		case SELECT:
		}
		int index = 0;
		for (String key : keys) {
			values[index++] = sqlConditions.get(key).getValue(); 
		}
		return values;
	}
	
	/**
	 * 添加Sql命令where条件
	 * <br>SqlCondition的name属性为物理表列名
	 * @param sqlCondition Sql命令where条件
	 */
	public void addSqlCondition(SqlCondition sqlCondition) {
		if (sqlCondition != null) {
			sqlConditions.put(sqlCondition.getName(), sqlCondition);
		}
	}
	
	public void valueEqual(String name, Object value) {
		if (columns.containsKey(name)) {
			addSqlCondition(new SqlCondition(columns.get(name), value, SqlOperator.EQUAL));
		} else {
			addSqlCondition(new SqlCondition(name, value, SqlOperator.EQUAL));
		}
	}
	
	public void valueNotEqual(String name, Object value) {
		if (columns.containsKey(name)) {
			addSqlCondition(new SqlCondition(columns.get(name), value, SqlOperator.NOT_EQUAL));
		} else {
			addSqlCondition(new SqlCondition(name, value, SqlOperator.NOT_EQUAL));
		}
	}
	
	public void valueNull(String name) {
		if (columns.containsKey(name)) {
			addSqlCondition(new SqlCondition(columns.get(name), null, SqlOperator.NULL));
		} else {
			addSqlCondition(new SqlCondition(name, null, SqlOperator.NULL));
		}
	}
	
	public void valueNotNull(String name) {
		if (columns.containsKey(name)) {
			addSqlCondition(new SqlCondition(columns.get(name), null, SqlOperator.NOT_NULL));
		} else {
			addSqlCondition(new SqlCondition(name, null, SqlOperator.NOT_NULL));
		}
	}
	
	public void valueLike(String name, Object value) {
		if (columns.containsKey(name)) {
			addSqlCondition(new SqlCondition(columns.get(name), value, SqlOperator.LIKE));
		} else {
			addSqlCondition(new SqlCondition(name, value, SqlOperator.LIKE));
		}
	}
	
	public void valueLessThan(String name, Object value) {
		if (columns.containsKey(name)) {
			addSqlCondition(new SqlCondition(columns.get(name), value, SqlOperator.LESS_THAN));
		} else {
			addSqlCondition(new SqlCondition(name, value, SqlOperator.LESS_THAN));
		}
	}
	
	public void valueLessThanOrEqual(String name, Object value) {
		if (columns.containsKey(name)) {
			addSqlCondition(new SqlCondition(columns.get(name), value, SqlOperator.LESS_THAN_OR_EQUAL));
		} else {
			addSqlCondition(new SqlCondition(name, value, SqlOperator.LESS_THAN_OR_EQUAL));
		}
	}
	
	public void valueGreaterThan(String name, Object value) {
		if (columns.containsKey(name)) {
			addSqlCondition(new SqlCondition(columns.get(name), value, SqlOperator.GREATER_THAN));
		} else {
			addSqlCondition(new SqlCondition(name, value, SqlOperator.GREATER_THAN));
		}
	}
	
	public void valueGreaterThanOrEqual(String name, Object value) {
		if (columns.containsKey(name)) {
			addSqlCondition(new SqlCondition(columns.get(name), value, SqlOperator.GREATER_THAN_OR_EQUAL));
		} else {
			addSqlCondition(new SqlCondition(name, value, SqlOperator.GREATER_THAN_OR_EQUAL));
		}
	}
	
	public void valueOr(String name, Object value) {
		addSqlCondition(new SqlCondition(name, value, SqlOperator.OR));
	}
}
