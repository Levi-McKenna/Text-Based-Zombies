# Text-Based-Zombies

## An in-terminal game application based off the hit games Call of Duty: Black Ops I | II | III Zombies!

This project was made as my final skills project for my intro to programming
class. It was quite rushed, so there are probably quite a few concurrency bugs.
I should also mention that, despite a lack of features, I don't currently see
myself revisiting this project. I may try to rewrite some parts of the
application to be more single-threaded oriented because it's what I had
originally planned for the game though. The only current release of the game has
only two levels: my test files. But, the current source code houses 4.

In the game you can currently do the following:

* Endlessly shoot zombies with a hit scan mechanic
* Buy various doors that lead to other level rooms
* Buy three varieties of weapons: M14, OLYMPIA, and PISTOL
* Gather points from shooting and defeating zombies 
* Increase your max health and heal your player with one perk scattered on the
map

## How to compile the project on your own

>Make sure to have <a
href="https://www.oracle.com/java/technologies/downloads/">JDK</a>23 or greater
installed)

This project was made with the help of the *Maven* build dependency and
can be packaged using it.
<a href="https://maven.apache.org/download.cgi">Navigate here to install *Maven*</a>

Navigate to the root of the project and run `mvn package` and `java -cp target/TBZ-1.0.jar TBZ.App`.

## Controls

* WASD for movement
* SPACE for shooting 
* R for reloading
* E for interacting
