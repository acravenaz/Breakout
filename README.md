# Breakout
****README****

This program is available to the public domain, and as such is distributed wholly without 
any license or copyright claim. You are free to  copy this code, modify it, use, distribute, 
and repackage it in any way you see fit. This project exists for purely educational purposes, 
in the hopes that it may be useful as a learning tool and a demonstration of basic proficiency 
in Java programming.

Breakout is a small Java game which implements the Breakout game from Assignment 3 of the 
freely available CS106A course on Stanford Engineering Everywhere: https://see.stanford.edu/Course/CS106A. 
Unlike the course which packages the ACM library in its project for convenience, 
this implementation completely eschews the ACM library, and uses java.awt directly for graphics rendering.

***TO RUN:

This repository comes with a compiled JAR version of the program pre-packaged in
/bin/ called Breakout.jar. This program does not have any dependencies outside of
a functional JRE, recommended version 7 or later (though older versions may also be
compatible). To run, copy to any desired directory and open.

***TO COMPILE:

This program was written in Eclipse and should contain all the necessary infrastructure
to be imported successfully into the workspace of any modern version of Eclipse.

To compile, create a default run configuration pointing the main class to Game. No other
configuration should be necessary outside of specifying which project the run configuration
is for. Then, go to File -> Export... and select Runnable JAR File. Pick your export
destination, select the "Package required libraries into generated JAR"
option under Library Handling, and click Finish.

In theory, this program should also compile with the standard java CL compiler, however
I have not tested this. Your mileage may vary.

Have fun!
