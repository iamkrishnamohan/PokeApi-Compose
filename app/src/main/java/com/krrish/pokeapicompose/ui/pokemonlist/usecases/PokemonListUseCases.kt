package com.krrish.pokeapicompose.ui.pokemonlist.usecases

import com.krrish.pokeapicompose.data.models.PokedexListEntry
import com.krrish.pokeapicompose.data.remote.response.PokemonList
import com.krrish.pokeapicompose.data.remote.response.Result
import com.krrish.pokeapicompose.util.getImageUrl

fun searchPokedexListEntries(
    listToSearch: List<PokedexListEntry>,
    query: String
): List<PokedexListEntry> {
    val results = listToSearch.filter {
        // Search by name or pokédex number
        it.pokemonName.contains(query.trim(), true) ||
                it.number.toString() == query.trim()
    }
    return results
}

fun pokedexListEntries(data: PokemonList): List<PokedexListEntry> {
    val pokedexEntries = data.results.mapIndexed { _, entry ->
        val number = getPokedexNumber(entry)
        val url = number.getImageUrl()
        PokedexListEntry(
            entry.name.replaceFirstChar(Char::titlecase),
            url,
            number.toInt()
        )
    }
    return pokedexEntries
}

fun getPokedexNumber(entry: Result): String {
    return if (entry.url.endsWith("/")) {
        entry.url.dropLast(1).takeLastWhile { it.isDigit() }
    } else {
        entry.url.takeLastWhile { it.isDigit() }
    }
}


