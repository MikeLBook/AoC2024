package day09

import day.Day

class Day09: Day("src/day09/input.txt") {
    private val disk = input.readText()
    private var blocks: MutableList<String> = buildList {
        var blockID = 0
        disk.forEachIndexed { index, char ->
            if (index % 2 == 0) {
                for (i in 1..char.digitToInt()) {
                    add(blockID.toString())
                }
                blockID++
            } else {
                for (i in 1..char.digitToInt()) {
                    add(".")
                }
            }
        }
    }.toMutableList()

    override fun doPart1(): Long {
        val freeBlockIndices = blocks.foldIndexed(mutableListOf<Int>()) { index, acc, _ ->
            val block = blocks[index]
            if (block == ".") acc.add(index)
            acc
        }

        for (i in blocks.indices.reversed()) {
            val block = blocks[i]
            if (block != ".") {
                val emptyBlockIndex = freeBlockIndices.removeFirstOrNull()
                if (emptyBlockIndex != null && emptyBlockIndex < i) {
                    blocks[emptyBlockIndex] = block
                    blocks[i] = "."
                }
            }
        }

        return blocks.foldIndexed(0L) { index, checksum, id ->
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