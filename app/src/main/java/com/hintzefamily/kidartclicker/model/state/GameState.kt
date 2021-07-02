package com.hintzefamily.kidartclicker.model.state

data class GameState(
    val stage: Int,
    val currentMonsterIndex: Int,
    val totalMonsters: Int,
){
    companion object {
        val INITIAL = GameState(
            stage = 1,
            currentMonsterIndex = 1,
            totalMonsters = 10
        )
        val PREVIEW get() = INITIAL
    }
}
