package com.api.autotest.common.utils;

import org.apache.jmeter.config.Arguments;
import org.apache.jmeter.protocol.java.sampler.JavaSamplerContext;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by harvey.xu on 2017/10/15.
 */
public class OracleConnectionUtils {

    private static String url;
    private static String user;
    private static String password;
    public static Connection conn;
    public static ResultSet rs;
    public static Statement st;

    public static void initDbResource(JavaSamplerContext ctx) {
        if (ctx.getParameter("env").equals("1")) {
            // 开发环境
        } else if (ctx.getParameter("env").equals("2")) {
            // 测试环境
            url = "jdbc:oracle:thin:@139.224.37.98:1521:paydb";
            user = "ACCOUNT";
            password = "V0919@account";
        } else if (ctx.getParameter("env").equals("5")){
            // 代维环境
        }
    }

    /**
     * 连接数据库
     */
    public static void getConnection() {
        try {
            Class.forName("oracle.jdbc.driver.OracleDriver");
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

    public static ArrayList<HashMap<String, String>> query(String sql) {
        getConnection();
        ArrayList<HashMap<String, String>> resultArrayList = new ArrayList<>();
        try {
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
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeConnection();
        }
        return resultArrayList;
    }

    public static Integer update(String sql) {
        getConnection();
        int total = 0;
        try {
            st = conn.createStatement();
            total = st.executeUpdate(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeConnection();
        }
        return total;
    }

    public static void main(String[] args) {
        Arguments params = new Arguments();
        params.addArgument("env", "2");

        JavaSamplerContext ctx = new JavaSamplerContext(params);
        initDbResource(ctx);
//        String sql = "UPDATE acquire_trade t SET t.status=0 WHERE t.channel_code='100008' AND t.trade_day='20170601'";
//        String sql = "DELETE FROM recon_exp WHERE channel_code = '100006' AND trade_day='20170601'";
//        System.out.println(MysqlConnectionUtils.update(sql));
        String sql = "SELECT * FROM account.ac_transaction t where t.out_trade_no='Detail20171211174746908'";
        //System.out.println(OracleConnectionUtils.query(sql));
    }
}
