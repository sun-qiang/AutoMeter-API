package com.api.autotest.common.utils;

import org.apache.jmeter.config.Arguments;

import java.io.FileOutputStream;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by harvey.xu on 2017/10/15.
 */
public class MysqlConnectionUtils {

    private static String url;
    private static String user;
    private static String password;
    public static Connection conn;
    public static ResultSet rs;
    public static PreparedStatement st;

    public static void initDbResource(String mysqluel,String mysqlusername,String mysqlpass ) {
        //PropertiesUtil pUtil = PropertiesUtil.getInstance("app.properties");
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
    public static void getConnection() throws Exception {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            conn = DriverManager.getConnection(url, user, password);
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }


    /**
     * 连接数据库
     */
    public static void getConnectionbycon(String conurl,String username,String password) throws Exception {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            conn = DriverManager.getConnection(conurl, username, password);
        } catch (Exception e) {
            throw new Exception(e.getMessage()+"|"+conurl+"|"+username+"|"+password);
        }
    }

    /**
     * 关闭数据库连接
     * @return
     */
    public static void closeConnection() throws Exception {
        if (st != null) {
            try {
                st.close();
                st=null;
            } catch (SQLException e) {
                throw new Exception(e.getMessage());
            }
        }
        if (rs != null) {
            try {
                rs.close();
                rs=null;
            } catch (SQLException e) {
                throw new Exception(e.getMessage());
            }
        }
        if (conn != null) {
            try {
                conn.close();
                conn=null;
            } catch (SQLException e) {
                throw new Exception(e.getMessage());
            }
        }
    }

    public static ArrayList<HashMap<String, String>> query(String sql) throws Exception {
        ArrayList<HashMap<String, String>> resultArrayList = new ArrayList<>();
        try {
            getConnection();
            st = conn.prepareStatement(sql);
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

    public static String update(String sql) throws Exception {
        String result="";
        try {
            getConnection();
            if(conn.isClosed()||conn==null)
            {
                getConnection();
            }
            st = conn.prepareStatement(sql);
            st.execute(sql);
        }  catch (Exception e) {
            throw  new Exception(e.getMessage());
        } finally {
            closeConnection();
        }
        return result;
    }

    public static int updatewithkey(String sql) throws Exception {
        int key=0;
        try {
            getConnection();
            if(conn.isClosed()||conn==null)
            {
                getConnection();
            }
            st = conn.prepareStatement(sql,Statement.RETURN_GENERATED_KEYS);
            st.execute();
            rs = st.getGeneratedKeys();
            if (rs.next()) {
                 key = rs.getInt(1);
            }
        }  catch (Exception e) {
            throw  new Exception(e.getMessage());
        } finally {
            closeConnection();
        }
        return key;
    }


//    public static String querybytes(String sql) throws Exception {
//        try {
//            PreparedStatement ps = conn
//                    .prepareStatement("select text from bankVoice where name=?");
//            ps.setString(1, file);
//            Blob blob = null;
//            ResultSet rs = ps.executeQuery();
//            if (rs.next()) {
//                blob = (Blob) rs.getBlob("text");
//            }
//            FileOutputStream fos = new FileOutputStream("D:\\test1.mp3");
//            fos.write(blob.getBytes(1, (int) blob.length()));
//            System.out.println("二进制文件获得成功");
//            ps.clearParameters();
//            ps.close();
//            fos.close();
//        } catch (Exception e) {
//            e.printStackTrace();
//            System.out.println("二进制文件读取时出现异常");
//        }
//    }
    //}





    public static void execsql(String sql) throws Exception {
        getConnection();
        int total = 0;
        try {
            st = conn.prepareStatement(sql);
            st.execute(sql);
        }  catch (Exception e) {
            throw new Exception("mysql执行sql发生异常: "+e.getMessage()+" sql:"+sql);
        } finally {
            closeConnection();
        }
    }


    public static void main(String[] args) {
        Arguments params = new Arguments();
        params.addArgument("env", "2");

//        JavaSamplerContext ctx = new JavaSamplerContext(params);
//        initDbResource(ctx);
//        String sql = "UPDATE acquire_trade t SET t.status=0 WHERE t.channel_code='100008' AND t.trade_day='20170601'";
//        String sql = "DELETE FROM recon_exp WHERE channel_code = '100006' AND trade_day='20170601'";
//        System.out.println(MysqlConnectionUtils.update(sql));
        //String sql = "SELECT t.`status` FROM acquire_trade t WHERE t.channel_code='100008' AND t.trade_day='20170601' ORDER BY `status`";
        //System.out.println(MysqlConnectionUtils.query(sql));
    }
}
