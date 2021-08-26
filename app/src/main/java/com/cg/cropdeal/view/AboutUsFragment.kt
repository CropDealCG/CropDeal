package com.cg.cropdeal.view

import android.app.ActionBar
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.cg.cropdeal.R
import com.cg.cropdeal.databinding.FragmentAboutUsBinding


class AboutUsFragment : Fragment() {
    private lateinit var binding: FragmentAboutUsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
       binding = FragmentAboutUsBinding.inflate(inflater,container,false)
        return binding.root

    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //getActionBar()?.setTitle("About Us")
        binding.rateUSBtn1.setOnClickListener {

        }
        binding.sendFeedbackBtn1.setOnClickListener {
            val email = Intent(Intent.ACTION_SEND)

            val mail={"cropdeals@gmail.com"}.toString()
            email.putExtra(Intent.EXTRA_EMAIL, mail)
            email.putExtra(Intent.EXTRA_SUBJECT, "Feedback")
            email.type = "message/rfc822"

            startActivity(
                Intent.createChooser(email, "Choose an Email client :"), null
            )
        }
    }


}