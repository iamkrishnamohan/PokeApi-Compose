package com.krrish.pokeapicompose.ui.detail.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.krrish.pokeapicompose.data.remote.response.Pokemon
import com.krrish.pokeapicompose.data.remote.response.Sprites
import com.krrish.pokeapicompose.repository.PokemonRepositoryImpl
import com.krrish.pokeapicompose.util.Resource
import kotlinx.coroutines.test.*
import org.junit.*
import org.junit.runner.RunWith
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class PokemonDetailViewModelTest {
    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var repository: PokemonRepositoryImpl
    private lateinit var viewModel: PokemonDetailViewModel

    @Before
    fun setUp() {
        repository = Mockito.mock(PokemonRepositoryImpl::class.java)
        viewModel = PokemonDetailViewModel(repository)
    }

    @Test
    fun getPokemonDetails() = runTest {
        val name = "Bulbasaur"

        Mockito.`when`(viewModel.getPokemonDetail(name)).thenReturn(getMockResponse())
        val result = viewModel.getPokemonDetail(name)

        Assert.assertEquals(getMockResponse().data, result.data)
    }

    private fun getMockResponse(): Resource<Pokemon> {
        return Resource.Success(getBulbasaur())
    }

    private fun getBulbasaur(): Pokemon {
        return Pokemon(7, 1, "Bulbasaur", Sprites(""), emptyList(), emptyList(), 69)
    }
}
