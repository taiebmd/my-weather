package mdtaieb.fr.myweather.Views.ViewModels

import android.databinding.ObservableField
import android.graphics.Bitmap
import io.reactivex.subjects.PublishSubject
import mdtaieb.fr.myweather.Models.WeatherResponse
import mdtaieb.fr.myweather.Services.WeatherService
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

/**
 * Created by mohamedtaieb on 12/06/2018.
 */
class WeatherViewModel @Inject constructor(private val weatherService: WeatherService) {
    val weatherSummary: ObservableField<String> = ObservableField()
    val temperature: ObservableField<String> = ObservableField()
    val weatherConditionIcon: PublishSubject<String> = PublishSubject.create()
    var windAndPressure:  ObservableField<String> = ObservableField()
    val city: ObservableField<String> = ObservableField()
    val date: ObservableField<String> = ObservableField()

    fun getWeather(): WeatherResponse? {
        return weatherService.getWeather()
    }

    fun updateWeather(weatherResponse: WeatherResponse?, position: Int = 0) {
        val firstWeatherResult = weatherResponse!!.list[position];

        temperature.set("Min: ${firstWeatherResult.temp.min} ºC / max: ${firstWeatherResult.temp.max} ºC")
        city.set("${weatherResponse.city.name} | ${weatherResponse.city.country}")
        weatherSummary.set("${firstWeatherResult.weather.first().description} ")
        windAndPressure.set("${firstWeatherResult.speed} / P: ${firstWeatherResult.pressure}")
        weatherConditionIcon.onNext(firstWeatherResult.weather.first().icon)
        date.set(SimpleDateFormat("EEE, MMM d, yyyy",
                Locale.getDefault()).format(Date(firstWeatherResult.dt * 1000L)))
    }

    fun getWeatherIcon(icon: String?): Bitmap? {
        return weatherService.getWeatherIcon(icon)
    }
}