package com.kata.battleshipsgame.util

import com.kata.battleshipsgame.model.Board
import com.kata.battleshipsgame.model.Boards

fun Board.printBoard() {
    this.forEach { row ->
        row.forEach { column -> print(column.valueToPrint) }
        println("")
    }
}

fun Boards.printBoards() {
    println("---")
    println("My board")
    this.mine.printBoard()
    println("---")
    println("Rival board")
    this.rivals.printBoard()
    println("---")
}

fun Boards.printWinner() {
    println("---")
    if (rivals.resolved()) {
        println("I win")
    } else if (mine.resolved()) {
        println("Mi clever rival won")
    } else {
        println("No winner")
    }
    println("---")
}