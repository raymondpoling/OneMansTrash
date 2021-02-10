package org.mousehole.americanairline.onemanstrash.view

import android.content.Intent
import android.os.Bundle
import android.widget.FrameLayout
import androidx.appcompat.app.AppCompatActivity
import org.mousehole.americanairline.onemanstrash.R
import org.mousehole.americanairline.onemanstrash.viewmodel.LoginViewModel

/**
 * This class will be used for any kind of startup. For example, permission checks, login checks
 * and another other feature that could require the application NOT start in the main view.
 * This Activity exists solely to give a cleaner transition into the app.
 *
 * Again, this activity is where most setup should occur. This activity should switch to login,
 * the main application, or if there are urgent messages or updates required, it should
 * transition into them.
 */
class StartupActivity : AppCompatActivity() {

    private val loginViewModel = LoginViewModel

    private lateinit var frameLayout: FrameLayout
    private val logonFragment = LogonFragment()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_startup)

        frameLayout = findViewById(R.id.logon_window)

        if(loginViewModel.loggedInVerifiedUser()) {
            val intent = Intent(this, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
            startActivity(intent)
        } else {
            supportFragmentManager.beginTransaction()
                .add(R.id.logon_window, logonFragment)
                .addToBackStack(logonFragment.tag)
                .commit()
        }
    }
}