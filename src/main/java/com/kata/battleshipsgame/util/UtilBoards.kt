package com.kata.battleshipsgame.util

import com.kata.battleshipsgame.model.Board
import com.kata.battleshipsgame.model.Boards
import com.kata.battleshipsgame.model.Player
import com.kata.battleshipsgame.model.Position

fun Board.resolved(): Boolean =
    this.flatMap { row -> row.map { column -> Position.BOAT_UNKNOWN == column } }.none { it }

fun Board.countPlays(): Int =
    this.flatMap { row -> row.map { column -> !column.isUnknown } }.count { it }


fun Boards.winner(): Player? = if (mine.resolved()) Player.RIVAL else if (rivals.resolved()) Player.ME else null
