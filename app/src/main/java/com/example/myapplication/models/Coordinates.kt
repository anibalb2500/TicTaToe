package com.example.myapplication.models

class Coordinates {

    val coordinates: Array<Array<Player?>> = arrayOf(
        arrayOf(null, null, null),
        arrayOf(null, null, null),
        arrayOf(null, null, null)
    )

    fun updateCoordinates(moveData: MoveData) {
        coordinates[moveData.xCoordinate][moveData.yCoordinate] = moveData.player

    }
}