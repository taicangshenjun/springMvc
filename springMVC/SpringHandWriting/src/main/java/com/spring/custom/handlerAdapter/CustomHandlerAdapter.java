package com.spring.custom.handlerAdapter;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.spring.custom.annotation.CustomService;
import com.spring.custom.argumentResolver.ArgumentResolver;

@CustomService("customHandlerAdapter")
public class CustomHandlerAdapter implements HandlerAdapterService {

	public Object[] handle(HttpServletRequest req, HttpServletResponse resp, Method method, Map<String, Object> beans) {
		//获取方法中含义的参数
		Class<?>[] paramClazzs = method.getParameterTypes();
		System.out.println("======当前需要解析的参数对应的类=========");
		for(Class<?> clazz: paramClazzs) {
			System.out.println(clazz);
		}
		
		// 定义一个返回参数的结果集
		Object[] args = new Object[paramClazzs.length];
		
		// 定义一个ArgumentResolver实现类的Map
		Map<String, Object> argumentResolvers = getBeansOfType(beans, ArgumentResolver.class);
		System.out.println("======当前需要解析的参数对应的类实例化======");
		for(Map.Entry<String, Object> map: argumentResolvers.entrySet()) {
			System.out.println("key:" + map.getKey() + "; value:" + map.getValue());
		}
		
		//定义参数索引
		int paramIndex = 0;
		//定义数组下标索引
		int i =0;
		// 开始处理参数
		for(Class<?> paramClazz: paramClazzs) {
			for(Map.Entry<String, Object> entry: argumentResolvers.entrySet()) {
				ArgumentResolver ar = (ArgumentResolver) entry.getValue();
				if(ar.support(paramClazz, paramIndex, method)) {
					args[i ++] = ar.argumentResolver(req, resp, paramClazz, paramIndex, method);
				}
			}
			paramIndex ++;
		}
		return args;
	}
	
	private Map<String, Object> getBeansOfType(Map<String, Object> beans, Class<ArgumentResolver> intfType){
		Map<String, Object> resultBeans = new HashMap<String, Object>();
		for(Map.Entry<String, Object> map: beans.entrySet()) {
			// 获取满足ArgumentResolver接口的bean
			Class<?>[] intfs = map.getValue().getClass().getInterfaces();
			if(intfs != null && intfs.length > 0) {
				for(Class<?> intf: intfs) {
					// 将满足的bean存储在resultBeans中
					if(intf.isAssignableFrom(intfType))
						resultBeans.put(map.getKey(), map.getValue());
				}
			}
		}
		
		
		
		
		
		
		
		return null;
	}

}
