package com.king.foodcabal

import android.app.AlertDialog
import android.app.ProgressDialog
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.net.http.SslError
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.webkit.*
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.ads.*
import com.google.firebase.auth.FirebaseAuth

class BabyRecipes : AppCompatActivity() {

    lateinit var adView: AdView
    lateinit var webView : WebView
    private lateinit var mInterstitialAd: InterstitialAd

    private val bannerUnitId : String by lazy {

            "BANNER UNIT ID"
        //test ads_banner_unit
//        "ca-app-pub-3940256099942544/6300978111"

    }
    private val appInstiatialUnitId : String by lazy {
        "instertitial unit id"
        //test ads_banner_unit
//        "ca-app-pub-3940256099942544/1033173712"
    }

    lateinit var babyUrl : String
    lateinit var errorUrl : String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_baby_recipes)


        //support action back button
        var actionBar = supportActionBar
        actionBar!!.setDisplayHomeAsUpEnabled(true)

        //instantiate variables
        adView = findViewById(R.id.adViewBaby)
        webView = findViewById(R.id.babyWebView)

        babyUrl = "https://www.shoprite.com.ng/baby/baby-recipes.html"
        errorUrl = "file:///android_asset/error.html"

        //initialize add here by calling the function
        initializeAdd(bannerUnitId)
//
//        //load banner function
        loadBanner()

        //for instialtial
        mInterstitialAd = InterstitialAd(this)

        initializeInterstitialAd(bannerUnitId)

        loadInterstitialAd(appInstiatialUnitId)


        //LOAD URL AND SET WEBVIEW PROPERTIES
        webView.loadUrl(babyUrl)
        //enable javascript
        webView.settings.javaScriptEnabled = true

        webView.settings.setAppCacheEnabled(true)
        webView.settings.setAppCachePath(applicationContext.filesDir.absolutePath + "/cache")
        webView.settings.domStorageEnabled = true
        // Set cache size to 8 mb by default. should be more than enough
        webView.settings.setAppCacheMaxSize(1024*8)
        // This next one is crazy. It's the DEFAULT location for your app's cache
        // But it didn't work for me without this line
        //webView.settings.setAppCachePath("/data/data/" + packageName + "/cache")
        webView.settings.allowFileAccess = true

        //get the instance of the webViewClient to handle how the page should load
        var webViewClient = myViewClient()
        webView.webViewClient = webViewClient





        runAdEvents()

    }
    //adListener for instialtitial
    private fun runAdEvents() {
        mInterstitialAd.adListener = object : AdListener() {

            // If user clicks on the ad and then presses the back, s/he is directed to DetailActivity.
            override fun onAdClicked() {
                super.onAdOpened()
                mInterstitialAd.adListener.onAdClosed()
            }

            // If user closes the ad, s/he is directed to DetailActivity.
            override fun onAdClosed() {
                startActivity(Intent(this@BabyRecipes, MainActivity::class.java))
                finish()
            }
        }
    }

    //insterstitial ads
    private fun loadInterstitialAd(interstitialAdUnitId: String) {

        mInterstitialAd.adUnitId = interstitialAdUnitId
        mInterstitialAd.loadAd(AdRequest.Builder().build())
    }

    private fun initializeInterstitialAd(appUnitId: String) {
        MobileAds.initialize(this, bannerUnitId)
    }

    //banner function
    private fun loadBanner() {
        var adRequest = AdRequest.Builder().build()
        adView.loadAd(adRequest)
    }

    private fun initializeAdd(appUnitId: String) {
        MobileAds.initialize(this, appUnitId)
    }
    //ends

    inner class myViewClient : WebViewClient() {
        var progress = ProgressDialog(this@BabyRecipes)


        override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
            progress.setMessage("Loading content... Please wait...")
            progress.setCancelable(false)
            progress.show()

            super.onPageStarted(view, url, favicon)
        }

        override fun onReceivedError(view: WebView?, errorCode: Int, description: String?, failingUrl: String?) {
            startActivity(Intent(this@BabyRecipes, error::class.java))
            finish()
            super.onReceivedError(view, errorCode, description, failingUrl)
        }

        override fun onLoadResource(view: WebView?, url: String?) {

            view!!.loadUrl("javascript:(function() { " +
                    "var head = document.getElementsByClassName('header')[1].style.display='none'; " +
                    "})()")

            view!!.loadUrl("javascript:(function() { " +
                    "var head = document.getElementsByClassName('footer')[0].style.display='none'; " +
                    "})()")

            view!!.loadUrl("javascript:(function() { " +
                    "var head = document.getElementsByClassName('breadcrumbs__item')[0].style.display='none'; " +
                    "})()")

            view!!.loadUrl("javascript:(function() { " +
                    "var head = document.getElementsByClassName('type--image__image')[0].style.display='none'; " +
                    "})()")
//            var img = "file:///android_res/drawable/babyeating.jpg"
//
//            view!!.loadUrl("javascript:(function() { " +
//                    "var head = document.querySelectorAll('img')[3].src = '$img'; " +
//                    "})()")


            view!!.loadUrl("javascript:(function() { " +
                    "var head = document.getElementsByClassName('customText');" +
                    "head[0].innerHTML = 'FoodCabal. We love Babies' " +
                    "})()")


            super.onLoadResource(view, url)
        }



        //TExt id for admob
        //ca-app-pub-3940256099942544/6300978111 //for banner
        //ca-app-pub-3940256099942544/1033173712 //for instatitials
        override fun onPageFinished(view: WebView?, url: String?) {
            progress.dismiss()
            super.onPageFinished(view, url)
        }

    }

    //Mobile ads function :-only for adMob
//    private fun initializeAdd(appUnitId: String) {
//        MobileAds.initialize(this, appUnitId)
//
//    }
    //LoadBanner ads :- only for adMob
//    private fun loadBanner() {
//        var adRequest = AdRequest.Builder().build()
//        //show ad
//        adView.loadAd(adRequest)
//    }

    //for action bar back arrow icon
    override fun onSupportNavigateUp(): Boolean {
        //Make action back arrow function to go to the previous page when clicked
        if(webView.canGoBack()){
            webView.goBack()
        }
        else{
            onBackPressed()
        }
        return super.onSupportNavigateUp()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.baby_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.allRecipe -> showInstatitialAds()
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

    //show instatitialAds
    private fun showInstatitialAds() {
        //check if instatitial ads is loaded else open baby recipe
        if (mInterstitialAd.isLoaded){
            mInterstitialAd.show()
        }
        else{
            startActivity(Intent(this, MainActivity::class.java))
            finish()
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

    override fun onBackPressed() {
        if (mInterstitialAd.isLoaded){
            mInterstitialAd.show()
        }
        else {
            startActivity(Intent(baseContext, MainActivity::class.java))
            finish()
        }
    }

}
