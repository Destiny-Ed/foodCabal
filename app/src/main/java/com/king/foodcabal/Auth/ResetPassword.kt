package com.king.foodcabal.Auth

import android.app.AlertDialog
import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.king.foodcabal.R
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_reset_password.*

class ResetPassword : AppCompatActivity() {

    private lateinit var email : EditText
    private lateinit var progress : ProgressDialog
    private lateinit var auth : FirebaseAuth


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reset_password)

        email = findViewById(R.id.reset_email)
        progress = ProgressDialog(this)
        auth = FirebaseAuth.getInstance()


        link_remember.setOnClickListener {
            startActivity(Intent(this, Login::class.java))
        }

        btn_reset.setOnClickListener {
            if (email.text.toString().isEmpty()){
                email.error = "Email must not be empty"
                progress.hide()
                return@setOnClickListener
            }
            else if(!Patterns.EMAIL_ADDRESS.matcher(email.text.toString()).matches()){
                email.error = "Email is invalid"
                progress.hide()
                return@setOnClickListener
            }
            else {
                resetPassword(email.text.toString().trim())
            }
        }


    }

    private fun resetPassword(email: String) {
        progress.show()
        progress.setTitle("Please wait...")
        progress.setCancelable(false)
        progress.setCanceledOnTouchOutside(false)

        auth.sendPasswordResetEmail(email).addOnCompleteListener { task ->
            if (task.isSuccessful){
                //show alertDialog
                showAlert(task.result.toString())
            }
            else{
                progress.hide()
                Toast.makeText(this, task.exception!!.message, Toast.LENGTH_SHORT).show()
            }
        }

    }

    private fun showAlert(message: String) {
        val alert = AlertDialog.Builder(baseContext)
        alert.setTitle("Mail Sent")
        alert.setMessage(message)
        alert.setPositiveButton("Ok", null)
        alert.create().show()
    }
}