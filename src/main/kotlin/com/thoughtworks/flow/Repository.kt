package com.thoughtworks.flow

class Repository(s: String) : Node(s) {
    override fun m(s: String): RepositoryF {
        if (methodCache.containsKey(s)) return methodCache[s] as RepositoryF
        val repositoryF = RepositoryF(s, this)
        methodCache[s] = repositoryF
        return repositoryF
    }

    class RepositoryScope(val repositoryF: RepositoryF) : Scope(repositoryF) {
    }

    class RepositoryF(name: String, repository: Repository) : F(name, repository) {
        operator fun invoke(function: RepositoryScope.() -> CASE): CASE {
            val repositoryScope = RepositoryScope(this)
            val case = function.invoke(repositoryScope)
            runRecord(case)
            repositoryScope.close()
            return case
        }
    }
}