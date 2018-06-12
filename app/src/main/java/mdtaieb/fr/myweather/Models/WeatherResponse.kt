package mdtaieb.fr.myweather.Models

import com.google.gson.annotations.SerializedName
import java.io.Serializable

/**
 * Created by mohamedtaieb on 12/06/2018.
 */
data class WeatherResponse(
        @SerializedName("list") val list: Array<WeatherList>,
        @SerializedName("city") val city: City
) : Serializable

data class WeatherList(
        @SerializedName("temp") val temp: Temp,
        @SerializedName("weather") val weather: Array<Weather>,
        @SerializedName("clouds") val clouds: Double,
        @SerializedName("dt") val dt: Long,
        @SerializedName("pressure") val pressure: Double,
        @SerializedName("speed") val speed: Double
) : Serializable

data class Weather(
        @SerializedName("main") val main: String,
        @SerializedName("description") val description: String,
        @SerializedName("icon") val icon: String
) : Serializable

data class Temp(
        @SerializedName("day") val day: Double,
        @SerializedName("min") val min: Double,
        @SerializedName("max") val max: Double
) : Serializable


data class City(
        @SerializedName("name") val name: String,
        @SerializedName("country") val country: String
) : Serializable