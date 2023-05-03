package com.kata.battleshipsgame.model

enum class Position(val isUnknown: Boolean, val isBoat: Boolean, val valueToPrint: String) {
    FREE_UNKNOWN(true, false, "  ?  "),
    BOAT_UNKNOWN(true, true, "  O  "),
    FREE_CHECKED(false, false, "  -  "),
    BOAT_CHECKED(false, true, "  X  "),
}
