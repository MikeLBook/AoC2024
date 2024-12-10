package day10

import models.Day
import models.Coordinate

class Day10: Day("src/day10/input.txt") {
    private val grid = input.readLines().map { line ->
        line.split("").filter { it.isNotBlank() }.map { it.toInt() }
    }

    private val trailheads: List<Coordinate> = buildList {
        for (y in grid.indices) {
            for (x in grid[y].indices) {
                if (grid[y][x] == 0) add(Coordinate(x, y))
            }
        }
    }

    private fun getNextLocations(currentLocation: Coordinate): List<Coordinate> {
        val up = Coordinate(currentLocation.x, currentLocation.y - 1)
        val down = Coordinate(currentLocation.x, currentLocation.y + 1)
        val left = Coordinate(currentLocation.x - 1, currentLocation.y)
        val right = Coordinate(currentLocation.x + 1, currentLocation.y)
        return listOf(up, down, left, right).filter {
            it.x >= 0 && it.x < grid.size && it.y >= 0 && it.y < grid.size
                    && grid[it.y][it.x] - grid[currentLocation.y][currentLocation.x] == 1
        }
    }

    private fun generateIdealRoutes(coordinate: Coordinate, isPartTwo: Boolean): List<List<Coordinate>> {
        val routes: MutableList<List<Coordinate>> = mutableListOf()
        fun buildRoutes(currentRoute: List<Coordinate>) {
            val nextLocations = getNextLocations(currentRoute.last())
            when {
                nextLocations.isEmpty() -> {
                    if (currentRoute.size == 10) {
                        if (isPartTwo || routes.none { it.last() == currentRoute.last() }) {
                            routes.add(currentRoute)
                        }
                    }
                }
                nextLocations.size == 1 -> buildRoutes(currentRoute + nextLocations.first())
                else -> {
                    nextLocations.forEach { buildRoutes(currentRoute + it) }
                }
            }
        }
        buildRoutes(listOf(coordinate))
        return routes
    }

    override fun doPart1() = trailheads.map { generateIdealRoutes(it, false) }.sumOf { it.size }
    override fun doPart2() = trailheads.map { generateIdealRoutes(it, true) }.sumOf { it.size }
}

fun main() {
    val day = Day10()
    day.part1()
    day.part2()
}