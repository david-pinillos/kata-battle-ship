package com.kata.battleshipsgame.util

import com.kata.battleshipsgame.model.Coordinate

fun Coordinate.isNextTo(coordinate: Coordinate): Boolean =
    this.isSameCell(other = coordinate, plusRow = 1)
        || this.isSameCell(other = coordinate, plusRow = -1)
        || this.isSameCell(other = coordinate, plusCol = 1)
        || this.isSameCell(other = coordinate, plusCol = -1)

fun Coordinate.isSameCell(other: Coordinate, plusRow: Int = 0, plusCol: Int = 0): Boolean =
    this.first == other.first + plusRow && this.second == other.second + plusCol
