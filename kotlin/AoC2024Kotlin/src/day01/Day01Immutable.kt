package day01

import java.io.File
import kotlin.math.abs

fun main() {
    val lines = File("src/day01/day01.txt").readLines()

    val (part1, part2) = lines
        .map { line ->
            line.split(' ')
                .mapNotNull { it.toIntOrNull() }
                .let { it.first() to it.last() }
        }
        .unzip()
        .let {
            val listA = it.first.sorted()
            val listB = it.second.sorted()
            listA.foldIndexed(0) { index, sum, current ->
                sum + abs(listB[index] - current)
            } to listA.fold(0) { sum, current ->
                sum + (current * listB.count { current == it })
            }
        }

    println("Part 1: $part1")
    println("Part 2: $part2")
}