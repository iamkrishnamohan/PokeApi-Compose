package com.krrish.pokeapicompose

import android.content.Context
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.krrish.pokeapicompose.data.remote.PokeAPI
import kotlinx.coroutines.runBlocking
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.hamcrest.CoreMatchers
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.InputStream
import java.net.HttpURLConnection
import java.util.concurrent.TimeUnit

@RunWith(AndroidJUnit4::class)
class NetworkCallTest {
    private var context: Context? = null
    private var mockWebServer = MockWebServer()
    private lateinit var apiService: PokeAPI

    @Before
    fun setup() {
        mockWebServer.start()

        val loggingInterceptor =
            HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
        context = InstrumentationRegistry.getInstrumentation().targetContext


        val client = OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .build()

        apiService = Retrofit.Builder()
            .baseUrl(mockWebServer.url("/"))
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
            .create(PokeAPI::class.java)

        val jsonStream: InputStream = context!!.resources.assets.open("response.json")
        val jsonBytes: ByteArray = jsonStream.readBytes()


        val response = MockResponse()
            .setResponseCode(HttpURLConnection.HTTP_OK)
            .setBody(String(jsonBytes))
        mockWebServer.enqueue(response)
    }

    @After
    fun teardown() {
        mockWebServer.shutdown()
    }

    @Test
    fun test_response_name() = runBlocking {
        val data = apiService.getPokemonList(1, 0)
        ViewMatchers.assertThat(data.results[0].name, CoreMatchers.equalTo("bulbasaur"))
    }

    @Test
    fun test_id_extraction() = runBlocking {
        val data = apiService.getPokemonList(1, 0)
        val id = data.results[0].url.substringAfter("pokemon").replace("/", "").toInt()
        ViewMatchers.assertThat(id, CoreMatchers.equalTo(1))
    }
}
