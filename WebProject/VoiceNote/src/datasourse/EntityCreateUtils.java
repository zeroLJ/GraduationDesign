package datasourse;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import datasourse.type.FieldType;
  
/**
 * @author ljl
 * 数据库表  查询类和实体类的生成工具类
 * 修改相关信息直接run即可
 */
public class EntityCreateUtils{  
//    private String entityOutPath = "datasourse.entity";//指定实体生成所在包的路径
//    private String queryOutPath = "datasourse.query";//指定查询类生成所在包的路径
	private String entityOutPath = "database.entity";//指定实体生成所在包的路径
    private String queryOutPath = "database.query";//指定查询类生成所在包的路径
    private String tablename = "userT";//表名  
    //数据库连接  
    private static final String URL = "jdbc:sqlserver://localhost:1433;" 
								            +"databaseName=demo;"
								            + "user=ljl;"
								            + "password=pp123456;";
    private static final String DRIVER ="com.microsoft.sqlserver.jdbc.SQLServerDriver"; 
    private String authorName = "ljl";//生成者名字  
    
    
    /******************************** 以下代码不需修改    **************************************/
    
    private String[] colnames; // 列名数组  
    private FieldType[] colTypes; //列名类型数组  
    private int[] colSizes; //列名大小数组  
    private List<String> keyList;//主键名称列表
  
    /* 
     * 构造函数 
     */  
    public EntityCreateUtils(){  
        //创建连接  
        Connection con;  
        //查要生成实体类的表  
        String sql = "select * from " + tablename;  
        PreparedStatement pStemt = null;  
        try {  
            try {  
                Class.forName(DRIVER);  
            } catch (ClassNotFoundException e1) {  
                // TODO Auto-generated catch block  
                e1.printStackTrace();  
            }  
            con = DriverManager.getConnection(URL);  
            pStemt = con.prepareStatement(sql);  
           
           
            //获取主键
            keyList = new ArrayList<>();
            ResultSet pkRSet = con.getMetaData().getPrimaryKeys(null, null, tablename);
            while(pkRSet.next() ) {
                System.err.println("****** Comment ******");
                System.err.println("TABLE_CAT  : "+pkRSet.getObject(1));
                System.err.println("TABLE_SCHEM: "+pkRSet.getObject(2));
                System.err.println("TABLE_NAME : "+pkRSet.getObject(3));
                System.err.println("COLUMN_NAME: "+pkRSet.getObject(4));
                System.err.println("KEY_SEQ    : "+pkRSet.getObject(5));
                System.err.println("PK_NAME    : "+pkRSet.getObject(6));
                System.err.println("****** ******* ******");    
                keyList.add(pkRSet.getString(4));
            }

            System.err.println(con.getMetaData().getIdentifierQuoteString());
            if(true) {
            	return;
            }
            
            ResultSetMetaData rsmd = pStemt.getMetaData();
            int size = rsmd.getColumnCount();   //统计列  
            colnames = new String[size];  
            colTypes = new FieldType[size];  
            colSizes = new int[size];  
            for (int i = 0; i < size; i++) {  
                colnames[i] = rsmd.getColumnName(i + 1);  
                colTypes[i] = sqlTypeToFieldType(rsmd.getColumnTypeName(i + 1));       
                colSizes[i] = rsmd.getColumnDisplaySize(i + 1);  
            }  
              
            String entityClassStr = toEntityClassStr(colnames,colTypes,colSizes, keyList);  
            String queryClassStr = toQueryClassStr(colnames,colTypes,colSizes);  
            //生成实体类文件
            try {  
                File directory = new File("");  
                //System.out.println("绝对路径："+directory.getAbsolutePath());  
                //System.out.println("相对路径："+directory.getCanonicalPath());           
//                System.out.println("src/?/"+path.substring(path.lastIndexOf("/com/", path.length())) );  
//              String outputPath = directory.getAbsolutePath()+ "/src/"+path.substring(path.lastIndexOf("/com/", path.length()), path.length()) + initcap(tablename) + ".java";  
                String outputPath = directory.getAbsolutePath()+ "/src/"+this.entityOutPath.replace(".", "/")+"/"+initcap(tablename) + ".java";  
                directory = new File(outputPath);
                System.out.println(directory.getAbsolutePath());           
                System.out.println(directory.getParentFile().getAbsolutePath());  
                if (!directory.getParentFile().exists()) {
					directory.getParentFile().mkdirs();
				}
                FileWriter fw = new FileWriter(outputPath);  
                PrintWriter pw = new PrintWriter(fw);  
                pw.println(entityClassStr);  
                pw.flush();  
                pw.close(); 
                
                
            } catch (IOException e) {  
                e.printStackTrace();  
            }  
            
            //生成查询类文件
            try {  
                File directory = new File("");  
                String outputPath = directory.getAbsolutePath()+ "/src/"+this.queryOutPath.replace(".", "/")+"/"+initcap(tablename) + "Query" + ".java";  
                directory = new File(outputPath);
                if (!directory.getParentFile().exists()) {
					directory.getParentFile().mkdirs();
				}
                FileWriter fw = new FileWriter(outputPath);  
                PrintWriter pw = new PrintWriter(fw);  
                pw.println(queryClassStr);  
                pw.flush();  
                pw.close(); 
            } catch (IOException e) {  
                e.printStackTrace();  
            }  
            //断开连接
            con.close();
        } catch (SQLException e) {  
            e.printStackTrace();  
        }
    }  
    
