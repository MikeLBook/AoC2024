package day02

import java.io.File
import kotlin.math.abs

fun isSafeRow(row: List<Int>): Boolean {
    var allIncreasing = true
    var allDecreasing = true
    var allAdjacent = true

    row.forEachIndexed { i, k ->
        if (i > 0) {
            if (k < row[i - 1]) allIncreasing = false
            if (k > row[i - 1]) allDecreasing = false
            val difference = abs(k - row[i - 1])
            if (difference < 1 || difference > 3) allAdjacent = false
        }
    }

    return (allIncreasing || allDecreasing) && allAdjacent
}

fun main() {
    val lines = File("src/day02/day02.txt").readLines()

    fun part1(): Int = lines.count { line ->
        val row = line.split(' ').map { it.toInt() }
        isSafeRow(row)
    }

    fun part2(): Int {
        var safeRows = mutableListOf<List<Int>>()
        var unsafeRows = mutableListOf<List<Int>>()

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

    println("Part 1: ${part1()}")
    println("Part 2: ${part2()}")
}