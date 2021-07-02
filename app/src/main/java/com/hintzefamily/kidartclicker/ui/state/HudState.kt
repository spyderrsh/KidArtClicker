package com.hintzefamily.kidartclicker.ui.state

data class HudState(
    val level: Int,
    val money: Int,
    val clickDamage: Int,
    val totalMonsterInLevel: Int,
    val currentMonsterInLevel: Int,
){
    companion object {
        val initial = HudState(
            level = 1,
            money = 0,
            clickDamage = 1,
            totalMonsterInLevel = 10,
            currentMonsterInLevel = 1
        )
    }
}
