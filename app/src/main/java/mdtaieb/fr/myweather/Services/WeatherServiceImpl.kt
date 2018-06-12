package mdtaieb.fr.myweather.Services

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.support.annotation.WorkerThread
import android.util.Log
import mdtaieb.fr.myweather.Configs.Constants
import mdtaieb.fr.myweather.Models.WeatherResponse
import mdtaieb.fr.myweather.Networking.WeatherApiImpl
import java.net.URL
import javax.inject.Inject

/**
 * Created by mohamedtaieb on 12/06/2018.
 */
class WeatherServiceImpl @Inject constructor(val weatherApi: WeatherApiImpl) : WeatherService {
    private val TAG = "WeatherServiceImpl"

    override fun getWeatherIcon(icon: String?): Bitmap? {
        var bitmap: Bitmap? = null

        try {
            val iconUrl = URL(Constants.IMAGE_URL + icon + ".png")
            bitmap = BitmapFactory.decodeStream(iconUrl.openConnection().getInputStream())

        } catch (ex: Exception) {
            Log.e(TAG, "Exception occurred", ex)
        }

        return bitmap
    }

    @WorkerThread
    override fun getWeather(): WeatherResponse? {
        var forecastForFiveDays: WeatherResponse? = null

        try {
            forecastForFiveDays = weatherApi.getWeatherForFiveDays().execute()?.body()
        } catch (ex: Exception) {
            Log.e(TAG, "Exception occurred", ex)
        }

        return forecastForFiveDays
    }
}