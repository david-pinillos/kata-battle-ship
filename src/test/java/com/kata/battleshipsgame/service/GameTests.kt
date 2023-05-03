package com.kata.battleshipsgame.service

import com.kata.battleshipsgame.model.Boards
import com.kata.battleshipsgame.model.Player.ME
import com.kata.battleshipsgame.model.Player.RIVAL
import com.kata.battleshipsgame.model.Plays
import com.kata.battleshipsgame.model.Position.BOAT_CHECKED
import com.kata.battleshipsgame.model.Position.BOAT_UNKNOWN
import com.kata.battleshipsgame.model.Position.FREE_CHECKED
import com.kata.battleshipsgame.model.Position.FREE_UNKNOWN
import com.kata.battleshipsgame.util.countPlays
import com.kata.battleshipsgame.util.resolved
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class GameTests {
    private val gameService: BattleShipsGame = BattleShipsGame()

    @Test
    fun `No winner after all plays`() {
        val myBoard = mutableListOf(
            mutableListOf(BOAT_UNKNOWN, BOAT_UNKNOWN, FREE_UNKNOWN),
            mutableListOf(FREE_UNKNOWN, FREE_UNKNOWN, FREE_UNKNOWN),
            mutableListOf(FREE_UNKNOWN, FREE_UNKNOWN, FREE_UNKNOWN),
        )
        val rivalBoard = mutableListOf(
            mutableListOf(FREE_UNKNOWN, BOAT_UNKNOWN, BOAT_UNKNOWN),
            mutableListOf(FREE_UNKNOWN, FREE_UNKNOWN, FREE_UNKNOWN),
            mutableListOf(FREE_UNKNOWN, FREE_UNKNOWN, FREE_UNKNOWN),
        )

        val myPlay = listOf(1 to 1, 2 to 2, 1 to 2)
        val itsPlay = listOf(0 to 1, 2 to 2, 1 to 0)

        val result = gameService.play(Boards(myBoard, rivalBoard), Plays(myPlay, itsPlay))

        assertThat(result.winner).isNull()
        assertThat(result.playsDone).isEqualTo(3)
        assertThat(result.boards.mine).isEqualTo(
            mutableListOf(
                mutableListOf(BOAT_UNKNOWN, BOAT_CHECKED, FREE_UNKNOWN),
                mutableListOf(FREE_CHECKED, FREE_UNKNOWN, FREE_UNKNOWN),
                mutableListOf(FREE_UNKNOWN, FREE_UNKNOWN, FREE_CHECKED),
            ),
        )
        assertThat(result.boards.rivals).isEqualTo(
            mutableListOf(
                mutableListOf(FREE_UNKNOWN, BOAT_UNKNOWN, BOAT_UNKNOWN),
                mutableListOf(FREE_UNKNOWN, FREE_CHECKED, FREE_CHECKED),
                mutableListOf(FREE_UNKNOWN, FREE_UNKNOWN, FREE_CHECKED),
            ),
        )
    }

    @Test
    fun `I win the match 3x3 in the 3 out 3 round with total boats 2, individual boat size 2`() {
        val myBoard = mutableListOf(
            mutableListOf(BOAT_UNKNOWN, BOAT_UNKNOWN, FREE_UNKNOWN),
            mutableListOf(FREE_UNKNOWN, FREE_UNKNOWN, FREE_UNKNOWN),
            mutableListOf(FREE_UNKNOWN, FREE_UNKNOWN, FREE_UNKNOWN),
        )
        val rivalBoard = mutableListOf(
            mutableListOf(FREE_UNKNOWN, FREE_UNKNOWN, FREE_UNKNOWN),
            mutableListOf(FREE_UNKNOWN, BOAT_UNKNOWN, BOAT_UNKNOWN),
            mutableListOf(FREE_UNKNOWN, FREE_UNKNOWN, FREE_UNKNOWN),
        )

        val myPlay = listOf(1 to 1, 2 to 2, 1 to 2)
        val itsPlay = listOf(0 to 1, 2 to 2, 1 to 0)

        val result = gameService.play(Boards(myBoard, rivalBoard), Plays(myPlay, itsPlay))

        assertThat(result.winner).isEqualTo(ME)
        assertThat(result.playsDone).isEqualTo(3)
    }

    @Test
    fun `Rival wins the match 3x3 in the 2 out 3 round with total boats 2, individual boat size 2, I would have won in the 3 out 3`() {
        val myBoard = mutableListOf(
            mutableListOf(BOAT_UNKNOWN, BOAT_UNKNOWN, FREE_UNKNOWN),
            mutableListOf(FREE_UNKNOWN, FREE_UNKNOWN, FREE_UNKNOWN),
            mutableListOf(FREE_UNKNOWN, FREE_UNKNOWN, FREE_UNKNOWN),
        )
        val rivalBoard = mutableListOf(
            mutableListOf(FREE_UNKNOWN, FREE_UNKNOWN, FREE_UNKNOWN),
            mutableListOf(FREE_UNKNOWN, BOAT_UNKNOWN, BOAT_UNKNOWN),
            mutableListOf(FREE_UNKNOWN, FREE_UNKNOWN, FREE_UNKNOWN),
        )

        val myPlay = listOf(1 to 1, 2 to 2, 1 to 2) // I would have won in the third round
        val itsPlay = listOf(0 to 0, 0 to 1, 1 to 1)

        val result = gameService.play(Boards(myBoard, rivalBoard), Plays(myPlay, itsPlay))

        assertThat(result.winner).isEqualTo(RIVAL)
        assertThat(result.playsDone).isEqualTo(2)
    }

    @Test
    fun `I do one play less than the rival, because it won in its last play`() {
        val myBoard = mutableListOf(
            mutableListOf(BOAT_UNKNOWN, BOAT_UNKNOWN, FREE_UNKNOWN),
            mutableListOf(FREE_UNKNOWN, FREE_UNKNOWN, FREE_UNKNOWN),
            mutableListOf(FREE_UNKNOWN, FREE_UNKNOWN, FREE_UNKNOWN),
        )
        val rivalBoard = mutableListOf(
            mutableListOf(FREE_UNKNOWN, FREE_UNKNOWN, FREE_UNKNOWN),
            mutableListOf(FREE_UNKNOWN, BOAT_UNKNOWN, BOAT_UNKNOWN),
            mutableListOf(FREE_UNKNOWN, FREE_UNKNOWN, FREE_UNKNOWN),
        )

        val myPlay = listOf(1 to 1, 1 to 2, 0 to 2) // I would have won in the same round if I would start first
        val itsPlay = listOf(0 to 0, 0 to 1, 1 to 1)

        val result = gameService.play(Boards(myBoard, rivalBoard), Plays(myPlay, itsPlay), RIVAL)

        assertThat(result.winner).isEqualTo(RIVAL)
        assertThat(result.boards.mine.resolved()).isTrue
        assertThat(result.boards.mine.countPlays()).isEqualTo(2)
        assertThat(result.boards.rivals.resolved()).isFalse
        assertThat(result.boards.rivals.countPlays()).isEqualTo(1)
        assertThat(result.playsDone).isEqualTo(2)
    }

    @Test
    fun `The rival does one play less than me, because I won in my last play`() {
        val myBoard = mutableListOf(
            mutableListOf(BOAT_UNKNOWN, BOAT_UNKNOWN, FREE_UNKNOWN),
            mutableListOf(FREE_UNKNOWN, FREE_UNKNOWN, FREE_UNKNOWN),
            mutableListOf(FREE_UNKNOWN, FREE_UNKNOWN, FREE_UNKNOWN),
        )
        val rivalBoard = mutableListOf(
            mutableListOf(FREE_UNKNOWN, FREE_UNKNOWN, FREE_UNKNOWN),
            mutableListOf(FREE_UNKNOWN, BOAT_UNKNOWN, BOAT_UNKNOWN),
            mutableListOf(FREE_UNKNOWN, FREE_UNKNOWN, FREE_UNKNOWN),
        )

        val myPlay = listOf(1 to 1, 1 to 2, 0 to 2)
        val itsPlay = listOf(0 to 0, 0 to 1, 1 to 1) // It would have won in the same round if I would start first

        val result = gameService.play(Boards(myBoard, rivalBoard), Plays(myPlay, itsPlay), ME)

        assertThat(result.winner).isEqualTo(ME)
        assertThat(result.boards.rivals.resolved()).isTrue
        assertThat(result.boards.rivals.countPlays()).isEqualTo(2)
        assertThat(result.boards.mine.resolved()).isFalse
        assertThat(result.boards.mine.countPlays()).isEqualTo(1)
        assertThat(result.playsDone).isEqualTo(2)
    }
}
