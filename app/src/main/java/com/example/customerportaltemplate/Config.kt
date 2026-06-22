package com.example.customerportaltemplate

/**
 * Configuration file for the template.
 * Edit only the constants below to switch modes and configure ads.
 */
object Config {
    // MODE: set to true to load local files from assets/www/, false to load a remote URL.
    // Replace this single boolean to switch between Mode 1 (remote URL) and Mode 2 (local assets).
    const val USE_LOCAL_ASSETS: Boolean = false // <-- Set to true to load from assets/www/index.html

    // REMOTE URL: when USE_LOCAL_ASSETS is false, replace this with the live website URL you want to load.
    const val REMOTE_URL: String = "https://hoabl--uat.sandbox.my.site.com/customerPortal" // <-- Replace with your website URL

    // LOCAL ASSETS ENTRY: when USE_LOCAL_ASSETS is true, the WebView will load this path inside assets.
    // Keep the folder structure exactly as assets/www/index.html or change this constant accordingly.
    const val LOCAL_INDEX_PATH: String = "file:///android_asset/www/index.html" // <-- Replace if using a different file

    // AdMob configuration: leave IDs empty to disable ads completely (no crashes).
    // Provide your Banner Ad Unit ID here to enable the bottom banner.
    const val ADMOB_BANNER_UNIT_ID: String = "" // <-- Put your banner Ad Unit ID here (e.g. ca-app-pub-xxxxxxxx/yyyyyyyy)

    // Provide your Interstitial Ad Unit ID here to enable interstitials.
    const val ADMOB_INTERSTITIAL_UNIT_ID: String = "" // <-- Put your interstitial Ad Unit ID here

    // Interval for showing interstitials in seconds. Set to 0 to disable automatic interstitials.
    const val INTERSTITIAL_INTERVAL_SECONDS: Long = 120 // <-- Change to desired interval in seconds
}
