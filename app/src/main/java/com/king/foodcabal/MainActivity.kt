package com.king.foodcabal

import android.app.*
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkInfo
import android.net.Uri
import android.net.http.SslError
import android.os.AsyncTask
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.speech.tts.TextToSpeech
import android.system.Os
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.webkit.*
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.ads.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*
import kotlin.system.exitProcess


class MainActivity : AppCompatActivity() {

    //for alarm
    private var alarmMgr: AlarmManager? = null
    private lateinit var alarmIntent: PendingIntent

    private lateinit var webView : WebView

    //get the instance of adView
    private lateinit var adView : AdView
    private lateinit var mInterstitialAd: InterstitialAd

    private val bannerUnitId : String by lazy {

        "BANNER ADS UNIT ID"
//        "ca-app-pub-3940256099942544/6300978111"
    }

    private val appInstiatialUnitId : String by lazy {
        "iNTERTIAL UNIT ID"
//        "ca-app-pub-3940256099942544/1033173712"
    }

    lateinit var url : String
    lateinit var errorUrl : String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        var actionBar = supportActionBar
        actionBar!!.setDisplayHomeAsUpEnabled(true)


        //get adView instance here
        adView = findViewById(R.id.adView)
        webView = findViewById(R.id.webView)

        errorUrl = "file:///android_asset/error.html"
        url = "https://www.shoprite.com.ng/recipes.html"

        //initialize add here by calling the function
        initializeAds(bannerUnitId)

        //load banner function
        loadBannerAds()

        //for instialtial
        mInterstitialAd = InterstitialAd(this)

        initializeInterstitialAd(bannerUnitId)

        loadInterstitialAd(appInstiatialUnitId)




        //check if devices has network or not
//        val cm = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
//        val activeNetwork: NetworkInfo? = cm.activeNetworkInfo
//        val isConnected: Boolean = activeNetwork?.isConnectedOrConnecting == true
//        val isMetered = cm.isActiveNetworkMetered == true

        //ENABLE JAVASCRIPT TO LOAD IMAGES
        webView.loadUrl(url)
//        webView.settings.setAppCacheEnabled(true)
//
//        webView.settings.setAppCachePath(applicationContext.filesDir.absolutePath + "/cache")
        webView.settings.javaScriptEnabled = true
        var webViewClient = myWebView()
        webView.webViewClient = webViewClient


        //add on clicked listener to insterstitial ads here..
        runAdEvents()

