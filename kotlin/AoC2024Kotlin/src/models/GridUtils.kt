package models

data class Coordinate(val x: Int, val y: Int)

enum class Axis {
    X, Y
}

enum class Direction {
    UP, DOWN, LEFT, RIGHT
}