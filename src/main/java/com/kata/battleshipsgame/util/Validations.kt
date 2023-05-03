package com.kata.battleshipsgame.util

import com.kata.battleshipsgame.model.Board
import com.kata.battleshipsgame.model.Boards
import com.kata.battleshipsgame.model.Boat
import java.math.BigDecimal

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
    this.verifyCellPercentageBoats()
    this.verifyCellsAreUnknown()
    this.verifyBoatSize()
}

fun Board.verifyCellsAreUnknown() {
    val anyNotUnknown = this.flatMap { row -> row.map { it.isUnknown } }.any { !it }
    if (anyNotUnknown) throw IllegalArgumentException("All cells needs to be unknown")
}

fun Board.verifyCellPercentageBoats() {
    val allCellsCount = this.sumOf { it.size }
    val allCellsBoatsCount = this.flatMap { row -> row.map { it.isBoat } }.count { it }
    val percentageBoats =
        BigDecimal(allCellsBoatsCount.toDouble() / allCellsCount.toDouble()).multiply(BigDecimal(100)).setScale(
            0,
            java.math.RoundingMode.HALF_UP,
        )

    if (percentageBoats < BigDecimal("20")) throw IllegalArgumentException("Min boat cells should be 20% and they are $percentageBoats%")
    if (percentageBoats > BigDecimal("80")) throw IllegalArgumentException("Max boat cells should be 80% and they are $percentageBoats%")
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
                val possibleBoat = verifyBoatCell(this, row, col)
                possibleBoat?.let { boats.add(it) }
                    ?.also { if (boats.size > 5) throw IllegalArgumentException("Can't create more than 5 boats, were ${boats.size}") }
            }
        }
    }
}

fun verifyBoatCell(board: Board, row: Int, col: Int): Boat? {
    val boat = countRow(board, row, col) ?: countColumn(board, row, col)
    val size = boat?.calculateSize() ?: 1
    if (size < 2) throw IllegalArgumentException("Boat can't be smaller than 2, size was $size")
    if (size > 5) throw IllegalArgumentException("Boat can't be bigger than 5, size was $size")
    return boat
}