        //call alarm
        showRecipeAlarm()
    }

    private fun runAdEvents() {
        mInterstitialAd.adListener = object : AdListener() {

            // If user clicks on the ad and then presses the back, s/he is directed to DetailActivity.
            override fun onAdClicked() {
                super.onAdOpened()
                mInterstitialAd.adListener.onAdClosed()
            }

            // If user closes the ad, s/he is directed to DetailActivity.
            override fun onAdClosed() {
//                startActivity(Intent(this@MainActivity, BabyRecipes::class.java))
//                finish()
                onAdClosed()

            }
        }
    }

    //insterstitial ads
    private fun loadInterstitialAd(interstitialAdUnitId: String) {

        mInterstitialAd.adUnitId = interstitialAdUnitId
        mInterstitialAd.loadAd(AdRequest.Builder().build())
    }

    private fun initializeInterstitialAd(mAppUnitId: String) {
        MobileAds.initialize(this, bannerUnitId)
    }

    //banner ads
    private fun initializeAds(appUnitId: String) {
        MobileAds.initialize(this, appUnitId)
    }
    //load banner ads
    private fun loadBannerAds() {
        var adRequest = AdRequest.Builder().build()
        adView.loadAd(adRequest)
    }

    inner class myWebView : WebViewClient() {

        var progress = ProgressDialog(this@MainActivity)
        override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
            progress.setMessage("Loading content... Please wait...")
            progress.show()
            progress.setCancelable(false)
            super.onPageStarted(view, url, favicon)
        }

        override fun onLoadResource(view: WebView?, url: String?) {
            view!!.loadUrl("javascript:(function() { " +
                    "var head = document.getElementsByClassName('header')[1].style.display='none'; " +
                    "})()")

            view!!.loadUrl("javascript:(function() { " +
                    "var head = document.getElementsByClassName('footer')[0].style.display='none'; " +
                    "})()")
            super.onLoadResource(view, url)
        }

        override fun onReceivedError(view: WebView?, errorCode: Int, description: String?, failingUrl: String?) {
            startActivity(Intent(this@MainActivity, error::class.java))
            finish()
            super.onReceivedError(view, errorCode, description, failingUrl)
        }

        override fun onPageFinished(view: WebView?, url: String?) {
            progress.dismiss()
            super.onPageFinished(view, url)
        }
    }

    //RETURN TO THE PREVIOUS PAGE IF USER CLICK ON THE BACK BUTTON ON THE ACTION BAR
    override fun onSupportNavigateUp(): Boolean {
        if (webView.canGoBack()){
            webView.goBack()
        }
        else{
            onBackPressed()
        }

        return super.onSupportNavigateUp()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_list, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.babyRecipe -> showInstatitialAds()
            R.id.share -> share()
            R.id.about -> startActivity(Intent(baseContext, about::class.java))
            R.id.rate -> RateApp()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun RateApp() {
        //rate app here
        try {
            //this will execute if user does not have playstore app enabled
            var uris =  Uri.parse("market://details?id=com.king.foodcabal")
            var intent = Intent(Intent.ACTION_VIEW, uris)
            startActivity(intent)
        }catch (e:Exception){
            //this will execute if user has the playStore app
            var uris = Uri.parse("https://play.google.com/store/apps/details?id=com.king.foodcabal")
            var intent = Intent(Intent.ACTION_VIEW, uris)
            startActivity(intent)
        }
    }

    private fun showInstatitialAds() {
        //check if instatitial ads is loaded else open baby recipe
        if (mInterstitialAd.isLoaded){
            mInterstitialAd.show()
        }
        else{
            startActivity(Intent(this, BabyRecipes::class.java))
        }
    }

    private fun share() {
        //Create a share intent
        var intent = Intent(Intent.ACTION_SEND)
        //create a type
        intent.type = "text/plain"

        //uri
        var url = "No one is born a great cook one learns by doing. Try this " +
                "wonderful meals...for every taste, budget and occasion" + " https://play.google.com/store/apps/details?id=com.king.foodcabal"
        //put url into the intent extra
        intent.putExtra(Intent.EXTRA_TEXT, url)

        //display share intent
        startActivity(Intent(Intent.createChooser(intent, "Share app via...")))
    }

    //show alarm to user every 8:30 in the morning
    private fun showRecipeAlarm(){
        //for alarm
        alarmMgr = getSystemService(Context.ALARM_SERVICE) as AlarmManager
        alarmIntent = Intent(this, AlarmBroadCast::class.java).let { intent ->
            PendingIntent.getBroadcast(this, 100, intent, PendingIntent.FLAG_ONE_SHOT)
        }

        // Set the alarm to start at approximately 7:30 p.m.
        val calendar: Calendar = Calendar.getInstance().apply {
            timeInMillis = System.currentTimeMillis()
            set(Calendar.HOUR_OF_DAY, 8)
            set(Calendar.MINUTE, 0)
        }


// With setInexactRepeating(), you have to use one of the AlarmManager interval
// constants--in this case, AlarmManager.INTERVAL_DAY.
        alarmMgr?.setInexactRepeating(
            AlarmManager.RTC_WAKEUP,
            calendar.timeInMillis,
            AlarmManager.INTERVAL_DAY,
            alarmIntent
        )

    }


    override fun onBackPressed() {
        if (mInterstitialAd.isLoaded){
            mInterstitialAd.show()
            Toast.makeText(baseContext, "Press again to exit", Toast.LENGTH_LONG).show()
        }
        else {
            this.finish()
        }
    }


}


//ends
//Destiny created this app
