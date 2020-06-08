package com.king.foodcabal

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.speech.tts.Voice
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.coordinatorlayout.widget.CoordinatorLayout
import com.google.android.material.snackbar.Snackbar
import java.sql.ClientInfoStatus
import java.util.*

class about : AppCompatActivity() {

    lateinit var layout : CoordinatorLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_about)

        //instantiate coordinator layout as a global variable
        layout = findViewById(R.id.layoutSnack)

        //enable action bar
        var actionBar = supportActionBar

        //set back arrow button to action bar
        actionBar!!.setDisplayHomeAsUpEnabled(true)

//        //make the splash screen logo to bounce
//        //get the image reference
        var image = findViewById<ImageView>(R.id.splashImage)
//        //create animation Utils
        var animation = AnimationUtils.loadAnimation(this, R.anim.bounce_logo)
//        //start animation
        image.startAnimation(animation)



    }


    //now enable the back arrow button to go back

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }

    fun showSnackBar(v: View){
        Snackbar.make(layout, "dikeochaeze@gmail.com", Snackbar.LENGTH_LONG).show()
    }


}
