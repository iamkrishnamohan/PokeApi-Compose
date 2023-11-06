package com.krrish.pokeapicompose.util

import com.krrish.pokeapicompose.util.Constants.WEIGHT_100
import com.krrish.pokeapicompose.util.Constants.WEIGHT_1000
import java.util.Locale

fun Int.toKg() = this * WEIGHT_100 / WEIGHT_1000

fun Int.toMeters() = this * WEIGHT_100 / WEIGHT_1000

fun String.extractId() = this.substringAfter("pokemon").replace("/", "").toInt()

fun String.getImageUrl(): String {
    val id = this.extractId()
    return "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/${id}.png"
}

fun Char.replaceFirstCharacter() =
    if (this.isLowerCase()) this.titlecase(Locale.ROOT) else this.toString()

