
package com.example.damiaanapp.UI

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.damiaanapp.viewmodels.RoutesViewModel
import com.example.damiaanapp.adapters.RoutesAdapter
import com.example.damiaanapp.adapters.RouteListener
import com.example.damiaanapp.databinding.FragmentMyRoutesBinding
import com.example.damiaanapp.util.SessionManager
import com.example.damiaanapp.viewmodels.LoginViewModel
import org.koin.android.ext.android.inject

class MyRoutesFragment : Fragment() {

    private lateinit var binding: FragmentMyRoutesBinding
    private lateinit var sessionManager: SessionManager

    //Author: Brent Goubert
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentMyRoutesBinding.inflate(inflater, container, false)
        sessionManager = SessionManager(this.requireContext())
        val viewModel: RoutesViewModel by inject()
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner

        // Recyclerview
        val manager = LinearLayoutManager(this.requireContext())
        binding.recyclerView.layoutManager = manager
        val adapter = RoutesAdapter(RouteListener {
             routeId,kml -> viewModel.onRouteClicked(routeId,kml)
        })

        binding.recyclerView.adapter = adapter

        viewModel.navigateToMapRouteId.observe(viewLifecycleOwner, Observer {id ->
            id?.let {
                this.findNavController().navigate(
                    MyRoutesFragmentDirections.actionMyRoutesFragmentToMapFragment(
                        id,
                        viewModel.navigateToMapRouteKml.value!!
                    )
                )
                viewModel.onMapFragmentNavigated()
            }
        })

        viewModel.routes.observe(viewLifecycleOwner , Observer  {
            adapter.submitList(it)
            if(it.isEmpty())
            {
                binding.emptyView.visibility = View.VISIBLE
            }
            else {
                binding.emptyView.visibility = View.GONE
            }
        })

        binding.emptyView.setOnClickListener{
            val url = "https://evening-hamlet-46004.herokuapp.com/en-US/"
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse(url)
            startActivity(intent)
        }

        return binding.root
    }
}



