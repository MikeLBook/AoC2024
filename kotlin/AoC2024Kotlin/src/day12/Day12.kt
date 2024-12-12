package day12

import models.Coordinate
import models.Day

data class Region(
    val plots: List<Coordinate>,
    val plant: String,
    val perimeter: Int
) {
    val area: Int
        get() = plots.size

    val sides: Int
        get() {
            var sides = 0
            val plotRows = plots.groupBy({ it.y }, { it }).mapValues { (_, coords) ->
                coords.sortedBy { it.x }
            }
            var lastRow = emptyList<Coordinate>()
            plotRows.entries.forEach { row ->
                if (row.key == plotRows.keys.first()) sides++ // top border
                if (row.key == plotRows.keys.last()) sides++ // bottom border
                if (lastRow.isEmpty()) {
                    sides += 2 // left and right borders beginning with the top row
                } else {
                    row.value.forEachIndexed { index, coordinate ->
                        when {
                            index == 0 && coordinate.x != lastRow.first().x -> sides += 2 // perpendicular outer border
                            index == row.value.size - 1 && coordinate.x != lastRow.last().x -> sides += 2 // perpendicular outer border
                            else -> {
                                
                            }
                        }
                    }
                }
                lastRow = row.value
            }
            return sides
        }
}

class Day12: Day("src/day12/example.txt") {
    private val grid = input.readLines().map { it.split("").filter { char -> char.isNotBlank() } }
    private val regions = mutableMapOf<Int, Region>()

    private fun isInBounds(coordinate: Coordinate) = coordinate.x in grid.indices && coordinate.y in grid.indices
    private fun isSamePlant(coordinate: Coordinate, plant: String) = grid[coordinate.y][coordinate.x] == plant

    private fun getRegion(coordinate: Coordinate): Region {
        val plant = grid[coordinate.y][coordinate.x]
        val plots = mutableListOf<Coordinate>()
        var perimeter = 0
        fun tracePlots(coordinate: Coordinate) {
            when {
                !isInBounds(coordinate) -> perimeter++
                plots.contains(coordinate) -> {}
                isSamePlant(coordinate, plant) -> {
                    plots.add(coordinate)
                    tracePlots(Coordinate(coordinate.x + 1, coordinate.y))
                    tracePlots(Coordinate(coordinate.x - 1, coordinate.y))
                    tracePlots(Coordinate(coordinate.x, coordinate.y + 1))
                    tracePlots(Coordinate(coordinate.x, coordinate.y - 1))
                }
                else -> perimeter++
            }
        }
        tracePlots(coordinate)
        return Region(plots, plant, perimeter)
    }

    override fun doPart1(): Any {
        var regionCount = 1
        for (x in grid.indices) {
            for (y in grid.indices) {
                val coordinate = Coordinate(x, y)
                if (regions.values.none { it.plots.contains(coordinate) }) {
                    regions[regionCount] = getRegion(coordinate)
                    regionCount++
                }
            }
        }
        return regions.values.sumOf { it.area * it.perimeter }
    }
    override fun doPart2(): Any {
        regions.values.forEach {
            println("${it.plant}: ${it.sides}")
        }

        return regions.values.sumOf { it.area * it.sides }
    }
}

fun main() {
    val day = Day12()
    day.part1()
    day.part2()
}