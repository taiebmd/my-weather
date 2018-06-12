package mdtaieb.fr.myweather.Networking

import mdtaieb.fr.myweather.Models.WeatherResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * Created by mohamedtaieb on 12/06/2018.
 */
interface WeatherApi {
    @GET("forecast/daily?q=Paris&units=metric&cnt=5")
    fun getWeatherForFiveDays(@Query("appid") appId: String) : Call<WeatherResponse?>
}