# Optimasi performa dan ukuran biner
-repackageclasses 'com.gwolf.tv'
-allowaccessmodification
-mergeinterfacesaggressively
-overloadaggressively

# Strip semua logging debug pada release APK
-assumenosideeffects class android.util.Log {
    public static boolean isLoggable(java.lang.String, int);
    public static int v(...);
    public static int d(...);
    public static int i(...);
}

# Keep essentials
-keep class com.gwolf.tvlauncher.ui.MainActivity
