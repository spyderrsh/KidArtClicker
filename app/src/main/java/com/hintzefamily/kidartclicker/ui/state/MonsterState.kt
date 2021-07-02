package com.hintzefamily.kidartclicker.ui.state

import com.hintzefamily.kidartclicker.ui.monster.KirbyMonster1
import com.hintzefamily.kidartclicker.ui.monster.UiMonster

data class MonsterState(
    val currentHealth: Int,
    val totalHealth: Int,
    val monster: UiMonster,
) {
    companion object {
        val PREVIEW = MonsterState(
            10,
            10,
            KirbyMonster1
        )
    }
}
