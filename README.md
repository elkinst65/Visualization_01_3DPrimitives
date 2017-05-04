# Visualization_01_3DPrimitives
Assignment 1

For this project, I took a set of data from an air defense exercise in 2012.  There are 7 different sources of data that I "normalized" into simpler formats:
1. CAP1 = GPS data from a Civil Air Patrol Cessna 182 acting as a hostile
2. CAP2 = GPS data from a Civil Air Patrol Cessna 172 acting as a hostile
3. DRP10 = NORAD Air Defense Tactical Data Link - this is what NORAD sees
4. DRP12 = A sensor we were testing (details classified)
5. DRP14 = A man-portable surveillance system (details classified)
6. DRP15 = FAA radar
7. DRP06 = Army tactical radar (details classified)

* The dates and times were converted to offsets from a common starting time to hide the details of the exercise.
* The positional data were transformed to X, Y, Z, offsets from a common location (the "bullseye" for the exercise) to hide the details of the exercise and to make plotting easier.
* The "ground" is simply a set of connected vertices to represent the operating area and to give some context to the data
* The "bullseye" is a dark red sphere placed at the center.
* The CAP birds are green and cyan - they take off from an airfield southeast of the bullseye and fly around.  You will see them circling at times - this is to hold them off until a particular time, at which point they will both make attack runs at the bullseye.  Some runs are from opposing angles, others are similar angles but different heights, etc.
  * The CAP data shows a trailing curved line - this shows the history of the previous 150 seconds so you can get a feel for the flight paths
  * There are altitude sticks that appear below the lead point, and every 100 points trailing, so you can get a feel for the altitude profiles
* The radar data are transparent spheres - each sphere is a separate target as perceived by that sensor.  This will give you a feel for how crowded the airspace was where we tested.
* You can zoom into/out of the center by scrolling the mouse wheel.
* You can raise/lower your viewing elevation angle by dragging the mouse up/down with the left mouse button
* You can display all radar data simultaneously (the default) by pressing the keypad "0" key
* You can display sensor 1 (DRP10) only by pressing the keypad "1" key
* You can display sensor 2 (DRP12) only by pressing the keypad "2" key
* You can display sensor 3 (DRP14) only by pressing the keypad "3" key
* You can display sensor 4 (DRP15) only by pressing the keypad "4" key
* You can display sensor 5 (DRP06) only by pressing the keypad "5" key

## Areas for Improvement/Expansion
* I tried breaking out the various classes into their own .java files, but Eclipse/Java barfed.  This works in Processing, though.
* Add a key for resetting the playback
* Add mouse-drag to change the horizontal viewing angle
