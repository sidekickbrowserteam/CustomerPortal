Customer Portal Android Template
================================

This is a minimal Android Studio Kotlin template that loads either a remote website or local HTML/CSS/JS files from `assets/www/`.

How to switch modes
- Open `app/src/main/java/com/example/customerportaltemplate/Config.kt` and set `USE_LOCAL_ASSETS` to `true` to load local files, or `false` to load the `REMOTE_URL`.

Local assets
- Place your `index.html` and other files under `app/src/main/assets/www/`. A sample `index.html`, `css/styles.css`, and `js/app.js` are included.

Ads
- Leave Ad Unit ID constants empty in `Config.kt` to disable ads. Fill them with your AdMob IDs to enable banner and interstitial ads.
- The interstitial interval is controlled by `INTERSTITIAL_INTERVAL_SECONDS`.

Permissions
- Internet permission is already added to `app/src/main/AndroidManifest.xml`.

Build
- Open the folder in Android Studio and build/run.
# CustomerPortal