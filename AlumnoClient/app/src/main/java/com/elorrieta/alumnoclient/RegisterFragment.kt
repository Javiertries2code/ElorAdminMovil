package com.elorrieta.alumnoclient


import android.Manifest

import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.elorrieta.alumnoclient.entity.Student
import com.elorrieta.alumnoclient.entity.Teacher
import com.elorrieta.alumnoclient.session.SessionManager
import com.elorrieta.alumnoclient.socketIO.RegisterSocket
import com.google.android.material.appbar.MaterialToolbar

private lateinit var thisSocket: RegisterSocket

private lateinit var nameEditText: EditText
private lateinit var surnameEditText: EditText
private lateinit var emailEditText: EditText
private lateinit var phone1EditText: EditText
private lateinit var phone2EditText: EditText
private lateinit var passwordEditText: EditText
private lateinit var password2EditText: EditText
private lateinit var addressEditText: EditText

private lateinit var dniEditText: EditText
private lateinit var registerCiclos: TextView
private lateinit var registerButton: Button
private lateinit var registerFoto: Button
private lateinit var fotoImageView: ImageView


private const val CAPTURA_IMAGEN = 1

private const val PERMISSION_CAMERA_REQUEST = 1


private val tag = "RegisterSocket"

class RegisterFragment : Fragment() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        thisSocket = (requireActivity() as MainActivity).getRegisterSocket()
        Log.d(tag, "got into Register fragment activity")
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_register, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        dniEditText = view.findViewById(R.id.registerDni)
        addressEditText = view.findViewById(R.id.registerAddress)
        nameEditText = view.findViewById(R.id.registerName)
        surnameEditText = view.findViewById(R.id.registerSurname)
        emailEditText = view.findViewById(R.id.registerEmail)
        phone1EditText = view.findViewById(R.id.registerPhone1)
        phone2EditText = view.findViewById(R.id.registerPhone2)
        passwordEditText = view.findViewById(R.id.registerNewPassword)
        password2EditText = view.findViewById(R.id.registerNewPassword2)

        registerButton = view.findViewById(R.id.registerSubmit)
        registerCiclos = view.findViewById(R.id.registerCiclos)
        fotoImageView = view.findViewById(R.id.avatar)
        var listaEdits = listOf(
            nameEditText,
            surnameEditText,
            emailEditText,
            phone1EditText,
            phone2EditText,
            passwordEditText,
            password2EditText,
            dniEditText,
            addressEditText
        )

        var emptyEdits = 0
        for (item in listaEdits) {
            if (item.text.isNullOrBlank())
                emptyEdits++
        }
        if (emptyEdits > 0)
            Toast.makeText(
                requireContext(),
                "Alguno de los campos solicitados está vacío",
                Toast.LENGTH_SHORT
            ).show()


        val packageManager: PackageManager = requireContext().packageManager
        val cameraAvailable: Boolean =
            packageManager.hasSystemFeature(PackageManager.FEATURE_CAMERA_ANY)
        val activity = requireActivity() as? MainActivity


        val btnCamera = view.findViewById<Button>(R.id.registerFoto)
        btnCamera.setOnClickListener {

            if (isCameraPermissionGranted()) {
                //
                if (cameraAvailable) {

                    try {
                        val hacerFotoIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                        startActivityForResult(
                            hacerFotoIntent,
                            CAPTURA_IMAGEN
                        ) // Código que puede lanzar una excepción
                    } catch (e: Exception) {
                        Toast.makeText(
                            requireContext(),
                            "Ha cascado la camara, lo he metido en un try cacth",
                            Toast.LENGTH_SHORT
                        ).show()

                        Log.d("RegisterFoto", e.toString())
                    }


                } else {
                    Toast.makeText(requireContext(), "Cámara no disponible", Toast.LENGTH_SHORT)
                        .show()
                }
            } else {
                if (activity != null) {
                    ActivityCompat.requestPermissions(
                        activity,
                        arrayOf(Manifest.permission.CAMERA),
                        PERMISSION_CAMERA_REQUEST
                    )
                }
            }

        }

        val toolbar = view.findViewById<MaterialToolbar>(R.id.registerToolbar)

        (requireActivity() as AppCompatActivity).setSupportActionBar(toolbar)

        (requireActivity() as AppCompatActivity).supportActionBar?.apply {
            setDisplayShowHomeEnabled(true)
            setLogo(R.drawable.elorrietalogo)
            setDisplayUseLogoEnabled(true)
            title = "Registro"
        }

        val user = arguments?.getSerializable("user")

        when (user) {
            is Student -> {
                registerCiclos.visibility = View.VISIBLE
                registerCiclos.text = ""

                nameEditText.setText(user.name ?: "")
                surnameEditText.setText(user.lastName ?: "")
                emailEditText.setText(user.email ?: "")
                phone1EditText.setText(user.phone1 ?: "")
                phone2EditText.setText(user.phone2 ?: "")
                passwordEditText.setText(user.phone2 ?: "")
                password2EditText.setText(user.phone2 ?: "")
                dniEditText.setText(user.dni ?: "")
                addressEditText.setText(user.address ?: "")

                Log.d(tag, "Student recibido: ${user.name}")
            }

            is Teacher -> {
                registerCiclos.visibility = View.GONE

                nameEditText.setText(user.name ?: "")
                surnameEditText.setText(user.lastName ?: "")
                emailEditText.setText(user.email ?: "")
                phone1EditText.setText(user.phone1 ?: "")
                phone2EditText.setText(user.phone2 ?: "")
                passwordEditText.setText(user.passwordNotHashed.toString() ?: "")
                password2EditText.setText(user.passwordNotHashed.toString() ?: "")
                dniEditText.setText(user.dni ?: "")
                addressEditText.setText(user.address ?: "")
                Log.d(tag, "Teacher recibido: ${user.name}")
            }

            else -> {
                Log.e(tag, "No se recibió ningún usuario válido")
                registerCiclos.visibility = View.GONE
            }
        }

        registerButton.setOnClickListener {
            Log.d("registerButton", "entra en la funcion")
            for (item in listaEdits) {
                if (item.text.isNullOrBlank()) {
                    Toast.makeText(
                        requireContext(),
                        "Completa todos los campos obligatorios",
                        Toast.LENGTH_LONG
                    ).show()
                    return@setOnClickListener
                }
            }
            Log.d("registerButton", "llama a same password")

            if (samePassword(
                    passwordEditText.text.toString(),
                    password2EditText.text.toString(),
                    "123"
                ) == true
            ) {
                //Log.d("registerButton", "Las contraseñas deben ser iguales, y diferentes a la Default")

                // Toast.makeText(requireContext(), "Las contraseñas deben ser iguales, y diferentes a la Default", Toast.LENGTH_SHORT).show()

                return@setOnClickListener
            }
            val newUser = SessionManager.getUser()
            when (newUser) {
                is Student -> {
                    val student = newUser as? Student
                    newUser?.let {
                        it.name = nameEditText.text.toString()
                        it.lastName = surnameEditText.text.toString()
                        it.email = emailEditText.text.toString()
                        it.phone1 = phone1EditText.text.toString()
                        it.phone2 = phone2EditText.text.toString()
                        it.passwordHashed = passwordEditText.text.toString()
                        it.passwordNotHashed = passwordEditText.text.toString().toIntOrNull()
                        it.dni = dniEditText.text.toString()
                        it.address = addressEditText.text.toString()
                        thisSocket.registerUser(it)
                    }
                }

                is Teacher -> {
                    val teacher = newUser as? Teacher
                    newUser?.let {
                        it.name = nameEditText.text.toString()
                        it.lastName = surnameEditText.text.toString()
                        it.email = emailEditText.text.toString()
                        it.phone1 = phone1EditText.text.toString()
                        it.phone2 = phone2EditText.text.toString()
                        it.passwordHashed = passwordEditText.text.toString()
                        it.passwordNotHashed = passwordEditText.text.toString().toIntOrNull()
                        it.dni = dniEditText.text.toString()
                        it.address = addressEditText.text.toString()
                        thisSocket.registerUser(it)
                    }
                }
            }

        }
    }

    //Aint sure the ribrica means  checking default password on db
    fun samePassword(pass1: String, pass2: String, pass3: String? = null): Boolean {

        Log.d("registerButton", "Dentro de same password")

        if (pass1 != pass2) {
            Toast.makeText(requireContext(), "Las contraseñas no coinciden", Toast.LENGTH_SHORT)
                .show()
            return true
        } else if (null != pass3 && pass1 == pass3) {
            Toast.makeText(
                requireContext(),
                "Las contraseñas coinciden con la designada por defecto",
                Toast.LENGTH_SHORT
            ).show()
            return true
        } else
            return false
    }


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        if (requestCode == PERMISSION_CAMERA_REQUEST) {
            if (isCameraPermissionGranted()) {
                // start camera
            } else {
                Log.e("cameraLog", "no camera permission")
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    private fun isCameraPermissionGranted(): Boolean {
        return ContextCompat.checkSelfPermission(
            requireContext(),
            Manifest.permission.CAMERA
        ) == PackageManager.PERMISSION_GRANTED
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        Log.d("RegisterFoto", "Entra en onActivityResult")

        if (requestCode == CAPTURA_IMAGEN && resultCode == AppCompatActivity.RESULT_OK) {
            Log.d("RegisterFoto", "Ha sacado la foto y la manda al avatar... pero casca")

            val extras = data?.extras
            val imageBitmap = extras?.get("data") as? Bitmap
            if (imageBitmap != null) {
                Toast.makeText(
                    requireContext(),
                    "ha sacado la foto y la manda al avatar... pero casca",
                    Toast.LENGTH_SHORT
                ).show()
                Log.d("RegisterFoto", "Ha sacado la foto y la manda al avatar... pero casca")
                fotoImageView.setImageBitmap(imageBitmap)
            } else {
                Toast.makeText(requireContext(), "No se pudo obtener la imagen", Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }
}
