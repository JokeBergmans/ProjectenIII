package com.example.damiaanapp.UI

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.example.damiaanapp.R
import com.example.damiaanapp.databinding.FragmentSosBinding

//Author: Tom Van der Weeën
class SosFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val binding = FragmentSosBinding.inflate(inflater, container,false)

        binding.sosStartCall.setOnClickListener{
            startDial()
        }
        binding.sosStartCallEm.setOnClickListener{
            startDialEm()
        }
        return binding.root
    }

    private fun startDial() {
        //Dialer intent
        val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + Uri.encode( "024225911")))
        startActivity(intent)
    }

    private fun startDialEm() {
        //Dialer intent
        val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + Uri.encode( "112")))
        startActivity(intent)
    }

}