    /** 
     * 功能：生成查询类主体代码 
     * @param colnames 
     * @param colTypes 
     * @param colSizes 
     * @return 
     */  
    private String toQueryClassStr(String[] colnames, FieldType[] colTypes, int[] colSizes) {  
    	  StringBuffer sb = new StringBuffer();  
          sb.append("package " + this.queryOutPath + ";\r\n");  
          sb.append("import java.util.Arrays;\r\n");
          sb.append("import java.util.Collections;\r\n");
          sb.append("import java.util.HashMap;\r\n");
          sb.append("import java.util.List;\r\n");
          sb.append("import java.util.Map;\r\n");  
          sb.append("\r\n");  
   
          sb.append("import datasourse.BaseEntity;\r\n");  
          sb.append("import datasourse.BaseQuery;\r\n"); 
          sb.append("import ").append(entityOutPath).append(".").append(initcap(tablename)).append(";\r\n");  
          for(FieldType type : colTypes) {
          	if (type == FieldType.STRING && !sb.toString().contains("import datasourse.queryField.StringFieldQuery;\r\n")) {
  				sb.append("import datasourse.queryField.StringFieldQuery;\r\n");
  			}else if (type == FieldType.NUMBER && !sb.toString().contains("import datasourse.entityField.NumberFieldQuery;\r\n")) {
  				sb.append("import datasourse.queryField.NumberFieldQuery;\r\n");
  			}else if (type == FieldType.DATETIME && !sb.toString().contains("import datasourse.queryField.DateTimeFieldQuery;\r\n")) {
  				sb.append("import datasourse.queryField.DateTimeFieldQuery;\r\n");
  			}else if (type == FieldType.BOOLEAN && !sb.toString().contains("import datasourse.queryField.BooleanFieldQuery;\r\n")) {
  				sb.append("import datasourse.queryField.BooleanFieldQuery;\r\n");
  			}
          }
          sb.append("import datasourse.type.FieldType;\r\n");  
          
          //注释部分  
          sb.append("/**\r\n");  
          sb.append(" * @author "+authorName+"\r\n");  
          sb.append(" * "+tablename+" 查询类\r\n");  
          sb.append(" * "+getDateString()+"\r\n");  
          sb.append(" */ \r\n");  
          //实体部分  
          sb.append("\r\npublic class " + initcap(tablename) + "Query extends BaseQuery {\r\n");  
          sb.append("\t").append("private final static List<String> NAMELIST = Collections.unmodifiableList(Arrays.asList(new String[]{");
          for(int i = 0; i < colnames.length; i++) {
          	sb.append("\"").append(colnames[i]).append("\"");
          	if (i + 1 < colnames.length) {
  				sb.append(",");
  			}
          }
          sb.append("}));").append("\r\n");
          
          sb.append("\t").append("private final static List<FieldType> TYPELIST = Collections.unmodifiableList(Arrays.asList(new FieldType[]{");
          for(int i = 0; i < colTypes.length; i++) {
          	sb.append("FieldType.").append(colTypes[i]);
          	if (i + 1 < colTypes.length) {
  				sb.append(",");
  			}
          }
          sb.append("}));").append("\r\n");
          
          sb.append("\t").append("private final static Map<String,Integer> FIELDPOSITION;\r\n");
          sb.append("\t").append("private final static String tablename = \"").append(tablename).append("\";").append("\r\n");
          sb.append("\t").append("static{\r\n");
          sb.append("\t").append("\t").append("Map<String,Integer> m = new HashMap<String,Integer>();").append("\r\n");
          sb.append("\t").append("\t").append("for(int i=0;i<NAMELIST.size();i++){").append("\r\n");
          sb.append("\t").append("\t").append("\t").append("m.put(NAMELIST.get(i), i);").append("\r\n");
          sb.append("\t").append("\t").append("}").append("\r\n");
          sb.append("\t").append("\t").append("FIELDPOSITION = Collections.unmodifiableMap(m);").append("\r\n");
          sb.append("\t").append("}").append("\r\n");
          
          
          sb.append("\t").append("@Override").append("\r\n");
          sb.append("\t").append("public Class<? extends BaseEntity> getEntityClass(){").append("\r\n");
          sb.append("\t").append("\t").append("return ").append(initcap(tablename)).append(".class;").append("\r\n");
          sb.append("\t").append("}").append("\r\n");
          
          sb.append("\t").append("@Override").append("\r\n");
          sb.append("\t").append("public String getTableName() {").append("\r\n");
          sb.append("\t").append("\t").append("return tablename;").append("\r\n");
          sb.append("\t").append("}").append("\r\n");
          
          sb.append("\t").append("@Override").append("\r\n");
          sb.append("\t").append("public List<String> getFieldNames() {").append("\r\n");
          sb.append("\t").append("\t").append("return NAMELIST;").append("\r\n");
          sb.append("\t").append("}").append("\r\n");
          
          sb.append("\t").append("@Override").append("\r\n");
          sb.append("\t").append("public List<FieldType> getFieldTypes() {").append("\r\n");
          sb.append("\t").append("\t").append("return TYPELIST;").append("\r\n");
          sb.append("\t").append("}").append("\r\n");
          
          sb.append("\t").append("@Override").append("\r\n");
          sb.append("\t").append("public Map<String, Integer> getFieldPositionMap() {").append("\r\n");
          sb.append("\t").append("\t").append("return FIELDPOSITION;").append("\r\n");
          sb.append("\t").append("}").append("\r\n");
          
          
          for(int i = 0; i < colTypes.length; i++) {
          	  FieldType type = colTypes[i];
          	  String s = "StringField";
          	  if (type == FieldType.STRING) {
          		 s = "StringField";
  			  }else if (type == FieldType.NUMBER) {
  				 s = "NumberField";
  			  }else if (type == FieldType.DATETIME) {
  			 	 s = "DateTimeField";
  			  }else if (type == FieldType.BOOLEAN) {
  			 	 s = "BooleanField";
  			  }
          	  sb.append("\n");
          	  sb.append("\t").append("public ").append(s).append("Query ").append("Field_").append(initcap(colnames[i])).append("() {").append("\r\n");
              sb.append("\t").append("\t").append("return get").append(s).append("(").append(i).append(");").append("\r\n");
              sb.append("\t").append("}").append("\r\n");
          }
          sb.append("}"); 
          return sb.toString();  
    }  
  
