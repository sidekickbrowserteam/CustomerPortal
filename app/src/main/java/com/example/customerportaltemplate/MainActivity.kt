package com.example.customerportaltemplate

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.net.http.SslError
import android.os.Bundle
import android.view.View
import android.webkit.SslErrorHandler
import android.webkit.WebChromeClient
import android.webkit.WebResourceRequest
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.google.android.gms.ads.AdView

/**
 * Minimal Activity that supports two modes (remote URL or local assets), pull-to-refresh,
 * a thin progress bar, and optional AdMob banner + interstitial.
 *
 * Edit only `Config.kt` to switch modes and configure AdMob.
 */
class MainActivity : AppCompatActivity() {
    private lateinit var webView: WebView
    private lateinit var swipeRefresh: SwipeRefreshLayout
    private lateinit var progressBar: View
    private lateinit var adView: AdView

    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        webView = findViewById(R.id.webView)
        swipeRefresh = findViewById(R.id.swipeRefresh)
        progressBar = findViewById(R.id.progressBar)
        adView = findViewById(R.id.adView)

        // Initialize Admob only if IDs are provided
        Admob.initialize(this)
        Admob.setupBanner(adView, this)
        Admob.loadInterstitial(this)

        // WebView settings: enable JavaScript and local file access.
        val ws: WebSettings = webView.settings
        ws.javaScriptEnabled = true // Required for many sites; disable if not needed
        ws.domStorageEnabled = true
        ws.allowFileAccess = true
        ws.allowContentAccess = true

        // WebView client to handle navigation inside the WebView
        webView.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(view: WebView?, request: WebResourceRequest?): Boolean {
                return false // Let WebView handle the URL
            }

            override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                progressBar.visibility = View.VISIBLE
                swipeRefresh.isRefreshing = false
            }

            override fun onPageFinished(view: WebView?, url: String?) {
                progressBar.visibility = View.GONE
                swipeRefresh.isRefreshing = false
                // Try to show interstitial based on configured interval
                Admob.tryShowInterstitial(this@MainActivity)
            }

            override fun onReceivedSslError(view: WebView?, handler: SslErrorHandler?, error: SslError?) {
                // For templates, we'll proceed on SSL errors to avoid blocking local testing.
                // In production, handle SSL errors properly.
                handler?.proceed()
            }
        }

        // WebChromeClient to update thin progress bar like Chrome
        webView.webChromeClient = object : WebChromeClient() {
            override fun onProgressChanged(view: WebView?, newProgress: Int) {
                progressBar.visibility = if (newProgress < 100) View.VISIBLE else View.GONE
                if (progressBar is android.widget.ProgressBar) {
                    (progressBar as android.widget.ProgressBar).progress = newProgress
                }
            }
        }

        // Pull-to-refresh reloads the current page
        swipeRefresh.setOnRefreshListener {
            webView.reload()
        }

        // Load either local assets or remote URL based on Config
        if (Config.USE_LOCAL_ASSETS) {
            // Mode 2: local assets. Put your files under app/src/main/assets/www/
            webView.loadUrl(Config.LOCAL_INDEX_PATH)
        } else {
            // Mode 1: remote URL. Replace REMOTE_URL in Config.kt with your target URL.
            webView.loadUrl(Config.REMOTE_URL)
        }
    }

    override fun onBackPressed() {
        if (webView.canGoBack()) {
            webView.goBack()
        } else {
            super.onBackPressed()
        }
    }
}
