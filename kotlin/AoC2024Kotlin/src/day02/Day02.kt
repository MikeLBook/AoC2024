package day02

import models.Day
import kotlin.math.abs

class Day02: Day("src/day02/day02.txt") {
    private val lines = input.readLines()

    override fun doPart1(): Int = lines.count { line ->
        val row = line.split(' ').map { it.toInt() }
        isSafeRow(row)
    }

    override fun doPart2(): Int {
        val safeRows = mutableListOf<List<Int>>()
        val unsafeRows = mutableListOf<List<Int>>()

        lines.forEach { line ->
            val row = line.split(' ').map { it.toInt() }
            if (isSafeRow(row)) safeRows.add(row)
            else unsafeRows.add(row)
        }

        val faultTolerantRows = unsafeRows.filter { unsafeRow ->
            unsafeRow.forEachIndexed { i, _ ->
                val dampenedRow = unsafeRow.toMutableList()
                dampenedRow.removeAt(i)
                if (isSafeRow(dampenedRow)) return@filter true
            }
            false
        }

        return safeRows.size + faultTolerantRows.size
    }

    private fun isSafeRow(numericRow: List<Int>): Boolean {
        var allIncreasing = true
        var allDecreasing = true
        var allAdjacent = true

        numericRow.forEachIndexed { i, k ->
            if (i > 0) {
                if (k < numericRow[i - 1]) allIncreasing = false
                if (k > numericRow[i - 1]) allDecreasing = false
                val difference = abs(k - numericRow[i - 1])
                if (difference < 1 || difference > 3) allAdjacent = false
            }
        }

        return (allIncreasing || allDecreasing) && allAdjacent
    }
}

fun main() {
    val day = Day02()
    day.part1()
    day.part2()
}