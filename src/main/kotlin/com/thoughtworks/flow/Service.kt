package com.thoughtworks.flow

class Service(s: String) : Node(s) {
    override fun m(s: String): ServiceF {
        if (methodCache.containsKey(s)) return methodCache[s] as ServiceF
        val serviceF = ServiceF(s, this)
        methodCache[s] = serviceF
        return serviceF
    }

    class ServiceScope(val serviceF: ServiceF) : Scope(serviceF) {
    }

    class ServiceF(name: String, service: Service) : F(name, service) {
        operator fun invoke(function: ServiceScope.() -> CASE): CASE {
            val serviceScope = ServiceScope(this)
            try {
                val case = function.invoke(serviceScope)
                runRecord(case)
                return case
            } catch (e: CaseException) {
                val case = e.case
                runRecord(case)
                throw e
            } finally {
                serviceScope.close()
            }
        }
    }
}
