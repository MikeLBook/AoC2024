package day09

import day.Day

class Day09: Day("src/day09/input.txt") {
    private val disk = input.readText()

    private fun getBlocksFromDisk() = buildList {
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
        val blocks = getBlocksFromDisk()
        val freeBlockIndices = blocks.foldIndexed(mutableListOf<Int>()) { index, acc, block ->
            if (block == ".") acc.add(index)
            acc
        }

        for (i in blocks.indices.reversed()) {
            val block = blocks[i]
            if (block != ".") {
                val freeBlockIndex = freeBlockIndices.removeFirstOrNull()
                if (freeBlockIndex != null && freeBlockIndex < i) {
                    blocks[freeBlockIndex] = block
                    blocks[i] = "."
                }
            }
        }

        return blocks.foldIndexed(0L) { index, checksum, id ->
            if (id != ".") checksum + (index.toLong() * id.toLong()) else checksum
        }
    }

    override fun doPart2(): Long {
        val blocks = getBlocksFromDisk()
        var block: Pair<String, List<Int>> = " " to emptyList()
        for (i in blocks.indices.reversed()) {
            val nextBlock = blocks[i]
            if (nextBlock != block.first) {
                if (block.first != ".") {
                    val freeBlockRanges = findFreeBlockRanges(blocks)
                    val freeBlockRange = freeBlockRanges.firstOrNull { block.second.size <= it.toList().size }
                    if (freeBlockRange != null && block.second.isNotEmpty() && freeBlockRange.last < block.second.last()) {
                        val freeBlocks = freeBlockRange.toList()
                        for (j in block.second.indices) {
                            val blockIndex = block.second[j]
                            val emptyBlockIndex = freeBlocks[j]
                            blocks[emptyBlockIndex] = blocks[blockIndex]
                            blocks[blockIndex] = "."
                        }
                    }
                }
                block = nextBlock to listOf(i)
            } else {
                block = nextBlock to block.second + listOf(i)
            }
        }

        return blocks.foldIndexed(0L) { index, checksum, id ->
            if (id != ".") checksum + (index.toLong() * id.toLong()) else checksum
        }
    }

    private fun findFreeBlockRanges(list: List<String>): List<IntRange> {
        val ranges = mutableListOf<IntRange>()
        var start: Int? = null

        for (i in list.indices) {
            if (list[i] == ".") {
                if (start == null) {
                    start = i
                }
            } else {
                if (start != null) {
                    ranges.add(start..<i)
                    start = null
                }
            }
        }

        if (start != null) {
            ranges.add(start..list.lastIndex)
        }

        return ranges
    }
}

fun main() {
    val day = Day09()
    day.part1()
    day.part2()
}