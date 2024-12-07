package day07

import day.Day

/**
 * Part 1 started with a cheeky idea. Since there were only two possible operations,
 * I decided to increment through each possibility in a binary fashion.
 * I could see additional operators coming from a mile away for part 2, but wanted to follow the thread.
 * If an equation requires 3 operators then the start would be [0, 0, 0] and the end would be [1, 1, 1] to exhaust
 * all possible equations from +, +, + to *, *, *
 *
 * The solution I came up with was more extensible than I initially thought it would be.
 * Part 2 only required a trinary solution where operations from [0, 0, 0] to [2, 2, 2] should be exhausted.
 */

fun List<Int>.incrementAsBase(base: Int): List<Int> {
    var basedString = joinToString("")
    val decimal = basedString.toInt(base)
    val incremented = decimal + 1
    basedString = incremented.toString(base).padStart(size, '0')
    return basedString.split("").filter { it.isNotBlank() }.map { it.toInt() }
}

class Day07: Day("src/day07/input.txt") {
    private val lines = input.readLines()

    private fun solveForOperatorCount(n: Int): Long = lines.sumOf { line ->
        val (e, o) = line.split(": ")
        val expected = e.toLong()
        val operands = o.split(" ").map { it.toLong() }
        val start = List(operands.size - 1) { 0 }
        val end = List(operands.size - 1) { n - 1 }

        var isValid = false
        var isExhausted = false
        var operators = List(start.size) { start[it] }

        while (!isValid && !isExhausted) {
            val result = operands.reduceIndexed { index, acc, l ->
                when (operators[index - 1]) {
                    0 -> acc * l
                    1 -> acc + l
                    2 -> (acc.toString() + l.toString()).toLong()
                    else -> throw IllegalArgumentException("Unspecified operation for $operators[index - 1]")
                }
            }
            when {
                result == expected -> isValid = true
                operators == end -> isExhausted = true
                else -> operators = operators.incrementAsBase(n)
            }
        }

        if (isValid) expected else 0
    }

    override fun doPart1(): Any {
        return solveForOperatorCount(2)
    }

    override fun doPart2(): Any {
        return solveForOperatorCount(3)
    }
}

fun main() {
    val day = Day07()
    day.part1()
    day.part2()
}