package com.dvt.weatherapp.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.dvt.weatherapp.R
import com.dvt.weatherapp.models.WeatherResponse
import com.dvt.weatherapp.util.Utils
import timber.log.Timber

class FavoriteWeatherItemAdapter(context: Context, items: List<WeatherResponse>) :

    RecyclerView.Adapter<FavoriteWeatherItemAdapter.MyViewHolder>() {

    private var mData: List<WeatherResponse> = items
    private val mContext: Context = context
    private val inflater: LayoutInflater = LayoutInflater.from(context)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = inflater.inflate(R.layout.list_saved_weather_item, parent, false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val current = mData[position]
        holder.setData(current, position)
    }

    override fun getItemCount(): Int {
        return mData.size
    }


    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tvDate: TextView = itemView.findViewById(R.id.tvDate)
        private val tvTown: TextView = itemView.findViewById(R.id.tvTown)
        private val tvTemp: TextView = itemView.findViewById(R.id.tvTemp)
        private val layoutMainBackground: LinearLayout = itemView.findViewById(R.id.layoutMainBackground)
        private val tvIcon: ImageView = itemView.findViewById(R.id.tvIcon)
        private val tvWeatherDescription: TextView = itemView.findViewById(R.id.tvWeatherDescription)
        private var pos = 0
        private var current: WeatherResponse? = null

        fun setData(current: WeatherResponse, position: Int) {
            tvDate.text = Utils.getUnderstandableDate(current.dt.toLong())
            tvTown.text = current.name
            tvTemp.text = current.main?.temp?.let { Utils.formatTemperature(it) }
            tvWeatherDescription.text = current.weather!![0].main
            setIcon(current)
            this.pos = position
            this.current = current
        }

        private fun setIcon(weatherItem: WeatherResponse) {
            when (weatherItem.weather?.get(0)?.main) {
                "Clouds" -> {
                    Glide.with(mContext).load(R.drawable.partly_sunny).into(tvIcon)
                    layoutMainBackground.setBackgroundColor(
                        ContextCompat.getColor(
                            mContext,
                            R.color.weather_cloudy
                        )
                    )
                }
                "Rain" -> {
                    Glide.with(mContext).load(R.drawable.rain).into(tvIcon)
                    layoutMainBackground.setBackgroundColor(
                        ContextCompat.getColor(
                            mContext,
                            R.color.weather_rainy
                        )
                    )
                }
                "Clear" -> {
                    Glide.with(mContext).load(R.drawable.clear).into(tvIcon)
                    layoutMainBackground.setBackgroundColor(
                        ContextCompat.getColor(
                            mContext,
                            R.color.weather_sunny
                        )
                    )
                }
                else -> {
                    Glide.with(mContext).load(R.drawable.clear).into(tvIcon)
                    layoutMainBackground.setBackgroundColor(
                        ContextCompat.getColor(
                            mContext,
                            R.color.weather_sunny
                        )
                    )
                }
            }
        }
    }
}