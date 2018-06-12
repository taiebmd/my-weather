package mdtaieb.fr.myweather.Modules

import android.app.Application

/**
 * Created by mohamedtaieb on 12/06/2018.
 */
class WeatherApp : Application() {

    override fun onCreate() {
        super.onCreate()
        instance = this
    }

    companion object {
        lateinit var instance: WeatherApp
    }
}