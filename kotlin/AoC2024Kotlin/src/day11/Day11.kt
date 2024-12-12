package day11

import models.Day

class Day11: Day("src/day11/input.txt") {
    private val initialArrangement = input.readText().split(" ")

    // It's tempting to refactor part 1 to use part 2's solution, but I'll keep it around.
    override fun doPart1(): Int {
        var stones = initialArrangement
        for (i in 1..25) {
            stones = stones.fold(mutableListOf()) { accumulatedStones, stone ->
                when {
                    stone == "0" -> accumulatedStones.add("1")
                    stone.length % 2 == 0 -> {
                        accumulatedStones.add(stone.substring(0, stone.length / 2).toLong().toString())
                        accumulatedStones.add(stone.substring(stone.length /2).toLong().toString())
                    }
                    else -> {
                        val product = stone.toLong() * 2024
                        accumulatedStones.add(product.toString())
                    }
                }
                accumulatedStones
            }
        }
        return stones.size
    }

    override fun doPart2(): Long {
        return initialArrangement.sumOf { startingStone ->
            1 + blink(startingStone, 1, mutableMapOf())
        }
    }

    private fun blink(stone: String, blinks: Int, cache: MutableMap<Pair<String, Int>, Long>): Long {
        if (blinks > 75) return 0
        return cache.getOrPut(stone to blinks) {
            when {
                stone == "0" -> blink("1", blinks + 1, cache)
                stone.length % 2 == 0 -> {
                    val splitStoneA = stone.substring(0, stone.length / 2).toLong().toString()
                    val splitStoneB = stone.substring(stone.length / 2).toLong().toString()
                    1 + blink(splitStoneA, blinks + 1, cache) + blink(splitStoneB, blinks + 1, cache)
                }
                else -> {
                    val product = stone.toLong() * 2024
                    blink(product.toString(), blinks + 1, cache)
                }
            }
        }
    }
}

fun main() {
    val day = Day11()
    day.part1()
    day.part2()
}