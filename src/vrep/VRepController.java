package vrep;

import coppelia.*;
import simulator.ReceivedDataVRep;
import simulator.RoboticSensorPart;
import transfer.TransferredColors;

/*
 * This class should communicate with the vRep and control the vRep. With this class an external class should be able
 * to move the robotic arm, with using only the functions of this class.
 */
public class VRepController {
    private final static String SIMULATOR_SCENE_OBJECT = "IRB140";
    private final static Float GRAB_SPEED_SCALE_FACTOR = 5f;
    private final static int MILLISECONDS_UNTIL_NEXT_RECONNECT_TRY = 2000;

    private VRepControllerCaller mCaller;

    private remoteApi mVrep;
    private int mClientID;

    private FloatWA mInFloats = new FloatWA(1);


    public VRepController(VRepControllerCaller caller) {
        mCaller = caller;
        mVrep = new remoteApi();
    }

    public void stopVRepController() {
        mVrep.simxFinish(mClientID);
    }

    public void start() throws VRepControllerException {
        connectToServer();
        if (isConnectedToServer())
            System.out.println("Connected to V-REP API server...");
        else
            throw new VRepControllerException("Failed to connect to V-REP API server...");
    }

    private void connectToServer() {
        mVrep.simxFinish(-1);
        mClientID = mVrep.simxStart("127.0.0.1",19999,true,
                                    true,5000,5);
    }

    private boolean isConnectedToServer() {
        return mVrep.simxGetConnectionId(mClientID) != -1;
    }

    private void reconnectToServer() {
        while (!isConnectedToServer()) {
            sleep(MILLISECONDS_UNTIL_NEXT_RECONNECT_TRY);
            System.out.println("Trying to reconnect to V-REP API server...");
            connectToServer();
        }
        System.out.println("Connected to V-REP API server...");
    }

    public void setSpeedGrab(float speed) throws VRepControllerException {
        // Scale speed
        speed = speed * GRAB_SPEED_SCALE_FACTOR;
        System.out.println("VREP.GRAB\t" + speed);
        mInFloats.getArray()[0] = speed;
        int result = mVrep.simxCallScriptFunction(mClientID,SIMULATOR_SCENE_OBJECT, remoteApi.sim_scripttype_childscript,
                                                "setSpeedGrab_function",null,mInFloats,
                                                null,null,null,null,null,
                                                null, remoteApi.simx_opmode_blocking);
        checkStatusCode(result, "setSpeedGrab()");
    }

    public void setSpeedTip(float speed) throws VRepControllerException {
        System.out.println("VREP.TIP\t" + speed);
        mInFloats.getArray()[0] = speed;
        int result=mVrep.simxCallScriptFunction(mClientID,SIMULATOR_SCENE_OBJECT, remoteApi.sim_scripttype_childscript,
                                                "setSpeedTip_function",null,mInFloats,
                                                null,null,null,null,null,
                                                null, remoteApi.simx_opmode_blocking);
        checkStatusCode(result, "setSpeedTip()");
    }

    public void setSpeedBody(float speed) throws VRepControllerException {
        System.out.println("VREP.BODY\t" + speed);
        mInFloats.getArray()[0] = speed;
        int result=mVrep.simxCallScriptFunction(mClientID,SIMULATOR_SCENE_OBJECT, remoteApi.sim_scripttype_childscript,
                                                "setSpeedBody_function",null,mInFloats,
                                                null,null,null,null,null,
                                                null, remoteApi.simx_opmode_blocking);
        checkStatusCode(result, "setSpeedBody()");
    }

    public void setSpeedRotation(float speed) throws VRepControllerException {
        System.out.println("VREP.ROTATION\t" + speed);
        mInFloats.getArray()[0] = speed;
        int result=mVrep.simxCallScriptFunction(mClientID,SIMULATOR_SCENE_OBJECT, remoteApi.sim_scripttype_childscript,
                                                "setSpeedRotation_function",null,mInFloats,
                                                null,null,null,null,null,
                                                null, remoteApi.simx_opmode_blocking);
        checkStatusCode(result, "setSpeedRotation()");
    }

    private void checkStatusCode(int statusCode, String statusMessage) throws VRepControllerException {
        if (statusCode != remoteApi.simx_return_ok) {
            if (!isConnectedToServer()) {
                System.out.println("Connection to V-REP API server lost...");
                reconnectToServer();
            } else {
                // ToDo: this throws an error when we are connected to the server or???
                throw new VRepControllerException(statusMessage + " remote function call failed...");
            }
        }
    }

    private static void sleep(int milliseconds) {
        try {
            Thread.sleep(milliseconds);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void sendSimulationDataToCaller() {
        // ToDo
        mCaller.receivedDataFromVRep(new ReceivedDataVRep(RoboticSensorPart.COLOR_GRAB, TransferredColors.RED));
        mCaller.receivedDataFromVRep(new ReceivedDataVRep(RoboticSensorPart.SCORE_BLUE, 3));
    }
}
