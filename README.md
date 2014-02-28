android-bootanimation-checker
=============================

Android bootanimation checker is an Android application to test bootanimations.

It tests bootanimation containing animation images and a descriptor text file with information about animation. These bootanimations
usually are located at /system/media/ or /data/local/ or /system/customize/resource/.

bootanimation.zip content:

- A description file (desc.txt) that outlines how the animation progresses, what images to use, image size etc…
- Folder(s) that contain the images for the animation

Example of bootanimation.zip (basic structure)

- desc.txt
- part0
- part1

part0 and part1 are directories that contain a series of images for example:

part0
- boot001.png
- boot002.png
- boot003.png
- boot004.png
- boot005.png

part1
- boot006.png
- boot007.png
- boot008.png
- boot009.png
- boot010.png

These images form the “part0” and “part1” animations that are combined as outlined in the “desc.txt” file to form the overall startup animation. The images are ordered by number and run in sequence.

The “desc.txt” file outlines how the animation progresses and a sample is as follows:

 480 800 20
 p 1 0 part0
 p 0 0 part1

 “480” is the width of the animation
 “800” is the height of the animation
 “20” is the desired fps of the animation

 “p” defines a first animation part
 “1” how many times this animation part loops (in this case - once)
 “0” defines a pause after this part is played (max 10, “0” is for no pause at all)
 “part0” is the folder name where the animation images are for the first animation part

 “p” defines a second animation part
 “0” defines that it loops forever (until android starts)
 “0” defines a pause
 “part1” is the folder for the second animation part.

-----------------------------

Application uncompress bootanimation.zip and build animation, looping it like system would do.

The procedure is:

- Build bootanimation.zip with boot animation (commented before)
- Store bootanimation.zip inside the sd card of the device
- Inside application, select it and animation will be built automatically and you will see it on full screen
