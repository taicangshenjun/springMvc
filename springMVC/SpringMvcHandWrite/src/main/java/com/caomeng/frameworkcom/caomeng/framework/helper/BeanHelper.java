package com.caomeng.frameworkcom.caomeng.framework.helper;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import com.caomeng.frameworkcom.caomeng.framework.util.ReflectionUtil;

public final class BeanHelper {

	/**
	 * 相当于spring容器，拥有应用所有的bean实例
	 */
	private static final Map<Class<?>, Object> BEAN_MAP = new HashMap<Class<?>, Object>();
	
	static {
		Set<Class<?>> beanClassSet = ClassHelper.getBeanClassSet();
		for(Class<?> beanClass: beanClassSet) {
			Object obj = ReflectionUtil.newInstance(beanClass);
			BEAN_MAP.put(beanClass, obj);
		}
	}
	
	/**
     * 获取Bean容器
     */
	public static Map<Class<?>, Object> getBeanMap(){
		return BEAN_MAP;
	}
	
	/**
     * 获取Bean实例
     */
	@SuppressWarnings("unchecked")
	public static <T> T getBean(Class<T> cls) {
		if(!BEAN_MAP.containsKey(cls))
			throw new RuntimeException("can not get bean by class：" + cls);
		return (T) BEAN_MAP.get(cls);
	}
	
	/**
     * 设置Bean实例
     */
    public static void setBean(Class<?> cls, Object obj) {
        BEAN_MAP.put(cls, obj);
    }
	
}
