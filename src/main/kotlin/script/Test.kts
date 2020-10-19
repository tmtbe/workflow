import com.thoughtworks.flow.*

val api = Api("api")
val service = Service("service")
val provisionClient = Client("provisionClient")
val msrClient = Client("msrClient")
val omsClient = Client("omsClient")
val commodityRepo = Repository("commodityRepo")

val apiGet = api.m("Get")
val redeem = service.m("redeem")

val isAvailableForUser = provisionClient.m("isAvailableForUser")
        .result("在圈人范围")
        .desc("true")
        .result("不在圈人范围")
        .desc("false")
val findById = commodityRepo.m("findById")
        .result("object")
        .desc("""
                {
                    "id":1
                }
        """.trimIndent())
        .given("""
                commodityRepo::save一个数据
                ```json
                {
                    "id":1
                }
                ```
        """.trimIndent())
        .result("null")
        .given("commodityRepo 没有数据")
val getUserDetail = msrClient.m("getUserDetail")
        .result("获取到用户信息")
        .exception("FeignException")
val getStock = omsClient.m("getStock")
        .result("库存不足")
        .desc("""
            {
                "stock":0
            }
        """.trimIndent())
        .result("库存充足")
        .desc("""
            {
                "stock":100
            }
        """.trimIndent())
        .exception("FeignException")
val createOrderPay = omsClient.m("createOrderPay")
        .result("创建订单成功")
        .exception("FeignException")

val story = Story("这是一个故事")
story.run({
    apiGet {
        try {
            verifyHead("token")
            redeem {
                when (isAvailableForUser.caseName()) {
                    "不在圈人范围" -> e("NotAvailableForUserException")
                }
                when (findById.caseName()) {
                    "null" -> e("NotFindByIdException")
                }
                getUserDetail.case()
                when (getStock.caseName()) {
                    "库存不足" -> e("StockNotAdequateException")
                }
                createOrderPay.case()
            }
            RESULT("200", """
                            {
                                "code":200,
                                "message":""
                            }
                        """.trimIndent())
        } catch (e: Throwable) {
            RESULT("400", """
                            {
                                "code":400,
                                "message":""
                            }
                        """.trimIndent())
        }
    }
})