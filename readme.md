# IDEA PLUG-IN 

##  This is a template generation plug-in

## What's the use of this plug-in?

This plug-in provides real-time code substitution, which replaces the selected 
code with the property values of the corresponding Datasheet fields.

this is  a table show what field can be replace.

| variable|  introduce| 
|----- |---------------|
|$fieldName$| field name  |
|$name$| Modify field names with hump nomenclature (lowercase first character) |
|$Name$|  Modify field names with hump nomenclature (uppercase first character)  |
|$NAME$| $name$ all to upperCase|
|$type$| field type |
|$TYPE$| $type$ all to upperCase|
|$class$| The attributes of fields correspond to Java types |
|$CLASS$|  $class$ all to upperCase|
|$comment$| Annotations to fields |
|$default$| Default values for fields|
|$isKey$| Are fields primary keys |

### Example

Write the following code in your entity class:

```
public void set$field.Name$($field.class$ $field.name$){
    this.$field.name$ = $field.name$;
}

```

Select this code and use QuicklyCode.

Select the configurable database information and fill in the form name.

The corresponding code can be generated as follows:

```

public void setUserId(Integer userId){
    this.userId = userId;
}

public void setAccount(String account){
    this.account = account;
}

public void setPassword(String password){
    this.password = password;
}

```

Very cool! Isn't it?

But IDEA already provides getter and setter, so let's try another example?

```
< v-input placeholder='$field.comment$' id='$field.name$' />
```

Repeat previous operations

```

< v-input placeholder='user'id' id='userId' />

< v-input placeholder='username' id='account' />

< v-input placeholder='password' id='password' />
```

Excellent!!

## HOW TO USE IT?

### 1. Configuration file config.txt for connect database

Configuration information of database

config.txt is as follows:
```
url =jdbc:mysql://127.0.0.1:3306/yourDatabase?characterEncoding=utf8&useSSL=false
user = root
password = root
```


### 2. Download QuicklyCode.zip to local disk


### 3. import zip to IDEA ,WebStorm 

1. File->Setting->Plugins
2. Choose an icon like a tire
3. choose button of Install plugin from desk...
4. restart IDE
5. coding $field.name$ and selected it. 
6. Help->Find Action...
7. input QuicklyCode and enter.
8. input table name and choose config.txt
9. enter and generate code.

