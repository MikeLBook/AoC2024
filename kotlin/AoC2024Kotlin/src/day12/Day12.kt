package day12

import models.*

data class Region(
    val plant: String,
    val plots: List<Coordinate>,
    val perimeter: List<Pair<Coordinate, Direction>>
) {
    val area: Int
        get() = plots.size

    val sides: Int
        get() {
            val northBorders = perimeter.filter { it.second == Direction.UP }
                .map { it.first }
                .groupBy { it.y }
                .values
                .sumOf { it.countContiguousSections(Axis.X) }
            val westBorders = perimeter.filter { it.second == Direction.LEFT }
                .map { it.first }
                .groupBy { it.x }
                .values
                .sumOf { it.countContiguousSections(Axis.Y) }
            val eastBorders = perimeter.filter { it.second == Direction.RIGHT }
                .map { it.first }
                .groupBy { it.x}
                .values
                .sumOf {it.countContiguousSections(Axis.Y)}
            val southBorders = perimeter.filter { it.second == Direction.DOWN }
                .map { it.first }
                .groupBy { it.y}
                .values
                .sumOf { it.countContiguousSections(Axis.X) }
            return northBorders + westBorders + eastBorders + southBorders
        }

    private fun List<Coordinate>.countContiguousSections(axis: Axis): Int {
        if (this.isEmpty()) return 0

        val sortedCoords = when (axis) {
            Axis.X -> this.sortedBy { it.x }
            Axis.Y -> this.sortedBy { it.y }
        }

        var sections = 1
        var prevCoord = sortedCoords[0]

        for (i in 1..< sortedCoords.size) {
            val currentCoord = sortedCoords[i]
            when (axis) {
                Axis.X -> {
                    if (currentCoord.x != prevCoord.x + 1) sections++
                }
                Axis.Y -> {
                    if (currentCoord.y != prevCoord.y + 1) sections++
                }
            }
            prevCoord = currentCoord
        }

        return sections
    }
}

class Day12: Day("src/day12/input.txt") {
    private val grid = input.readLines().map { it.split("").filter { char -> char.isNotBlank() } }

    private fun isInBounds(coordinate: Coordinate) = coordinate.x in grid.indices && coordinate.y in grid.indices
    private fun isSamePlant(coordinate: Coordinate, plant: String) = grid[coordinate.y][coordinate.x] == plant

    private fun getRegion(coordinate: Coordinate): Region {
        val plant = grid[coordinate.y][coordinate.x]
        val plots = mutableListOf<Coordinate>()
        val perimeter = mutableListOf<Pair<Coordinate, Direction>>()
        fun tracePlots(coordinate: Coordinate, direction: Direction = Direction.UP) {
            when {
                !isInBounds(coordinate) -> perimeter.add(coordinate to direction)
                plots.contains(coordinate) -> {}
                isSamePlant(coordinate, plant) -> {
                    plots.add(coordinate)
                    tracePlots(Coordinate(coordinate.x + 1, coordinate.y), Direction.RIGHT)
                    tracePlots(Coordinate(coordinate.x - 1, coordinate.y), Direction.LEFT)
                    tracePlots(Coordinate(coordinate.x, coordinate.y + 1), Direction.DOWN)
                    tracePlots(Coordinate(coordinate.x, coordinate.y - 1), Direction.UP)
                }
                else -> perimeter.add(coordinate to direction)
            }
        }
        tracePlots(coordinate)
        return Region(plant, plots, perimeter)
    }

    private val regions = buildList<Region> {
        for (x in grid.indices) {
            for (y in grid.indices) {
                val coordinate = Coordinate(x, y)
                if (none { it.plots.contains(coordinate) }) {
                    add(getRegion(coordinate))
                }
            }
        }
    }

    override fun doPart1() = regions.sumOf { it.area * it.perimeter.size }
    override fun doPart2() = regions.sumOf { it.area * it.sides }
}

fun main() {
    val day = Day12()
    day.part1()
    day.part2()
}