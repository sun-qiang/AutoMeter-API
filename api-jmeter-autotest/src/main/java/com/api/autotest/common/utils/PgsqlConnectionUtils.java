package com.api.autotest.common.utils;


import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;


public class PgsqlConnectionUtils {

    private static String url;
    private static String user;
    private static String password;
    public static Connection conn;
    public static ResultSet rs;
    public static Statement st;

    public static void initDbResource(String mysqluel,String mysqlusername,String mysqlpass ) {
        url =mysqluel;// pUtil.getProperty("mysql.host");
        //System.out.println(url);
        user =mysqlusername;// pUtil.getProperty("username");
        //System.out.println(user);
        password = mysqlpass;// pUtil.getProperty("password");
        //System.out.println(password);


    }
    /**
     * 连接数据库
     */
    public static void getConnection() {
        try {
            Class.forName("org.postgresql.Driver");
            conn = DriverManager.getConnection(url, user, password);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 关闭数据库连接
     * @return
     */
    public static void closeConnection() {
        if (st != null) {
            try {
                st.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        if (rs != null) {
            try {
                rs.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public static ArrayList<HashMap<String, String>> query(String sql) throws Exception {
        ArrayList<HashMap<String, String>> resultArrayList = new ArrayList<>();
        try {
            getConnection();
            st = conn.createStatement();
            rs = st.executeQuery(sql);
            ResultSetMetaData data = rs.getMetaData();
            while (rs.next()) {
                HashMap<String, String> map = new HashMap<>();
                for (int i = 1; i <= data.getColumnCount(); i++) {
                    String key = data.getColumnName(i);
                    String value = rs.getString(key);
                    map.put(key, value);
                }
                resultArrayList.add(map);
            }
        } catch (Exception e) {
            throw  new Exception(e.getMessage());
        } finally {
            closeConnection();
        }
        return resultArrayList;
    }

    public static String update(String sql) {
        getConnection();
        String result="";
        try {
            st = conn.createStatement();
            st.executeUpdate(sql);
        }  catch (Exception e) {
            result=e.getMessage();
        } finally {
            closeConnection();
        }
        return result;
    }


    public static int execsql(String sql) throws Exception {
        getConnection();
        int total = 0;
        try {
            st = conn.createStatement();
            total=st.executeUpdate(sql);
        }  catch (Exception e) {
            throw new Exception("pgsql执行sql发生异常: "+e.getMessage()+" sql:"+sql);
        } finally {
            closeConnection();
        }
        return total;
    }
}
