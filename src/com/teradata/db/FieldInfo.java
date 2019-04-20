package com.teradata.db;

public class FieldInfo {

    private String fieldName;  //字段名
    private String type;       //字段类型
    private String commit;     //字段注解
    private String isAuto;     //是否自动递增
    private String defultValue;     //默认值
    private String canNull;      //能否为空
    private String length;      //长度
    private String isKey;          //是否为主键


    public String getIsKey() {
        return isKey;
    }
    public void setIsKey(String isKey) {
        this.isKey = isKey;
    }
    public String getFieldName() {
        return fieldName;
    }
    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }
    public String getType() {
        return type;
    }
    public void setType(String type) {
        this.type = type;
    }
    public String getCommit() {
        return commit;
    }
    public void setCommit(String commit) {
        this.commit = commit;
    }
    public String getIsAuto() {
        return isAuto;
    }
    public void setIsAuto(String isAuto) {
        this.isAuto = isAuto;
    }
    public String getDefultValue() {
        return defultValue;
    }
    public void setDefultValue(String defultValue) {
        this.defultValue = defultValue;
    }
    public String getCanNull() {
        return canNull;
    }
    public void setCanNull(String canNull) {
        this.canNull = canNull;
    }
    public String getLength() {
        return length;
    }
    public void setLength(String length) {
        this.length = length;
    }
    @Override
    public String toString() {
        return "FieldInfo [fieldName=" + fieldName + ", type=" + type + ", commit=" + commit + ", isAuto=" + isAuto
                + ", defultValue=" + defultValue + ", canNull=" + canNull + ", length=" + length + ", isKey=" + isKey
                + "]";
    }


}
