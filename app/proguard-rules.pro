# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

# Uncomment this to preserve the line number information for
# debugging stack traces.
#-keepattributes SourceFile,LineNumberTable

# If you keep the line number information, uncomment this to
# hide the original source file name.
#-renamesourcefileattribute SourceFile

# Keep Gson classes from obfuscation
-keep class com.google.gson.** { *; }

-keep class com.google.gson.reflect.TypeToken
-keep class * extends com.google.gson.reflect.TypeToken
-keep public class * implements java.lang.reflect.Type

# Keep attributes for Gson's SerializedName annotations
-keepclassmembers class * {
    @com.google.gson.annotations.SerializedName <fields>;
}

-keepclassmembers class * {
    public <init>(...);
}

# Keep all classes in the models package from obfuscation
-keep class com.moutamid.trip4pet.models.** { *; }

# Keep the Cities class and its inner classes from obfuscation
-keep class com.moutamid.trip4pet.models.Cities { *; }
-keep class com.moutamid.trip4pet.models.Cities$* { *; }