package com.thoughtworks.flow

import java.util.*

interface CASE {
    fun name(): String
    fun desc(): String
    fun given(): String
    fun sDesc(desc: String)
    fun run(): Unit

    fun toDescString(): String {
        if (desc().isEmpty()) {
            return name()
        } else {
            return """
${name()}
```json
${desc()}
```
            """.trimIndent()
        }
    }

    fun sGiven(given: String)
}

data class RESULT(val name: String, var desc: String = "", var given: String = "") : CASE {
    override fun name(): String {
        return name
    }

    override fun desc(): String {
        return desc
    }

    override fun given(): String {
        return given
    }

    override fun sDesc(desc: String) {
        this.desc = desc
    }

    override fun run() {

    }

    override fun sGiven(given: String) {
        this.given = given
    }
}

data class EXCEPTION(val name: String, var desc: String = "", var given: String = "") : CASE {
    override fun name(): String {
        return name
    }

    override fun desc(): String {
        return desc
    }

    override fun given(): String {
        return given
    }

    override fun run() {
        throw CaseException(this)
    }

    override fun sDesc(desc: String) {
        this.desc = desc
    }

    override fun sGiven(given: String) {
        this.given = given
    }
}

class CaseSelect(val f: F) {
    private val caseList = LinkedList<CASE>()
    private var keep: Boolean = true
    var selectCase: CASE? = null
    fun addCase(case: CASE): CaseSelect {
        caseList.add(case)
        return this
    }

    fun isEmpty() = caseList.isEmpty()
    fun select(): CASE {
        if (keep && selectCase != null) {
            Story.context.updateCurrentTreeNode(this)
            return selectCase!!
        }
        if (caseList.isEmpty()) throw EmptySelectException()
        val pop = caseList.pop()
        selectCase = pop
        Story.context.updateCurrentTreeNode(this)
        keep = true
        return pop
    }

    fun hasNext() = !caseList.isEmpty()
    fun next() {
        keep = false
    }

    override fun toString(): String {
        return "$f->$selectCase"
    }

    fun addDesc(desc: String) {
        caseList.last.sDesc(desc)
    }

    fun addGiven(given: String) {
        caseList.last.sGiven(given)
    }
}

class EmptySelectException : Exception() {

}

class CaseException(val case: CASE) : Exception() {
    override val message: String?
        get() = case.name()
}