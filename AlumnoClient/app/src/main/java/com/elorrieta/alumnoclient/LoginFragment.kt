package com.elorrieta.alumnoclient

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup



/**
 * A simple [Fragment] subclass.
 * Use the [LoginFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class LoginFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var name: String? = null
    private var password: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            name = it.getString(NAME_BUNDLE)
            password = it.getString(PASSWORD_BUNDLE)
            Log.i("LoginFragment", name.orEmpty())
            Log.i("LoginFragment", password.orEmpty())

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_login, container, false)
    }

    companion object {

        // TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
         const val NAME_BUNDLE = "name"
         const val PASSWORD_BUNDLE = "password"

        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(name: String, password: String) =
            LoginFragment().apply {
                arguments = Bundle().apply {
                    putString(NAME_BUNDLE, name)
                    putString(PASSWORD_BUNDLE, password)
                }
            }
    }
}