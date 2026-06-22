package com.example.customerportaltemplate

import android.app.Activity
import android.content.Context
import android.os.SystemClock
import android.util.Log
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import com.google.android.gms.ads.MobileAds

/**
 * Small Ad manager that activates only when Ad Unit IDs are provided in Config.
 * If IDs are empty, nothing is loaded and no crashes occur.
 */
object Admob {
    private var interstitial: InterstitialAd? = null
    private var lastShownTimeMs: Long = 0
    private var enabled = false

    // Call from Activity onCreate() to initialize ads if IDs provided.
    fun initialize(context: Context) {
        val banner = Config.ADMOB_BANNER_UNIT_ID.trim()
        val inter = Config.ADMOB_INTERSTITIAL_UNIT_ID.trim()
        enabled = banner.isNotEmpty() || inter.isNotEmpty()
        if (!enabled) return

        // Initialize the Mobile Ads SDK at runtime (safe even if banner id is empty later).
        MobileAds.initialize(context) { initializationStatus ->
            Log.d("Admob", "Initialized: $initializationStatus")
        }
    }

    // Load a banner into the provided AdView. If banner ID is empty, the AdView will be hidden.
    fun setupBanner(adView: AdView, activity: Activity) {
        val bannerId = Config.ADMOB_BANNER_UNIT_ID.trim()
        if (bannerId.isEmpty()) {
            adView.visibility = android.view.View.GONE
            return
        }
        adView.adUnitId = bannerId
        adView.visibility = android.view.View.VISIBLE
        val adRequest = AdRequest.Builder().build()
        adView.loadAd(adRequest)
    }

    // Load interstitial in background if configured.
    fun loadInterstitial(context: Context) {
        val interId = Config.ADMOB_INTERSTITIAL_UNIT_ID.trim()
        if (interId.isEmpty()) return

        val adRequest = AdRequest.Builder().build()
        InterstitialAd.load(context, interId, adRequest, object : InterstitialAdLoadCallback() {
            override fun onAdLoaded(ad: InterstitialAd) {
                interstitial = ad
                interstitial?.fullScreenContentCallback = object : FullScreenContentCallback() {
                    override fun onAdDismissedFullScreenContent() {
                        interstitial = null
                        lastShownTimeMs = SystemClock.elapsedRealtime()
                        // Preload next interstitial
                        loadInterstitial(context)
                    }

                    override fun onAdFailedToShowFullScreenContent(adError: AdError) {
                        interstitial = null
                        Log.w("Admob", "Failed to show interstitial: $adError")
                    }
                }
            }

            override fun onAdFailedToLoad(loadAdError: LoadAdError) {
                interstitial = null
                Log.w("Admob", "Interstitial failed to load: ${loadAdError.message}")
            }
        })
    }

    // Show interstitial if loaded and interval has elapsed.
    fun tryShowInterstitial(activity: Activity) {
        val interval = Config.INTERSTITIAL_INTERVAL_SECONDS
        if (interval <= 0) return
        val now = SystemClock.elapsedRealtime()
        if (interstitial != null && (now - lastShownTimeMs) >= interval * 1000L) {
            interstitial?.show(activity)
        }
    }
}
