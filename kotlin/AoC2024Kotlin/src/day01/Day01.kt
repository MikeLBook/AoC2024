package day01

import java.io.File
import kotlin.math.abs

fun main() {
    val lines = File("src/day01/day01.txt").readLines()
    val listA = mutableListOf<Int>()
    val listB = mutableListOf<Int>()

    lines.forEach { line ->
        val cols = line.split(' ').mapNotNull{ it.toIntOrNull() }
        listA.add(cols[0])
        listB.add(cols[1])
    }

    listA.sort()
    listB.sort()

    val part1 = listA.foldIndexed(0) { index, sum, current ->
        sum + abs(listB[index] - current)
    }

    val part2 = listA.fold(0) { sum, current ->
        sum + (current * listB.count { current == it })
    }

    println("Part 1: $part1")
    println("Part 2: $part2")
}