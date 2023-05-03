package com.kata.battleshipsgame.util

import com.kata.battleshipsgame.model.Board
import com.kata.battleshipsgame.model.Boards
import com.kata.battleshipsgame.model.Boat
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
        BigDecimal(allCellsBoatsCount.toDouble() / allCellsCount.toDouble()).multiply(BigDecimal(100)).setScale(0, HALF_UP)

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
    // TODO add verifications for boat size
}

private fun getBoatFromCell(board: Board, row: Int, col: Int): Boat? =
    findBoatInRow(board, row, col) ?: findBoatInColumn(board, row, col)
