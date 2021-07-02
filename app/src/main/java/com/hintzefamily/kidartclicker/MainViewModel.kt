package com.hintzefamily.kidartclicker

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hintzefamily.kidartclicker.main.MainApi
import com.hintzefamily.kidartclicker.model.heroes.Hero1
import com.hintzefamily.kidartclicker.model.heroes.Hero2
import com.hintzefamily.kidartclicker.model.heroes.NpcHero
import com.hintzefamily.kidartclicker.model.heroes.TheBlake
import com.hintzefamily.kidartclicker.model.state.GameState
import com.hintzefamily.kidartclicker.model.upgrades.Upgrade
import com.hintzefamily.kidartclicker.model.user.HeroState
import com.hintzefamily.kidartclicker.ui.monster.AdamZombie
import com.hintzefamily.kidartclicker.ui.monster.KirbyMonster1
import com.hintzefamily.kidartclicker.ui.monster.KirbyMonster2
import com.hintzefamily.kidartclicker.ui.monster.KirbyMonster3
import com.hintzefamily.kidartclicker.ui.state.MonsterState
import kotlin.math.ceil
import kotlin.math.roundToInt
import kotlin.time.Duration
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

class MainViewModel : ViewModel(), MainApi {
    private val currentPoolStateFlow = MutableStateFlow(
        listOf(
            KirbyMonster1,
            KirbyMonster2,
            KirbyMonster3,
            AdamZombie,
        )
    )

    private val _gameStateFlow = MutableStateFlow(GameState.INITIAL)
    val gameState: StateFlow<GameState> = _gameStateFlow

    private val _heroStateFlow = MutableStateFlow(HeroState.INITIAL)
    val heroState: StateFlow<HeroState> = _heroStateFlow

    private val npcHeroes: List<NpcHero> = listOf(
        Hero1,
        Hero2,
        TheBlake,
        Hero1,
        Hero2,
        TheBlake,
    )

    private val _monsterStateFlow = MutableStateFlow(getMonsterFromCurrentPool())
    val monsterFlow: StateFlow<MonsterState> = _monsterStateFlow

    private val _monsterStateFlowJob = _monsterStateFlow.onEach {
        if (it.currentHealth <= 0) {
            killMonster()
        }
    }.launchIn(viewModelScope)

    private val npcHeroesJobMap: MutableMap<NpcHero, List<Job>> = npcHeroes.associateWithTo(mutableMapOf()) { listOf() }

    private val MIN_DELAY = 30
    private val _npcHeroesFlowJobs = npcHeroes.onEach { hero ->
        hero.obtained.filter { it }
            .flatMapLatest { hero.dps }
            .onEach { dps ->
                npcHeroesJobMap[hero]?.onEach { it.cancel() }
                println("Dps is $dps.")
                val delay = Duration.Companion.milliseconds(maxOf(MIN_DELAY, 1000 / dps))
                val damage = (dps.toDouble() / (1000.0 / delay.inWholeMilliseconds.toDouble())).toInt()
                val damageRemainder = ceil(dps.toDouble() - damage * (1000.0 / delay.inWholeMilliseconds.toDouble())).toInt()
                val jobList = mutableListOf(
                    launchDamageCoroutine(damage, delay)
                )
                if (damageRemainder > 0) {
                    val damageRemainderDelay = Duration.Companion.milliseconds(maxOf(MIN_DELAY, 1000 / damageRemainder))
                    val damageRemainderDamage = damageRemainder / (1000L / damageRemainderDelay.inWholeMilliseconds).toInt()
                    jobList += launchDamageCoroutine(damageRemainderDamage, damageRemainderDelay)
                }
                npcHeroesJobMap[hero] = jobList
            }.launchIn(viewModelScope)
    }

    private fun launchDamageCoroutine(damage: Int, delay: Duration): Job = viewModelScope.launch {
        println("Doing $damage damage every ${delay.inWholeMilliseconds}ms")
        while (true) {
            delay(delay)
            damageMonster(damage)
        }
    }

    private val _clickDamageCost = MutableStateFlow(10)

    private val _upgradesStateFlow = MutableStateFlow(listOf(
        Upgrade(
            text = "Click Damage",
            cost = _clickDamageCost,
            upgradeAction = {
                _heroStateFlow.apply {
                    value = value.run {
                        copy(
                            clickDamage = clickDamage + 1,
                            money = money - _clickDamageCost.value
                        )
                    }
                }
                _clickDamageCost.value += _clickDamageCost.value / 10
            }
        )
    ).plus(npcHeroes.map { getUpgradeFromHero(it) })
    )

    private fun getUpgradeFromHero(hero: NpcHero): Upgrade {
        return Upgrade(
            text = hero.name,
            cost = hero.cost,
            upgradeAction = {
                _heroStateFlow.apply {
                    value = value.copy(
                        money = value.money - hero.cost.value
                    )
                }
                hero.upgrade()
            }
        )
    }

    val upgradesStateFlow: StateFlow<List<Upgrade>> = _upgradesStateFlow
    private fun killMonster() {
        addMoneyFromCurrentMonster()
        drawMonsterDying {
            advanceToNextMonster()
            spawnNewMonster()
        }
    }

    private fun advanceToNextMonster() {
        _gameStateFlow.value = _gameStateFlow.value.run {
            if (currentMonsterIndex + 1 > totalMonsters) {
                copy(currentMonsterIndex = 1, stage = stage + 1)
            } else {
                copy(currentMonsterIndex = currentMonsterIndex + 1)
            }
        }
    }

    private fun spawnNewMonster() {
        _monsterStateFlow.value = getMonsterFromCurrentPool()
    }

    private fun getMonsterFromCurrentPool(): MonsterState {
        val totalHealth = _gameStateFlow.value.stage * 10
        return MonsterState(
            totalHealth,
            totalHealth,
            monster = currentPoolStateFlow.value.random()
        )
    }

    private fun drawMonsterDying(afterDeath: () -> Unit) {
        // TODO Send a kill animation
        afterDeath()
    }

    private fun addMoneyFromCurrentMonster() {
        _heroStateFlow.value = _heroStateFlow.value.run { copy(money = money + getCurrentMonsterMoneyWorth()) }
    }

    private fun getCurrentMonsterMoneyWorth(): Int {
        return _gameStateFlow.value.stage
    }

    override fun onStageClick(): Boolean {
        return damageMonster(_heroStateFlow.value.clickDamage)

    }

    private fun damageMonster(damage: Int): Boolean {
        if (_monsterStateFlow.value.currentHealth <= 0) {
            //Monster is currently dying -- do nothing
            return false
        }
        _monsterStateFlow.value = _monsterStateFlow.value.run { copy(currentHealth = currentHealth - damage) }
        return _monsterStateFlow.value.currentHealth <= 0
    }
}
