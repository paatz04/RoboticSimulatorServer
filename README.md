# RoboticSimulatorServer

# Description
This is the Simulator Server, used to controll the communication between the App and the V-REP simulation. The Server only works on devices that support Bluetooth and on which runs the V-REP simulation.

# Installation
Make sure you have following files in your directory, in order to run the various examples:

1. folder "coppelia"
3. the appropriate remote API library: "remoteApiJava.dll" (Windows), "libremoteApi.dylib" (Mac) or "libremoteApi.so" (Linux)
4. simpleTest.java (or any other example program)

You might also have to add the folder to the system path. In Linux for instance, you could call:

export LD_LIBRARY_PATH=$LD_LIBRARY_PATH:`pwd`

before executing simpleTest.

# Credits
Patric Corletto, Hannes Oberprantache, Jose Acevedo

Sebastian Eckl

# Wiki
https://github.com/paatz04/RoboticSimulatorServer/wiki




