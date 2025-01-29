package com.elorrieta.alumnoclient


import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.add
import androidx.fragment.app.commit
import androidx.fragment.app.replace
import com.elorrieta.alumnoclient.LoginFragment.Companion.NAME_BUNDLE
import com.elorrieta.alumnoclient.LoginFragment.Companion.PASSWORD_BUNDLE
import com.elorrieta.alumnoclient.socketIO.LoginSocket
import com.elorrieta.alumnoclient.socketIO.MeetingSocket
import com.elorrieta.alumnoclient.socketIO.SocketClient
import com.elorrieta.alumnoclient.socketIO.StudentSocket
import com.elorrieta.alumnoclient.socketIO.TeacherSocket
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

   

    private lateinit var socketClient: SocketClient
    private lateinit var studentSocket: StudentSocket
    private lateinit var loginSocket: LoginSocket
    private lateinit var meetingSocket: MeetingSocket
    private lateinit var teacherSocket: TeacherSocket
    private val   mainActivity = "mainActivity"



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        loginSocket = LoginSocket(this) // Inicializar LoginSocket

        if (savedInstanceState == null) {
            val bundle =
                bundleOf(NAME_BUNDLE to "teacher1@email.com", PASSWORD_BUNDLE to "123")

            supportFragmentManager.commit {
                setReorderingAllowed(true)
                replace<LoginFragment>(R.id.fragmentContainer, args = bundle)

            }
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }



        socketClient = SocketClient(this).apply {
            connect()
        }

      // socketClient = SocketClient(this)
        studentSocket = StudentSocket(this)
        //El lonffragment actitvy, he creado loginSocket, pasandole el fragment activity
        //la conexion debera er la misma pq cada socket la recibe del singleton

       loginSocket = LoginSocket(this)
        meetingSocket = MeetingSocket(this)
        teacherSocket = TeacherSocket(this)



/*Disconnect already in on destroy event so not in button anymore
        findViewById<Button>(R.id.buttonDisconnect)
            .setOnClickListener {
                socketClient!!.disconnect()
                Thread.sleep(3000) // A little delay...
            }*/


        findViewById<Button>(R.id.login)
            .setOnClickListener {
                loginSocket.doLogin("teacher1@email.com", "123")
                Log.d(mainActivity, "it got clicked on main activity")
                lifecycleScope.launch {
                    delay(3000) // Espera 3 segundos sin bloquear la UI
                    loginSocket.doLogin("teacher1@email.com", "123")
                }                }


        findViewById<Button>(R.id.btnResetPasswrod)
            .setOnClickListener {
                loginSocket!!.resetPassword("teacher1@email.com" )
                lifecycleScope.launch {
                    delay(3000) // Espera 3 segundos sin bloquear la UI
                    loginSocket.doLogin("teacher1@email.com", "123")
                }                }

        findViewById<Button>(R.id.buttonGetAll)
            .setOnClickListener {
                studentSocket!!.doGetAll()
                lifecycleScope.launch {
                    delay(3000) // Espera 3 segundos sin bloquear la UI
                    loginSocket.doLogin("teacher1@email.com", "123")
                }                }

        findViewById<Button>(R.id.buttonLogout)
            .setOnClickListener {
                loginSocket!!.doLogout()
                lifecycleScope.launch {
                    delay(3000) // Espera 3 segundos sin bloquear la UI
                    loginSocket.doLogin("teacher1@email.com", "123")
                }                }

    }

    fun getLoginSocket(): LoginSocket {
        return loginSocket
    }

    // Anctivity lifecycle, better close the socket...
    override fun onDestroy() {
        super.onDestroy()
        this.socketClient
            .disconnect()
    }


}