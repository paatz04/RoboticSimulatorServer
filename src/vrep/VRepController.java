package vrep;

/* Make sure to have the server side running in V-REP:
 * in a child script of a V-REP scene, add following command
 * to be executed just once, at simulation start:
 *
 * simExtRemoteApiStart(19999)
 *
 * then start simulation, and run this program.
 *
 * IMPORTANT: for each successful call to simxStart, there
 * should be a corresponding call to simxFinish at the end!
 */
public class VRepController {
    public VRepController() {
        // ToDo
//        remoteApi vrep = new remoteApi();
//        vrep.simxFinish(-1); // just in case, close all opened connections
//        int clientID = vrep.simxStart("127.0.0.1",19999,true,true,5000,5);
//
//        if (clientID!=-1) {
//            null;
//        }else {
//            System.out.println("Failed connecting to remote API server");
//        }

        // Now retrieve streaming data (non-blocking):
            /*
            long startTime=System.currentTimeMillis();
            IntW mouseX = new IntW(0);
            vrep.simxGetIntegerParameter(clientID,vrep.sim_intparam_mouse_x,mouseX,vrep.simx_opmode_streaming); // Initialize streaming
            while (System.currentTimeMillis()-startTime < 5000)
            {
                int ret=vrep.simxGetIntegerParameter(clientID,vrep.sim_intparam_mouse_x,mouseX,vrep.simx_opmode_buffer); // Try to retrieve the streamed data
                if (ret==vrep.simx_return_ok) // After initialization of streaming, it will take a few ms before the first value arrives, so check the return code
                    System.out.format("Mouse position x: %d\n",mouseX.getValue()); // Mouse position x is actualized when the cursor is over V-REP's window
            }
            */

        // Now send some data to V-REP non-blocking:
//            vrep.simxAddStatusbarMessage(clientID,"Test: send simulator data successful",vrep.simx_opmode_oneshot);

        // Before closing the connection to V-REP, make sure that the last
        // command sent out had time to arrive.
//            IntW pingTime = new IntW(0);
//            vrep.simxGetPingTime(clientID,pingTime);

        // Now close the connection to V-REP:
//            vrep.simxFinish(clientID);
    }


}
