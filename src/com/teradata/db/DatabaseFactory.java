package com.teradata.db;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.HashMap;

public class DatabaseFactory {

    /**
     * return a HashMap contain databaseInfo
     * @param file
     * @return
     * @throws Exception
     * {
     *     url:
     *     user:
     *     password:
     * }
     */
    public static DataBaseUtil readFile(File file)throws  Exception{
        BufferedReader br  = new BufferedReader(new FileReader(file));
        String str ;
        HashMap<String,String>map = new HashMap<>();
        int lineNumber = 0;
        while ((str = br.readLine())!=null){
            lineNumber ++ ;
            int position = str.indexOf("=");
            if(position==-1){
                throw new Exception("database config Exception:format error,your need a '=' in line "+lineNumber);
            }
            String key = str.substring(0,position);
            String value = str.substring(position+1);

            key = key.trim();
            if(key.startsWith("#")){
                continue;
            }
            value = value.trim();
            String exist = map.get(key);
            if(exist!=null){
                throw new Exception("database config Exception: Keyword duplication is not allowed:"+key);
            }else{
                map.put(key,value);
            }
        }
        String[] checkString = new String[]{"url","user","password"};
        for(String check:checkString){
            String s = map.get(check);
            if(s==null){
                throw new Exception("database config Exception:need has :"+check+" config");
            }
        }
        DataBaseUtil dataBaseUtil =new DataBaseUtil() {
            @Override
            protected String getUser() {
                return map.get("user");
            }

            @Override
            protected String getDatabaseName() {
                return null;
            }

            @Override
            protected String getPassword() {
                return map.get("password");
            }

            @Override
            protected String getUrl() {
                return map.get("url");
            }
        };
        return dataBaseUtil;
    }

}
