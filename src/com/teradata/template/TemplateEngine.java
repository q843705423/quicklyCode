package com.teradata.template;

import com.teradata.db.DB;
import com.teradata.db.FieldInfo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class TemplateEngine {
    public static String render(String text, HashMap<String, Object> all) throws Exception {


        List<FieldInfo> fieldList = (List<FieldInfo>) all.get("field");
        StringBuilder texts = new StringBuilder();
        for (int i = 0; i < fieldList.size(); i++) {
            String temp = text;
            while (temp.contains("$")) {
                int first = temp.indexOf("$");
                int next = temp.indexOf("$", first+1);
                String anliayzed = temp.substring(first+1, next );
                String result = change(anliayzed, fieldList.get(i));
                temp = temp.replace("$"+anliayzed+"$", result);
            }
            texts.append(temp);
        }
        return texts.toString();
    }


    public static void  main(String...args) throws Exception {
        HashMap<String,Object>map = new HashMap<>();
        List<FieldInfo>list = new ArrayList<>();
        FieldInfo fieldInfo = new FieldInfo();
        fieldInfo.setFieldName("user_id");
        fieldInfo.setCommit("用户Id");
        list.add(fieldInfo);
        FieldInfo fieldInfo1 = new FieldInfo();
        fieldInfo1.setFieldName("username");
        fieldInfo1.setCommit("用户名");
        list.add(fieldInfo1);
        map.put("field",list);
        String con = render("<v-template field='$field.name$' >$field.comment$<v-template>",map);
    }
    public static String change(String text, FieldInfo fieldInfo) throws Exception {
        String str[] = text.split("\\.");
        if (str.length != 2) {
            throw new Exception("template Exception, analysis ',' anomaly");
        }
        if (!str[0].equals("field")) {
            return text;
        }
        if (str[1].equals("name")) {
            String t = fieldInfo.getFieldName();
            String humpString = DB.underscore2hump(t);
            return humpString;
        } else if (str[1].equals("comment")) {
            return fieldInfo.getCommit();
        } else if (str[1].equals("default")) {
            return fieldInfo.getDefultValue();
        } else if (str[1].equals("isKey")) {
            return fieldInfo.getIsKey();
        } else if (str[1].equals("canNull")) {
            return fieldInfo.getCanNull();
        }else if(str[1].equals("Name")){
            String humpString = DB.underscore2hump(fieldInfo.getFieldName());
            return humpString.substring(0,1).toUpperCase()+humpString.substring(1);
        } else if (str[1].equals("fieldName")) {
            return fieldInfo.getFieldName();
        }else if(str[1].equals("type")){
            return fieldInfo.getType();
        }else if(str[1].equals("class")){
            return DB.type2Class(fieldInfo.getType());
        }
        return "###it is a error field###";
    }


}
