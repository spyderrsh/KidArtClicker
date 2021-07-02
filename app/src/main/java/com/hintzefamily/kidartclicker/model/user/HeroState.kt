package com.hintzefamily.kidartclicker.model.user

import com.hintzefamily.kidartclicker.BuildConfig

data class HeroState(
    val clickDamage: Int,
    val money: Int
) {
    companion object {
        val INITIAL = HeroState(
            clickDamage = 1,
            money = if(BuildConfig.DEBUG) 10000 else 0
        )
        val PREVIEW =  INITIAL

    }
}
