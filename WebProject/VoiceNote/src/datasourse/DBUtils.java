package datasourse;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import datasourse.type.FieldType;

public class DBUtils {
	private Connection connection;
	private String connectionUrl = 
    		"jdbc:sqlserver://localhost:1433;" 
 	               +"databaseName=demo;"
 	               + "user=ljl;"
 	               + "password=pp123456;"; 
	public DBUtils() throws SQLException {
		try {
			Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		connection = DriverManager.getConnection(connectionUrl);
		//设置自动提交事务为false； 即需要手动使用connection.commit提交
		connection.setAutoCommit(false);
	}
	
	public Connection getConnection() {
		return connection;
	}
	
	public boolean saveToDB(DBOperation operation) {
		return saveToDB(Arrays.asList(new DBOperation[] {operation}));
	}
	
	public boolean saveToDB(BaseEntity entity) {
		return saveToDB(entity.toDBOperation());
	}	
	
	public boolean saveToDB(List<? extends DBOperation> operations) {
		try {
			for (DBOperation operation : operations) {
				if (operation.getSQL().equals("")) {
					continue;
				}
				PreparedStatement pStatement = createStatement(operation.toDBOperation());
				int a = pStatement.executeUpdate();
				System.out.println("影响行数:" + a);
			}
			connection.commit();
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
		
	}
	
	
	public List<Map<String, Object>> queryList(BaseQuery query) {
		return queryList(query.toDBOperation());
	}
	
	public List<Map<String, Object>> queryList(DBOperation operation){
		if (operation.getSQL().equals("")) {
			return new ArrayList<>();
		}
		try {
			PreparedStatement pStatement = createStatement(operation);
			ResultSet resultSet = pStatement.executeQuery();
			List<Map<String, Object>> list = new ArrayList<>();
			while (resultSet.next()) {
				Map<String, Object> map = new HashMap<>();
				ResultSetMetaData metaData = resultSet.getMetaData();
				int count = metaData.getColumnCount();
				for(int i = 1; i <= count; i++) {
//					map.put(metaData.getColumnName(i), resultSet.getObject(i));		
					Object object = resultSet.getObject(i);
					map.put(metaData.getColumnLabel(i), object);
//					if (object!=null) {
//						System.out.println(i+":"+object.getClass());
//					}
				}
				list.add(map);
			}
			System.out.println("result："+list.toString());
			return list;
		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException("执行查询出错");
		}
//		return new ArrayList<>();
	}
	
	
	
	/**
	 * @param query 查询类 
	 * @return 符合查询条件的对应的实体类列表
	 * @throws SQLException 
	 */
	public <T extends BaseEntity> List<T> queryEntity(BaseQuery query) {
		return (List<T>) queryEntity(query, query.getEntityClass());
	}
	
	/**
	 * @param query 查询类 
	 * @return 符合查询条件的对应的实体类列表
	 * @throws SQLException 
	 */
	private <T extends BaseEntity> List<T> queryEntity(BaseQuery query, Class<T> c){
		List<Map<String, Object>> list = queryList(query);
		List<T> entityList = new ArrayList<>();
		for(Map<String, Object> map : list) {
			T t = null;
			try {
				t = c.newInstance();
			} catch (InstantiationException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}
			if (t!=null) {
				t.initData(map);
				t.initKeyField(map);
				entityList.add(t);
			}
		}
		return entityList;
	}
	
	private PreparedStatement createStatement(DBOperation operation) throws SQLException {
		PreparedStatement pStatement = connection.prepareStatement(operation.getSQL());
		List<FieldType> fieldTypes= operation.getParamTypes();
		List<Object> params = operation.getParams();
		for (int i = 0; i < params.size(); i++) {
			FieldType type = fieldTypes.get(i);
			if (type == FieldType.NUMBER) {
				pStatement.setBigDecimal(i+1, (BigDecimal) params.get(i));
			}else if (type == FieldType.STRING) {
				pStatement.setString(i+1, (String) params.get(i));
			}else if (type == FieldType.DATETIME){
//				java.sql.Date date = new java.sql.Date(value.getTime());
//				pStatement.setDate(count, date);
				String date = getDateString((Date) params.get(i));
				pStatement.setString(i+1, date);
			}else if (type == FieldType.BOOLEAN) {
				pStatement.setBoolean(i+1, (boolean) params.get(i));
			}
		}
		System.out.println("执行sql语句："+operation.getSQL());
		System.out.println("参数列表："+params.toString());
		System.out.println("参数类型："+fieldTypes.toString());
		return pStatement;
	}
	
	
	private String getDateString(Date date) {
		if (date==null) {
			return null;
		}
		SimpleDateFormat fmt=new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		return fmt.format(date);
	}
	
	public void destory() {
		if (connection!=null) {
			try {
				connection.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
	}
}
