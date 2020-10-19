package com.thoughtworks.flow


class Api(s: String) : Node(s) {
    override fun m(s: String): ApiF {
        if (methodCache.containsKey(s)) return methodCache[s] as ApiF
        val apiF = ApiF(s, this)
        methodCache[s] = apiF
        return apiF
    }

    class ApiScope(val apiF: ApiF) : Scope(apiF) {
        fun verifyHead(head: String, ex: EXCEPTION = EXCEPTION("VerifyHeaderFail")): CASE {
            val function = apiF.api.m("verificationHeader($head)")
            if (function.isCaseEmpty()) {
                function.case(RESULT("通过验证"))
                        .case(ex)
            }
            return function.case()
        }

        fun verifyParam(param: String, ex: EXCEPTION = EXCEPTION("VerifyParamFail")): CASE {
            val function = apiF.api.m("verificationParam($param)")
            if (function.isCaseEmpty()) {
                function.case(RESULT("通过验证"))
                        .case(ex)
            }
            return function.case()
        }
    }

    class ApiF(name: String, val api: Api) : F(name, api) {
        operator fun invoke(function: ApiScope.() -> CASE): CASE {
            val apiScope = ApiScope(this)
            try {
                val case = function.invoke(apiScope)
                runRecord(case)
                return case
            } catch (e: CaseException) {
                val case = e.case
                runRecord(case)
                throw e
            } finally {
                apiScope.close()
            }
        }
    }
}
