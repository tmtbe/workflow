package com.thoughtworks.flow

class Client(s: String) : Node(s) {
    override fun m(s: String): ClientF {
        if (methodCache.containsKey(s)) return methodCache[s] as ClientF
        val clientF = ClientF(s, this)
        methodCache[s] = clientF
        return clientF
    }

    class ClientScope(val clientF: ClientF) : Scope(clientF) {
    }

    class ClientF(name: String, client: Client) : F(name, client) {
        operator fun invoke(function: ClientScope.() -> CASE): CASE {
            val clientScope = ClientScope(this)
            val case = function.invoke(clientScope)
            runRecord(case)
            clientScope.close()
            return case
        }
    }
}