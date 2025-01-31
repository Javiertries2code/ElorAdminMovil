package com.elorrieta.alumnoclient

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import com.google.android.material.appbar.MaterialToolbar


class CourseFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_course, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val toolbar = view.findViewById<MaterialToolbar>(R.id.coursesToolbar)
        val buttonHorarios = view.findViewById<Button>(R.id.studentHorarios)
        val buttonCourses = view.findViewById<Button>(R.id.studentCourses)
        val buttonDocs = view.findViewById<Button>(R.id.studentDocs)
        val buttonProfile = view.findViewById<Button>(R.id.studentProfile)


        (requireActivity() as AppCompatActivity).supportActionBar?.apply {
            setDisplayShowHomeEnabled(true)
            setLogo(R.drawable.elorrietalogo)
            setDisplayUseLogoEnabled(true)
            title = "Cursos"
        }


        buttonProfile.setOnClickListener {
            val activity = requireActivity() as? MainActivity
            val newFragment: Fragment = StudentProfileFragment()
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

        buttonHorarios.setOnClickListener {

        }


        buttonCourses.setOnClickListener {

        }

        // Listener para botón "Documentos"
        buttonDocs.setOnClickListener {

        }
    }

    }
