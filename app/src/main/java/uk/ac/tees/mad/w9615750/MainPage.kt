package uk.ac.tees.mad.w9615750

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat
import com.google.firebase.auth.FirebaseAuth
import java.util.concurrent.Executor

class MainPage : AppCompatActivity() {

    private var auth: FirebaseAuth = FirebaseAuth.getInstance()

    private lateinit var executor: Executor
    private lateinit var biometricPrompt: BiometricPrompt
    private lateinit var promptInfo: BiometricPrompt.PromptInfo

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_page)


        executor = ContextCompat.getMainExecutor(this)
        biometricPrompt = BiometricPrompt(this, executor, object : BiometricPrompt.AuthenticationCallback() {
            override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                super.onAuthenticationError(errorCode, errString)
            }

            override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                super.onAuthenticationSucceeded(result)

            }

            override fun onAuthenticationFailed() {
                super.onAuthenticationFailed()
                auth.signOut()
                startActivity(Intent(applicationContext,firstLoginpage::class.java))
            }
        })

        promptInfo = BiometricPrompt.PromptInfo.Builder()
            .setTitle("Fingerprint Authentication")
            .setSubtitle("Log in using your fingerprint").setNegativeButtonText("Reload App")
            .build()
        biometricPrompt.authenticate(promptInfo)


        val sharedPreferences = getSharedPreferences("UserDetails", Context.MODE_PRIVATE)
        val userName = sharedPreferences.getString("Name", "User")

        findViewById<TextView>(R.id.usernameTextView).text = userName

        val allergenDatabaseButton = findViewById<LinearLayout>(R.id.allergiandatabsebutton)
        val searchByQRButton = findViewById<LinearLayout>(R.id.searchbyqr)
        val communityButton = findViewById<LinearLayout>(R.id.communiutybutton)
        val emergencyHelpButton = findViewById<LinearLayout>(R.id.emergencyhelp)
        val logoutButton = findViewById<Button>(R.id.logoutButton)

        // Set click listeners
        allergenDatabaseButton.setOnClickListener {
            startActivity(Intent(this, AllergenDB::class.java))
        }

        searchByQRButton.setOnClickListener {
            startActivity(Intent(this, Qr::class.java))
        }

        communityButton.setOnClickListener {
            startActivity(Intent(this, Community::class.java))
        }

        emergencyHelpButton.setOnClickListener {
            startActivity(Intent(this, Help::class.java))
        }

        logoutButton.setOnClickListener {
            auth.signOut()
            val intent = Intent(this, firstLoginpage::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            finish()
        }
    }
}