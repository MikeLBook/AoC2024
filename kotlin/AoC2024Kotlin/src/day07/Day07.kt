package day07

import day.Day

/**
 * Part 1 started with a cheeky idea. Since there were only two possible operations,
 * I decided to increment through each possibility in a binary fashion.
 * I could see additional operators coming from a mile away for part 2, but wanted to follow the thread.
 * If an equation has 4 operands requiring 3 operators then the start would be [0, 0, 0],
 * and the end would be [1, 1, 1] to exhaust all possible equations from +, +, + to *, *, *
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
        val expectedResult = e.toLong()
        val operands = o.split(" ").map { it.toLong() }

        val initialOperatorArrangement = List(operands.size - 1) { 0 }
        val finalOperatorArrangement = List(operands.size - 1) { n - 1 }
        var operators = List(initialOperatorArrangement.size) { initialOperatorArrangement[it] }
        var calibrated: Boolean? = null
        while (calibrated == null) {
            val result = operands.reduceIndexed { index, total, operand ->
                when (operators[index - 1]) {
                    0 -> total * operand
                    1 -> total + operand
                    2 -> (total.toString() + operand.toString()).toLong()
                    else -> throw IllegalArgumentException("Unspecified operation for $operators[index - 1]")
                }
            }
            when {
                result == expectedResult -> calibrated = true
                operators == finalOperatorArrangement -> calibrated = false
                else -> operators = operators.incrementAsBase(n)
            }
        }
        if (calibrated) expectedResult else 0
    }
    override fun doPart1() = solveForOperatorCount(2)
    override fun doPart2() = solveForOperatorCount(3)
}

fun main() {
    val day = Day07()
    day.part1()
    day.part2()
}