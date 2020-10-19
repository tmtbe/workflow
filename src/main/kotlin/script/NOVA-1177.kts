import com.thoughtworks.flow.*

val api = Api("Bff")
val service = Service("Service")
val amsClient = Client("AmsClient")

val apiVerify = api.m("POST 验证&绑卡")
val verify = service.m("verify")

val amsVerify = amsClient.m("verify")
        .result("成功，不需要展示条款")
        .desc("""
            {
                "code":0,
                "msg":"success",
                "body":{
                    "needShow": false 
                }
            }
        """.trimIndent())
        .result("成功，需要展示条款")
        .desc("""
            {
                "code":0,
                "msg":"success",
                "body":{
                    "needShow": true 
                }
            }
        """.trimIndent())
        .result("失败")
        .desc("""
            {
                "code":1002,
                "msg":"fail",
                "body":{
                    "needShow": false 
                }
            }
        """.trimIndent())
        .exception("超时等500错误")

val amsCreate = amsClient.m("create")
        .result("成功")
        .desc("""
            {
                "code":0,
                "msg":"success"
            }
        """.trimIndent())
        .result("失败")
        .desc("""
            {
                "code":2001,
                "msg":"fail"
            }
        """.trimIndent())
        .exception("超时等500错误")

val story = Story("用户在绑定特殊星礼卡/包时，可以收到提示并勾选额外条款。")
story.run(
        {
            apiVerify {
                try {
                    verifyHead("token")
                    verify {
                        when (amsVerify.caseName()) {
                            "成功，不需要展示条款" -> {
                                when (amsCreate.caseName()) {
                                    "成功" -> RESULT("Success")
                                    "失败" -> e("FailWithCode")
                                }
                            }
                            "失败" -> e("FailWithCode")
                        }
                        RESULT("Success")
                    }
                    RESULT("200")
                } catch (e: Throwable) {
                    when (e.message) {
                        "FailWithCode" -> RESULT("500 FailWithCode")
                        else -> RESULT("500")
                    }
                }
            }
        }
)