package com.krrish.pokeapicompose.util

import androidx.compose.ui.graphics.Color
import com.krrish.pokeapicompose.data.remote.response.Stat
import com.krrish.pokeapicompose.data.remote.response.Type
import com.krrish.pokeapicompose.ui.theme.AtkColor
import com.krrish.pokeapicompose.ui.theme.DefColor
import com.krrish.pokeapicompose.ui.theme.HPColor
import com.krrish.pokeapicompose.ui.theme.SpAtkColor
import com.krrish.pokeapicompose.ui.theme.SpDefColor
import com.krrish.pokeapicompose.ui.theme.SpdColor
import com.krrish.pokeapicompose.ui.theme.TypeBug
import com.krrish.pokeapicompose.ui.theme.TypeDark
import com.krrish.pokeapicompose.ui.theme.TypeDragon
import com.krrish.pokeapicompose.ui.theme.TypeElectric
import com.krrish.pokeapicompose.ui.theme.TypeFairy
import com.krrish.pokeapicompose.ui.theme.TypeFighting
import com.krrish.pokeapicompose.ui.theme.TypeFire
import com.krrish.pokeapicompose.ui.theme.TypeFlying
import com.krrish.pokeapicompose.ui.theme.TypeGhost
import com.krrish.pokeapicompose.ui.theme.TypeGrass
import com.krrish.pokeapicompose.ui.theme.TypeGround
import com.krrish.pokeapicompose.ui.theme.TypeIce
import com.krrish.pokeapicompose.ui.theme.TypeNormal
import com.krrish.pokeapicompose.ui.theme.TypePoison
import com.krrish.pokeapicompose.ui.theme.TypePsychic
import com.krrish.pokeapicompose.ui.theme.TypeRock
import com.krrish.pokeapicompose.ui.theme.TypeSteel
import com.krrish.pokeapicompose.ui.theme.TypeWater
import java.util.*

fun parseTypeToColor(type: Type): Color {
    return when (type.type.name.lowercase(Locale.ROOT)) {
        "normal" -> TypeNormal
        "fire" -> TypeFire
        "water" -> TypeWater
        "electric" -> TypeElectric
        "grass" -> TypeGrass
        "ice" -> TypeIce
        "fighting" -> TypeFighting
        "poison" -> TypePoison
        "ground" -> TypeGround
        "flying" -> TypeFlying
        "psychic" -> TypePsychic
        "bug" -> TypeBug
        "rock" -> TypeRock
        "ghost" -> TypeGhost
        "dragon" -> TypeDragon
        "dark" -> TypeDark
        "steel" -> TypeSteel
        "fairy" -> TypeFairy
        else -> Color.Black
    }
}

fun parseStatToColor(stat: Stat): Color {
    return when (stat.stat.name.lowercase(Locale.ROOT)) {
        "hp" -> HPColor
        "attack" -> AtkColor
        "defense" -> DefColor
        "special-attack" -> SpAtkColor
        "special-defense" -> SpDefColor
        "speed" -> SpdColor
        else -> Color.White
    }
}

fun parseStatToAbbr(stat: Stat): String {
    return when (stat.stat.name.lowercase(Locale.ROOT)) {
        "hp" -> "Hp"
        "attack" -> "Atk"
        "defense" -> "Def"
        "special-attack" -> "Sp.Atk"
        "special-defense" -> "Sp.Def"
        "speed" -> "Spd"
        else -> ""
    }
}