    /** 
     * 功能：生成实体类主体代码 
     * @param colnames 字段名称列表
     * @param colTypes 字段类型列表
     * @param colSizes 
     * @param primaryKeys 主键名称列表
     * @return 
     */  
    private String toEntityClassStr(String[] colnames, FieldType[] colTypes, int[] colSizes, List<String> primaryKeys) {  
        StringBuffer sb = new StringBuffer();  
        sb.append("package " + this.entityOutPath + ";\r\n");  
        sb.append("import java.util.Arrays;\r\n");
        sb.append("import java.util.Collections;\r\n");
        sb.append("import java.util.HashMap;\r\n");
        sb.append("import java.util.List;\r\n");
        sb.append("import java.util.Map;\r\n");  
        sb.append("\r\n");  
 
        sb.append("import datasourse.BaseEntity;\r\n");  
        for(FieldType type : colTypes) {
        	if (type == FieldType.STRING && !sb.toString().contains("import datasourse.entityField.StringFieldValue;\r\n")) {
				sb.append("import datasourse.entityField.StringFieldValue;\r\n");
			}else if (type == FieldType.NUMBER && !sb.toString().contains("import datasourse.entityField.NumberFieldValue;\r\n")) {
				sb.append("import datasourse.entityField.NumberFieldValue;\r\n");
			}else if (type == FieldType.DATETIME && !sb.toString().contains("import datasourse.entityField.DateTimeFieldValue;\r\n")) {
				sb.append("import datasourse.entityField.DateTimeFieldValue;\r\n");
			}else if (type == FieldType.BOOLEAN && !sb.toString().contains("import datasourse.entityField.BooleanFieldValue;\r\n")) {
				sb.append("import datasourse.entityField.BooleanFieldValue;\r\n");
			}
        }
        sb.append("import datasourse.type.FieldType;\r\n");  
        
        //注释部分  
        sb.append("/**\r\n");  
        sb.append(" * @author "+authorName+"\r\n");  
        sb.append(" * "+tablename+" 实体类\r\n");  
        sb.append(" * "+getDateString()+"\r\n");  
        sb.append(" */ \r\n");  
        //实体部分  
        sb.append("\r\npublic class " + initcap(tablename) + " extends BaseEntity {\r\n");  
        sb.append("\t").append("private final static List<String> NAMELIST = Collections.unmodifiableList(Arrays.asList(new String[]{");
        for(int i = 0; i < colnames.length; i++) {
        	sb.append("\"").append(colnames[i]).append("\"");
        	if (i + 1 < colnames.length) {
				sb.append(",");
			}
        }
        sb.append("}));").append("\r\n");
        
        sb.append("\t").append("private final static List<FieldType> TYPELIST = Collections.unmodifiableList(Arrays.asList(new FieldType[]{");
        for(int i = 0; i < colTypes.length; i++) {
        	sb.append("FieldType.").append(colTypes[i]);
        	if (i + 1 < colTypes.length) {
				sb.append(",");
			}
        }
        sb.append("}));").append("\r\n");
        
        sb.append("\t").append("private final static Map<String,Integer> FIELDPOSITION;\r\n");
        sb.append("\t").append("private final static String tablename = \"").append(tablename).append("\";").append("\r\n");
        sb.append("\t").append("static{\r\n");
        sb.append("\t").append("\t").append("Map<String,Integer> m = new HashMap<String,Integer>();").append("\r\n");
        sb.append("\t").append("\t").append("for(int i=0;i<NAMELIST.size();i++){").append("\r\n");
        sb.append("\t").append("\t").append("\t").append("m.put(NAMELIST.get(i), i);").append("\r\n");
        sb.append("\t").append("\t").append("}").append("\r\n");
        sb.append("\t").append("\t").append("FIELDPOSITION = Collections.unmodifiableMap(m);").append("\r\n");
        sb.append("\t").append("}").append("\r\n");
        
        sb.append("\t").append("private final static List<String> KEYLIST = Collections.unmodifiableList(Arrays.asList(new String[]{");
        for(int i = 0; i < primaryKeys.size(); i++) {
        	sb.append("\"").append(primaryKeys.get(i)).append("\"");
        	if (i + 1 < primaryKeys.size()) {
				sb.append(",");
			}
        }
        sb.append("}));").append("\r\n");
        
        sb.append("\t").append("@Override").append("\r\n");
        sb.append("\t").append("protected List<String> getKeys() {").append("\r\n");
        sb.append("\t").append("\t").append("return KEYLIST;").append("\r\n");
        sb.append("\t").append("}").append("\r\n");
        
        sb.append("\t").append("@Override").append("\r\n");
        sb.append("\t").append("public String getTableName() {").append("\r\n");
        sb.append("\t").append("\t").append("return tablename;").append("\r\n");
        sb.append("\t").append("}").append("\r\n");
        
        sb.append("\t").append("@Override").append("\r\n");
        sb.append("\t").append("public List<String> getFieldNames() {").append("\r\n");
        sb.append("\t").append("\t").append("return NAMELIST;").append("\r\n");
        sb.append("\t").append("}").append("\r\n");
        
        sb.append("\t").append("@Override").append("\r\n");
        sb.append("\t").append("public List<FieldType> getFieldTypes() {").append("\r\n");
        sb.append("\t").append("\t").append("return TYPELIST;").append("\r\n");
        sb.append("\t").append("}").append("\r\n");
        
        sb.append("\t").append("@Override").append("\r\n");
        sb.append("\t").append("public Map<String, Integer> getFieldPositionMap() {").append("\r\n");
        sb.append("\t").append("\t").append("return FIELDPOSITION;").append("\r\n");
        sb.append("\t").append("}").append("\r\n");
        
        
        for(int i = 0; i < colTypes.length; i++) {
        	FieldType type = colTypes[i];
        	String s = "StringField";
        	if (type == FieldType.STRING) {
        		s = "StringField";
			}else if (type == FieldType.NUMBER) {
				s = "NumberField";
			}else if (type == FieldType.DATETIME) {
				s = "DateTimeField";
			}else if (type == FieldType.BOOLEAN) {
				s = "BooleanField";
			}
        	sb.append("\n");
        	sb.append("\t").append("public ").append(s).append("Value ").append("Field_").append(initcap(colnames[i])).append("() {").append("\r\n");
            sb.append("\t").append("\t").append("return get").append(s).append("(").append(i).append(");").append("\r\n");
            sb.append("\t").append("}").append("\r\n");
        }
        sb.append("}"); 
        return sb.toString();  
    }  
      
      
    /** 
     * 功能：将输入字符串的首字母改成大写 
     * @param str 
     * @return 
     */  
    private String initcap(String str) {  
          
        char[] ch = str.toCharArray();  
        if(ch[0] >= 'a' && ch[0] <= 'z'){  
            ch[0] = (char)(ch[0] - 32);  
        }  
          
        return new String(ch);  
    }  
  
