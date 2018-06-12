package mdtaieb.fr.myweather.Modules

import dagger.Component
import mdtaieb.fr.myweather.Views.WeatherListActivity

/**
 * Created by mohamedtaieb on 12/06/2018.
 */
@Component(modules = arrayOf(WeatherModule::class))
interface WeatherComponent {
    fun inject(weatherListActivity: WeatherListActivity)
}