package com.thoughtworks.flow

import com.thoughtworks.flow.Story.Companion.context

data class RunRecord(val scope: F, val f: F, val case: CASE) {
    var sorted: Boolean = false
    override fun toString(): String {
        return "$f -> ${case.name()}"
    }

    fun toDescString(): String {
        return "$f -> ${case.toDescString()}"
    }
}

open class F(val name: String, val node: Node) {
    private val caseSelect = CaseSelect(this)
    fun case(case: CASE): F {
        caseSelect.addCase(case)
        return this
    }

    fun result(name: String, desc: String = ""): F {
        caseSelect.addCase(RESULT(name, desc))
        return this
    }

    fun desc(desc: String): F {
        caseSelect.addDesc(desc)
        return this
    }

    fun given(given: String): F {
        caseSelect.addGiven(given)
        return this
    }

    fun exception(name: String, desc: String = ""): F {
        caseSelect.addCase(EXCEPTION(name, desc))
        return this
    }

    fun isCaseEmpty() = caseSelect.isEmpty()
    fun case(): CASE {
        val case = caseSelect.select()
        context.recordRun(RunRecord(context.currentScope(), this, case))
        case.run()
        return case
    }

    fun caseName(): String {
        return case().name()
    }

    protected fun runRecord(case: CASE) {
        context.recordRun(RunRecord(context.previousScope(), this, case))
    }

    override fun toString(): String {
        return node.name + "::" + name
    }
}

abstract class Node(val name: String) {
    protected val methodCache = HashMap<String, F>()
    abstract infix fun m(s: String): F
}
