package com.dvt.weatherapp.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.dvt.weatherapp.R
import com.dvt.weatherapp.models.WeatherResponse
import com.dvt.weatherapp.util.Utils


class FavoriteWeatherItemAdapter(private val context: Context, items: List<WeatherResponse>) :

    RecyclerView.Adapter<FavoriteWeatherItemAdapter.MyViewHolder>() {

    private var mData: List<WeatherResponse>
    private val inflater: LayoutInflater = LayoutInflater.from(context)
    private val mContext: Context

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
        private val tvDate: TextView
        private val tvTown: TextView
        private val tvTemp: TextView
        private val layoutMainBackground: LinearLayout
        private var pos = 0
        private val tvIcon: ImageView
        private val tvWeatherDescription: TextView

        private var current: WeatherResponse? = null

        fun setData(current: WeatherResponse, position: Int) {
            tvDate.text = Utils.getUnderstandableDate(current.dt.toLong())
            tvTown.text = current.name
            tvTemp.text = "%.1f".format(current.main?.temp) + "\u00B0"
            setIcon(current)
            tvWeatherDescription.text = current.weather!![0].main
            this.pos = position
            this.current = current
        }

        private fun setIcon(weatherItem: WeatherResponse) {
            when (weatherItem.weather?.get(0)?.main) {
                "Clouds" -> {
                    Glide.with(mContext).load(R.drawable.partly_sunny).into(tvIcon)

                    layoutMainBackground.setBackgroundColor(
                        ContextCompat.getColor(
                            context,
                            R.color.weather_cloudy
                        )
                    )
                }
                "Rain" -> {
                    Glide.with(mContext).load(R.drawable.rain).into(tvIcon)
                    layoutMainBackground.setBackgroundColor(
                        ContextCompat.getColor(
                            context!!,
                            R.color.weather_rainy
                        )
                    )
                }
                "Clear" -> {
                    Glide.with(mContext).load(R.drawable.clear).into(tvIcon)
                    layoutMainBackground.setBackgroundColor(
                        ContextCompat.getColor(
                            context!!,
                            R.color.weather_sunny
                        )
                    )
                }
                else -> {
                    Glide.with(mContext).load(R.drawable.clear).into(tvIcon)
                    layoutMainBackground.setBackgroundColor(
                        ContextCompat.getColor(
                            context!!,
                            R.color.weather_sunny
                        )
                    )
                }
            }
        }


        init {
            tvDate = itemView.findViewById(R.id.tvDate)
            tvTown = itemView.findViewById(R.id.tvTown)
            tvIcon = itemView.findViewById(R.id.tvIcon)
            tvWeatherDescription = itemView.findViewById(R.id.tvWeatherDescription)
            tvTemp = itemView.findViewById(R.id.tvTemp)
            layoutMainBackground = itemView.findViewById(R.id.layoutMainBackground)
        }
    }


    companion object {
        private val TAG = FavoriteWeatherItemAdapter::class.java.simpleName
    }

    init {
        mData = items
        mContext = context
    }
}