# Default ProGuard rules for Android

# Keep Room entities
-keep class com.poultry.broiler.data.local.entity.** { *; }

# Keep Hilt generated classes
-keep class dagger.hilt.** { *; }
-keep class javax.inject.** { *; }
-keep class * extends dagger.hilt.android.internal.managers.ViewComponentManager$FragmentContextWrapper { *; }

# Keep kotlinx.serialization
-keepattributes *Annotation*, InnerClasses
-dontnote kotlinx.serialization.AnnotationsKt

-keepclassmembers class kotlinx.serialization.json.** {
    *** Companion;
}
-keepclasseswithmembers class kotlinx.serialization.json.** {
    kotlinx.serialization.KSerializer serializer(...);
}
-keep,includedescriptorclasses class com.poultry.broiler.**$$serializer { *; }
-keepclassmembers class com.poultry.broiler.** {
    *** Companion;
}
-keepclasseswithmembers class com.poultry.broiler.** {
    kotlinx.serialization.KSerializer serializer(...);
}
