package main;




import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class ObjUtils {
	  /**
     * ԭ���õ�4����cwk�����8λ����0����.
     * ԭ�������ݿⱣ����ǰ�λС����������ְ�λ�������ݿⱣ�����ݵ�����
     */
    public static DecimalFormat mDecimalFormat=new DecimalFormat("#.########");
    /**
     * ����תString
     *
     * @param obj
     * @return
     */
    public static String objToStr(Object obj) {

        String value;

        if(obj == null){
            value = "";
        }else if(obj instanceof Number){
            value = mDecimalFormat.format(obj);
            //value = mDecimalFormat.parse(String.valueOf(obj).trim());
        }else{
            value = String.valueOf(obj).trim();
        }
        //String value = (obj == null) ? "" : String.valueOf(obj).trim();

        ///return (obj == null) ? "" : String.valueOf(obj).trim();

        return value;
    }

    public static String objToStrNull(Object obj) {
        return (obj == null) ? null : String.valueOf(obj).trim();
    }


    /**
     * ����תDouble
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
     * ����תInt
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

    public static Long objToLong(Object obj) {
        if (!objToStr(obj).equals("") && !objToStr(obj).equals("null")) {
            return Long.parseLong(objToStr(obj));
        } else {
            return 0L;
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
            return (Date) obj;
        }catch (Exception e){
            return new Date(0);
        }
    }
    /**
     * list��ȿ���
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
     * ����
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
     * ����
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
}
