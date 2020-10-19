# Story 这是一个故事
## AC1: `when` api::verificationHeader(token) -> VerifyHeaderFail `then` api::Get -> 400
### api Test
[given] 
* api::verificationHeader(token) -> VerifyHeaderFail


[stub] 

[then] 
api::Get -> 400
```json
{
    "code":400,
    "message":""
}
```

## AC2: `when` provisionClient::isAvailableForUser -> 不在圈人范围 `then` api::Get -> 400
### api Test
[given] 
* api::verificationHeader(token) -> 通过验证


[stub] 
* service::redeem -> NotAvailableForUserException

[then] 
api::Get -> 400
```json
{
    "code":400,
    "message":""
}
```

### service Test
[given] 


[stub] 
* provisionClient::isAvailableForUser -> 不在圈人范围
```json
false
```

[then] 
service::redeem -> NotAvailableForUserException

### infra Test
[given] 


[stub] 

[then] 
provisionClient::isAvailableForUser -> 不在圈人范围
```json
false
```

## AC3: `when` commodityRepo::findById -> null `then` api::Get -> 400
### api Test
[given] 
* api::verificationHeader(token) -> 通过验证


[stub] 
* service::redeem -> NotFindByIdException

[then] 
api::Get -> 400
```json
{
    "code":400,
    "message":""
}
```

### service Test
[given] 


[stub] 
* provisionClient::isAvailableForUser -> 在圈人范围
```json
true
```
* commodityRepo::findById -> null

[then] 
service::redeem -> NotFindByIdException

### infra Test
[given] 


[stub] 

[then] 
provisionClient::isAvailableForUser -> 在圈人范围
```json
true
```

### infra Test
[given] 
commodityRepo 没有数据

[stub] 

[then] 
commodityRepo::findById -> null

## AC4: `when` msrClient::getUserDetail -> FeignException `then` api::Get -> 400
### api Test
[given] 
* api::verificationHeader(token) -> 通过验证


[stub] 
* service::redeem -> FeignException

[then] 
api::Get -> 400
```json
{
    "code":400,
    "message":""
}
```

### service Test
[given] 


[stub] 
* provisionClient::isAvailableForUser -> 在圈人范围
```json
true
```
* commodityRepo::findById -> object
```json
{
    "id":1
}
```
* msrClient::getUserDetail -> FeignException

[then] 
service::redeem -> FeignException

### infra Test
[given] 


[stub] 

[then] 
provisionClient::isAvailableForUser -> 在圈人范围
```json
true
```

### infra Test
[given] 
commodityRepo::save一个数据
```json
{
    "id":1
}
```

[stub] 

[then] 
commodityRepo::findById -> object
```json
{
    "id":1
}
```

### infra Test
[given] 


[stub] 

[then] 
msrClient::getUserDetail -> FeignException

## AC5: `when` omsClient::getStock -> FeignException `then` api::Get -> 400
### api Test
[given] 
* api::verificationHeader(token) -> 通过验证


[stub] 
* service::redeem -> FeignException

[then] 
api::Get -> 400
```json
{
    "code":400,
    "message":""
}
```

### service Test
[given] 


[stub] 
* provisionClient::isAvailableForUser -> 在圈人范围
```json
true
```
* commodityRepo::findById -> object
```json
{
    "id":1
}
```
* msrClient::getUserDetail -> 获取到用户信息
* omsClient::getStock -> FeignException

[then] 
service::redeem -> FeignException

### infra Test
[given] 


[stub] 

[then] 
provisionClient::isAvailableForUser -> 在圈人范围
```json
true
```

### infra Test
[given] 
commodityRepo::save一个数据
```json
{
    "id":1
}
```

[stub] 

[then] 
commodityRepo::findById -> object
```json
{
    "id":1
}
```

### infra Test
[given] 


[stub] 

[then] 
msrClient::getUserDetail -> 获取到用户信息

### infra Test
[given] 


[stub] 

[then] 
omsClient::getStock -> FeignException

## AC6: `when` omsClient::createOrderPay -> FeignException `then` api::Get -> 400
### api Test
[given] 
* api::verificationHeader(token) -> 通过验证


