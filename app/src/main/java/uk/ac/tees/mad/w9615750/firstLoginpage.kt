package uk.ac.tees.mad.w9615750

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class firstLoginpage : AppCompatActivity() {

    private lateinit var fBAuth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_first_loginpage)

        findViewById<TextView>(R.id.forgotPasswordTextView).setOnClickListener{
            startActivity(Intent(this,secondRegistationPage::class.java))
        }


        fBAuth = FirebaseAuth.getInstance()

        findViewById<Button>(R.id.loginButton).setOnClickListener {
            val email = findViewById<EditText>(R.id.emailEditText).text.toString()
            val password = findViewById<EditText>(R.id.passwordEditText).text.toString()
            loginUser(email, password)
        }


    }

    private fun loginUser(email: String, password: String) {
        fBAuth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    val uid = fBAuth.currentUser?.uid

                    val databaseReference = FirebaseDatabase.getInstance().getReference("Users")


                    uid?.let {
                        databaseReference.child(it).addListenerForSingleValueEvent(object : ValueEventListener {
                            override fun onDataChange(snapshot: DataSnapshot) {
                                val userName = snapshot.child("fullName").getValue(String::class.java) ?: ""
                                Toast.makeText(applicationContext, "Welcome, $userName", Toast.LENGTH_SHORT).show()

                                saveUserData(email, userName)
                                startActivity(Intent(applicationContext, MainPage::class.java))
                            }

                            override fun onCancelled(error: DatabaseError) {
                                Toast.makeText(applicationContext, "Error: ${error.message}", Toast.LENGTH_SHORT).show()
                            }
                        })
                    } ?: run {
                        Toast.makeText(applicationContext, "Error: User ID not found", Toast.LENGTH_SHORT).show()
                    }

                } else {
                    Toast.makeText(applicationContext, "Login Failed", Toast.LENGTH_SHORT).show()
                }
            }
    }

    private fun saveUserData(email: String, name: String) {
        val sharedPreferences = getSharedPreferences("UserDetails", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString("Email", email)
        editor.putString("Name", name)
        editor.apply()
    }


}