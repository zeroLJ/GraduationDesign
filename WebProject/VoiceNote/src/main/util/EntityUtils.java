package main.util;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

public class EntityUtils {
	/**
	 * ʵ����תMap
	 * @param object
	 * @return
	 */
	public static Map<String, Object> entityToMap(Object object) {
		Map<String, Object> map = new HashMap();
	    for (Field field : object.getClass().getDeclaredFields()){
	        try {
	        	boolean flag = field.isAccessible();
	            field.setAccessible(true);
	            Object o = field.get(object);
	            map.put(field.getName(), o);
	            field.setAccessible(flag);
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	    }
	    return map;
	}
	
	/**
	 * Mapתʵ����
	 * @param map ��Ҫ��ʼ�������ݣ�key�ֶα�����ʵ����ĳ�Ա����һ��������ֵΪ��
	 * @param entity  ��Ҫת���ɵ�ʵ����
	 * @return
	 */
	public static <T> T mapToEntity(Map<String, Object> map, Class<T> entity) {
		T t = null;
		try {
			t = entity.newInstance();
			for(Field field : entity.getDeclaredFields()) {
				if (map.containsKey(field.getName())) {
					boolean flag = field.isAccessible();
		            field.setAccessible(true);
		            Object object = map.get(field.getName());
		            if (object!= null && field.getType().isAssignableFrom(object.getClass())) {
		            	 field.set(t, object);
					}
		            field.setAccessible(flag);
				}
			}
			return t;
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return t;
	}
}
