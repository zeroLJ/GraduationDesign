package com.zero.voicenote.database;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.query.WhereCondition;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by zero on 2018/1/20.
 * 进行增删改查询的统一工具类，所有使用GreenDao生成的表都可以使用此类
 */

public class DaoUtils {
    //工具类，不创建对象
    private DaoUtils() {

    }
    /**
     * 注意！！！！！！！！！每次新增表的时候必须在此方法添加对应的判断，否则新增的表无法使用此类。如：新增User表，就要添加getUserDao()；
     * 根据传入的对象获取对相应表进行增删改的实例
//     * @param entity 必须是使用GreenDAO表结构的对象
     * @return
     */
//    public static AbstractDao getDao(Class c){
//        if (c.isAssignableFrom(User.class)){
//            return GreenDaoManager.getInstance().getDaoSession().getUserDao();
//        }else if (c.isAssignableFrom(Student.class)){
//            return GreenDaoManager.getInstance().getDaoSession().getStudentDao();
//        }
//        return null;
//    }


    /**
     * 优化getDao()方法，通过反射获取相应的Dao实例，新增表时不需修改此类代码
     * 目前GreenDao获取Dao实例的方法名称都是get + 表名 + Dao这种格式，若以后有变，需要修改invokeMethod（）方法
     * @param c
     * @return
     */
    public static AbstractDao getDao(Class c){
        try {
            return (AbstractDao) invokeMethod(GreenDaoManager.getInstance().getDaoSession(), c.getSimpleName()+"Dao",null);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 通过反射获得对象属性的值（该类必须有相应的get方法,例如：要获取name变量的值，就要有定义getName（）的方法）
     */
    @SuppressWarnings("unchecked")
    private static Object invokeMethod(Object owner, String methodName,
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

    /**
     * 插入一条记录
     */
    public static boolean insert(BaseEntity entity){
        boolean flag = false;
        try {
           getDao(entity.getClass()).insert(entity);
            flag = true;
        }catch (Exception e){
            e.printStackTrace();
        }
        return flag;
    }

    /**
     * 插入多条记录
     */
    public static <T extends BaseEntity> boolean insert(List<T> entitys){
        boolean flag = false;
        try {
            getDao(entitys.get(0).getClass()).insertInTx(entitys);
            flag = true;
        }catch (Exception e){
            e.printStackTrace();
        }
        return flag;
    }

    /**
     * 通过map插入一条记录
     * @param c 要插入的表
     * @param map 带有表c所需的值，key值必须与表c的变量名相同
     * @return
     */
    public static boolean insert(Class c, Map<String,?> map){
        boolean flag = false;
        try {
            getDao(c).insert(mapToEntity(c, map));
            flag = true;
        }catch (Exception e){
            e.printStackTrace();
        }
        return flag;
    }

    /**
     * 通过map插入多条记录
     * @param c 要插入的表
     * @param list map中带有表c所需的值，key值必须与表c的变量名相同
     * @return
     */
    public static <T extends BaseEntity> boolean insert(Class<T> c, List<Map<String,Object>> list){
        boolean flag = false;
        try {
            List<T> mList = new ArrayList<>();
            for (Map<String, Object> note : list){
                mList.add(DaoUtils.mapToEntity(c, note));
            }
            insert(mList);
            flag = true;
        }catch (Exception e){
            e.printStackTrace();
        }
        return flag;
    }

    /**
     * 把map类型数据转化成表c数据的一个实例
     * @param c
     * @param map
     * @param <T>
     * @return
     */
    public static <T> T mapToEntity(Class<T> c, Map<String, ?> map){
        T t;
        try {
            t = c.newInstance();
            for (Field field : c.getDeclaredFields()){
                boolean a = field.isAccessible();
                if (!a) field.setAccessible(true);
                field.set(t, map.get(field.getName()));
                field.setAccessible(a);
//                if (field.getType().isAssignableFrom(Long.class)){
//                    invokeMethod2(t,field.getName(), ObjUtils.objToInt(map.get(field.getName())),null);
//                }
//                invokeMethod2(t,field.getName(),map.get(field.getName()),null);
            }
            return t;
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 根据条件删除记录
     * @param c 需要执行删除的表类、如User.class
     * @param condition 匹配条件，若为空，会删除所有记录
     * @param condMore  可增加复数条件
     */

    public static boolean delete(Class c,WhereCondition condition, WhereCondition... condMore){
        boolean flag = false;
        try {
            getDao(c).queryBuilder()
                    .where(condition, condMore).buildDelete().executeDeleteWithoutDetachingEntities();
            flag = true;
        }catch (Exception e){
            e.printStackTrace();
        }
        return flag;

    }

    /**
     * 根据条件查询
     * @param condition 匹配条件，若为空，会查询所有记录
     * @param condMore 可增加复数条件
     * @return
     */
    public static <T> List<T> query(Class<T> c, WhereCondition condition, WhereCondition... condMore){
        return getDao(c).queryBuilder()
                .where(condition, condMore).list();
    }

    /**
     * 获取记录总数量
     * @return
     */
    public static long getCount(Class entity){
        return getDao(entity).count();
    }

    /**
     * 获取所有记录
     * @return
     */
    public static <T> List<T> loadAll(Class<T> entity){
        return getDao(entity).loadAll();
    }

    /**
     * 根据主键查询
     * @param id
     * @return
     */
    public static <T> T load(Class<T> entity, Long id){
        return (T) getDao(entity).load(id);
    }



    /**
     * 更新某条记录
     * @param entity 必须通过query查询出来，修改后再调用此方法，不能直接new
     */
    public static boolean updata(Object entity){
        boolean flag = false;
        try {
            getDao(entity.getClass()).update(entity);
            flag = true;
        }catch (Exception e){
            e.printStackTrace();
        }
        return flag;
    }

}
