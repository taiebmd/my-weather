package mdtaieb.fr.myweather.Views

import android.databinding.DataBindingUtil
import android.graphics.Bitmap
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.widget.Toast
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import mdtaieb.fr.myweather.Models.WeatherList
import mdtaieb.fr.myweather.Models.WeatherResponse
import mdtaieb.fr.myweather.Modules.DaggerWeatherComponent
import mdtaieb.fr.myweather.Modules.WeatherComponent
import mdtaieb.fr.myweather.R
import mdtaieb.fr.myweather.Views.ViewHelpers.RefreshWeather
import mdtaieb.fr.myweather.Views.ViewHelpers.WeatherListAdapter
import mdtaieb.fr.myweather.Views.ViewModels.WeatherViewModel
import mdtaieb.fr.myweather.databinding.WeatherListActivityBinding
import javax.inject.Inject

class WeatherListActivity : AppCompatActivity(), RefreshWeather {
    private val TAG = "WeatherListActivity"
    private val compositeDisposable = CompositeDisposable()

    @Inject
    @JvmField
    var weatherRow: WeatherViewModel? = null

    private var response: WeatherResponse? = null

    // Whether or not the activity is in two-pane mode, i.e. running on a tablet device.
    private var twoPane: Boolean = false

    private lateinit var binding: WeatherListActivityBinding
    private lateinit var daggerWeatherComponent: WeatherComponent

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        this.binding = DataBindingUtil.setContentView<WeatherListActivityBinding>(this, R.layout.weather_list_activity)

        daggerWeatherComponent = DaggerWeatherComponent.create()
        daggerWeatherComponent.inject(this)

        binding.weatherRow = weatherRow
        binding.weatherList = this

        binding.weatherRecyclerView.layoutManager = LinearLayoutManager(this,  LinearLayoutManager.HORIZONTAL, false)
        binding.weatherRecyclerView.adapter = WeatherListAdapter(mutableListOf(), { position : Int -> weatherItemClicked(position) })
    }

    override fun onStart() {
        super.onStart()
        loadWeather()
    }

    fun loadWeather() {
        binding.rootView.isRefreshing = true

        val weatherForecastSubscription = Single.fromCallable { weatherRow?.getWeather() }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doFinally { binding.rootView.isRefreshing = false }
                .subscribe { weatherResponse, exception -> processWeatherResponse(weatherResponse, exception) }

        compositeDisposable.add(weatherForecastSubscription)

        val weatherConditionIconSubscription = weatherRow?.weatherConditionIcon
                ?.subscribe { updateWeatherConditionIconDrawable(it) }

        compositeDisposable.addAll(weatherConditionIconSubscription)
    }

    private fun processWeatherResponse(weatherResponse: WeatherResponse?, ex: Throwable?) {
        if (weatherResponse != null) {
            response = weatherResponse;
            weatherRow!!.updateWeather(weatherResponse)
            val weatherForecastListAdapter = binding.weatherRecyclerView.adapter as WeatherListAdapter
            weatherForecastListAdapter.updateList(weatherResponse.list.toMutableList())
            weatherForecastListAdapter.clickListener = { position : Int -> weatherItemClicked(position) }
            weatherForecastListAdapter.notifyDataSetChanged()
        } else {
            Toast.makeText(this, getText(R.string.loading_weather_error), Toast.LENGTH_SHORT).show()
        }
    }

    private fun updateWeatherConditionIconDrawable(icon: String?) {
        Single.fromCallable { weatherRow?.getWeatherIcon(icon) }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { bitmap, exception -> processWeatherConditionIconDrawableResponse(bitmap, exception) }
    }

    private fun processWeatherConditionIconDrawableResponse(bitmap: Bitmap?, ex: Throwable?) {
        if (bitmap != null) {
            binding.weatherStatusImg.setImageBitmap(bitmap)
        } else {
            Log.e(TAG, "Exception occurred", ex)
        }
    }

    private fun weatherItemClicked(position : Int) {
        Toast.makeText(this, "GoTo: ${position}", Toast.LENGTH_LONG).show()
        weatherRow!!.updateWeather(response, position)
    }

    override fun reloadWeather() {
        loadWeather()
    }
}
