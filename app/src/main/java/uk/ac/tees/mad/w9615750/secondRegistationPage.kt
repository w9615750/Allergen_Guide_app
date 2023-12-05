package uk.ac.tees.mad.w9615750

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class secondRegistationPage : AppCompatActivity() {

    private lateinit var FBAuth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_second_registation_page)


        FBAuth = FirebaseAuth.getInstance()

        var signupButton :Button = findViewById(R.id.signupButton)
        var login:TextView  = findViewById(R.id.loginTextView);

        var emailEditText :EditText = findViewById(R.id.emailEditText)
        var passwordEditText :EditText = findViewById(R.id.passwordEditText)
        var nameEditText :EditText = findViewById(R.id.fullNameEditText)


        login.setOnClickListener{
            startActivity(Intent(applicationContext,firstLoginpage::class.java))
            finish()
        }

        signupButton.setOnClickListener {
            val email = emailEditText.text.toString().trim()
            val password = passwordEditText.text.toString().trim()
            val fullName = nameEditText.text.toString().trim()
            Toast.makeText(applicationContext,"asdf",Toast.LENGTH_SHORT).show()

            if (email.isNotEmpty() && password.isNotEmpty() && fullName.isNotEmpty()) {
                FBAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        // Get the UID for the newly created user
                        val uid = task.result?.user?.uid


                        val databaseReference = FirebaseDatabase.getInstance().getReference("Users")


                        val userData = HashMap<String, Any>()
                        userData["email"] = email
                        userData["fullName"] = fullName

                        uid?.let {
                            databaseReference.child(it).setValue(userData).addOnCompleteListener { task ->
                                if (task.isSuccessful) {
                                    Toast.makeText(this, "Signup successful and data saved", Toast.LENGTH_SHORT).show()
                                    startActivity(Intent(this, firstLoginpage::class.java))
                                } else {
                                    Toast.makeText(this, "Failed to save data: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                                }
                            }
                        } ?: run {
                            Toast.makeText(this, "Signup successful but failed to save data", Toast.LENGTH_SHORT).show()
                        }

                    } else {
                        Toast.makeText(this, "Signup failed"+task.exception.toString(), Toast.LENGTH_SHORT).show()
                    }
                }
            } else {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show()
            }
        }

    }
}