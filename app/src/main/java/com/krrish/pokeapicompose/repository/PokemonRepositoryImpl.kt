package com.krrish.pokeapicompose.repository

import com.krrish.pokeapicompose.data.remote.PokeAPI
import com.krrish.pokeapicompose.data.remote.response.Pokemon
import com.krrish.pokeapicompose.data.remote.response.PokemonList
import com.krrish.pokeapicompose.util.Resource
import javax.inject.Inject

class PokemonRepositoryImpl @Inject constructor(
    private val api: PokeAPI
) : PokemonRepository {

    override suspend fun getPokemonList(limit: Int, offset: Int): Resource<PokemonList> {
        val response = api.getPokemonList(limit, offset)
        return Resource.Success(response)
    }

    override suspend fun getPokemonDetail(pokemonName: String): Resource<Pokemon> {
        val response = api.getPokemonDetail(pokemonName)
        return Resource.Success(response)
    }
}


