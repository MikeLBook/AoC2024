package day

import java.io.File

interface IDay {
    fun part1()
    fun part2()
}

abstract class Day(inputLocation: String): IDay {
    val input = File(inputLocation)

    override fun part1() {
        println(doPart1().toString())
    }

    protected abstract fun doPart1(): Any

    override fun part2() {
        println(doPart2().toString())
    }

    protected abstract fun doPart2(): Any
}