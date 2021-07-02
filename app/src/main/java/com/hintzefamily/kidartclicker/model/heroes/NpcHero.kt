package com.hintzefamily.kidartclicker.model.heroes

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

interface NpcHero {
    val name: String
    val cost: StateFlow<Int>
    val dps: StateFlow<Int>
    val obtained: StateFlow<Boolean>
    val level: StateFlow<Int>
    fun upgrade()
}

abstract class BasicHero(baseCost: Int, baseDps: Int) : NpcHero {
    private val _cost = MutableStateFlow(baseCost)
    override val cost: StateFlow<Int> = _cost
    private val _dps = MutableStateFlow(baseDps)
    override val dps: StateFlow<Int> = _dps
    private val _obtained = MutableStateFlow(false)
    override val obtained: StateFlow<Boolean> = _obtained
    private val _level = MutableStateFlow(1)
    override val level: StateFlow<Int> = _level

    override fun upgrade() {
        _cost.value += _cost.value/10
        if(_obtained.compareAndSet(expect = false, update = true)){
            println("Obtained ${Hero1.name}")
            return
        }
        _level.value += 1
        _dps.value += 1 + _dps.value/10
    }
}

object Hero1 : BasicHero(30, 1) {
    override val name: String = "Adam Anderson"
}

object Hero2 : BasicHero(300, 100) {
    override val name: String = "Norah Anderson"
}
object TheBlake : BasicHero(0, 1) {
    override val name: String = "Blake Hintze"
}


