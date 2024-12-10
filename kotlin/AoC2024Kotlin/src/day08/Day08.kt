package day08

import models.Coordinate
import models.Day

class Day08: Day("src/day08/input.txt") {
    private val grid = input.readLines().map { it.split("").filter { char -> char.isNotBlank() } }
    private val antennae = buildMap<String, MutableList<Coordinate>> {
        for (y in grid.indices) {
            for (x in grid.indices) {
                val character = grid[y][x]
                if (character != ".") {
                    val locations = getOrPut(grid[y][x]) { mutableListOf() }
                    locations.add(Coordinate(x, y))
                }
            }
        }
    }

    override fun doPart1() = buildSet {
        for (antenna in antennae.entries) {
            val locations = antenna.value
            for (locationA in locations) {
                for (locationB in locations) {
                    if (locationA != locationB) {
                        val mx = locationB.x - locationA.x
                        val my = locationB.y - locationA.y
                        val antinodeA = Coordinate(locationA.x - mx, locationA.y - my)
                        if (isInBounds(antinodeA)) add(antinodeA)
                        val antinodeB = Coordinate(locationB.x + mx, locationB.y + my)
                        if (isInBounds(antinodeB)) add(antinodeB)
                    }
                }
            }
        }
    }.size

    override fun doPart2() = buildSet {
        for (antenna in antennae.entries) {
            val locations = antenna.value
            for (locationA in locations) {
                add(locationA)
                for (locationB in locations) {
                    if (locationA != locationB) {
                        val mx = locationB.x - locationA.x
                        val my = locationB.y - locationA.y
                        var antinode = Coordinate(locationA.x - mx, locationA.y - my)
                        while (isInBounds(antinode)) {
                            add(antinode)
                            antinode = Coordinate(antinode.x - mx, antinode.y - my)
                        }
                        antinode = Coordinate(locationB.x + mx, locationB.y + my)
                        while(isInBounds(antinode)) {
                            add(antinode)
                            antinode = Coordinate(antinode.x + mx, antinode.y + my)
                        }
                    }
                }
            }
        }
    }.size

    private fun isInBounds(coordinate: Coordinate) = coordinate.x in grid.indices && coordinate.y in grid.indices
}

fun main() {
    val day = Day08()
    day.part1()
    day.part2()
}