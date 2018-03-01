package com.zero.voicenote.database;

import com.alibaba.fastjson.JSON;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by zero on 2018/1/20.
 * 所有使用GreenDao的表都继承此类，以便通过instanceof判断对象是否属于GreenDao的数据
 * 另外，公共的方法也可以写在此处
 */

public class BaseEntity {
    @Override
    public String toString(){
        return toMap().toString();
    }


    /**
     * 把表数据实例转成map类型，便于进行网络数据传输
     * @return
     */
    public Map<String,Object> toMap(){
        Map<String, Object> map = new HashMap();
        for (Field field : this.getClass().getDeclaredFields()){
            try {
                field.setAccessible(true);
                map.put(field.getName(),invokeMethod(this, field.getName(),null));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return map;
    }

    public String toJson(){
        return JSON.toJSONString(toMap());
    }

    /**
     * 通过反射获得对象属性的值（该类必须有相应的get方法,例如：要获取name变量的值，就要有定义getName（）的方法）
     */
    @SuppressWarnings("unchecked")
    private Object invokeMethod(Object owner, String methodName,
                                       Object[] args) throws Exception {
        Class ownerClass = owner.getClass();
        methodName = methodName.substring(0, 1).toUpperCase()
                + methodName.substring(1);
        Method method = null;
        try {
            method = ownerClass.getMethod("get" + methodName);
        } catch (SecurityException e) {
        } catch (NoSuchMethodException e) {
            return " can't find 'get" + methodName + "' method";
        }
        return method.invoke(owner);
    }
}
