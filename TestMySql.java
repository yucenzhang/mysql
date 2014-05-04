package com.yczh.s2s3h4.mysql;

import com.yczh.s2s3h4.system.entity.UserEntity;

public class TestMySql {
	public static void main(String[] args) {
		SqlProperty userProperty = new SqlProperty(UserEntity.class);
		System.err.println(userProperty.getPkName());
		System.err.println(userProperty.getPkValue());
		userProperty.valueEqual("id", "123");
		userProperty.valueGreaterThanOrEqual("password","2");
		userProperty.valueLike("name", "%12");
//		userProperty.addSqlCondition(new SqlCondition("USERNAME", "12", SqlOperator.EQUAL));
		System.err.println(userProperty.getSqlCommand(SqlCommandType.INSERT));
		System.err.println(userProperty.getSqlCommand(SqlCommandType.UPDATE));
		userProperty.valueGreaterThan("name", "%12");
		System.err.println(userProperty.getSqlCommand(SqlCommandType.SELECT));
		
		System.err.println(userProperty.getSqlCommand(SqlCommandType.DELETE));
		
		userProperty.getSqlCommand(SqlCommandType.INSERT);
//		userProperty.getValues();
//		for (Object value : userProperty.getValues()) {
//			System.err.println(value);
//		}
		System.err.println("");
	}
}
