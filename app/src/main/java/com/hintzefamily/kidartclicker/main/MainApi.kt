package com.hintzefamily.kidartclicker.main

interface MainApi {
    fun onStageClick() : Boolean

    companion object {
        val PREVIEW = object : MainApi{
            override fun onStageClick() : Boolean = false
        }
    }
}
