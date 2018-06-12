package mdtaieb.fr.myweather.Services

import android.graphics.Bitmap
import mdtaieb.fr.myweather.Models.WeatherResponse

/**
 * Created by mohamedtaieb on 12/06/2018.
 */
interface WeatherService {
    fun getWeatherIcon(icon: String?): Bitmap?
    fun getWeather() : WeatherResponse?

}