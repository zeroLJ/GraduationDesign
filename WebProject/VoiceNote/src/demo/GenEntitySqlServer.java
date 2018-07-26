package demo;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import datasourse.DBOperation;
  
public class GenEntitySqlServer{  
      
    private String entityOutPath = "datasourse.entity";//ָ��ʵ���������ڰ���·��
    private String queryOutPath = "datasourse.query";//ָ����ѯ���������ڰ���·��
    
    private String authorName = "ljl";//��������  
    private String tablename = "note";//����  
    private String[] colnames; // ��������  
    private String[] colTypes; //������������  
    private int[] colSizes; //������С����  
    private boolean f_util = false; // �Ƿ���Ҫ�����java.util.*  
    private boolean f_sql = false; // �Ƿ���Ҫ�����java.sql.*  
      
    //���ݿ�����  
//    private static final String URL ="jdbc:microsoft:sqlserver://localhost:1433";  
//    private static final String NAME = "ljl";  
//    private static final String PASS = "pp123456";  
    private static final String URL = "jdbc:sqlserver://localhost:1433;" 
								            +"databaseName=demo;"
								            + "user=ljl;"
								            + "password=pp123456;";
    private static final String DRIVER ="com.microsoft.sqlserver.jdbc.SQLServerDriver";  
  
    /* 
     * ���캯�� 
     */  
    public GenEntitySqlServer(){  
        //��������  
        Connection con;  
        //��Ҫ����ʵ����ı�  
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
            Statement stmt = con.createStatement();
//            ResultSet rs = stmt.executeQuery(sql);
//            ResultSetMetaData rsmd = rs.getMetaData();
            pStemt = con.prepareStatement(sql);  
            ResultSetMetaData rsmd = pStemt.getMetaData();
            int size = rsmd.getColumnCount();   //ͳ����  
            colnames = new String[size];  
            colTypes = new String[size];  
            colSizes = new int[size];  
            for (int i = 0; i < size; i++) {  
                colnames[i] = rsmd.getColumnName(i + 1);  
                colTypes[i] = rsmd.getColumnTypeName(i + 1);  
                  
                if(colTypes[i].equalsIgnoreCase("datetime")){  
                    f_util = true;  
                }  
                if(colTypes[i].equalsIgnoreCase("image") || colTypes[i].equalsIgnoreCase("text")){  
                    f_sql = true;  
                }  
                colSizes[i] = rsmd.getColumnDisplaySize(i + 1);  
            }  
              
            String classStr = parse(colnames,colTypes,colSizes);  
            String queryClassStr = toQueryClassStr(colnames,colTypes,colSizes);  
            //����ʵ�����ļ�
            try {  
                File directory = new File("");  
                //System.out.println("����·����"+directory.getAbsolutePath());  
                //System.out.println("���·����"+directory.getCanonicalPath());           
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
                pw.println(classStr);  
                pw.flush();  
                pw.close(); 
                
                
            } catch (IOException e) {  
                e.printStackTrace();  
            }  
            
            //���ɲ�ѯ���ļ�
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
            //�Ͽ�����
            con.close();
        } catch (SQLException e) {  
            e.printStackTrace();  
        }
    }  
    
    /** 
     * ���ܣ����ɲ�ѯ��������� 
     * @param colnames 
     * @param colTypes 
     * @param colSizes 
     * @return 
     */  
    private String toQueryClassStr(String[] colnames, String[] colTypes, int[] colSizes) {  
        StringBuffer sb = new StringBuffer();  
        sb.append("package " + this.queryOutPath + ";\r\n");  
        //�ж��Ƿ��빤�߰�  
        if(f_util){  
            sb.append("import java.util.Date;\r\n");  
        }  
        if(f_sql){  
            sb.append("import java.sql.*;\r\n");  
        }  
        sb.append("\r\n");  
        //ע�Ͳ���  
        sb.append("/**\r\n");  
        sb.append("* "+tablename+" ��ѯ\r\n");  
        sb.append("* "+new Date()+" "+this.authorName+"\r\n");  
        sb.append("*/ \r\n");  
        //ʵ�岿��  
        sb.append("\r\n\r\npublic class " + initcap(tablename) + "Query" + " extends BaseQuery" + "{\r\n");  
        processAllAttrs(sb);//����  
        processAllMethod(sb);//get set����  
        sb.append("}\r\n");  
        return sb.toString();  
    }  
  
    /** 
     * ���ܣ�����ʵ����������� 
     * @param colnames 
     * @param colTypes 
     * @param colSizes 
     * @return 
     */  
    private String parse(String[] colnames, String[] colTypes, int[] colSizes) {  
        StringBuffer sb = new StringBuffer();  
        sb.append("package " + this.entityOutPath + ";\r\n");  
        //�ж��Ƿ��빤�߰�  
        if(f_util){  
            sb.append("import java.util.Date;\r\n");  
        }  
        if(f_sql){  
            sb.append("import java.sql.*;\r\n");  
        }  
        sb.append("\r\n");  
        //ע�Ͳ���  
        sb.append("/**\r\n");  
        sb.append("* "+tablename+" ʵ����\r\n");  
        sb.append("* "+new Date()+" "+this.authorName+"\r\n");  
        sb.append("*/ \r\n");  
        //ʵ�岿��  
        sb.append("\r\n\r\npublic class " + initcap(tablename) + "{\r\n");  
        processAllAttrs(sb);//����  
        processAllMethod(sb);//get set����  
        sb.append("}\r\n");  
          
        //System.out.println(sb.toString());  
        return sb.toString();  
    }  
      
    /** 
     * ���ܣ������������� 
     * @param sb 
     */  
    private void processAllAttrs(StringBuffer sb) {  
          
        for (int i = 0; i < colnames.length; i++) {  
            sb.append("\tprivate " + sqlType2JavaType(colTypes[i]) + " " + colnames[i] + ";\r\n");  
        }  
          
    }  
  
    /** 
     * ���ܣ��������з��� 
     * @param sb 
     */  
    private void processAllMethod(StringBuffer sb) {  
          
        for (int i = 0; i < colnames.length; i++) {  
            sb.append("\tpublic void set" + initcap(colnames[i]) + "(" + sqlType2JavaType(colTypes[i]) + " " +   
                    colnames[i] + "){\r\n");  
            sb.append("\t\tthis." + colnames[i] + "=" + colnames[i] + ";\r\n");  
            sb.append("\t}\r\n");  
            sb.append("\tpublic " + sqlType2JavaType(colTypes[i]) + " get" + initcap(colnames[i]) + "(){\r\n");  
            sb.append("\t\treturn " + colnames[i] + ";\r\n");  
            sb.append("\t}\r\n");  
        }  
          
    }  
      
    /** 
     * ���ܣ��������ַ���������ĸ�ĳɴ�д 
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
     * ���ܣ�����е��������� 
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
     * ���� 
     * TODO 
     * @param args 
     */  
    public static void main(String[] args) {  
          
//        new GenEntitySqlServer();  
    	
//    	NoteQuery noteQuery = new NoteQuery();
//    	noteQuery.setName("111");
//        System.out.println(noteQuery.toSqlStr());
        
        List<Object> list = new ArrayList<>();
        list.add("adsasdas");
        DBOperation dbOperation = new DBOperation("select 1 where a = ?", list);
//        System.out.println(dbOperation.getExecuteSQL());
    }  
  
}  