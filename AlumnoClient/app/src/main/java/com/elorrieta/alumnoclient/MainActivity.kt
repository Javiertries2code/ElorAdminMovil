package com.elorrieta.alumnoclient


import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.add
import androidx.fragment.app.commit
import com.elorrieta.alumnoclient.LoginFragment.Companion.NAME_BUNDLE
import com.elorrieta.alumnoclient.LoginFragment.Companion.PASSWORD_BUNDLE
import com.elorrieta.alumnoclient.session.SessionManager
import com.elorrieta.alumnoclient.socketIO.LoginSocket
import com.elorrieta.alumnoclient.socketIO.MeetingSocket
import com.elorrieta.alumnoclient.socketIO.SocketClient
import com.elorrieta.alumnoclient.socketIO.StudentSocket
import com.elorrieta.alumnoclient.socketIO.TeacherSocket

class MainActivity : AppCompatActivity() {

    private var socketClient : SocketClient? = null
    private var studentSocket : StudentSocket? = null
    private var loginSocket : LoginSocket? = null
    private var meetingSocket : MeetingSocket? = null
    private var teacherSocket : TeacherSocket? = null



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        if (savedInstanceState == null) {
            val bundle =
                bundleOf(NAME_BUNDLE to "Email@ROOM.com", PASSWORD_BUNDLE to "savedpassword Room")

            supportFragmentManager.commit {
                setReorderingAllowed(true)
                add<LoginFragment>(R.id.fragmentContainer, args = bundle)
            }
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        socketClient = SocketClient(this)
        studentSocket = StudentSocket(this)
        loginSocket = LoginSocket(this)
        meetingSocket = MeetingSocket(this)
        teacherSocket = TeacherSocket(this)


        findViewById<Button>(R.id.buttonConnect)
            .setOnClickListener {
                socketClient!!.connect()
                Thread.sleep(3000) // A little delay...
            }

        findViewById<Button>(R.id.buttonDisconnect)
            .setOnClickListener {
                socketClient!!.disconnect()
                Thread.sleep(3000) // A little delay...
            }

        findViewById<Button>(R.id.buttonLogin)
            .setOnClickListener {
                loginSocket!!.doLogin("teacher1@email.com", "123")
                Thread.sleep(3000) // A little delay...
            }

        findViewById<Button>(R.id.btnResetPasswrod)
            .setOnClickListener {
                loginSocket!!.resetPassword("teacher1@email.com" )
                Thread.sleep(3000) // A little delay...
            }

        findViewById<Button>(R.id.buttonGetAll)
            .setOnClickListener {
                studentSocket!!.doGetAll()
                Thread.sleep(3000) // A little delay...
            }

        findViewById<Button>(R.id.buttonLogout)
            .setOnClickListener {
                loginSocket!!.doLogout()
                Thread.sleep(3000) // A little delay...
            }

    }

    // Anctivity lifecycle, better close the socket...
    override fun onDestroy() {
        super.onDestroy()
        socketClient!!.disconnect()
    }
}