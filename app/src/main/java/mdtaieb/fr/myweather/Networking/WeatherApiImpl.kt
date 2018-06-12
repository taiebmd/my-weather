package mdtaieb.fr.myweather.Networking

import mdtaieb.fr.myweather.Models.WeatherResponse
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Converter
import retrofit2.Retrofit
import javax.inject.Inject
import javax.inject.Named

/**
 * Created by mohamedtaieb on 12/06/2018.
 */
class WeatherApiImpl @Inject constructor(converterFactory: Converter.Factory,
                                         @Named("apiUrl") apiUrl: String,
                                         private @Named("apiKey") val apiKey: String,
                                         okHttpClient: OkHttpClient) {
    private var weatherAPI: WeatherApi

    init {
        val retrofit = Retrofit.Builder()
                .baseUrl(apiUrl)
                .addConverterFactory(converterFactory)
                .client(okHttpClient)
                .build()

        weatherAPI = retrofit.create(WeatherApi::class.java)
    }

    fun getWeatherForFiveDays(): Call<WeatherResponse?> {
        return weatherAPI.getWeatherForFiveDays(appId = apiKey)
    }
}