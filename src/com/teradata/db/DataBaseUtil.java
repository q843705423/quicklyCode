package com.teradata.db;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public abstract class DataBaseUtil {

    private Connection con;
    private Statement stmt;
    private boolean transactionIsOpen;

    public DataBaseUtil() throws Exception {
        try {
            transactionIsOpen = false;
            Class.forName(getDriver());//com.mysql.jdbc.Driver
            con = getConnection();
        } catch (ClassNotFoundException e) {

            showError("please go to download the drive about " + getDriver() + " for java!!!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * please return your database driver.
     */
    protected abstract String getDriver();


    /**
     * please return user about your database.
     *
     * @return
     */
    protected abstract String getUser();

    /**
     * your can overwrite this method ,and return your database server IP, default is 127.0.0.1
     *
     * @return
     */
    protected String getIp() {
        return "127.0.0.1";
    }

    /**
     * your can overwrite this method ,and return your database server port, default is 3306
     *
     * @return
     */
    protected Integer getPort() {
        return 3306;
    }

    /**
     * your must overwrite it,this is your connected database name
     *
     * @return
     */
    protected abstract String getDatabaseName();

    /**
     * this is your database password,please overwrite it
     *
     * @return
     */
    protected abstract String getPassword();

    /**
     * @param ObjectProperty
     * @return
     */

    protected String objectProperty2tableFieldMap(String ObjectProperty) {

        return hump2Underscore(ObjectProperty);
    }

    protected String tableField2ObjectPropertyMap(String tableField) {

        return underscore2hump(tableField);
    }


    public String type2Class(String type) {
        if (type.equalsIgnoreCase("int")) {
            return "Integer";
        } else if (type.equalsIgnoreCase("bigint")) {
            return "Long";
        } else if (type.equalsIgnoreCase("float")) {
            return "Float";
        } else if (type.contains("int")) {
            return "Integer";
        } else if (type.equalsIgnoreCase("varchar")) {
            return "String";
        } else if (type.equalsIgnoreCase("double")) {
            return "Double";
        } else if (type.equalsIgnoreCase("blob")) {
            return "String";
        } else if (type.equalsIgnoreCase("date")) {
            return "String";
        }
        return "String";
    }


    protected String getUrl() {
        return "jdbc:mysql://${ip}:${port}/${databaseName}?useSSL=true&&characterEncoding=utf8"
                .replace("${ip}", getIp())
                .replace("${port}", getPort().toString())
                .replace("${databaseName}", getDatabaseName());
    }


    private void showError(String message) throws Exception {

        throw new Exception(message);
    }

    private Connection getConnection() throws Exception {

        if (con == null) {
            con = DriverManager.getConnection(getUrl(), getUser(), getPassword());
        }
        return con;
    }

    private Statement getStatement() throws Exception {
        if (stmt == null) {
            stmt = con.createStatement();
        }

        return stmt;
    }

    public void insert(Object obj, String tableName) throws Exception {

        List<FieldInfo> tableFileds = getTableFieldsBySql("select * from " + tableName);

        Map<String, Object> map = object2Map(obj);
        HashMap<String, Object> insertMap = new HashMap<>();
        for (FieldInfo tableFieldObject : tableFileds) {
            String tableField = tableFieldObject.getFieldName();
            if (map.keySet().contains(tableField2ObjectPropertyMap(tableField))) {
                insertMap.put(tableField, map.get(tableField2ObjectPropertyMap(tableField)));
            }
        }
        insert(insertMap, tableName);

    }

    public String object2String(Object object) {
        if (object instanceof Integer || object instanceof Long) {
            return object.toString();
        } else if (object instanceof String) {
            return "'${data}'".replace("${data}", object.toString());
        }
        return object.toString();
    }

    public void insert(HashMap<String, Object> map, String tableName) throws Exception {
        Set<String> keySet = map.keySet();
        String fields = "";
        String values = "";
        boolean first = true;
        for (String s : keySet) {
            if (first) {
                first = false;
                fields += s;
                values += object2String(map.get(s));
            } else {
                fields += "," + s;
                values += "," + object2String(map.get(s));
            }
        }
        String sql = "insert into ${tableName}(${fields}) values(${values})"
                .replace("${tableName}", tableName)
                .replace("${fields}", fields)
                .replace("${values}", values);
        con = getConnection();
        stmt = stmt == null || stmt.isClosed() ? (Statement) con.createStatement() : stmt;
        stmt.execute(sql);
    }

    private Map<String, Object> object2Map(Object obj) throws Exception {
        Map<String, Object> map = new HashMap<>();
        if (obj == null) {
            return map;
        }
        Class<?> clazz = obj.getClass();
        Field[] objFields = clazz.getDeclaredFields();
        String getMethodName = null;
        try {
            for (Field f : objFields) {
                getMethodName = getGetMethodName(f.getName());
                Method getMethod = clazz.getDeclaredMethod(getMethodName);
                Object o = getMethod.invoke(obj);
                if (o != null) {
                    map.put(f.getName(), o);
                }

            }
        } catch (NoSuchMethodException e) {
            showError("your don't have a method named :" + getMethodName);
        } catch (SecurityException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            showError("please check your method about " + getMethodName + ".");
        } catch (InvocationTargetException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }


        return map;
    }

    private String getGetMethodName(String objectProperty) {
        String getMethodName = "get" + objectProperty.substring(0, 1).toUpperCase() + objectProperty.substring(1);
        return getMethodName;

    }

    private String getSetMethodName(String objectProperty) {
        return objectProperty;
    }

    public List<Map<String, String>> select(String sql) throws Exception {
        List<FieldInfo> tableFields = getTableFieldsBySql(sql);
        List<Map<String, String>> list = new ArrayList<Map<String, String>>();
        con = getConnection();
        try {
            stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                Map<String, String> map = new HashMap<>();
                for (FieldInfo fieldObject : tableFields) {
                    String f = fieldObject.getFieldName();
                    map.put(f, rs.getString(f));
                }
                list.add(map);

            }
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return list;

    }

    /*public static String tableField2ObjectProperty(String tableField) {
        return "";
    }
    public static String objProperty2tableField(String objectProperty) {
        return objectProperty;
    }*/
    public static String hump2Underscore(String humpString) {
        String underscoreString = "";
        for (int i = 0; i < humpString.length(); i++) {
            char c = humpString.charAt(i);
            underscoreString += c >= 'A' && c <= 'Z' ? "_" + (char) (c - 'A' + 'a') : c;
        }
        return underscoreString;
    }

    public static String underscore2hump(String underscoreString) {
        String humpString = "";
        boolean now2Up = false;
        for (int i = 0; i < underscoreString.length(); i++) {
            char c = underscoreString.charAt(i);
            if (now2Up) {
                humpString += c >= 'a' && c <= 'z' ? (char) (c - 'a' + 'A') : c;
                now2Up = false;
            } else {
                if (c == '_') {
                    now2Up = true;
                } else {
                    humpString += c;
                }
            }

        }
        return humpString;
    }

    public List<FieldInfo> getTableFieldsBySql(String table) throws Exception {
        Connection conn = getConnection();

        Statement stmt = (Statement) conn.createStatement();


        ResultSet rs = stmt.executeQuery("show full columns from " + table);

        List<FieldInfo> ls = new ArrayList<>();
        int cnt = 1;


        while (rs.next()) {

            FieldInfo t = new FieldInfo();

            t.setIsAuto(rs.getString(7).contains("auto_increment") ? "YES" : "NO");

            t.setCommit(rs.getString("Comment"));

            t.setFieldName(rs.getString("Field"));


            String typeDate = rs.getString("type");
            String type = "";
            String len = "";

            for (int i = 0; i < typeDate.length(); i++) {

                if (typeDate.charAt(i) != '(') {
                    type += typeDate.charAt(i);

                } else {
                    break;
                }

            }
            for (int i = 0; i < typeDate.length(); i++) {

                if (typeDate.charAt(i) >= '0' && typeDate.charAt(i) <= '9') {
                    len += typeDate.charAt(i);

                }

            }

            t.setIsKey(rs.getString(5).contains("PRI") ? "YES" : "NO");


            t.setType(type);

            t.setLength(len);

            t.setDefultValue(rs.getString("default"));

            t.setCanNull(rs.getString(4));


            ls.add(t);

            cnt++;
        }

        stmt.close();
        conn.close();

        return ls;
    }

}
