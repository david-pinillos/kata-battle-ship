package com.kata.battleshipsgame.util

import com.kata.battleshipsgame.model.Boat
import com.kata.battleshipsgame.model.Coordinate

fun Boat.getAllCoordinates(): List<Coordinate> = handleBoat<Any, List<Coordinate>>(
    boatInAColumn = {
        val intRange: IntRange = this.first.first..this.second.first
        intRange.map { it to this.first.second }
    },
    boatInARow = {
        val intRange: IntRange = this.first.second..this.second.second
        intRange.map { this.first.first to it }
    },
)

fun Boat.isCloseCell(coordinate: Coordinate): Boolean =
    this.getAllCoordinates().any { it.isNextTo(coordinate) }

fun Boat.calculateSize(): Int = handleBoat<Any, Int>(
    boatInAColumn = { (this.second.first - this.first.first) + 1 },
    boatInARow = { (this.second.second - this.first.second) + 1 },
)

fun List<Boat>.containsCell(coordinate: Coordinate) = this.none { it.containsCell(coordinate) }

fun List<Boat>.isCloseCells(coordinate: Coordinate) = this.any { it.isCloseCell(coordinate) }

fun Boat.containsCell(coordinate: Coordinate): Boolean = handleBoat<Coordinate, Boolean>(
    boatInAColumn = {
        coordinate.second == this.first.second &&
            coordinate.first in this.first.first..this.second.first
    },
    boatInARow = {
        coordinate.first == this.first.first &&
            coordinate.second in this.first.second..this.second.second
    },
)
