package main.util;




import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ObjUtils {
    /**
     * 原来用的4但是cwk提出用8位会以0补足.
     * 原因是数据库保存的是八位小数，如果出现八位就是数据库保存数据的问题
     */
    public static DecimalFormat mDecimalFormat=new DecimalFormat("#.########");
    /**
     * 对象转String
     *
     * @param obj
     * @return
     */
    public static String objToStr(Object obj) {

        String value;

        if(obj == null){
            value = "";
        }else if(obj instanceof Number){
            BigDecimal b = new BigDecimal(String.valueOf(obj));
            value = mDecimalFormat.format(b);
        }else{
            value = String.valueOf(obj).trim();
        }
        return value;
    }

    public static String objToStrNull(Object obj) {
        return (obj == null) ? null : String.valueOf(obj).trim();
    }


    /**
     * 对象转Double
     *
     * @param obj
     * @return
     */

    public static double objToDouble(Object obj) {
        if (!objToStr(obj).equals("") && !objToStr(obj).equals("null")) {
            return Double.parseDouble(objToStr(obj));
        } else {
            return Double.NaN;
        }
    }

    /**
     * 对象转Int
     *
     * @param obj
     * @return
     */
    public static int objToInt(Object obj) {
        if (!objToStr(obj).equals("") && !objToStr(obj).equals("null")) {
            return objToFloat(obj).intValue();
        } else {
            return 0;
        }
    }

    public static Float objToFloat(Object obj) {
        if (!objToStr(obj).equals("") && !objToStr(obj).equals("null")) {
            return Float.parseFloat(objToStr(obj));
        } else {
            return Float.NaN;
        }
    }

    public static boolean objToBoolean(Object obj) {
        if (!objToStr(obj).equals("") && !objToStr(obj).equals("null")) {
            return Boolean.parseBoolean(objToStr(obj));
        } else {
            return false;
        }
    }

    

    public static Date objToDate(Object obj){
        try {
        	if (obj instanceof Date) {
        		return (Date) obj;
			}else if (obj instanceof Number) {
				return new Date(Long.valueOf(ObjUtils.objToStr(obj)));
			}else {
				try {
					System.out.println(4);
					return new Date(Long.valueOf(ObjUtils.objToStr(obj)));	
				} catch (Exception e) {
					try {
						System.out.println(5);
						return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(ObjUtils.objToStr(obj));
					} catch (Exception e2) {
						System.out.println(6);
						return new SimpleDateFormat("yyyy-MM-dd").parse(ObjUtils.objToStr(obj));
					}
					
				}
			}
//            return (Date) obj;
        }catch (Exception e){
            return new Date(0);
        }
    }

    public static boolean isInt(Object obj){
        try {
            String s = objToStr(obj);
            Integer.valueOf(s);
            if (s.contains(".")){
                return false;
            }
            return true;
        }catch (Exception e){
            return false;
        }
    }
    
    public static BigDecimal objToBigDecimal(Object value) {
        return objToBigDecimal(value,null);
    }
    
    public static BigDecimal objToBigDecimal(Object value,BigDecimal defaultValue) {
    	BigDecimal ret = defaultValue;
    	if(defaultValue==null){
    		ret = BigDecimal.ZERO;
    	}
        if( StringUtils.isNotEmpty(ObjUtils.objToStrNull(value))) {
            if( value instanceof BigDecimal ) {
                ret = (BigDecimal) value;
            } else if( value instanceof String ) {
                ret = new BigDecimal( (String) value );
            } else if( value instanceof BigInteger ) {
                ret = new BigDecimal( (BigInteger) value );
            } else if( value instanceof Number ) {
                ret = new BigDecimal( ((Number)value).doubleValue() );
            } else {
                throw new ClassCastException("Not possible to coerce ["+value+"] from class "+value.getClass()+" into a BigDecimal.");
            }
        }
        return ret;
    }

    /**
     * list深度拷贝
     * @param src
     * @param <T>
     * @return
     */
    public static <T> List<T> deepCopy(List<T> src) {
        try {
            ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
            ObjectOutputStream out = new ObjectOutputStream(byteOut);
            out.writeObject(src);

            ByteArrayInputStream byteIn = new ByteArrayInputStream(byteOut.toByteArray());
            ObjectInputStream in = new ObjectInputStream(byteIn);
            @SuppressWarnings("unchecked")
            List<T> dest = (List<T>) in.readObject();
            return dest;
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<T>();
        }

    }

    public static <T,K> Map<T,K> deepCopy(Map<T,K> src) {
        try {
            ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
            ObjectOutputStream out = new ObjectOutputStream(byteOut);
            out.writeObject(src);

            ByteArrayInputStream byteIn = new ByteArrayInputStream(byteOut.toByteArray());
            ObjectInputStream in = new ObjectInputStream(byteIn);
            @SuppressWarnings("unchecked")
            Map<T,K> dest = (Map<T,K>) in.readObject();
            return dest;
        } catch (Exception e) {
            e.printStackTrace();
            return new HashMap<T,K>();
        }

    }

    public static Map<String,Object> findMap(List<Map<String,Object>> data,String name,Object s){
        Map<String,Object> map=null;
        for(Map<String,Object> item:data){
            if(s instanceof Number){
                if(s instanceof Integer){
                    if(ObjUtils.objToInt(item.get(name))==ObjUtils.objToInt(s)){
                        map=item;break;
                    }
                }else{
                    if(Float.compare(ObjUtils.objToFloat(item.get(name)),ObjUtils.objToFloat(s))==0){
                        map=item;break;
                    }
                }
            }else{
                if(ObjUtils.objToStr(item.get(name)).equals(ObjUtils.objToStr(s))){
                    map=item;break;
                }
            }
        }
        return map;
    }

    public static Map<String,Object> findMap(List<Map<String,Object>> data,List<String> names,Map<String,Object> maps){
        Map<String,Object> map=null;
        for(Map<String,Object> item:data){
            boolean flag = true;
            for(String name:names){
                Object s = maps.get(name);
                if(s!=null){
                    if(s instanceof Number){
                        if(s instanceof Integer){
                            if(ObjUtils.objToInt(item.get(name))!=ObjUtils.objToInt(s)){
                                flag =false;
                                continue;
                            }
                        }else{
                            if(Float.compare(ObjUtils.objToFloat(item.get(name)),ObjUtils.objToFloat(s))!=0){
                                flag =false;
                                continue;
                            }
                        }
                    }else{
                        if(!ObjUtils.objToStr(item.get(name)).equals(ObjUtils.objToStr(s))){
                            flag =false;
                            continue;
                        }
                    }
                }
            }
            if(flag){
                map = item;break;
            }

        }
        return map;
    }


    /**
     * Created by ljl 简化查找方法
     * 查找list中满足匹配条件的map
     * @param data  被匹配的list
     * @param maps  匹配条件
     * @return
     */
    public static Map<String,Object> findMap(List<Map<String,Object>> data, Map<String,Object> maps){
        Map<String,Object> map=null;
        for(Map<String,Object> item:data){
            boolean flag = true;
            for(String name:maps.keySet()){
                Object s = maps.get(name);
                if(s!=null){
                    if(s instanceof Number){
                        if(s instanceof Integer){
                            if(ObjUtils.objToInt(item.get(name))!=ObjUtils.objToInt(s)){
                                flag =false;
                                break;
                            }
                        }else{
                            if(Float.compare(ObjUtils.objToFloat(item.get(name)),ObjUtils.objToFloat(s))!=0){
                                flag =false;
                                break;
                            }
                        }
                    }else{
                        if(!ObjUtils.objToStr(item.get(name)).equals(ObjUtils.objToStr(s))){
                            flag =false;
                            break;
                        }
                    }
                }else {
                    flag = false;
                    break;
                }
            }
            if(flag){
                map = item;break;
            }

        }
        return map;
    }

    /**
     * 查询满足条件之一的map列表
     * @param data
     * @param maps
     * @return
     */
    public static List<Map<String,Object>>  findMapList(List<Map<String,Object>> data, Map<String,Object> maps){
        List<Map<String,Object>> list = new ArrayList<>();
        for(Map<String,Object> item:data){
            for(String name:maps.keySet()){
                Object s = maps.get(name);
                if(s!=null){
                    if(s instanceof Number){
                        if(s instanceof Integer){
                            if(ObjUtils.objToInt(item.get(name))==ObjUtils.objToInt(s)){
                                list.add(item);
                                break;
                            }
                        }else{
                            if(Float.compare(ObjUtils.objToFloat(item.get(name)),ObjUtils.objToFloat(s))==0){
                                list.add(item);
                                break;
                            }
                        }
                    }else{
                        if(ObjUtils.objToStr(item.get(name)).equals(ObjUtils.objToStr(s))){
                            list.add(item);
                            break;
                        }
                    }
                }
            }
        }
        return list;
    }

    public static int findMapPositon(List<Map<String,Object>> data, Map<String,Object> maps){
        int position = -1;
        for(int i=0; i< data.size(); i++){
            Map<String,Object> item = data.get(i);
            boolean flag = true;
            for(String name:maps.keySet()){
                Object s = maps.get(name);
                if(s!=null){
                    if(s instanceof Number){
                        if(s instanceof Integer){
                            if(ObjUtils.objToInt(item.get(name))!=ObjUtils.objToInt(s)){
                                flag =false;
                                continue;
                            }
                        }else{
                            if(Float.compare(ObjUtils.objToFloat(item.get(name)),ObjUtils.objToFloat(s))!=0){
                                flag =false;
                                continue;
                            }
                        }
                    }else{
                        if(!ObjUtils.objToStr(item.get(name)).equals(ObjUtils.objToStr(s))){
                            flag =false;
                            continue;
                        }
                    }
                }
            }
            if(flag){
                position = i;break;
            }

        }
        return position;
    }

    public static List<Map<String,Object>> findList(List<Map<String,Object>> data,String name,Object s){
        List<Map<String,Object>> list = new ArrayList<>();
        for(Map<String,Object> item:data){
            if(s instanceof Number){
                if(s instanceof Integer){
                    if(ObjUtils.objToInt(item.get(name))==ObjUtils.objToInt(s)){
                        list.add(item);
                    }
                }else{
                    if(Float.compare(ObjUtils.objToFloat(item.get(name)),ObjUtils.objToFloat(s))==0){
                        list.add(item);
                    }
                }
            }else{
                if(ObjUtils.objToStr(item.get(name)).indexOf(ObjUtils.objToStr(s))>=0){
                    list.add(item);
                }
            }
        }
        return list;
    }

    public static int listMapCount(List<Map<String,Object>> data,String name,Object s){
        int count = 0;
        for(Map<String,Object> item:data){
            if(s instanceof Number){
                if(s instanceof Integer){
                    if(ObjUtils.objToInt(item.get(name))==ObjUtils.objToInt(s)){
                        count++;
                    }
                }else{
                    if(Float.compare(ObjUtils.objToFloat(item.get(name)),ObjUtils.objToFloat(s))==0){
                        count++;
                    }
                }
            }else{
                if(ObjUtils.objToStr(item.get(name)).equals(ObjUtils.objToStr(s))){
                   count++;
                }
            }
        }
        return count;
    }


    /**
     * 降序
     */
    public static void sortDESC(List bData,final String name){
        Collections.sort(bData, new Comparator<Map<String, Object>>() {
            public int compare(Map<String, Object> o1, Map<String, Object> o2) {
                if(o1.get(name) instanceof Number){
                    return ObjUtils.objToInt(o2.get(name))-ObjUtils.objToInt(o1.get(name));
                }else{
                    return ObjUtils.objToStr(o2.get(name)).compareTo(ObjUtils.objToStr(o1.get(name)));
                }
            }
        });
    }

    /**
     * 升序
     */
    public static void sortASC(List bData,final String name){
        Collections.sort(bData, new Comparator<Map<String, Object>>() {
            public int compare(Map<String, Object> o1, Map<String, Object> o2) {
                if(o1.get(name) instanceof Number){
                    return ObjUtils.objToInt(o1.get(name))-ObjUtils.objToInt(o2.get(name));
                }else{
                    return ObjUtils.objToStr(o1.get(name)).compareTo(ObjUtils.objToStr(o2.get(name)));
                }
            }
        });
    }

    public static float mutiply(float a, float b){
        BigDecimal bigDecimalA = new BigDecimal(Float.toString(a));
        BigDecimal bigDecimalB = new BigDecimal(Float.toString(b));
        return bigDecimalA.multiply(bigDecimalB).floatValue();
    }

    public static float add(float a, float b){
        BigDecimal bigDecimalA = new BigDecimal(Float.toString(a));
        BigDecimal bigDecimalB = new BigDecimal(Float.toString(b));
        return bigDecimalA.add(bigDecimalB).floatValue();
    }

    public static float divide(float a, float b){
        BigDecimal bigDecimalA = new BigDecimal(Float.toString(a));
        BigDecimal bigDecimalB = new BigDecimal(Float.toString(b));
        return bigDecimalA.divide(bigDecimalB, BigDecimal.ROUND_CEILING).floatValue();
    }

    public static float subtract(float a, float b){
        BigDecimal bigDecimalA = new BigDecimal(Float.toString(a));
        BigDecimal bigDecimalB = new BigDecimal(Float.toString(b));
        return bigDecimalA.subtract(bigDecimalB).floatValue();
    }
}
