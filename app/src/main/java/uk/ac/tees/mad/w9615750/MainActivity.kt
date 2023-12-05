package uk.ac.tees.mad.w9615750

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat
import com.google.firebase.auth.FirebaseAuth
import java.util.concurrent.Executor


class MainActivity : AppCompatActivity() {

    private lateinit var fBAuth: FirebaseAuth



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        fBAuth = FirebaseAuth.getInstance()



        val logo = findViewById<ImageView>(R.id.logo)
        val fadeIn: Animation = AnimationUtils.loadAnimation(this, R.anim.fade)
        logo.startAnimation(fadeIn)

        Handler(Looper.getMainLooper()).postDelayed({
            if (fBAuth.currentUser != null) {
                startActivity(Intent(applicationContext, MainPage::class.java))
            } else {
                startActivity(Intent(this, firstLoginpage::class.java))
            }
            finish()
        }, 2000)
    }
}