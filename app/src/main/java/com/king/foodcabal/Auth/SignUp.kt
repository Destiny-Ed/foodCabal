package com.king.foodcabal.Auth

import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.king.foodcabal.MainActivity
import com.king.foodcabal.R
import kotlinx.android.synthetic.main.activity_sign_up.*

class SignUp : AppCompatActivity() {

    private lateinit var email : EditText
    private lateinit var password : EditText
    private lateinit var _auth : FirebaseAuth

    private lateinit var progress : ProgressDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        email = findViewById(R.id.input_email)
        password = findViewById(R.id.input_password)

        progress = ProgressDialog(this)

        _auth = FirebaseAuth.getInstance()


        link_login.setOnClickListener {
            startActivity(Intent(this, Login::class.java))
        }

        btn_signup.setOnClickListener {
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
                signUp_auth(email.text.toString().trim(), password.text.toString().trim())

            }
        }
    }

    private fun signUp_auth(email: String, password: String) {
        progress.show()

        _auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener { task ->
            if (task.isSuccessful){
                progress.hide()
                startActivity(Intent(this, MainActivity::class.java))
                finish()
            }
            else {
                progress.hide()
                Toast.makeText(this, task.exception?.message, Toast.LENGTH_SHORT).show()
            }
        }
    }
}