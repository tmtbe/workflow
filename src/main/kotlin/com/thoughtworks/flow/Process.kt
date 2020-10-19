package com.thoughtworks.flow

import kotlin.reflect.KClass

class FindProcess(val sortRunRecordList: ArrayList<RunRecord>) {
    fun findProcessRecord(name: String, then: RunRecord, vararg endNodeClass: KClass<out Node>): Process {
        val endNodeClassList = endNodeClass.toList()
        val stubList = ArrayList<RunRecord>()
        val givenList = ArrayList<RunRecord>()
        val needFind = HashSet<F>()
        needFind.add(then.f)
        sortRunRecordList.filter { it != then }.forEach {
            if (needFind.contains(it.scope)) {
                if (!endNodeClassList.contains(it.f.node::class)) {
                    givenList.add(it)
                    needFind.add(it.f)
                } else {
                    stubList.add(it)
                }
            }
        }
        return Process(name, then, stubList, givenList)
    }
}

data class Process(
        val name: String,
        val then: RunRecord,
        val stubList: ArrayList<RunRecord>,
        val givenList: ArrayList<RunRecord>,
        var repeat:Boolean = false
) {
    override fun toString(): String {
        val repeatString = if(repeat) "[重复] " else ""
        return """
### $repeatString$name Test 
[given] ${givenList.toDescString()}
${then.case.given()}

[stub] ${stubList.toDescString()}

[then] 
${then.toDescString()}

        """.trimIndent()
    }
}

fun ArrayList<RunRecord>.toDescString(): String {
    var s = ""
    this.forEach {
        s += "\n* ${it.toDescString()}"
    }
    return s
}