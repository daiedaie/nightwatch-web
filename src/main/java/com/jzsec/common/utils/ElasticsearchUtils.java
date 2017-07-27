package com.jzsec.common.utils;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.pool.ElasticSearchDruidDataSourceFactory;
import com.jzsec.common.config.LoadConfig;
import com.jzsec.modules.func.entity.FuncField;
import net.sf.json.JSONObject;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * Created by caodaoxi on 16-10-20.
 */
public class ElasticsearchUtils {
    private static DruidDataSource dds = null;
    static {
        Properties properties = new Properties();
        properties.put("url", LoadConfig.getElasticsearchNodeUrl());
        try {
            dds = (DruidDataSource) ElasticSearchDruidDataSourceFactory.createDataSource(properties);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static List<JSONObject> search(List<FuncField> funcFields, String sql) {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        List<JSONObject> hits = new ArrayList<JSONObject>();
        try {
            con = dds.getConnection();
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();
//            int count = metaData.getColumnCount();
            System.out.println("===============sql :" + sql);
            JSONObject hit = null;
            while (rs.next()) {
                hit = new JSONObject();
                for(FuncField funcField : funcFields) {
                    try {
                        hit.put(funcField.getFieldName(), rs.getObject(funcField.getFieldName()));
                    } catch (Exception e) {
                        e.printStackTrace();
                        continue;
                    }
                }
                hits.add(hit);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            close(con, ps, rs);
        }
        return hits;
    }

    public static void close(Connection con, PreparedStatement ps, ResultSet rs) {
        try {
            if (con != null) con.close();
            if (ps != null) ps.close();
            if (rs != null) rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
