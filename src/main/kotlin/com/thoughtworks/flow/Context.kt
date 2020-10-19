package com.thoughtworks.flow

import org.jetbrains.kotlin.codegen.state.md5base64
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashSet

class Context(val story: Story) {
    val runRecordList = ArrayList<ArrayList<RunRecord>>()
    var currentRunRecord: ArrayList<RunRecord> = ArrayList()
    val rootTreeNode = TreeNode(null)
    var currentTreeNode = rootTreeNode
    var scope = LinkedList<F>()
    var logText: String = ""
    var processMd5Set = HashSet<String>()

    fun addLog(str: Any) {
        logText += str.toString()
        logText += "\n"
    }

    fun newStart() {
        currentRunRecord = ArrayList()
        scope.clear()
        runRecordList.add(currentRunRecord)
        currentTreeNode = rootTreeNode
    }

    fun next(): Boolean {
        if (runRecordList.isEmpty()) return true
        var find = currentTreeNode
        while (true) {
            if (find.parentTreeNode == null) return false
            if (find.parentTreeNode!!.caseSelect == null) {
                find = find.parentTreeNode!!
                continue
            }
            if (!find.parentTreeNode!!.caseSelect!!.hasNext()) {
                find = find.parentTreeNode!!
                continue
            } else {
                find.parentTreeNode!!.caseSelect!!.next()
                return true
            }
        }
    }

    fun updateCurrentTreeNode(caseSelect: CaseSelect) {
        val treeNode = TreeNode(currentTreeNode)
        treeNode.case = caseSelect.selectCase
        currentTreeNode.caseSelect = caseSelect
        currentTreeNode.addChild(treeNode)
        currentTreeNode = treeNode
    }

    fun recordRun(runRecord: RunRecord) {
        currentRunRecord.add(runRecord)
    }

    fun print(): String {
        addLog("## ${runRecordList[0].last().f}")
        runRecordList.reverse()
        var i = 0
        runRecordList.forEach { currentRunRecord ->
            i++
            val sortRunRecord = sortRunRecord(currentRunRecord)
            addLog("### AC${i}: `when` ${sortRunRecord.last()} `then` ${sortRunRecord.first()}")
            val buildSequence = buildSequence(sortRunRecord)
            addLog(buildSequence)
            val buildProcess = buildProcess(sortRunRecord)
            buildProcess.filter {
                val md5 = md5base64(it.toString())
                val has = processMd5Set.contains(md5)
                if (!has) processMd5Set.add(md5)
                else it.repeat = true
                !has
            }.forEach {
                addLog(it)
            }
        }
        return logText
    }

    private fun buildSequence(sortRunRecordList: ArrayList<RunRecord>): String {
        var result: String = ""
        val linkedList = LinkedList<RunRecord>()
        val linkedList2 = LinkedList<RunRecord>()
        val resultList = LinkedList<String>()
        sortRunRecordList.forEach {
            var one = ""
            if (linkedList.isNotEmpty() && it.scope != linkedList.peekLast().f) {
                val removeLast = linkedList.removeLast()
                one = getSequenceName(removeLast.f.node) + "-->" + getSequenceName(removeLast.scope.node) + ":" + removeLast.case.name()
                resultList.addLast(one)
            }
            one = getSequenceName(it.scope.node) + "->" + getSequenceName(it.f.node) + ":" + it.f.name
            resultList.addLast(one)
            linkedList.add(it)
        }
        linkedList.forEach {
            linkedList2.addFirst(it)
        }
        linkedList2.forEach {
            var one = ""
            one = getSequenceName(it.f.node) + "-->" + getSequenceName(it.scope.node) + ":" + it.case.name()
            resultList.addLast(one)
        }
        result = "```sequence\n" + resultList.joinToString("\n") + "\n```"
        return result
    }

    private fun getSequenceName(node: Node): String {
        if (node is Story) {
            return "Actor"
        }
        return node.name
    }

    private fun buildProcess(sortRunRecordList: ArrayList<RunRecord>): ArrayList<Process> {
        val findProcess = FindProcess(sortRunRecordList)
        val apiProcess = findProcess.findProcessRecord("api", sortRunRecordList[0], Service::class)
        val processes = ArrayList<Process>()
        processes.add(apiProcess)
        apiProcess.stubList.forEach { service ->
            val serviceProcess = findProcess.findProcessRecord("service", service, Client::class, Repository::class)
            processes.add(serviceProcess)
            serviceProcess.stubList.forEach { infra ->
                val infraProcess = findProcess.findProcessRecord("infra", infra)
                processes.add(infraProcess)
            }
        }
        return processes
    }


    private fun sortRunRecord(runRecordList: ArrayList<RunRecord>): ArrayList<RunRecord> {
        val newRunRecordList = ArrayList<RunRecord>()
        val iterator = runRecordList.iterator()
        while (iterator.hasNext()) {
            val next = iterator.next()
            if (next.sorted) continue
            val scope = runRecordList.firstOrNull { find ->
                next.scope == find.f
            }
            if (scope != null && !scope.sorted) {
                scope.sorted = true
                newRunRecordList.add(scope)
            }
            next.sorted = true
            newRunRecordList.add(next)
        }
        newRunRecordList.forEach { it.sorted = false }
        if (md5base64(runRecordList.toString()) == md5base64(newRunRecordList.toString())) {
            return newRunRecordList
        }
        return sortRunRecord(newRunRecordList)
    }

    fun currentScope(): F {
        return scope.peekLast()
    }

    fun previousScope(): F {
        val takeLast = scope.takeLast(2)
        return takeLast[0]
    }
}