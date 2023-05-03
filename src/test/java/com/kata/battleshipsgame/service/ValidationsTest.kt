package com.kata.battleshipsgame.service

import com.kata.battleshipsgame.model.Boards
import com.kata.battleshipsgame.model.Player.ME
import com.kata.battleshipsgame.model.Plays
import com.kata.battleshipsgame.model.Position
import com.kata.battleshipsgame.model.Position.BOAT_CHECKED
import com.kata.battleshipsgame.model.Position.BOAT_UNKNOWN
import com.kata.battleshipsgame.model.Position.FREE_UNKNOWN
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource

class ValidationsTest {
    private val gameService: BattleShipsGame = BattleShipsGame()

    private fun oneCellBoard() = mutableListOf(mutableListOf(BOAT_UNKNOWN))
    private val validBoard = mutableListOf(
        mutableListOf(BOAT_UNKNOWN, BOAT_UNKNOWN, FREE_UNKNOWN),
        mutableListOf(FREE_UNKNOWN, FREE_UNKNOWN, FREE_UNKNOWN),
        mutableListOf(FREE_UNKNOWN, FREE_UNKNOWN, FREE_UNKNOWN),
    )

    @Test
    fun `Exception when 1x1`() {
        val myBoard = oneCellBoard()
        val rivalBoard = oneCellBoard()

        val myPlay = listOf(1 to 1)
        val itsPlay = listOf(0 to 1)

        assertThatThrownBy { gameService.play(Boards(myBoard, rivalBoard), Plays(myPlay, itsPlay), ME) }
            .hasMessage("Min rows = 3, actual = 1")
    }

    @ParameterizedTest
    @CsvSource(
        value = [
            "3,4",
            "4,3",
            "4,5",
            "8,9",
            "9,8",
        ],
    )
    fun `Exception when rows not equals to columns`(rows: Int, columns: Int) {
        val myBoard = MutableList(rows) { MutableList(columns) { FREE_UNKNOWN } }
        val rivalBoard = validBoard

        val myPlay = listOf(1 to 1)
        val itsPlay = listOf(0 to 1)

        assertThatThrownBy { gameService.play(Boards(myBoard, rivalBoard), Plays(myPlay, itsPlay), ME) }
            .hasMessage("Columns ($columns) and rows ($rows) should have the same size")
    }

    @Test
    fun `Exception when less rows than expected`() {
        val rows = 2
        val columns = 8
        val myBoard = MutableList(rows) { MutableList(columns) { FREE_UNKNOWN } }
        val rivalBoard = validBoard

        val myPlay = listOf(1 to 1)
        val itsPlay = listOf(0 to 1)

        assertThatThrownBy { gameService.play(Boards(myBoard, rivalBoard), Plays(myPlay, itsPlay), ME) }
            .hasMessage("Min rows = 3, actual = $rows")
    }

    @Test
    fun `Exception when more rows than expected`() {
        val rows = 10
        val columns = 5
        val myBoard = MutableList(rows) { MutableList(columns) { FREE_UNKNOWN } }
        val rivalBoard = validBoard

        val myPlay = listOf(1 to 1)
        val itsPlay = listOf(0 to 1)

        assertThatThrownBy { gameService.play(Boards(myBoard, rivalBoard), Plays(myPlay, itsPlay), ME) }
            .hasMessage("Max rows = 9, actual = $rows")
    }

    @Test
    fun `Exception when less columns than expected`() {
        val rows = 5
        val columns = 2
        val myBoard = MutableList(rows) { MutableList(columns) { FREE_UNKNOWN } }
        val rivalBoard = validBoard

        val myPlay = listOf(1 to 1)
        val itsPlay = listOf(0 to 1)

        assertThatThrownBy { gameService.play(Boards(myBoard, rivalBoard), Plays(myPlay, itsPlay), ME) }
            .hasMessage("Min cols = 3, actual = $columns")
    }

