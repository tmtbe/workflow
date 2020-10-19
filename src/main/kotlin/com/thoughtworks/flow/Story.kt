package com.thoughtworks.flow

import java.io.BufferedWriter
import java.io.File
import java.io.FileWriter


class Story(name: String) : Node(name) {
    companion object {
        var context: Context = Context(Story("none"))
    }

    fun run(vararg functions: StoryScope.() -> CASE) {
        val storyScope = StoryScope(this, m("run"))
        var log = "# Story $name\n"
        functions.forEach { function ->
            context = Context(this)
            while (context.next()) {
                context.newStart()
                storyScope.open()
                function.invoke(storyScope)
                storyScope.close()
            }
            log += context.print()
        }
        out(log)
    }

    fun out(log: String) {
        val tempName = name.split(" ")[0]
        val temp = File.createTempFile(tempName, ".txt")
        val bw = BufferedWriter(FileWriter(temp))
        bw.write(log)
        bw.close()
        println("Written to Temp file : " + temp.absolutePath)
        Runtime.getRuntime().exec("open ${temp.absolutePath} -a typora")
    }

    class StoryScope(story: Node, storyF: F) : Scope(storyF) {

    }

    override fun m(s: String): F {
        return StoryF(this)
    }

    class StoryF(story: Story) : F("run", story) {
    }
}