[stub] 
* service::redeem -> FeignException

[then] 
api::Get -> 400
```json
{
    "code":400,
    "message":""
}
```

### service Test
[given] 


[stub] 
* provisionClient::isAvailableForUser -> 在圈人范围
```json
true
```
* commodityRepo::findById -> object
```json
{
    "id":1
}
```
* msrClient::getUserDetail -> 获取到用户信息
* omsClient::getStock -> 库存充足
```json
{
    "stock":100
}
```
* omsClient::createOrderPay -> FeignException

[then] 
service::redeem -> FeignException

### infra Test
[given] 


[stub] 

[then] 
provisionClient::isAvailableForUser -> 在圈人范围
```json
true
```

### infra Test
[given] 
commodityRepo::save一个数据
```json
{
    "id":1
}
```

[stub] 

[then] 
commodityRepo::findById -> object
```json
{
    "id":1
}
```

### infra Test
[given] 


[stub] 

[then] 
msrClient::getUserDetail -> 获取到用户信息

### infra Test
[given] 


[stub] 

[then] 
omsClient::getStock -> 库存充足
```json
{
    "stock":100
}
```

### infra Test
[given] 


[stub] 

[then] 
omsClient::createOrderPay -> FeignException

## AC7: `when` omsClient::createOrderPay -> 创建订单成功 `then` api::Get -> 200
### api Test
[given] 
* api::verificationHeader(token) -> 通过验证


[stub] 
* service::redeem -> 创建订单成功

[then] 
api::Get -> 200
```json
{
    "code":200,
    "message":""
}
```

### service Test
[given] 


[stub] 
* provisionClient::isAvailableForUser -> 在圈人范围
```json
true
```
* commodityRepo::findById -> object
```json
{
    "id":1
}
```
* msrClient::getUserDetail -> 获取到用户信息
* omsClient::getStock -> 库存充足
```json
{
    "stock":100
}
```
* omsClient::createOrderPay -> 创建订单成功

[then] 
service::redeem -> 创建订单成功

### infra Test
[given] 


[stub] 

[then] 
provisionClient::isAvailableForUser -> 在圈人范围
```json
true
```

### infra Test
[given] 
commodityRepo::save一个数据
```json
{
    "id":1
}
```

[stub] 

[then] 
commodityRepo::findById -> object
```json
{
    "id":1
}
```

### infra Test
[given] 


[stub] 

[then] 
msrClient::getUserDetail -> 获取到用户信息

### infra Test
[given] 


[stub] 

[then] 
omsClient::getStock -> 库存充足
```json
{
    "stock":100
}
```

### infra Test
[given] 


[stub] 

[then] 
omsClient::createOrderPay -> 创建订单成功

## AC8: `when` omsClient::getStock -> 库存不足 `then` api::Get -> 400
### api Test
[given] 
* api::verificationHeader(token) -> 通过验证


[stub] 
* service::redeem -> StockNotAdequateException

[then] 
api::Get -> 400
```json
{
    "code":400,
    "message":""
}
```

### service Test
[given] 


[stub] 
* provisionClient::isAvailableForUser -> 在圈人范围
```json
true
```
* commodityRepo::findById -> object
```json
{
    "id":1
}
```
* msrClient::getUserDetail -> 获取到用户信息
* omsClient::getStock -> 库存不足
```json
{
    "stock":0
}
```

[then] 
service::redeem -> StockNotAdequateException

### infra Test
[given] 


[stub] 

[then] 
provisionClient::isAvailableForUser -> 在圈人范围
```json
true
```

### infra Test
[given] 
commodityRepo::save一个数据
```json
{
    "id":1
}
```

[stub] 

[then] 
commodityRepo::findById -> object
```json
{
    "id":1
}
```

### infra Test
[given] 


[stub] 

[then] 
msrClient::getUserDetail -> 获取到用户信息

### infra Test
[given] 


[stub] 

[then] 
omsClient::getStock -> 库存不足
```json
{
    "stock":0
}
```

