package day09

import day.Day

class Day09: Day("src/day09/sample.txt") {
    val disk = input.readText()

    override fun doPart1(): Any {
        var fileBlockIndex = 0
        var blocks = ""
        disk.forEachIndexed { index, char ->
            if (index % 2 == 0) {
                for (i in 1..char.digitToInt()) {
                    blocks = "$blocks$fileBlockIndex"
                }
                fileBlockIndex++
            } else {
                for (i in 1..char.digitToInt()) {
                    blocks = "$blocks."
                }
            }
        }
        
        return blocks
    }

    override fun doPart2(): Any {
        TODO("Not yet implemented")
    }
}

fun main() {
    val day = Day09()
    day.part1()
    day.part2()
}