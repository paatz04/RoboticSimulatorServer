//
// Android Praktikum
//
//	Robotic Arm Simulator
//
// 	V-REP process commands script
//

import coppelia.IntW;
import coppelia.IntWA;
import coppelia.remoteApi;

import java.io.InputStream;
import javax.microedition.io.StreamConnection;

public class ProcessConnectionThread implements Runnable {

    private StreamConnection mConnection;
    private remoteApi vrep;
    private int clientID;

    // Constant that indicate command from devices
    private static final int EXIT_CMD = -1;
    private static final int CMD_1 = 1;
    private static final int CMD_2 = 2;

    public ProcessConnectionThread(StreamConnection connection, remoteApi vrep, int clientID) {
        mConnection = connection;
        this.vrep = vrep;
        this.clientID = clientID;
    }

    @Override
    public void run() {
        try {
            // prepare to receive data
            InputStream inputStream = mConnection.openInputStream();

            System.out.println("waiting for input");

            // Now retrieve streaming data (non-blocking):
            long startTime=System.currentTimeMillis();
            IntW mouseX = new IntW(0);
            vrep.simxGetIntegerParameter(clientID,vrep.sim_intparam_mouse_x,mouseX,vrep.simx_opmode_streaming); // Initialize streaming
            while (System.currentTimeMillis()-startTime < 5000)
            {
                int ret=vrep.simxGetIntegerParameter(clientID,vrep.sim_intparam_mouse_x,mouseX,vrep.simx_opmode_buffer); // Try to retrieve the streamed data
                if (ret==vrep.simx_return_ok) // After initialization of streaming, it will take a few ms before the first value arrives, so check the return code
                    System.out.format("Mouse position x: %d\n",mouseX.getValue()); // Mouse position x is actualized when the cursor is over V-REP's window
            }

            // Now send some data to V-REP in a non-blocking fashion:
            vrep.simxAddStatusbarMessage(clientID,"Test: send simulator data successful",vrep.simx_opmode_oneshot);

            // Before closing the connection to V-REP, make sure that the last
            // command sent out had time to arrive.
            IntW pingTime = new IntW(0);
            vrep.simxGetPingTime(clientID,pingTime);

            // Now close the connection to V-REP:
            vrep.simxFinish(clientID);


            while (true) {
                int command = inputStream.read();

                if (command == EXIT_CMD) {
                    System.out.println("finish process");
                    break;
                }
                processCommand(command);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Process the command from client
     * @param command the command code
     */
    private void processCommand(int command) {
        try {
            switch (command) {
                case CMD_1:
                    System.out.println("Received 1");
                    break;
                case CMD_2:
                    System.out.println("Received 2");
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
