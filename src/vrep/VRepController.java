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

    private final static String BOX_COLOR_SIGNAL        = "boxColor";
    private final static String BOX_GRAB_SIGNAL         = "grab";
    private final static String COUNTER_RED_SIGNAL      = "countR";
    private final static String COUNTER_GREEN_SIGNAL    = "countG";
    private final static String COUNTER_BLUE_SIGNAL     = "countB";
    private final static String COUNTER_MISS_SIGNAL     = "countMiss";

    private remoteApi mVrep;
    private int mClientID;

    private FloatWA mInFloats = new FloatWA(1);
    private IntW mVrepSignal = new IntW(-1);

    private int mVrepSignalBoxColor = -1;
    private int mVrepSignalGrab = -1;
    private int mVrepSignalCounterR = -1;
    private int mVrepSignalCounterG = -1;
    private int mVrepSignalCounterB = -1;
    private int mVrepSignalCounterMiss = -1;

    public VRepController(VRepControllerCaller caller) {
        mCaller = caller;
        mVrep = new remoteApi();
    }

    public void stopVRepController() {
        mVrep.simxFinish(mClientID);
    }

    public void start() throws VRepControllerException {
        connectToServer();
        if (isConnectedToServer()) {
            System.out.println("Connected to V-REP API server...");
            openServerCommunication();
        } else {
            throw new VRepControllerException("Failed to connect to V-REP API server...");
        }
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

    private void openServerCommunication() {
        mVrep.simxGetIntegerSignal(mClientID, BOX_COLOR_SIGNAL     ,mVrepSignal,remoteApi.simx_opmode_streaming);
        mVrep.simxGetIntegerSignal(mClientID, BOX_GRAB_SIGNAL      ,mVrepSignal,remoteApi.simx_opmode_streaming);
        mVrep.simxGetIntegerSignal(mClientID, COUNTER_RED_SIGNAL   ,mVrepSignal,remoteApi.simx_opmode_streaming);
        mVrep.simxGetIntegerSignal(mClientID, COUNTER_GREEN_SIGNAL ,mVrepSignal,remoteApi.simx_opmode_streaming);
        mVrep.simxGetIntegerSignal(mClientID, COUNTER_BLUE_SIGNAL  ,mVrepSignal,remoteApi.simx_opmode_streaming);
        mVrep.simxGetIntegerSignal(mClientID, COUNTER_MISS_SIGNAL  ,mVrepSignal,remoteApi.simx_opmode_streaming);
    }

    /**
     * If this function is called, the speed of grabbing/releasing should be changed.
     * positive value = grabbing
     * negative value = releasing
     *
     * @param speed - speed grab
     */
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
        handleServerSignal(BOX_GRAB_SIGNAL);
        if (boxIsGrabbed())
            handleServerSignal(BOX_COLOR_SIGNAL);
        checkCounterSignals();
    }

    public void setSpeedTip(float speed) throws VRepControllerException {
        System.out.println("VREP.TIP\t" + speed);
        mInFloats.getArray()[0] = speed;
        int result=mVrep.simxCallScriptFunction(mClientID,SIMULATOR_SCENE_OBJECT, remoteApi.sim_scripttype_childscript,
                                                "setSpeedTip_function",null,mInFloats,
                                                null,null,null,null,null,
                                                null, remoteApi.simx_opmode_blocking);
        checkStatusCode(result, "setSpeedTip()");
        checkCounterSignals();
    }

    public void setSpeedBody(float speed) throws VRepControllerException {
        System.out.println("VREP.BODY\t" + speed);
        mInFloats.getArray()[0] = speed;
        int result=mVrep.simxCallScriptFunction(mClientID,SIMULATOR_SCENE_OBJECT, remoteApi.sim_scripttype_childscript,
                                                "setSpeedBody_function",null,mInFloats,
                                                null,null,null,null,null,
                                                null, remoteApi.simx_opmode_blocking);
        checkStatusCode(result, "setSpeedBody()");
        checkCounterSignals();
    }

    public void setSpeedRotation(float speed) throws VRepControllerException {
        System.out.println("VREP.ROTATION\t" + speed);
        mInFloats.getArray()[0] = speed;
        int result=mVrep.simxCallScriptFunction(mClientID,SIMULATOR_SCENE_OBJECT, remoteApi.sim_scripttype_childscript,
                                                "setSpeedRotation_function",null,mInFloats,
                                                null,null,null,null,null,
                                                null, remoteApi.simx_opmode_blocking);
        checkStatusCode(result, "setSpeedRotation()");
        checkCounterSignals();
    }

    private void checkStatusCode(int statusCode, String statusMessage) throws VRepControllerException {
        if (statusCode != remoteApi.simx_return_ok) {
            if (!isConnectedToServer()) {
                System.out.println("Connection to V-REP API server lost...");
                reconnectToServer();
                openServerCommunication();
            } else {
                throw new VRepControllerException(statusMessage + " remote function call failed...");
            }
        }
    }

    private void handleServerSignal(String serverSignalType) {

        mVrep.simxGetIntegerSignal(mClientID,serverSignalType,mVrepSignal,remoteApi.simx_opmode_buffer);

        if (mVrepSignal.getValue() == -1)
            return;

        if (serverSignalType.equals(BOX_COLOR_SIGNAL)) {
            mVrepSignalBoxColor = mVrepSignal.getValue();
            System.out.println("VREP.BOX_COLOR\t" + mVrepSignalBoxColor);
            mCaller.receivedDataFromVRep(new ReceivedDataVRep(RoboticSensorPart.COLOR_GRAB, mVrepSignalBoxColor));
        } else if (serverSignalType.equals(BOX_GRAB_SIGNAL)) {
            mVrepSignalGrab = mVrepSignal.getValue();
            System.out.println("VREP.BOX_GRAB\t" + mVrepSignalGrab);
        } else if (serverSignalType.equals(COUNTER_RED_SIGNAL) && mVrepSignalCounterR != mVrepSignal.getValue()) {
            mVrepSignalCounterR = mVrepSignal.getValue();
            System.out.println("VREP.COUNTER_RED\t" + mVrepSignalCounterR);
            mCaller.receivedDataFromVRep(new ReceivedDataVRep(RoboticSensorPart.SCORE_RED, mVrepSignalCounterR));
        } else if (serverSignalType.equals(COUNTER_GREEN_SIGNAL) && mVrepSignalCounterG != mVrepSignal.getValue()) {
            mVrepSignalCounterG = mVrepSignal.getValue();
            System.out.println("VREP.COUNTER_GREEN\t" + mVrepSignalCounterG);
            mCaller.receivedDataFromVRep(new ReceivedDataVRep(RoboticSensorPart.SCORE_GREEN, mVrepSignalCounterG));
        } else if (serverSignalType.equals(COUNTER_BLUE_SIGNAL) && mVrepSignalCounterB != mVrepSignal.getValue()) {
            mVrepSignalCounterB = mVrepSignal.getValue();
            System.out.println("VREP.COUNTER_BLUE\t" + mVrepSignalCounterB);
            mCaller.receivedDataFromVRep(new ReceivedDataVRep(RoboticSensorPart.SCORE_BLUE, mVrepSignalCounterB));
        } else if (serverSignalType.equals(COUNTER_MISS_SIGNAL) && mVrepSignalCounterMiss != mVrepSignal.getValue()) {
            mVrepSignalCounterMiss = mVrepSignal.getValue();
            System.out.println("VREP.COUNTER_MISS\t" + mVrepSignalCounterMiss);
            mCaller.receivedDataFromVRep(new ReceivedDataVRep(RoboticSensorPart.SCORE_MISSED, mVrepSignalCounterMiss));
        }
    }

    private void checkCounterSignals() {
        handleServerSignal(COUNTER_RED_SIGNAL);
        handleServerSignal(COUNTER_GREEN_SIGNAL);
        handleServerSignal(COUNTER_BLUE_SIGNAL);
        handleServerSignal(COUNTER_MISS_SIGNAL);
    }

    private boolean boxIsGrabbed() {
        return mVrepSignalGrab == 1;
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
