package com.king.foodcabal


import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.ImageView

class splash_screen : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        supportActionBar!!.hide()

        //time to wait
        var timer :Long = 3000
        //start main Activity

        Handler().postDelayed({
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }, timer)

        //make the splashImage to bounce
        //get the image reference
//        var image = findViewById<ImageView>(R.id.splashImage)
//        //create animation
//        var animation = AnimationUtils.loadAnimation(this, R.anim.bounce_logo)
//        //set the animation
//        image.startAnimation(animation)

    }

}

