package day09

import day.Day

class Day09: Day("src/day09/input.txt") {
    val disk = input.readText()

    override fun doPart1(): Long {
        var fileBlockIndex = 0
        var freeBlockCount = 0
        var blockString = ""

        disk.forEachIndexed { index, char ->
            if (index % 2 == 0) {
                for (i in 1..char.digitToInt()) {
                    blockString = "$blockString$fileBlockIndex"
                }
                fileBlockIndex++
            } else {
                for (i in 1..char.digitToInt()) {
                    blockString = "$blockString."
                    freeBlockCount++
                }
            }
        }

        val blockList = blockString
            .split("")
            .filter { it.isNotBlank() }
            .toMutableList()

        val freeBlockIndices = blockList.foldIndexed(mutableListOf<Int>()) { index, acc, _ ->
            val block = blockList[index]
            if (block == ".") acc.add(index)
            acc
        }

        for (i in blockList.indices.reversed()) {
            val block = blockList[i]
            if (block != ".") {
                val emptyBlockIndex = freeBlockIndices.removeFirstOrNull()
                if (emptyBlockIndex != null && emptyBlockIndex < i) {
                    blockList[emptyBlockIndex] = block
                    blockList[i] = "."
                }
            }
        }

        return blockList.foldIndexed(0L) { index, checksum, id ->
            if (id != ".") checksum + (index.toLong() * id.toLong()) else checksum
        }
    }


    override fun doPart2(): Any {
        TODO("Not yet implemented")
    }
}

fun main() {
    val day = Day09()
    day.part1()
//    day.part2()
}