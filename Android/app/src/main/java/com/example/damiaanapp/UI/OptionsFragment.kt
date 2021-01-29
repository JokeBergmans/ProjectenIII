package com.example.damiaanapp.UI

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.damiaanapp.R
import com.example.damiaanapp.databinding.FragmentOptionsBinding
import com.example.damiaanapp.util.SessionManager
import com.example.damiaanapp.util.Status
import com.example.damiaanapp.viewmodels.LoginViewModel
import com.example.damiaanapp.viewmodels.OptionsViewModel
import org.koin.android.ext.android.inject

//Author: Tom Van der Weeën
class OptionsFragment : Fragment() {
    private lateinit var binding: FragmentOptionsBinding

    private lateinit var _viewModel: OptionsViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentOptionsBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = this
        val viewModel: OptionsViewModel by inject()
        _viewModel = viewModel
        binding.viewModel = viewModel
        val sessionManager = SessionManager(this.requireContext())
        viewModel.updateParticipantId(sessionManager.fetchUserId())
        viewModel.participant.observe(
            viewLifecycleOwner,
            Observer {
                if (it != null)
                    viewModel._canBeFollowed.value = it.canBeFollowed })
        viewModel.canBeFollowed.observe(
            viewLifecycleOwner, Observer {
                if (it != viewModel.participant.value!!.canBeFollowed) {
                    viewModel.updatePreferences().observe(viewLifecycleOwner, Observer { updateCall ->
                        updateCall?.let { resource ->
                            when (resource.status) {
                                Status.SUCCESS -> {
                                    viewModel.participant.value!!.canBeFollowed = it
                                }
                                Status.ERROR -> {
                                    showToast(resource.message!!)
                                }
                                else ->  {}
                            }
                        }
                    })
                }
            }
        )

        ArrayAdapter.createFromResource(
            this.requireContext(),
            R.array.language_array,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            // Specify the layout to use when the list of choices appears
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            // Apply the adapter to the spinner
            binding.spinner1.adapter = adapter
        }

        val lines = resources.getStringArray(R.array.language_array).toList()
        val index = lines.indexOf(resources.configuration.locale.language)
        binding.spinner1.setSelection(index)

        binding.spinner1.onItemSelectedListener =  object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(p0: AdapterView<*>?) {

            }

            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                if(p0?.selectedItem.toString() != resources.configuration.locale.language)
                {
                    (activity as MainActivity?)!!.changeLanguage(p0?.selectedItem.toString())

                }
            }
        }

        return binding.root
    }

    private fun showToast(text: String) {
        Toast.makeText(this.requireContext(), text, Toast.LENGTH_LONG).show()
    }



}