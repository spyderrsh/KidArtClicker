package com.hintzefamily.kidartclicker.ui.monster

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.hintzefamily.kidartclicker.R

interface UiMonster {
    val name: String
    val height: Dp
    val width: Dp
    @Composable
    fun DrawMonster()
}

abstract class DrawableMonster : UiMonster {
    abstract val resId: Int

    @Preview
    @Composable
    override fun DrawMonster() {
        Box(
            modifier = Modifier
                .height(height)
                .width(width)
        ) {
            Image(
                painter = painterResource(id = resId),
                contentDescription = name,
                contentScale = ContentScale.Fit,
                modifier = Modifier.align(Alignment.Center)
            )
        }
    }
}

abstract class UiTextMonster : UiMonster {
    protected val size: Dp = 120.dp
    override val height: Dp = size
    override val width: Dp = size
    abstract val text: String
    @Preview
    @Composable
    override fun DrawMonster() {
        Box(
            modifier = Modifier
                .size(size)
        ) {
            Text(
                text = text,
                modifier = Modifier.align(Alignment.Center),
                fontSize = 38.sp,
                maxLines = 1,
                color = Color.White,
                textAlign = TextAlign.Center
            )
        }
    }
}

object KirbyMonster1 : UiTextMonster() {
    override val name: String = "Kirby"
    override val text: String = "<(\")>"
}

object KirbyMonster2 : UiTextMonster() {
    override val name: String = "Kirby Left"
    override val text: String = "<(\"<)"
}

object KirbyMonster3 : UiTextMonster() {
    override val name: String = "Kirby Right"
    override val text: String = "(>\")>"
}

object AdamZombie : DrawableMonster() {
    override val name: String = "Zombie"
    override val resId: Int = R.drawable.adam_monster_zombie_0001
    override val height: Dp = 200.dp
    override val width: Dp = 200.dp
}