package com.elorrieta.alumnoclient


import android.content.Context
import android.net.ConnectivityManager
import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.fragment.app.replace
import com.elorrieta.alumnoclient.socketIO.LoginSocket
import com.elorrieta.alumnoclient.socketIO.SocketClient
import com.elorrieta.alumnoclient.socketIO.StudentSocket
import com.elorrieta.alumnoclient.socketIO.TeacherSocket
import androidx.lifecycle.lifecycleScope
import com.elorrieta.alumnoclient.socketIO.RegisterSocket
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import android.net.NetworkCapabilities
import android.widget.Toast
import androidx.core.content.ContentProviderCompat.requireContext
import com.elorrieta.alumnoclient.room.AppDatabase
import com.elorrieta.alumnoclient.room.RoomUser
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope


class MainActivity : AppCompatActivity() {

   

    private lateinit var socketClient: SocketClient
    private lateinit var studentSocket: StudentSocket
    private lateinit var loginSocket: LoginSocket
    private lateinit var teacherSocket: TeacherSocket
    private lateinit var registerSocket: RegisterSocket
    public var remeberFlag: Int = 0



    private val   mainActivity = "mainActivity"



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        // Inicializar Sockets
        socketClient= SocketClient(this)
         studentSocket= StudentSocket(this)
       // meetingSocket= MeetingSocket(this)
        teacherSocket = TeacherSocket(this)
        registerSocket = RegisterSocket(this)
        loginSocket = LoginSocket(this)
        loginSocket = LoginSocket(this)
        loginSocket = LoginSocket(this)
        loginSocket = LoginSocket(this)




        /*
        It migh appear quite confusing at first

        here is the thing, every socket created, its getting it from the SockerManager, a singleton pointing to the same place,
        hence once we do here socket client connect, the connecion remains oopnened.

        later one every fragment gets its own socket (which in real is the same, butit facilitates the separation of code, on the flipside of having a bunch
        of sockets runing in memory... but noone spoke about efiiciency so far)
         */

       // i was connecting automatically, now i leave it for the connecting button
        socketClient = SocketClient(this)
     //   socketClient = SocketClient(this).apply {
//            connect()
//        }

        if (savedInstanceState == null) {


            supportFragmentManager.commit {
                setReorderingAllowed(true)
                replace<LandingFragment>(R.id.fragmentContainer)

            }
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }






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

    fun saveRoomUser(email : String, passwordHashed: String, passwordNotHashed: Int){
val roomtag = "roomtag"
        Log.d(roomtag, "ESTO ES EN main ACTIVITY")

        Log.d(roomtag, email)
        Log.d(roomtag, passwordHashed)
        Log.d(roomtag, passwordNotHashed.toString())
        Log.d(roomtag, email)
        var myRemember = false

        val db = AppDatabase.getInstance(this)
        if (remeberFlag > 0)
        {
            myRemember = true}
        else {
            myRemember = false}

        val lastUser = RoomUser(

            email = email,
            password = passwordNotHashed.toString(),
            remember = myRemember,
            lastLogin = System.currentTimeMillis()
        )


        lifecycleScope.launch(Dispatchers.IO) {
            db.roomDao().insert(lastUser)
        }

    }
    fun getLoginSocket(): LoginSocket {
        return loginSocket
    }

    fun getStudentSocket(): StudentSocket {
        return studentSocket
    }

    fun getTeacherSocket(): TeacherSocket {
        return teacherSocket

    }

    fun getRegisterSocket(): RegisterSocket {
        return registerSocket

    }

    fun connect(){
        socketClient.connect()
        Log.d(mainActivity, "connect function called in main")
    }

    // Anctivity lifecycle, better close the socket...
    override fun onDestroy() {
        super.onDestroy()
        this.socketClient
            .disconnect()
    }

    fun isInternetAvailable(): Boolean {
        val connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val network = connectivityManager.activeNetwork ?: return false
        val capabilities = connectivityManager.getNetworkCapabilities(network) ?: return false
        return capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) ||
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)
    }
//I hope this one handles the login change, along passing a bundle if needed
    fun navigate(fragment: AppFragments, args: Bundle? = null) {
        val fragmentManager = supportFragmentManager
        val existingFragment = fragmentManager.findFragmentByTag(fragment.name)

        val fragmentToNavigate = existingFragment ?: fragment.newInstance().apply {
            arguments = args
        }

        fragmentManager.beginTransaction()
            .replace(R.id.fragmentContainer, fragmentToNavigate, fragment.name)
            .addToBackStack(null)
            .commit()
    }




//    fun navigate(nextFragment: Fragment)
//    {
//
//     this.supportFragmentManager
//                .beginTransaction()
//                .replace(R.id.fragmentContainer, nextFragment)
//                .addToBackStack(null)
//                .commit()
//        }

    fun validPasswords(newPass1:String, newPAss2: String)
    {
        Log.d("main", newPass1)


    }

    fun toaster(message: String) {
        runOnUiThread {
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
        }
    }
    }
