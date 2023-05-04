package com.kata.battleshipsgame.service

import com.kata.battleshipsgame.model.Board
import com.kata.battleshipsgame.model.Boards
import com.kata.battleshipsgame.model.MatchResults
import com.kata.battleshipsgame.model.Play
import com.kata.battleshipsgame.model.Player
import com.kata.battleshipsgame.model.Player.ME
import com.kata.battleshipsgame.model.Player.RIVAL
import com.kata.battleshipsgame.model.PlayerData
import com.kata.battleshipsgame.model.Plays
import com.kata.battleshipsgame.model.Position.BOAT_CHECKED
import com.kata.battleshipsgame.model.Position.BOAT_UNKNOWN
import com.kata.battleshipsgame.model.Position.FREE_CHECKED
import com.kata.battleshipsgame.model.Position.FREE_UNKNOWN
import com.kata.battleshipsgame.util.printBoards
import com.kata.battleshipsgame.util.printWinner
import com.kata.battleshipsgame.util.resolved
import com.kata.battleshipsgame.util.verifyBoards
import com.kata.battleshipsgame.util.verifyBoats
import com.kata.battleshipsgame.util.winner
import kotlin.math.max

class BattleShipsGame {

    fun play(boards: Boards, plays: Plays, firstPlayer: Player = ME): MatchResults {
        doVerifications(boards)

        boards.printBoards()

        val (firstPlayerData: PlayerData, secondPlayerData: PlayerData) = orderPlayers(boards, plays, firstPlayer)

        val currentTurn = doTheGame(boards, firstPlayerData, secondPlayerData)

        boards.printWinner()
        boards.printBoards()

        return MatchResults(boards = boards, winner = boards.winner(), playsDone = currentTurn)
    }

    private fun orderPlayers(boards: Boards, plays: Plays, firstPlayer: Player): Pair<PlayerData, PlayerData> {
        val rivalPlayData = PlayerData(RIVAL, boards.mine, plays.rivals)
        val myPlayData = PlayerData(ME, boards.rivals, plays.mine)
        return if (firstPlayer == ME) myPlayData to rivalPlayData else rivalPlayData to myPlayData
    }

    private fun doVerifications(boards: Boards) {
        boards.verifyBoards()
        boards.verifyBoats()
    }

    private fun doTheGame(boards: Boards, firstPlayerData: PlayerData, secondPlayerData: PlayerData): Int {
        val maxPlaySize = max(firstPlayerData.playerPlay.size, secondPlayerData.playerPlay.size)
        var playNumber = 0
        while (!boards.endGame() && playNumber < maxPlaySize) {
            doOnePlay(firstPlayerData.targetBoard, firstPlayerData.playerPlay, playNumber)
            if (!boards.endGame()) {
                doOnePlay(secondPlayerData.targetBoard, secondPlayerData.playerPlay, playNumber)
            }
            playNumber++
        }
        return playNumber
    }

    private fun doOnePlay(board: Board, plays: List<Play>, numberOfPlay: Int) {
        if (numberOfPlay > plays.size) {
            throw IllegalArgumentException("There is no play in the player for number of play $numberOfPlay")
        }
        val play = plays[numberOfPlay]
        board[play.first][play.second] = when (val currentPosition = board[play.first][play.second]) {
            FREE_UNKNOWN -> FREE_CHECKED
            BOAT_UNKNOWN -> BOAT_CHECKED
            else -> throw IllegalArgumentException("Position (${play.first} ${play.second}) already known, value = $currentPosition")
        }
    }
}

fun Boards.endGame(): Boolean = mine.resolved() || rivals.resolved()
