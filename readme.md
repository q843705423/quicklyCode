# IDEA PLUG-IN 

##  This is a template generation plug-in

## What's the use of this plug-in?

This plug-in provides real-time code substitution, which replaces the selected 
code with the property values of the corresponding Datasheet fields.

### Example

Write the following code in your entity class:

```
public  set$field.Name$($field.type$ $field.name$){
    this.$field.name$ = $field.name$;
}

```

Select this code and use QuicklyCode.

Select the configurable database information and fill in the form name.

The corresponding code can be generated as follows:

```

public  setUserId(Integer userId){
    this.userId = userId;
}

public  setAccount(String account){
    this.account = account;
}

public  setPassword(String password){
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

Configuration information of database

Config.txt is as follows:

```
url =jdbc:mysql://127.0.0.1:3306/yourDatabase?characterEncoding=utf8&useSSL=false
user = root
password = root
```

Please enjoy it!!!
