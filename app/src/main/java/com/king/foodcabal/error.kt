package com.king.foodcabal


import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout

class error : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_error)

        val reloadPage = findViewById<SwipeRefreshLayout>(R.id.reloadPage)
        reloadPage.setOnRefreshListener {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }


    }

    fun reload(view: View) {
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }
}
