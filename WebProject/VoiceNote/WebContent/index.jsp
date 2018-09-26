<%@page import="java.sql.ResultSet"%>
<%@page import="java.sql.Statement"%>
<%@page import="java.sql.DriverManager"%>
<%@page import="java.sql.Connection"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Insert title here</title>
</head>
<body>

<%
String connectionUrl = "jdbc:sqlserver://localhost:1433;" 
        +"databaseName=Demo;"
        + "user=ljl;"
        + "password=pp123456;";
       
        //注册驱动程序所需语句
        Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
        Connection con = DriverManager.getConnection(connectionUrl);
  
        Statement stmt = con.createStatement();
        //从 “S”表中查询 “Sno”列和“Ssex”列
        ResultSet rs = stmt.executeQuery("select id,name from dbo.[userT]");
        //如果查到有数据，全部输出
        while(rs.next()){
            System.out.println(rs.getString("id")+","+rs.getString("name"));
        }
%>
</body>
</html>