package com.teradata.db;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public abstract class OracleDB extends DataBaseUtil {
    private static Connection con = null;

    public OracleDB() throws Exception {
    }

    public Connection getConnection() {
        try {
            Class.forName(getDriver());
            con = DriverManager.getConnection(getUrl(), getUser(), getPassword());
            return con;
        } catch (Exception ex) {
            System.out.println("2:" + ex.getMessage());
        }
        return con;
    }

    @Override
    public List<FieldInfo> getTableFieldsBySql(String Table) throws Exception {
        Table = Table.toUpperCase();

        getConnection();

        List<HashMap<String, String>> columns = new ArrayList<HashMap<String, String>>();
        List<FieldInfo> fieldInfos = new ArrayList<>();

        try {
            Statement stmt = con.createStatement();

            String sql =
                    "select " +
                            "         comments as \"Name\"," +
                            "         a.column_name \"Code\"," +
                            "         a.DATA_TYPE as \"DataType\"," +
                            "         b.comments as \"Comment\"," +
                            "         decode(c.column_name,null,'FALSE','TRUE') as \"Primary\"," +
                            "         decode(a.NULLABLE,'N','TRUE','Y','FALSE','') as \"Mandatory\"," +
                            "         '' \"sequence\"" +
                            "   from " +
                            "       all_tab_columns a, " +
                            "       all_col_comments b," +
                            "       (" +
                            "        select a.constraint_name, a.column_name" +
                            "          from user_cons_columns a, user_constraints b" +
                            "         where a.constraint_name = b.constraint_name" +
                            "               and b.constraint_type = 'P'" +
                            "               and a.table_name = '" + Table + "'" +
                            "       ) c" +
                            "   where " +
                            "     a.Table_Name=b.table_Name " +
                            "     and a.column_name=b.column_name" +
                            "     and a.Table_Name='" + Table + "'" +
                            "     and a.owner=b.owner " +
                            "     and a.owner='" + getUser().toUpperCase() + "'" +
                            "     and a.COLUMN_NAME = c.column_name(+)" +
                            "  order by a.COLUMN_ID";
            System.out.println(sql);
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                HashMap<String, String> map = new HashMap<String, String>();
                FieldInfo fieldInfo = new FieldInfo();
                fieldInfo.setCommit(rs.getString("Comment"));
                fieldInfo.setFieldName(rs.getString("Code").toLowerCase());
//                fieldInfo.setCanNull(rs.getString("Code").toLowerCase());
                fieldInfo.setIsKey(rs.getString("Primary"));
                fieldInfo.setType(rs.getString("DataType"));
                fieldInfos.add(fieldInfo);
//                map.put("DataType", rs.getString("DataType"));
//                map.put("Comment", rs.getString("Comment"));
//                map.put("Primary", rs.getString("Primary"));
//                map.put("Mandatory", rs.getString("Mandatory"));
//                columns.add(map);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            con.close();
        }
        return fieldInfos;
    }

    @Override
    public String type2Class(String type) {
        if ("VARCHAR2".equals(type)) {
            return "String";
        } else if ("NUMBER".equals(type)) {
            return "Integer";
        } else {
            return "String";
        }
    }
}
