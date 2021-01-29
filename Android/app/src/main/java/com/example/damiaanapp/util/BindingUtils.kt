package com.example.damiaanapp.util

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.example.damiaanapp.R
import com.example.damiaanapp.data.local.Route
import com.example.damiaanapp.util.Status
import java.text.SimpleDateFormat
import java.util.*

//Author: Brent Goubert
@BindingAdapter("routeName")
fun TextView.setRouteName(item : Route)
{
    item.let {
        text = item.name
    }
}

//Author: Brent Goubert
@BindingAdapter("routeDateFormatted")
fun TextView.setRouteDate(item : Route)
{
    item.let {
        text = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(item.start)
    }
}

//Author: Brent Goubert
@BindingAdapter("routeDistanceString")
fun TextView.setRouteDistance(item : Route)
{
    item.let {
        text = item.distance.toString() + "km"
    }
}

//Author: Tom Van der Weeën
@BindingAdapter("myRoutesStatus")
fun bindStatus(statusImageView: ImageView, status: Status?) {
    when(status) {
        Status.LOADING -> {
            statusImageView.visibility = View.VISIBLE
            statusImageView.setImageResource(R.drawable.loading_animation)
        }
        Status.ERROR -> {
            statusImageView.visibility = View.VISIBLE
            statusImageView.setImageResource(R.drawable.ic_connection_error)
        }
        Status.SUCCESS -> {
            statusImageView.visibility = View.GONE
        }
    }
}
