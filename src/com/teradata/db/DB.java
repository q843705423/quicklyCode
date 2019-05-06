package com.teradata.db;

public class DB extends DataBaseUtil {
    public DB() throws Exception {
    }

    @Override
    protected String getDriver() {
        return "com.mysql.jdbc.Driver";
    }

    @Override
    protected String getUser() {
        return "root";
    }

    @Override
    protected String getDatabaseName() {
        return "apicloud";
    }

    @Override
    protected String getUrl() {
//        return "jdbc:mysql://127.0.0.1:3306/class_system?characterEncoding=utf8";
        return "jdbc:mysql://10.73.134.229:3306/apicloud?characterEncoding=utf8&zeroDateTimeBehavior=convertToNull&useSSL=false&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=GMT%2B8&allowMultiQueries=true";
//        return "jdbc:mysql://127.0.0.1:3306/man?characterEncoding=utf8&zeroDateTimeBehavior=convertToNull&useSSL=false&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=GMT%2B8";
    }

    @Override
    protected Integer getPort() {
        return 3306;
    }

    @Override
    protected String getIp() {
        return "10.73.134.229";
    }


    @Override
    protected String getPassword() {
//        return "root";
        return "sdfa@eWs127";
    }
}
