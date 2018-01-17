package vrep;

import coppelia.*;
import org.jetbrains.annotations.Contract;
import simulator.received.data.ReceivedSimulatorData;
import simulator.received.data.RoboticSensorPart;

public class VRepController {
    private final static String SIMULATOR_SCENE_OBJECT = "IRB140";
    private final static Float GRAB_SPEED_SCALE_FACTOR = 5f;
    private final static int MILLISECONDS_UNTIL_NEXT_RECONNECT_TRY = 2000;

    private final static String BOX_COLOR_SIGNAL        = "boxColor";
    private final static String BOX_GRAB_SIGNAL         = "grab";
    private final static String COUNTER_RED_SIGNAL      = "countR";
    private final static String COUNTER_GREEN_SIGNAL    = "countG";
    private final static String COUNTER_BLUE_SIGNAL     = "countB";
    private final static String COUNTER_MISS_SIGNAL     = "countMiss";

    private VRepControllerCaller mCaller;

    private remoteApi mVrep;
    private int mClientID;

    private FloatWA mInFloats = new FloatWA(1);
    private IntW mVrepSignal = new IntW(-1);

    private int mVrepSignalGrab = -1;
    private int mVrepSignalCounterRed = -1;
    private int mVrepSignalCounterGreen = -1;
    private int mVrepSignalCounterBlue = -1;
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
        mClientID = mVrep.simxStart("127.0.0.1",19999,true,true,5000,5);
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

    public void setSpeedGrab(float speed) {
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

    public void setSpeedTip(float speed) {
        System.out.println("VREP.TIP\t" + speed);
        mInFloats.getArray()[0] = speed;
        int result=mVrep.simxCallScriptFunction(mClientID,SIMULATOR_SCENE_OBJECT, remoteApi.sim_scripttype_childscript,
                                                "setSpeedTip_function",null,mInFloats,
                                                null,null,null,null,null,
                                                null, remoteApi.simx_opmode_blocking);
        checkStatusCode(result, "setSpeedTip()");
        checkCounterSignals();
    }

    public void setSpeedBody(float speed) {
        System.out.println("VREP.BODY\t" + speed);
        mInFloats.getArray()[0] = speed;
        int result=mVrep.simxCallScriptFunction(mClientID,SIMULATOR_SCENE_OBJECT, remoteApi.sim_scripttype_childscript,
                                                "setSpeedBody_function",null,mInFloats,
                                                null,null,null,null,null,
                                                null, remoteApi.simx_opmode_blocking);
        checkStatusCode(result, "setSpeedBody()");
        checkCounterSignals();
    }

    public void setSpeedRotation(float speed) {
        System.out.println("VREP.ROTATION\t" + speed);
        mInFloats.getArray()[0] = speed;
        int result=mVrep.simxCallScriptFunction(mClientID,SIMULATOR_SCENE_OBJECT, remoteApi.sim_scripttype_childscript,
                                                "setSpeedRotation_function",null,mInFloats,
                                                null,null,null,null,null,
                                                null, remoteApi.simx_opmode_blocking);
        checkStatusCode(result, "setSpeedRotation()");
        checkCounterSignals();
    }

    private void checkStatusCode(int statusCode, String statusMessage) {
        if (statusCode != remoteApi.simx_return_ok) {
            if (!isConnectedToServer()) {
                System.out.println("Connection to V-REP API server lost...");
                reconnectToServer();
                openServerCommunication();
            }
        }
    }

    private void checkCounterSignals() {
        handleServerSignal(COUNTER_RED_SIGNAL);
        handleServerSignal(COUNTER_GREEN_SIGNAL);
        handleServerSignal(COUNTER_BLUE_SIGNAL);
        handleServerSignal(COUNTER_MISS_SIGNAL);
    }

    private void handleServerSignal(String serverSignalType) {
        mVrep.simxGetIntegerSignal(mClientID, serverSignalType, mVrepSignal, remoteApi.simx_opmode_buffer);

        if (mVrepSignal.getValue() == -1)
            return;

        switch(serverSignalType) {
            case BOX_COLOR_SIGNAL:
                handleServerBoxColorSignal(mVrepSignal.getValue());
                break;
            case BOX_GRAB_SIGNAL:
                handleServerBoxGrabSignal(mVrepSignal.getValue());
                break;
            case COUNTER_RED_SIGNAL:
                handleServerRedSignal(mVrepSignal.getValue());
                break;
            case COUNTER_GREEN_SIGNAL:
                handleServerGreenSignal(mVrepSignal.getValue());
                break;
            case COUNTER_BLUE_SIGNAL:
                handleServerBlueSignal(mVrepSignal.getValue());
                break;
            case COUNTER_MISS_SIGNAL:
                handleServerMissSignal(mVrepSignal.getValue());
                break;
        }
    }

    private void handleServerBoxColorSignal(int vrepSignalBoxColor) {
        System.out.println("VREP.BOX_COLOR\t" + vrepSignalBoxColor);
        mCaller.receivedDataFromVRep(new ReceivedSimulatorData(RoboticSensorPart.COLOR_GRAB, vrepSignalBoxColor));
    }

    private void handleServerBoxGrabSignal(int vrepSignalGrab) {
        mVrepSignalGrab = vrepSignalGrab;
        System.out.println("VREP.BOX_GRAB\t" + vrepSignalGrab);
    }

    private void handleServerRedSignal(int numberElements) {
        if (numberElements != mVrepSignalCounterRed) {
            mVrepSignalCounterRed = mVrepSignal.getValue();
            System.out.println("VREP.COUNTER_RED\t" + mVrepSignalCounterRed);
            mCaller.receivedDataFromVRep(new ReceivedSimulatorData(RoboticSensorPart.SCORE_RED, mVrepSignalCounterRed));
        }
    }

    private void handleServerGreenSignal(int numberElements) {
        if (numberElements != mVrepSignalCounterGreen) {
            mVrepSignalCounterGreen = mVrepSignal.getValue();
            System.out.println("VREP.COUNTER_GREEN\t" + mVrepSignalCounterGreen);
            mCaller.receivedDataFromVRep(new ReceivedSimulatorData(RoboticSensorPart.SCORE_GREEN, mVrepSignalCounterGreen));
        }
    }

    private void handleServerBlueSignal(int numberElements) {
        if (numberElements != mVrepSignalCounterBlue) {
            mVrepSignalCounterBlue = mVrepSignal.getValue();
            System.out.println("VREP.COUNTER_BLUE\t" + mVrepSignalCounterBlue);
            mCaller.receivedDataFromVRep(new ReceivedSimulatorData(RoboticSensorPart.SCORE_BLUE, mVrepSignalCounterBlue));
        }
    }

    private void handleServerMissSignal(int numberElements) {
        if (numberElements != mVrepSignalCounterMiss) {
            mVrepSignalCounterMiss = mVrepSignal.getValue();
            System.out.println("VREP.COUNTER_MISS\t" + mVrepSignalCounterMiss);
            mCaller.receivedDataFromVRep(new ReceivedSimulatorData(RoboticSensorPart.SCORE_MISSED, mVrepSignalCounterMiss));
        }
    }

    @Contract(pure = true)
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
}
