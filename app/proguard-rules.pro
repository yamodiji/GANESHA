# Add project specific ProGuard rules here.

# Keep Compose runtime
-keep class androidx.compose.** { *; }
-dontwarn androidx.compose.**

# Keep ViewModels
-keep class * extends androidx.lifecycle.ViewModel

# Keep data classes
-keep class com.appdrawer.app.model.** { *; }

# Keep PackageManager related classes
-keep class android.content.pm.** { *; }

# Keep reflection for serialization
-keepattributes *Annotation*
-keepclassmembers class ** {
    @androidx.compose.runtime.Composable *;
}

# Common Android optimizations
-optimizationpasses 5
-dontusemixedcaseclassnames
-dontskipnonpubliclibraryclasses
-verbose 