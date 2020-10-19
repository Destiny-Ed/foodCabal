package com.king.foodcabal.Auth

import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.widget.EditText
import android.widget.Toast
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.king.foodcabal.MainActivity
import com.king.foodcabal.R
import kotlinx.android.synthetic.main.activity_login.*
import java.util.regex.Pattern

class Login : AppCompatActivity() {

    private lateinit var email : EditText
    private lateinit var password : EditText
    private lateinit var _auth : FirebaseAuth
    private lateinit var progress : ProgressDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        progress = ProgressDialog(this)


        email = findViewById(R.id.input_email)
        password = findViewById(R.id.input_password)
        _auth = FirebaseAuth.getInstance()

        link_signup.setOnClickListener {
            startActivity(Intent(this, SignUp()::class.java))
        }

        link_reset_password.setOnClickListener {
            startActivity(Intent(this, ResetPassword::class.java))
        }

        btn_login.setOnClickListener{

            progress.setTitle("Please wait...")
            progress.setCancelable(false)
            progress.setCanceledOnTouchOutside(false)
            progress.show()
            //Validate form and signup user
            if (email.text.toString().isEmpty() || password.text.toString().isEmpty()){
                progress.hide()
                email.error = "Email must be provided"
                password.error = "Password must be provided"
                return@setOnClickListener
            }
            else if(!Patterns.EMAIL_ADDRESS.matcher(email.text.toString()).matches()){
                progress.hide()
                email.error = "Provide a valid email address"
                return@setOnClickListener
            }
            else {
                signIn_auth(email.text.toString().trim(), password.text.toString().trim())

            }
        }
    }

    private fun signIn_auth(email: String, password: String) {
        _auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener {task ->
                if (task.isSuccessful) {

                    progress.hide()
                    // Sign in success, update UI with the signed-in user's information
                   startActivity(Intent(this, MainActivity::class.java))
                    Toast.makeText(this, "Sign Up successfull", Toast.LENGTH_SHORT).show()
                    finish()
                } else {

                    progress.hide()
                    // If sign in fails, display a message to the user.
                    Toast.makeText(this, "${task.exception!!.message}", Toast.LENGTH_LONG).show()
                    // ...
                }


            }
    }
}