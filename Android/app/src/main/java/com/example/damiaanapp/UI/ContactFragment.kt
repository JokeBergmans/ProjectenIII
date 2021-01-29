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
import com.example.damiaanapp.databinding.FragmentContactBinding
import com.example.damiaanapp.databinding.FragmentMyRoutesBinding

//Authors: Tom Van der Weeën
class ContactFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val binding = FragmentContactBinding.inflate(inflater, container, false)

        binding.contactStartCall.setOnClickListener{
            startDial()
        }
        binding.contactStartMail.setOnClickListener{
            startMail()
        }
        return binding.root
    }

    private fun startDial() {
        //Dialer intent
        val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + Uri.encode( "024225911")))
        startActivity(intent)
    }

    private fun startMail() {
        // Mail intent
        val intent = Intent(Intent.ACTION_SENDTO)
        intent.data = Uri.parse("mailto:")
        intent.type = "text/plain"
        intent.putExtra(Intent.EXTRA_EMAIL, arrayOf(getString(R.string.contact_email)))
        startActivity(intent)
    }
}