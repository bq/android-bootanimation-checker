android-bootanimation-checker
=============================

Android bootanimation checker is an Android application to test bootanimations.

It tests bootanimation containing animation images and a descriptor text file with information about animation. These bootanimations
usually are located at /system/media/ or /data/local/ or /system/customize/resource/.

Application uncompress bootanimation.zip and build animation, looping it like system would do.

The procedure is:

- Build bootanimation.zip with boot animation (parts folders with animation images and desc.txt with animation info)
- Store bootanimation.zip inside the sd card of the device
- Inside application, select it and animation will be built automatically and you will see it on full screen
