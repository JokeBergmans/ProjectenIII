package com.example.damiaanapp.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.damiaanapp.data.local.Route
import com.example.damiaanapp.databinding.RecyclerItemRouteBinding
//Author: Brent Goubert
class RoutesAdapter(val clickListener: RouteListener) :
    ListAdapter<Route, RoutesAdapter.MyViewHolder>(RouteDiffCallback()) {

    //Author: Brent Goubert
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder.from(parent)
    }
    //Author: Brent Goubert
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bind(getItem(position)!!, clickListener)
    }

    //Author: Brent Goubert
    class MyViewHolder private constructor(val binding:  RecyclerItemRouteBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(
            route: Route,
            clickListener: RouteListener
        ) {
            binding.route = route
            binding.clickListener = clickListener
            binding.executePendingBindings()
        }
        //Author: Brent Goubert
        companion object {
            fun from(parent: ViewGroup): MyViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = RecyclerItemRouteBinding.inflate(layoutInflater, parent, false)
                return MyViewHolder(binding)
            }
        }
    }

}
//Author: Brent Goubert
class RouteDiffCallback : DiffUtil.ItemCallback<Route>() {

    //Author: Brent Goubert
    override fun areItemsTheSame(oldItem: Route, newItem: Route): Boolean {
        return oldItem.id == newItem.id
    }

    //Author: Brent Goubert
    override fun areContentsTheSame(oldItem: Route, newItem: Route): Boolean {
        return oldItem == newItem
    }

}
//Author: Brent Goubert
class RouteListener(val clickListener: (routeId: Int, kml: String) -> Unit)
{
    fun  onClick(route : Route) = clickListener(route.id, route.kml)

}