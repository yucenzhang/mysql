package com.yczh.s2s3h4.mysql;

public class SqlCondition {

	private String name;
	private Object value;
	private SqlOperator sqlOperator;
	private boolean isOr;
	
	public SqlCondition(String name, Object value, SqlOperator sqlOperator) {
		this.name = name;
		this.value = value;
		this.sqlOperator = sqlOperator;
	}

	public SqlCondition(String name) {
		this.name = name;
	}

	public SqlCondition(String name, Object value) {
		this.name = name;
		this.value = value;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name
	 *            the name to set
	 */
	@SuppressWarnings("unused")
	private void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the value
	 */
	public Object getValue() {
		return value;
	}

	/**
	 * @param value
	 *            the value to set
	 */
	public void setValue(Object value) {
		this.value = value;
	}

	/**
	 * @return the sqlOperator
	 */
	public SqlOperator getSqlOperator() {
		return sqlOperator;
	}

	/**
	 * @param sqlOpertator
	 *            the sqlOpertator to set
	 */
	public void setSqlOperator(SqlOperator sqlOperator) {
		this.sqlOperator = sqlOperator;
	}

	/**
	 * @return the isOr
	 */
	public boolean isOr() {
		return isOr;
	}

	/**
	 * @param isOr the isOr to set
	 */
	public void setOr(boolean isOr) {
		this.isOr = isOr;
	}

	static enum SqlType {
		INSERT, UPDATE, SELECT, DELATE,
	}

	public String toString() {
		switch (sqlOperator) {
		case NULL:
			return name.concat("IS NULL ");
		case NOT_NULL:
			return name.concat("IS NOT NULL ");
		case EQUAL:
			return name.concat(" = ? ");
		case NOT_EQUAL:
			return name.concat(" != ? ");
		case LIKE:
			return name.concat(" LIKE ? ");
		case LESS_THAN:
			return name.concat(" < ? ");
		case LESS_THAN_OR_EQUAL:
			return name.concat(" <= ? ");
		case GREATER_THAN:
			return name.concat(" > ? ");
		case GREATER_THAN_OR_EQUAL:
			return name.concat(" >= ? ");
		default:
			return "";
		}
	}
}
