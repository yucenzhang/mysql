package com.yczh.s2s3h4.mysql;

import java.util.HashMap;
import java.util.Map;

public class SqlPropertyFactory {

	private static Map<String, SqlProperty> queryProperties = new HashMap<String, SqlProperty>();
	/**
	 * 获取指定实体类的sql属性集合对象。
	 * <br>采用延迟加载策略，当entityClassName对象不存在时，进行初始化；
	 * 当entityClassName对象存在时直接返回静态对象。
	 * @param entityClass 实体类
	 * @return 指定实体类的sql属性集合对象
	 * @see #getSqlProperty(String entityClassName)
	 */
	public SqlProperty getSqlProperty(Class<?> entityClass) {
		if (entityClass != null) {
			return getSqlProperty(entityClass.getName());
		}
		return null;
	}
	/**
	 * 获取指定实体类的sql属性集合对象。
	 * <br>采用延迟加载策略，当entityClassName对象不存在时，进行初始化；
	 * 当entityClassName对象存在时直接返回静态对象。
	 * @param entityClassName 实体类名称
	 * @return 指定实体类的sql属性集合对象
	 */
	public SqlProperty getSqlProperty(String entityClassName) {
		if (!queryProperties.containsKey(entityClassName)) {
			queryProperties.put(entityClassName, new SqlProperty(entityClassName));
		}
		return queryProperties.get(entityClassName);
	}

}
