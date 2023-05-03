package com.kata.battleshipsgame.util

import com.kata.battleshipsgame.model.Board
import com.kata.battleshipsgame.model.Boat

fun countColumn(board: Board, row: Int, col: Int): Boat? {
    val length = board.size
    var max = row
    while (max + 1 < length && board[max + 1][col].isBoat) {
        max += 1
    }
    var min = row
    while (min - 1 >= 0 && board[min - 1][col].isBoat) {
        min -= 1
    }
    return if (min != max) {
        (min to col) to (max to col)
    } else {
        null
    }
}

fun <T : Any, R> Boat.handleBoat(body: T? = null, boatInAColumn: (T?) -> R, boatInARow: (T?) -> R): R =
    if (this.first.first == this.second.first) {
        boatInARow(body)
    } else if (this.first.second == this.second.second) {
        boatInAColumn(body)
    } else {
        throw IllegalArgumentException("Boat need to be in a row or a column")
    }

fun countRow(
    board: Board,
    row: Int,
    col: Int,
): Boat? {
    val length = board.first().size
    var max = col
    while (max + 1 < length && board[row][max + 1].isBoat) {
        max += 1
    }
    var min = col
    while (min - 1 >= 0 && board[row][min - 1].isBoat) {
        min -= 1
    }
    return if (min != max) {
        (row to min) to (row to max)
    } else {
        null
    }
}
