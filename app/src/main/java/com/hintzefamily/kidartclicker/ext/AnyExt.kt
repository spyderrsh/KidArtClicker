package com.hintzefamily.kidartclicker.ext

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf

val <T : Any> T.previewState: MutableState<T> get() = mutableStateOf(this)