    @Test
    fun `Exception when more columns than expected`() {
        val rows = 5
        val columns = 10
        val myBoard = MutableList(rows) { MutableList(columns) { FREE_UNKNOWN } }
        val rivalBoard = validBoard

        val myPlay = listOf(1 to 1)
        val itsPlay = listOf(0 to 1)

        assertThatThrownBy { gameService.play(Boards(myBoard, rivalBoard), Plays(myPlay, itsPlay), ME) }
            .hasMessage("Max cols = 9, actual = $columns")
    }

    @Test
    fun `Exception when number of boats lower than 20 percent`() {
        val rows = 3
        val columns = 3
        val myBoard = MutableList(rows) { MutableList(columns) { FREE_UNKNOWN } }
            .apply { this[0][0] = BOAT_UNKNOWN }
        val rivalBoard = validBoard

        val myPlay = listOf(1 to 1)
        val itsPlay = listOf(0 to 1)

        assertThatThrownBy { gameService.play(Boards(myBoard, rivalBoard), Plays(myPlay, itsPlay)) }
            .hasMessage("Min boat cells should be 20% and they are 11%")
    }

    @Test
    fun `Exception when number of boats upper than 80 percent`() {
        val rows = 3
        val columns = 3
        val myBoard = MutableList(rows) { MutableList(columns) { BOAT_UNKNOWN } }
            .apply { this[0][0] = FREE_UNKNOWN }
        val rivalBoard = validBoard

        val myPlay = listOf(1 to 1)
        val itsPlay = listOf(0 to 1)

        assertThatThrownBy { gameService.play(Boards(myBoard, rivalBoard), Plays(myPlay, itsPlay)) }
            .hasMessage("Max boat cells should be 80% and they are 89%")
    }

    @Test
    fun `Exception when boat size greater than 5, it is 6 horizontal`() {
        val myBoard = mutableListOf(
            mutableListOf(BOAT_UNKNOWN, BOAT_UNKNOWN, BOAT_UNKNOWN),
            mutableListOf(FREE_UNKNOWN, FREE_UNKNOWN, FREE_UNKNOWN),
            mutableListOf(FREE_UNKNOWN, FREE_UNKNOWN, FREE_UNKNOWN),
        )
        val rivalBoard = mutableListOf(
            mutableListOf(BOAT_UNKNOWN, BOAT_UNKNOWN, BOAT_UNKNOWN, BOAT_UNKNOWN, BOAT_UNKNOWN, BOAT_UNKNOWN),
            mutableListOf(FREE_UNKNOWN, FREE_UNKNOWN, FREE_UNKNOWN, FREE_UNKNOWN, FREE_UNKNOWN, FREE_UNKNOWN),
            mutableListOf(FREE_UNKNOWN, FREE_UNKNOWN, BOAT_UNKNOWN, FREE_UNKNOWN, FREE_UNKNOWN, FREE_UNKNOWN),
            mutableListOf(FREE_UNKNOWN, FREE_UNKNOWN, BOAT_UNKNOWN, FREE_UNKNOWN, FREE_UNKNOWN, FREE_UNKNOWN),
            mutableListOf(FREE_UNKNOWN, FREE_UNKNOWN, BOAT_UNKNOWN, FREE_UNKNOWN, FREE_UNKNOWN, FREE_UNKNOWN),
            mutableListOf(FREE_UNKNOWN, FREE_UNKNOWN, FREE_UNKNOWN, FREE_UNKNOWN, FREE_UNKNOWN, FREE_UNKNOWN),
        )

        val myPlay = listOf(1 to 1)
        val itsPlay = listOf(0 to 1)

        assertThatThrownBy { gameService.play(Boards(myBoard, rivalBoard), Plays(myPlay, itsPlay)) }
            .hasMessage("Boat can't be bigger than 5, size was 6")
    }

