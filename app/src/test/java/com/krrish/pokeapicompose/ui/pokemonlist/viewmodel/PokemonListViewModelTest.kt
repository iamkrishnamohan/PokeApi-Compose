package com.krrish.pokeapicompose.ui.pokemonlist.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.krrish.pokeapicompose.data.remote.response.Pokemon
import com.krrish.pokeapicompose.data.remote.response.Result
import com.krrish.pokeapicompose.data.remote.response.Sprites
import com.krrish.pokeapicompose.repository.PokemonRepositoryImpl
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito.mock
import org.mockito.junit.MockitoJUnitRunner

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(MockitoJUnitRunner::class)
class PokemonListViewModelTest {

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var repository: PokemonRepositoryImpl
    private lateinit var viewModel: PokemonListViewModel

    @Before
    fun setUp() {
        Dispatchers.setMain(Dispatchers.Unconfined)
        repository = mock(PokemonRepositoryImpl::class.java)
        viewModel = PokemonListViewModel(repository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun getImageUrl() = runTest {
        val pkm = getBulbasaur()
        val url = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/1.png"

        Assert.assertEquals(url, viewModel.getImageUrl(pkm.id.toString()))
    }

    @Test
    fun getPokedexNumber() = runTest {
        val result = Result(
            "bulbasaur",
            "https://pokeapi.co/api/v2/pokemon/1/"
        )

        Assert.assertEquals("1", viewModel.getPokedexNumber(result))
    }

    private fun getBulbasaur(): Pokemon {
        return Pokemon(7, 1, "Bulbasaur", Sprites(""), emptyList(), emptyList(), 69)
    }
}
