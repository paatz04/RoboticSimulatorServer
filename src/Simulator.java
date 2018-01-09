//
// Android Praktikum
//
//	Robotic Arm Simulator
//
// 	V-REP connection script
//

import coppelia.IntW;
import coppelia.IntWA;
import coppelia.remoteApi;

// Make sure to have the server side running in V-REP:
// in a child script of a V-REP scene, add following command
// to be executed just once, at simulation start:
//
// simExtRemoteApiStart(19999)
//
// then start simulation, and run this program.
//
// IMPORTANT: for each successful call to simxStart, there
// should be a corresponding call to simxFinish at the end!

public class Simulator
{
    public static void main(String[] args)
    {
        System.out.println("Program started");
        remoteApi vrep = new remoteApi();
        vrep.simxFinish(-1); // just in case, close all opened connections
        int clientID = vrep.simxStart("127.0.0.1",19999,true,true,5000,5);

        if (clientID!=-1)
        {
            System.out.println("Connected to remote API server");

            WaitThread waitThread = new WaitThread(vrep, clientID);
            waitThread.run();
        }
        else {
            System.out.println("Failed connecting to remote API server");
        }

        System.out.println("Program ended");
    }
}
