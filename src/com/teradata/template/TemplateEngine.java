package com.teradata.template;

import com.teradata.db.DB;
import com.teradata.db.DataBaseUtil;
import com.teradata.db.FieldInfo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class TemplateEngine {
    public static String render(String text, HashMap<String, Object> all, DataBaseUtil dataBaseUtil) throws Exception {


        List<FieldInfo> fieldList = (List<FieldInfo>) all.get("field");
        StringBuilder texts = new StringBuilder();
        for (int i = 0; i < fieldList.size(); i++) {
            String temp = text;
            while (temp.contains("$")) {
                int first = temp.indexOf("$");
                int next = temp.indexOf("$", first + 1);
                String anliayzed = temp.substring(first + 1, next);
                String result = change(anliayzed, fieldList.get(i), dataBaseUtil);
                temp = temp.replace("$" + anliayzed + "$", result);
            }
            texts.append(temp);
        }
        return texts.toString();
    }

    private static Integer cnt = 0;

    public static void main(String... args) throws Exception {

    }

    public static String change(String text, FieldInfo fieldInfo, DataBaseUtil db) throws Exception {
//        String str[] = text.split("\\.");
//        if (str.length > 2) {
//            throw new Exception("template Exception, analysis ',' anomaly");
//        }
//        if (!str[0].equals("field")) {
//            return text;
//        }

        if (text.equals("name")) {
            String t = fieldInfo.getFieldName();
            String humpString = DB.underscore2hump(t);
            return humpString;
        } else if (text.equals("comment")) {
            return fieldInfo.getCommit();
        } else if (text.equals("default")) {
            return fieldInfo.getDefultValue();
        } else if (text.equals("isKey")) {
            return fieldInfo.getIsKey();
        } else if (text.equals("canNull")) {
            return fieldInfo.getCanNull();
        } else if (text.equals("Name")) {
            String humpString = DB.underscore2hump(fieldInfo.getFieldName());
            return humpString.substring(0, 1).toUpperCase() + humpString.substring(1);
        } else if (text.equals("field")) {
            return fieldInfo.getFieldName();
        } else if (text.equals("FIELD")) {
            return fieldInfo.getFieldName().toUpperCase();
        } else if (text.equals("type")) {
            return fieldInfo.getType();
        } else if (text.equals("TYPE")) {
            return fieldInfo.getType().toUpperCase();
        } else if (text.equals("class")) {
            return db.type2Class(fieldInfo.getType());
        } else if (text.equals("CLASS")) {
            return db.type2Class(fieldInfo.getType()).toUpperCase();
        } else if (text.equals("NAME")) {
            String humpString = DB.underscore2hump(fieldInfo.getFieldName());
            return humpString.toUpperCase();
        }  else if (text.contains("random")) {
            return randomString(8);
        } else if (text.contains("id")) {
            cnt++;
            return cnt.toString();
        } else if (text.contains("randomInt")) {
            return String.valueOf((int) (Math.random() * 256));
        }
        return "?" + text + "?";
    }

    public static void clear() {
        cnt = 0;
    }

    public static String randomString(int cnt) {
        return cnt == 0 ? "" : cnt == 1 ? ((char) ('a' + Math.random() * 26)) + "" : randomString(cnt / 2) + randomString(cnt / 2);

    }


}
