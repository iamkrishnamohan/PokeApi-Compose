package com.krrish.pokeapicompose.ui.detail.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.krrish.pokeapicompose.data.remote.response.Pokemon
import com.krrish.pokeapicompose.util.replaceFirstCharacter

@Composable
fun PokemonDetailSection(
    pokemonDetail: Pokemon,
    modifier: Modifier = Modifier
) {

    val scrollState = rememberScrollState()

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .fillMaxSize()
            .offset(y = 100.dp)
            .verticalScroll(scrollState)
    ) {
        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "#${pokemonDetail.id} ${
                pokemonDetail.name.replaceFirstChar {
                    it.replaceFirstCharacter()
                }
            }",
            fontWeight = FontWeight.Bold,
            fontSize = 30.sp,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.onSurface
        )

        PokemonTypeSection(types = pokemonDetail.types)

        Spacer(modifier = Modifier.height(16.dp))

        PokemonDetailDataSection(
            pokemonWeight = pokemonDetail.weight,
            pokemonHeight = pokemonDetail.height
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Base Stats",
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        PokemonBaseStats(pokemonDetail = pokemonDetail)

        // Set space equal to the column offset, it handles screen orientation
        Spacer(modifier = Modifier.height(100.dp))
    }
}
