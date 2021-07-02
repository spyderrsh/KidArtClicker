package com.hintzefamily.kidartclicker.model.upgrades

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

data class Upgrade (
    val text: String,
    val cost: StateFlow<Int>,
    val upgradeAction: () -> Unit
) {
    companion object {
        val PREVIEW = Upgrade(
            "Click Damage (PREVIEW)",
            cost = MutableStateFlow(10),
            upgradeAction = {}
        )
    }
}
