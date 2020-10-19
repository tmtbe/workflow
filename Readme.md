**`这是一个可以生成如下AC文档的工具`**

# Story 用户在绑定特殊卡/包时，可以收到提示并勾选额外条款。

## Bff::POST 验证&绑卡

### AC1: `when` Bff::verificationHeader(token) -> VerifyHeaderFail `then` Bff::POST 验证&绑卡 -> 500

```sequence
Actor->Bff:POST 验证&绑卡
Bff->Bff:verificationHeader(token)
Bff-->Bff:VerifyHeaderFail
Bff-->Actor:500
```

### api Test 

[given] 

* Bff::verificationHeader(token) -> VerifyHeaderFail


[stub] 

[then] 
Bff::POST 验证&绑卡 -> 500

### AC2: `when` AwsClient::verify -> 超时等500错误 `then` Bff::POST 验证&绑卡 -> 500

```sequence
Actor->Bff:POST 验证&绑卡
Bff->Bff:verificationHeader(token)
Bff-->Bff:通过验证
Bff->Service:verify
Service->AwsClient:verify
AwsClient-->Service:超时等500错误
Service-->Bff:超时等500错误
Bff-->Actor:500
```

### api Test 

[given] 

* Bff::verificationHeader(token) -> 通过验证


[stub] 

* Service::verify -> 超时等500错误

[then] 
Bff::POST 验证&绑卡 -> 500

### service Test 

[given] 


[stub] 

* AwsClient::verify -> 超时等500错误

[then] 
Service::verify -> 超时等500错误

### infra Test 

[given] 


[stub] 

[then] 
AwsClient::verify -> 超时等500错误

### AC3: `when` AwsClient::verify -> 失败 `then` Bff::POST 验证&绑卡 -> 500 FailWithCode

```sequence
Actor->Bff:POST 验证&绑卡
Bff->Bff:verificationHeader(token)
Bff-->Bff:通过验证
Bff->Service:verify
Service->AwsClient:verify
AwsClient-->Service:失败
Service-->Bff:FailWithCode
Bff-->Actor:500 FailWithCode
```

### api Test 

[given] 

* Bff::verificationHeader(token) -> 通过验证


[stub] 

* Service::verify -> FailWithCode

[then] 
Bff::POST 验证&绑卡 -> 500 FailWithCode

### service Test 

[given] 


[stub] 

* AwsClient::verify -> 失败

```json
{
    "code":1002,
    "msg":"fail",
    "body":{
        "needShow": false 
    }
}
```

[then] 
Service::verify -> FailWithCode

### infra Test 

[given] 


[stub] 

[then] 
AwsClient::verify -> 失败

```json
{
    "code":1002,
    "msg":"fail",
    "body":{
        "needShow": false 
    }
}
```

### AC4: `when` AwsClient::verify -> 成功，需要展示条款 `then` Bff::POST 验证&绑卡 -> 200

```sequence
Actor->Bff:POST 验证&绑卡
Bff->Bff:verificationHeader(token)
Bff-->Bff:通过验证
Bff->Service:verify
Service->AwsClient:verify
AwsClient-->Service:成功，需要展示条款
Service-->Bff:Success
Bff-->Actor:200
```

### api Test 

[given] 

* Bff::verificationHeader(token) -> 通过验证


[stub] 

* Service::verify -> Success

[then] 
Bff::POST 验证&绑卡 -> 200

### service Test 

[given] 


[stub] 

* AwsClient::verify -> 成功，需要展示条款

```json
{
    "code":0,
    "msg":"success",
    "body":{
        "needShow": true 
    }
}
```

[then] 
Service::verify -> Success

### infra Test 

[given] 


[stub] 

[then] 
AwsClient::verify -> 成功，需要展示条款

```json
{
    "code":0,
    "msg":"success",
    "body":{
        "needShow": true 
    }
}
```

### AC5: `when` AwsClient::create -> 超时等500错误 `then` Bff::POST 验证&绑卡 -> 500

```sequence
Actor->Bff:POST 验证&绑卡
Bff->Bff:verificationHeader(token)
Bff-->Bff:通过验证
Bff->Service:verify
Service->AwsClient:verify
AwsClient-->Service:成功，不需要展示条款
Service->AwsClient:create
AwsClient-->Service:超时等500错误
Service-->Bff:超时等500错误
Bff-->Actor:500
```

### service Test 

[given] 


[stub] 

* AwsClient::verify -> 成功，不需要展示条款

```json
{
    "code":0,
    "msg":"success",
    "body":{
        "needShow": false 
    }
}
```

* AwsClient::create -> 超时等500错误

[then] 
Service::verify -> 超时等500错误

### infra Test 

[given] 


[stub] 

[then] 
AwsClient::verify -> 成功，不需要展示条款

```json
{
    "code":0,
    "msg":"success",
    "body":{
        "needShow": false 
    }
}
```

### infra Test 

[given] 


[stub] 

[then] 
AwsClient::create -> 超时等500错误

### AC6: `when` AwsClient::create -> 失败 `then` Bff::POST 验证&绑卡 -> 500 FailWithCode

```sequence
Actor->Bff:POST 验证&绑卡
Bff->Bff:verificationHeader(token)
Bff-->Bff:通过验证
Bff->Service:verify
Service->AwsClient:verify
AwsClient-->Service:成功，不需要展示条款
Service->AwsClient:create
AwsClient-->Service:失败
Service-->Bff:FailWithCode
Bff-->Actor:500 FailWithCode
```

### service Test 

[given] 


[stub] 

* AwsClient::verify -> 成功，不需要展示条款

```json
{
    "code":0,
    "msg":"success",
    "body":{
        "needShow": false 
    }
}
```

* AwsClient::create -> 失败

```json
{
    "code":2001,
    "msg":"fail"
}
```

[then] 
Service::verify -> FailWithCode

### infra Test 

[given] 


[stub] 

[then] 
AwsClient::create -> 失败

```json
{
    "code":2001,
    "msg":"fail"
}
```

### AC7: `when` AwsClient::create -> 成功 `then` Bff::POST 验证&绑卡 -> 200

```sequence
Actor->Bff:POST 验证&绑卡
Bff->Bff:verificationHeader(token)
Bff-->Bff:通过验证
Bff->Service:verify
Service->AwsClient:verify
AwsClient-->Service:成功，不需要展示条款
Service->AwsClient:create
AwsClient-->Service:成功
Service-->Bff:Success
Bff-->Actor:200
```

### service Test 

[given] 


[stub] 

* AwsClient::verify -> 成功，不需要展示条款

```json
{
    "code":0,
    "msg":"success",
    "body":{
        "needShow": false 
    }
}
```

* AwsClient::create -> 成功

```json
{
    "code":0,
    "msg":"success"
}
```

[then] 
Service::verify -> Success

### infra Test 

[given] 


[stub] 

[then] 
AwsClient::create -> 成功

```json
{
    "code":0,
    "msg":"success"
}
```

