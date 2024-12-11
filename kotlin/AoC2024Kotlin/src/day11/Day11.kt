package day11

import models.Day

class Day11: Day("src/day11/input.txt") {
    private val initialArrangement = input.readText().split(" ")

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
            var stoneCount = 1L
            fun blink(stone: String, blinks: Int) {
                if (blinks <= 75) {
                    when {
                        stone == "0" -> blink("1", blinks + 1)
                        stone.length % 2 == 0 -> {
                            blink(stone.substring(0, stone.length / 2).toLong().toString(), blinks + 1)
                            blink(stone.substring(stone.length /2).toLong().toString(), blinks + 1)
                            stoneCount++
                        }
                        else -> {
                            val product = stone.toLong() * 2024
                            blink(product.toString(), blinks + 1)
                        }
                    }
                }
            }
            blink(startingStone, 1)
            stoneCount
        }
    }
}

fun main() {
    val day = Day11()
    day.part1()
    day.part2()
}