    @Test
    fun `Exception when boat size greater than 5, it is 6 vertical`() {
        val myBoard = mutableListOf(
            mutableListOf(FREE_UNKNOWN, FREE_UNKNOWN, BOAT_UNKNOWN),
            mutableListOf(FREE_UNKNOWN, FREE_UNKNOWN, BOAT_UNKNOWN),
            mutableListOf(FREE_UNKNOWN, FREE_UNKNOWN, BOAT_UNKNOWN),
        )
        val rivalBoard = mutableListOf(
            mutableListOf(BOAT_UNKNOWN, FREE_UNKNOWN, FREE_UNKNOWN, FREE_UNKNOWN, FREE_UNKNOWN, FREE_UNKNOWN),
            mutableListOf(BOAT_UNKNOWN, FREE_UNKNOWN, FREE_UNKNOWN, FREE_UNKNOWN, FREE_UNKNOWN, FREE_UNKNOWN),
            mutableListOf(BOAT_UNKNOWN, FREE_UNKNOWN, BOAT_UNKNOWN, BOAT_UNKNOWN, BOAT_UNKNOWN, FREE_UNKNOWN),
            mutableListOf(BOAT_UNKNOWN, FREE_UNKNOWN, FREE_UNKNOWN, FREE_UNKNOWN, FREE_UNKNOWN, FREE_UNKNOWN),
            mutableListOf(BOAT_UNKNOWN, FREE_UNKNOWN, BOAT_UNKNOWN, BOAT_UNKNOWN, BOAT_UNKNOWN, FREE_UNKNOWN),
            mutableListOf(BOAT_UNKNOWN, FREE_UNKNOWN, FREE_UNKNOWN, FREE_UNKNOWN, FREE_UNKNOWN, FREE_UNKNOWN),
        )

        val myPlay = listOf(1 to 1)
        val itsPlay = listOf(0 to 1)

        assertThatThrownBy { gameService.play(Boards(myBoard, rivalBoard), Plays(myPlay, itsPlay)) }
            .hasMessage("Boat can't be bigger than 5, size was 6")
    }

    @Test
    fun `Exception when more than 5 boats`() {
        val myBoard = mutableListOf(
            mutableListOf(FREE_UNKNOWN, FREE_UNKNOWN, BOAT_UNKNOWN),
            mutableListOf(FREE_UNKNOWN, FREE_UNKNOWN, BOAT_UNKNOWN),
            mutableListOf(FREE_UNKNOWN, FREE_UNKNOWN, BOAT_UNKNOWN),
        )
        val rivalBoard = mutableListOf(
            mutableListOf(FREE_UNKNOWN, BOAT_UNKNOWN, BOAT_UNKNOWN, FREE_UNKNOWN, BOAT_UNKNOWN, BOAT_UNKNOWN),
            mutableListOf(BOAT_UNKNOWN, FREE_UNKNOWN, FREE_UNKNOWN, BOAT_UNKNOWN, FREE_UNKNOWN, FREE_UNKNOWN),
            mutableListOf(BOAT_UNKNOWN, FREE_UNKNOWN, FREE_UNKNOWN, BOAT_UNKNOWN, FREE_UNKNOWN, FREE_UNKNOWN),
            mutableListOf(BOAT_UNKNOWN, FREE_UNKNOWN, FREE_UNKNOWN, BOAT_UNKNOWN, FREE_UNKNOWN, FREE_UNKNOWN),
            mutableListOf(BOAT_UNKNOWN, FREE_UNKNOWN, FREE_UNKNOWN, BOAT_UNKNOWN, FREE_UNKNOWN, FREE_UNKNOWN),
            mutableListOf(FREE_UNKNOWN, BOAT_UNKNOWN, BOAT_UNKNOWN, FREE_UNKNOWN, BOAT_UNKNOWN, BOAT_UNKNOWN),
        )

        val myPlay = listOf(1 to 1)
        val itsPlay = listOf(0 to 1)

        assertThatThrownBy { gameService.play(Boards(myBoard, rivalBoard), Plays(myPlay, itsPlay)) }
            .hasMessage("Can't create more than 5 boats, were 6")
    }