    /** 
     * 功能：获得列的数据类型 
     * @param sqlType 
     * @return 
     */  
    private String sqlType2JavaType(String sqlType) {  
          
        if(sqlType.equalsIgnoreCase("bit")){  
            return "boolean";  
        }else if(sqlType.equalsIgnoreCase("tinyint")){  
            return "byte";  
        }else if(sqlType.equalsIgnoreCase("smallint")){  
            return "short";  
        }else if(sqlType.equalsIgnoreCase("int")){  
            return "Integer";  
        }else if(sqlType.equalsIgnoreCase("bigint")){  
            return "long";  
        }else if(sqlType.equalsIgnoreCase("float")){  
            return "float";  
        }else if(sqlType.equalsIgnoreCase("decimal") || sqlType.equalsIgnoreCase("numeric")   
                || sqlType.equalsIgnoreCase("real") || sqlType.equalsIgnoreCase("money")   
                || sqlType.equalsIgnoreCase("smallmoney")){  
            return "double";  
        }else if(sqlType.equalsIgnoreCase("varchar") || sqlType.equalsIgnoreCase("char")   
                || sqlType.equalsIgnoreCase("nvarchar") || sqlType.equalsIgnoreCase("nchar")   
                || sqlType.equalsIgnoreCase("text")){  
            return "String";  
        }else if(sqlType.equalsIgnoreCase("datetime")){  
            return "Date";  
        }else if(sqlType.equalsIgnoreCase("image")){  
            return "Blod";  
        }  
          
        return null;  
    }  
    
