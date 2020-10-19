package com.thoughtworks.flow

open class Scope(val f: F) {
    fun open() {
        Story.context.scope.add(f)
    }

    fun close() {
        Story.context.scope.pollLast()
    }

    init {
        open()
    }

    fun e(name: String) {
        throw CaseException(EXCEPTION(name))
    }
}