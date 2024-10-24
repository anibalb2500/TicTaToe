package com.example.myapplication.models

enum class Player { X, O }

fun String.toPlayer(): Player? {
    if (this.lowercase() == "x") {
        return Player.X
    } else if (this.lowercase() == "o") {
        return Player.O
    }

    return null
}

fun Player.toOpponent(): Player {
    return when (this) {
        Player.X -> Player.O
        Player.O -> Player.X
    }
}