package com.dvt.weatherapp.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.dvt.weatherapp.R
import com.dvt.weatherapp.models.WeatherItem
import com.dvt.weatherapp.util.Utils

class WeatherItemAdapter(context: Context, items: List<WeatherItem>) :

    RecyclerView.Adapter<WeatherItemAdapter.MyViewHolder>() {

    private var mData: List<WeatherItem> = items
    private val inflater: LayoutInflater = LayoutInflater.from(context)
    private val mContext: Context = context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = inflater.inflate(R.layout.list_weather_item, parent, false)
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

        private val tvDay: TextView = itemView.findViewById(R.id.tvDay)
        private val tvTemp: TextView = itemView.findViewById(R.id.tvTemp)
        private val tvIcon: ImageView = itemView.findViewById(R.id.tvIcon)
        private var current: WeatherItem? = null
        private var pos = 0


        fun setData(current: WeatherItem, position: Int) {
            tvDay.text = Utils.getDayOfTheWeek(current.dt)
            tvTemp.text = current.main?.temp?.let { Utils.formatTemperature(it) }
            setIcon(current)
            this.pos = position
            this.current = current
        }

        private fun setIcon(weatherItem: WeatherItem) {

            when (weatherItem.weather?.get(0)?.main) {
                "Clouds" -> {
                    Glide.with(mContext).load(R.drawable.partly_sunny).into(tvIcon)
                }
                "Rain" -> {
                    Glide.with(mContext).load(R.drawable.rain).into(tvIcon)
                }
                "Clear" -> {
                    Glide.with(mContext).load(R.drawable.clear).into(tvIcon)
                }
                else -> {

                }
            }
        }
    }
}