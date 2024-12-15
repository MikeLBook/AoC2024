package day13

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.runBlocking
import models.Day

data class LongCoordinate(val x: Long, val y: Long)

typealias WinningCombination = Pair<Long, Long>

data class ClawMachine(
    val buttonA: Button,
    val buttonB: Button,
    val prize: LongCoordinate
) {
    val boundedWinningCombinations: List<WinningCombination>
        get() = buildList {
            for (aCount in 1..100) {
                for (bCount in 1..100) {
                    val x = (buttonA.xMovement * aCount) + (buttonB.xMovement * bCount)
                    val y = (buttonA.yMovement * aCount) + (buttonB.yMovement * bCount)
                    val winCondition = prize.x == x.toLong() && prize.y == y.toLong()
                    if (winCondition) {
                        add(Pair(aCount.toLong(), bCount.toLong()))
                    }
                }
            }
        }

    val winningCombinations: List<WinningCombination>
        get() = buildList {
            var x = buttonA.xMovement.toLong()
            var count = 1L
            while (x <= prize.x) {
                val y = buttonA.yMovement * count
                if (x == prize.x && y == prize.y) {
                    add(count to 0)
                } else {
                    val xRemainder = prize.x - x
                    val yRemainder = prize.y - y

                    if (xRemainder % buttonB.xMovement == 0L && yRemainder % buttonB.yMovement == 0L) {
                        if (xRemainder / buttonB.xMovement == yRemainder / buttonB.yMovement) {
                            add(Pair(count, xRemainder / buttonB.xMovement))
                        }
                    }
                }

                x += buttonA.xMovement
                count++
            }
        }
}

fun List<WinningCombination>.fewestWinningTokens() = minOfOrNull { combination ->
    (combination.first * Button.Type.A.tokens) + (combination.second * Button.Type.B.tokens)
} ?: 0


data class Button(
    val type: Type,
    val xMovement: Int,
    val yMovement: Int
) {
    companion object {
        fun from (type: Type, instruction: String): Button {
            val strippedLabel = instruction.split(": ")[1]
            val movementStrings = strippedLabel.split(", ")
            val xMovement = movementStrings[0].split("+")[1].toInt()
            val yMovement = movementStrings[1].split("+")[1].toInt()
            return Button(type, xMovement, yMovement)
        }
    }

    enum class Type(val tokens: Int) {
        A(3), B(1)
    }
}

class Day13: Day("src/day13/input.txt") {
    private fun createClawMachines(includeConversionError: Boolean) = input
        .readLines()
        .filter { it.isNotBlank() }
        .windowed(3, 3) { instructions ->
            val buttonA = Button.from(Button.Type.A, instructions[0])
            val buttonB = Button.from(Button.Type.B, instructions[1])
            val prize = getPrizeCoordinates(instructions[2], includeConversionError)
            ClawMachine(buttonA, buttonB, prize)
        }

    private fun getPrizeCoordinates(instruction: String, includeConversionError: Boolean): LongCoordinate {
        val strippedLabel = instruction.split(": ")[1]
        val coordinateStrings = strippedLabel.split(", ")
        var xCoordinate = coordinateStrings[0].split("=")[1].toLong()
        var yCoordinate = coordinateStrings[1].split("=")[1].toLong()
        if (!includeConversionError) {
            xCoordinate += 10000000000000
            yCoordinate += 10000000000000
        }
        return LongCoordinate(xCoordinate, yCoordinate)
    }

    override fun doPart1() = createClawMachines(includeConversionError = true)
        .sumOf { it.boundedWinningCombinations.fewestWinningTokens() }

    override fun doPart2(): Long = runBlocking {
        val clawMachines = createClawMachines(includeConversionError = false)
        val results = clawMachines.mapIndexed { index, machine ->
            async(Dispatchers.Default) {
                println("Processing machine $index")
                machine.winningCombinations.fewestWinningTokens()
            }
        }
        results.awaitAll().sum()
    }
}

fun main() {
    val day = Day13()
    day.part1()
    day.part2()
}