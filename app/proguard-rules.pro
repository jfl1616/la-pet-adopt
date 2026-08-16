# Add project specific ProGuard rules here.
# Kotlinx Serialization
-keepattributes *Annotation*, InnerClasses
-dontnote kotlinx.serialization.AnnotationsKt
-keepclassmembers class kotlinx.serialization.json.** {
    *** Companion;
}
-keepclasseswithmembers class kotlinx.serialization.json.** {
    kotlinx.serialization.KSerializer serializer(...);
}
-keep,includedescriptorclasses class com.joseapps.lapetadopt.**$$serializer { *; }
-keepclassmembers class com.joseapps.lapetadopt.** {
    *** Companion;
}
-keepclasseswithmembers class com.joseapps.lapetadopt.** {
    kotlinx.serialization.KSerializer serializer(...);
}