    @Test
    fun `Exception when boats touch other boats, second in row`() {
        val myBoard = mutableListOf(
            mutableListOf(BOAT_UNKNOWN, FREE_UNKNOWN, FREE_UNKNOWN),
            mutableListOf(BOAT_UNKNOWN, FREE_UNKNOWN, FREE_UNKNOWN),
            mutableListOf(BOAT_UNKNOWN, BOAT_UNKNOWN, BOAT_UNKNOWN),
        )
        val rivalBoard = mutableListOf(
            mutableListOf(FREE_UNKNOWN, FREE_UNKNOWN, FREE_UNKNOWN),
            mutableListOf(BOAT_UNKNOWN, BOAT_UNKNOWN, BOAT_UNKNOWN),
            mutableListOf(FREE_UNKNOWN, FREE_UNKNOWN, FREE_UNKNOWN),
        )

        val myPlay = listOf(1 to 1)
        val itsPlay = listOf(0 to 1)

        assertThatThrownBy { gameService.play(Boards(myBoard, rivalBoard), Plays(myPlay, itsPlay)) }
            .hasMessage("Cant put a boat near other. Check row 2, col 1")
    }

    @Test
    fun `Exception when boats touch other boats, second in column`() {
        val myBoard = mutableListOf(
            mutableListOf(BOAT_UNKNOWN, BOAT_UNKNOWN, FREE_UNKNOWN),
            mutableListOf(FREE_UNKNOWN, BOAT_UNKNOWN, FREE_UNKNOWN),
            mutableListOf(FREE_UNKNOWN, BOAT_UNKNOWN, FREE_UNKNOWN),
        )
        val rivalBoard = mutableListOf(
            mutableListOf(FREE_UNKNOWN, FREE_UNKNOWN, FREE_UNKNOWN),
            mutableListOf(BOAT_UNKNOWN, BOAT_UNKNOWN, BOAT_UNKNOWN),
            mutableListOf(FREE_UNKNOWN, FREE_UNKNOWN, FREE_UNKNOWN),
        )

        val myPlay = listOf(1 to 1)
        val itsPlay = listOf(0 to 1)

        assertThatThrownBy { gameService.play(Boards(myBoard, rivalBoard), Plays(myPlay, itsPlay)) }
            .hasMessage("Cant put a boat near other. Check row 1, col 1")
    }

    @Test
    fun `Exception when any cell is known, a boat one`() {
        val myBoard = MutableList(3) { MutableList(3) { FREE_UNKNOWN } }
            .apply {
                this[0][0] = BOAT_UNKNOWN
                this[1][0] = BOAT_CHECKED
                this[2][0] = BOAT_UNKNOWN
            }
        val rivalBoard = validBoard

        val myPlay = listOf(1 to 1)
        val itsPlay = listOf(0 to 1)

        assertThatThrownBy { gameService.play(Boards(myBoard, rivalBoard), Plays(myPlay, itsPlay)) }
            .hasMessage("All cells needs to be unknown")
    }

    @Test
    fun `Exception when any cell is known, a free one`() {
        val myBoard = MutableList(3) { MutableList(3) { FREE_UNKNOWN } }
            .apply {
                this[0][0] = BOAT_UNKNOWN
                this[1][0] = BOAT_UNKNOWN
                this[2][0] = BOAT_UNKNOWN
                this[0][1] = Position.FREE_CHECKED
            }
        val rivalBoard = validBoard

        val myPlay = listOf(1 to 1)
        val itsPlay = listOf(0 to 1)

        assertThatThrownBy { gameService.play(Boards(myBoard, rivalBoard), Plays(myPlay, itsPlay)) }
            .hasMessage("All cells needs to be unknown")
    }
}
