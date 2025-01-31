package com.elorrieta.alumnoclient

import android.graphics.drawable.AnimationDrawable
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import androidx.core.os.bundleOf
import androidx.fragment.app.FragmentActivity


class LandingFragment : Fragment() {
    // TODO: Rename and change types of parameters

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {


        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_landing, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val imageView = view.findViewById<ImageView>(R.id.animatedImage)
        val animation = imageView.drawable as? AnimationDrawable
        animation?.start()


        val buttonConnect = view.findViewById<Button>(R.id.buttonConnect)
        buttonConnect.setOnClickListener {
            val activity = requireActivity() as? MainActivity
            val newFragment: Fragment = LoginFragment()
            activity?.connect()
            //bundle only for testing while developing
            newFragment.apply { bundleOf("email" to "teacher1@email.com", "password" to "123") }

            if (activity != null) {
                activity.supportFragmentManager
                    .beginTransaction()
                    .replace(R.id.fragmentContainer, newFragment)
                    .addToBackStack(null)
                    .commit()
            }
        }
    }

}


