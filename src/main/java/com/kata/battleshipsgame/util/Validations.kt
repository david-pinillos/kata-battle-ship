package com.kata.battleshipsgame.util

import com.kata.battleshipsgame.model.Board
import com.kata.battleshipsgame.model.Boards
import com.kata.battleshipsgame.model.Boat
import com.kata.battleshipsgame.model.Position.BOAT_UNKNOWN
import java.math.BigDecimal
import java.math.RoundingMode.HALF_UP

fun Boards.verifyBoards() {
    val (mine, rivals) = this
    mine.verifySize()
    rivals.verifySize()
}

fun Boards.verifyBoats() {
    val (mine, rivals) = this
    mine.verifyBoats()
    rivals.verifyBoats()
}

fun Board.verifyBoats() {
    val numberOfBoatCells = this.flatten().filter { it == BOAT_UNKNOWN }.size
    val totalNumberOfCells = this.flatten().size
    val boatCellsPercentage = BigDecimal(numberOfBoatCells).divide(BigDecimal(totalNumberOfCells), 2, HALF_UP).multiply(BigDecimal(100)).setScale(0)
    if (boatCellsPercentage < BigDecimal("20")) {
        throw IllegalArgumentException("Min boat cells should be 20% and they are $boatCellsPercentage%")
    }
    this.verifyCellPercentageBoats()
    this.verifyCellsAreUnknown()
    this.verifyBoatSize()
}

fun Board.verifyCellsAreUnknown() {
    // TODO verify
}

fun Board.verifyCellPercentageBoats() {
    // TODO verify
}

fun Board.verifySize() {
    val rows = this.size
    if (rows < 3) throw IllegalArgumentException("Min rows = 3, actual = $rows")
    if (rows > 9) throw IllegalArgumentException("Max rows = 9, actual = $rows")

    if (this.map { it.size }.any { this.first().size != it }) {
        throw IllegalArgumentException("All rows needs to have same value")
    }

    val columns = this.first().size
    if (columns < 3) throw IllegalArgumentException("Min cols = 3, actual = $columns")
    if (columns > 9) throw IllegalArgumentException("Max cols = 9, actual = $columns")

    if (columns != rows) throw IllegalArgumentException("Columns ($columns) and rows ($rows) should have the same size")
}

fun Board.verifyBoatSize() {
    val boats = mutableListOf<Boat>()
    this.forEachIndexed { row, rowList ->
        rowList.forEachIndexed { col, cell ->
            if (cell.isBoat && boats.containsCell(row to col)) {
                if (boats.isCloseCells(row to col)) throw IllegalArgumentException("Cant put a boat near other. Check row $row, col $col")
                val possibleBoat = getBoatFromCell(this, row, col)
                possibleBoat?.let { boats.add(it) }
                    ?.also { if (boats.size > 5) throw IllegalArgumentException("Can't create more than 5 boats, were ${boats.size}") }
            }
        }
    }
}

private fun getBoatFromCell(board: Board, row: Int, col: Int): Boat? {
    val boat = findBoatInRow(board, row, col) ?: findBoatInColumn(board, row, col)
    val size = boat?.calculateSize() ?: 1
    if (size < 2) throw IllegalArgumentException("Boat can't be smaller than 2, size was $size")
    if (size > 5) throw IllegalArgumentException("Boat can't be bigger than 5, size was $size")
    return boat
}
