package com.kata.battleshipsgame.model

data class PlayerData(
    val player: Player,
    val targetBoard: Board,
    val playerPlay: List<Play>,
)
