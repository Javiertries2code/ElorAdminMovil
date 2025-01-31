package com.elorrieta.alumnoclient

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.elorrieta.alumnoclient.socketIO.LoginSocket
import com.elorrieta.alumnoclient.socketIO.StudentSocket
import com.google.android.material.appbar.MaterialToolbar


private lateinit var thisSocket: StudentSocket



private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [StudentProfileFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class StudentProfileFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

            thisSocket = (requireActivity() as MainActivity).getStudentSocket()

            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_student_profile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val toolbar = view.findViewById<MaterialToolbar>(R.id.studentToolbar)
        val buttonHorarios = view.findViewById<Button>(R.id.studentHorarios)
        val buttonCourses = view.findViewById<Button>(R.id.studentCourses)
        val buttonDocs = view.findViewById<Button>(R.id.studentDocs)

        (requireActivity() as AppCompatActivity).supportActionBar?.apply {
            setDisplayShowHomeEnabled(true)
            setLogo(R.drawable.elorrietalogo)
            setDisplayUseLogoEnabled(true)
            title = "Panel de Estudiante"
        }
        buttonHorarios.setOnClickListener {

        }

        buttonCourses.setOnClickListener {

        }

        // Listener para botón "Documentos"
        buttonDocs.setOnClickListener {

        }
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment StudentProfileFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            StudentProfileFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}