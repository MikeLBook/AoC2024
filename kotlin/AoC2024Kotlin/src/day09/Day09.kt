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
        var fileID: Pair<String, List<Int>> = " " to emptyList()
        var lastMovedFileID = Int.MAX_VALUE
        for (i in blocks.indices.reversed()) {
            val nextBlock = blocks[i]
            if (nextBlock != fileID.first) {
                if (fileID.first.toIntOrNull() != null && fileID.first.toInt() < lastMovedFileID && fileID.second.isNotEmpty()) {
                    val freeBlockRanges = findFreeBlockRanges(blocks)
                    freeBlockRanges.firstOrNull {
                        fileID.second.size <= it.toList().size && it.max() < fileID.second.min()
                    }?.let {
                        val freeBlocks = it.toList()
                        for (j in fileID.second.indices) {
                            val blockIndex = fileID.second[j]
                            val emptyBlockIndex = freeBlocks[j]
                            blocks[emptyBlockIndex] = blocks[blockIndex]
                            blocks[blockIndex] = "."
                        }
                        lastMovedFileID = fileID.first.toInt()
                    }
                }
                fileID = nextBlock to listOf(i)
            } else {
                fileID = nextBlock to fileID.second + listOf(i)
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