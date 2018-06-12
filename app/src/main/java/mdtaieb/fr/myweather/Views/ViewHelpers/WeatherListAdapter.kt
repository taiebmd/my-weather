package mdtaieb.fr.myweather.Views.ViewHelpers

import android.databinding.DataBindingUtil
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.squareup.picasso.Picasso
import mdtaieb.fr.myweather.BuildConfig
import mdtaieb.fr.myweather.Configs.Constants
import mdtaieb.fr.myweather.Models.WeatherList
import mdtaieb.fr.myweather.R
import mdtaieb.fr.myweather.databinding.WeatherListContentBinding
import java.text.SimpleDateFormat
import java.util.*

/**
 * Created by mohamedtaieb on 12/06/2018.
 */
class WeatherListAdapter (private val weatherReports: MutableList<WeatherList>,
                          public var clickListener: (Int) -> Unit) :
        RecyclerView.Adapter<WeatherListAdapter.CustomViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomViewHolder {
        val binding = DataBindingUtil.inflate<WeatherListContentBinding>(
                LayoutInflater.from(parent.context),
                R.layout.weather_list_content,
                parent,
                false
        )

        return CustomViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CustomViewHolder, position: Int) {
        val weatherReport = weatherReports[position]
        val context = holder.itemView.context

        if (holder.binding != null) {
            (holder as CustomViewHolder).bind(position, clickListener)

            holder.binding!!.dateTxt.text = SimpleDateFormat("EEE, MMM d",
                    Locale.getDefault()).format(Date(weatherReport.dt * 1000L))

            holder.binding!!.weatherSummaryTxt.text = "${weatherReport.temp.day} º C"

            Picasso.Builder(context)
                    .listener({ picasso, uri, exception -> if (BuildConfig.DEBUG) exception.printStackTrace() })
                    .loggingEnabled(BuildConfig.DEBUG)
                    .build()
                    .load("${Constants.IMAGE_URL}${weatherReport.weather.first().icon}.png")
                    .into(holder.binding!!.weatherStatusImg)
        }

    }

    fun updateList(newItems: MutableList<WeatherList>) {
        weatherReports.clear()
        weatherReports.addAll(newItems)
    }

    override fun getItemCount(): Int {
        return weatherReports.size
    }

    class CustomViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var binding: WeatherListContentBinding? = null

        fun bind(position: Int, clickListener: (Int) -> Unit) {
            itemView.setOnClickListener { clickListener(position)}
        }

        constructor(binding: WeatherListContentBinding?) : this(binding!!.root) {
            this.binding = binding
        }
    }
}
