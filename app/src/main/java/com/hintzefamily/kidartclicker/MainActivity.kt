package com.hintzefamily.kidartclicker

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.Animatable
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.hintzefamily.kidartclicker.ext.previewState
import com.hintzefamily.kidartclicker.main.MainApi
import com.hintzefamily.kidartclicker.model.state.GameState
import com.hintzefamily.kidartclicker.model.upgrades.Upgrade
import com.hintzefamily.kidartclicker.model.user.HeroState
import com.hintzefamily.kidartclicker.ui.state.MonsterState
import com.hintzefamily.kidartclicker.ui.theme.KidArtClickerTheme
import com.hintzefamily.kidartclicker.ui.theme.Shapes

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            KidArtClickerTheme {
                // A surface container using the 'background' color from the theme
                Surface(color = MaterialTheme.colors.background) {
                    AppContent()
                }
            }
        }
    }
}

@Composable
fun AppContent(model: MainViewModel = viewModel()) {
    val monsterState = model.monsterFlow.collectAsState()
    val heroState = model.heroState.collectAsState()
    val gameState = model.gameState.collectAsState()
    val upgrades = model.upgradesStateFlow.collectAsState()
    AppScreen(
        monsterState,
        heroState,
        gameState,
        upgrades,
        model,
    )
}

@Composable
fun AppScreen(
    monsterState: State<MonsterState> = MonsterState.PREVIEW.previewState,
    heroState: State<HeroState> = HeroState.PREVIEW.previewState,
    gameState: State<GameState> = GameState.PREVIEW.previewState,
    upgrades: State<List<Upgrade>> = listOf(Upgrade.PREVIEW).previewState,
    mainApi: MainApi = MainApi.PREVIEW
) {
    Box(modifier = Modifier.fillMaxSize(1f)) {
        Box(Modifier.matchParentSize()) {
            Stage(monsterState) {
                mainApi.onStageClick()
            }
            Hud(
                heroState,
                gameState,
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .fillMaxWidth(1f)
                    .fillMaxHeight(0.5f)
            )

            BottomSheet(
                upgrades,
                heroState,
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth(1f)
                    .fillMaxHeight(0.5f)
            )
        }

    }
}

@Composable
fun Hud(
    heroState: State<HeroState> = HeroState.PREVIEW.previewState,
    gameState: State<GameState> = GameState.PREVIEW.previewState,
    modifier: Modifier = Modifier
) {
    Box(modifier = modifier) {
        Text(
            text = "Money: ${heroState.value.money}",
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(16.dp)
        )
        Column(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(16.dp)
        ) {
            Text(text = "Level", modifier = Modifier.align(Alignment.CenterHorizontally))
            Text(text = "${gameState.value.stage}", modifier = Modifier.align(Alignment.CenterHorizontally))
            Text(text = "${gameState.value.currentMonsterIndex}/${gameState.value.totalMonsters}")
        }
        Text(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(16.dp),
            text = "Click Damage: ${heroState.value.clickDamage}"
        )
    }
}

@Preview
@Composable
fun Stage(
    monster: State<MonsterState> = MonsterState.PREVIEW.previewState,
    onClick: () -> Boolean = { false }
) {
    val bounceOffset = rememberInfiniteTransition().animateFloat(
        initialValue = 0f,
        targetValue = 10f,
        animationSpec = infiniteRepeatable(
            animation = tween(300, easing = LinearOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        )
    )
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Cyan)
            .clickable {
                if (onClick()) {
                    //reset bounce here if monster dies
                }
            }
    ) {
        val offset = remember {
            mutableStateOf(
                0f
            )
        }
        Box(modifier = Modifier
            .fillMaxHeight(.3f)
            .fillMaxWidth(.5f)
            .align(Alignment.Center)
            .onSizeChanged {
                offset.value = (-it.height.toFloat() / 2f)
                println("offset = ${offset.value}")
            }
            .offset(y = with(LocalDensity.current) { offset.value.toDp() })
            .background(Color.Green)
        ) {
            Monster(
                monster,
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .offset(
                        y = (-16).dp
                            .plus(bounceOffset.value.dp)
                    )
            )
            Text(
                modifier = Modifier.align(Alignment.TopCenter),
                text = monster.value.run { "$currentHealth/$totalHealth" })
        }
    }
}

@Preview
@Composable
fun Monster(monster: State<MonsterState> = MonsterState.PREVIEW.previewState, modifier: Modifier = Modifier.size(120.dp)) {
    Box(
        modifier = modifier
            .height(monster.value.monster.height)
            .width(monster.value.monster.width)
    ) {
        monster.value.monster.DrawMonster()
    }
}

@Composable
fun BottomSheet(
    upgrades: State<List<Upgrade>>,
    heroState: State<HeroState>,
    modifier: Modifier = Modifier
) {
    Surface(
        color = Color.White,
        modifier = modifier
    ) {
        Box(modifier = modifier) {
            LazyColumn(modifier = Modifier.fillMaxSize(1f)) {
                item {
                        Text(
                            modifier = Modifier
                                .align(Alignment.Center)
                                .fillMaxWidth(1f),
                            text = "Javier rocks!!!",
                            color = Color.Blue,
                            textAlign = TextAlign.Center
                        )

                }
                items(upgrades.value) { upgrade ->
                    UpgradeRow(upgrade, heroState)
                }
            }
            BottomNav(modifier = Modifier.align(Alignment.BottomCenter))
        }

    }

}

@Preview(showBackground = true)
@Composable
fun UpgradeRow(
    upgrade: Upgrade = Upgrade.PREVIEW,
    heroState: State<HeroState> = HeroState.PREVIEW.previewState
) {
    val cost = upgrade.cost.collectAsState()
    val canAfford = heroState.value.money >= cost.value

    val color = remember { Animatable(Color.Gray) }

    LaunchedEffect(key1 = canAfford) {
        color.animateTo(if (canAfford) Color.Blue else Color.Gray)
    }

    Surface(
        modifier = Modifier
            .fillMaxWidth(1f)
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .clickable(enabled = canAfford) { upgrade.upgradeAction() },
        color = color.value,
        shape = Shapes.medium,
        onClick = {
            if (canAfford) {
                upgrade.upgradeAction()
            }
        }
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 16.dp)
        ) {
            Text(
                text = upgrade.text,
                modifier = Modifier
                    .align(Alignment.CenterStart)
                    .padding(horizontal = 16.dp),
            )
            Text(
                text = cost.value.toString(),
                modifier = Modifier
                    .align(Alignment.CenterEnd)
                    .padding(horizontal = 16.dp)
            )
        }
    }
}

@Composable
fun BottomNav(modifier: Modifier = Modifier) {
    Text(text = "BottomNav", modifier = modifier.fillMaxWidth(1f))

}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    KidArtClickerTheme {
        AppScreen()
    }
}