    /** 
     * 功能：获得列的数据类型 
     * @param sqlType 
     * @return 
     */  
    private FieldType sqlTypeToFieldType(String sqlType) {  
        System.out.println(sqlType);  
        if(sqlType.equalsIgnoreCase("bit")){  
            return FieldType.BOOLEAN;  
        }else if(sqlType.equalsIgnoreCase("tinyint")){  
        	//待定
            return FieldType.STRING;  
        }else if(sqlType.equalsIgnoreCase("smallint")){  
            return FieldType.NUMBER;  
        }else if(sqlType.equalsIgnoreCase("int")){  
            return FieldType.NUMBER;  
        }else if(sqlType.equalsIgnoreCase("bigint")){  
            return FieldType.NUMBER;  
        }else if(sqlType.equalsIgnoreCase("float")){  
            return FieldType.NUMBER;  
        }else if(sqlType.equalsIgnoreCase("decimal") || sqlType.equalsIgnoreCase("numeric")   
                || sqlType.equalsIgnoreCase("real") || sqlType.equalsIgnoreCase("money")   
                || sqlType.equalsIgnoreCase("smallmoney")){  
            return FieldType.NUMBER;  
        }else if(sqlType.equalsIgnoreCase("varchar") || sqlType.equalsIgnoreCase("char")   
                || sqlType.equalsIgnoreCase("nvarchar") || sqlType.equalsIgnoreCase("nchar")   
                || sqlType.equalsIgnoreCase("text")){  
            return FieldType.STRING;  
        }else if(sqlType.equalsIgnoreCase("datetime") || sqlType.equalsIgnoreCase("date")){  
            return FieldType.DATETIME;  
        }else if(sqlType.equalsIgnoreCase("image")){
        	//待定
            return FieldType.STRING;  
        }  
          
        return FieldType.STRING;  
    }  
    
    private String getDateString() {
		SimpleDateFormat fmt=new SimpleDateFormat("yyyy-MM-dd hh:mm");
		return fmt.format(new Date());
	}
      
    /** 
     * 出口 
     * TODO 
     * @param args 
     */  
    public static void main(String[] args) {  
        new EntityCreateUtils();  
    }  
  
}  