package com.straycat.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.naming.Context;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class DBUtility {
    private static final Logger logger = LoggerFactory.getLogger(DBUtility.class);
    private static final String DB_URL = ParmApi.getParameter("datasource.postgres.url");
    private static final String USER = ParmApi.getParameter("datasource.postgres.user");
    private static final String PASS = ParmApi.getParameter("datasource.postgres.password");
    private static String msgCode = "0004OB9002";

    public static Map<String, Object> query(Context context) {
        try {
            Map<String, Object> ret = new HashMap<>();
            return ret;
        } catch (Exception e) {
            logger.info(e.getMessage());
            throw e;
        }
//        return null;
    }
    public static List<Map<String, Object>> queryForList(String sqlName, Map<String, String> sqlParamMap) throws Exception {
        try {
            List<Map<String, Object>> result = new ArrayList<>();
            return result;
        } catch (Exception e) {
            logger.info(e.getMessage());
            throw new Exception(msgCode);
        }
    }
    private static List<Map<String, Object>> executeSqlQuery(String sql) throws SQLException {
        Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery(sql);
        List<Map<String, Object>> resultList = new ArrayList<>();
        ResultSetMetaData rsmd = rs.getMetaData();
        int columnsNumber = rsmd.getColumnCount();
        while (rs.next()) {
            Map<String, Object> rowData = new HashMap<>();
            for (int i = 1; i <= columnsNumber; i++) {
                rowData.put(rsmd.getColumnName(i), rs.getObject(i));
            }
            resultList.add(rowData);
        }
        rs.close();
        stmt.close();
        conn.close();
        return resultList;
    }
    public static void main(String[] args) {
        return;
    }
}
