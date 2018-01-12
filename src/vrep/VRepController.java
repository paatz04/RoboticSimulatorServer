package vrep;

import coppelia.*;

/*
 * This class should communicate with the vRep and control the vRep. With this class an external class should be able
 * to move the robotic arm, with using only the functions of this class.
 */
public class VRepController extends Thread {
    /**
     * defines the time frequency, with which the robotic arm parts should be moved with the set speed.
     *
     * Example:
     *   set speed for the tip = 3 -> move the tip of the robotic arm every 20 milliseconds per 3 units up
     */
    private final static int UPDATE_FREQUENCY_MILLI_SECONDS = 20;
    private final static String SIMULATOR_SCENE_OBJECT = "IRB140#0";

    private remoteApi mVrep;
    private int mClientID;

    private boolean mStopped = false;

    private float mSpeedGrab = 0;
    private float mSpeedTip = 0;
    private float mSpeedBody = 0;
    private float mSpeedRotation = 0;
    private FloatWA mInFloats = new FloatWA(1);


    public VRepController() {
        mVrep = new remoteApi();
    }

    public void stopVRepController() {
        mVrep.simxFinish(mClientID);
        mStopped = true;
    }

    public void run() {
        connectToServer();
        if (isConnectedToServer()) {
            System.out.println("Connected to V-REP API server...");
        } else {
            System.out.println("Failed to connect to V-REP API server...");
            System.exit(1);
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

    /**
     * If this function is called, the speed of grabbing/releasing should be changed.
     * positive value = grabbing
     * negative value = releasing
     *
     * @param speed - speed grab
     */
    public void setSpeedGrab(float speed) {
        System.out.println("VREP.GRAB\t" + speed);
        mInFloats.getArray()[0] = speed;
        int result=mVrep.simxCallScriptFunction(mClientID,SIMULATOR_SCENE_OBJECT,mVrep.sim_scripttype_childscript,
                                                "setSpeedGrab_function",null,mInFloats,
                                                null,null,null,null,null,
                                                null,mVrep.simx_opmode_blocking);
        if (result == mVrep.simx_return_ok) {
            mSpeedGrab = speed;
        } else {
            System.out.format("setSpeedGrab() remote function call failed\n");
        }
    }

    /**
     * If this function is called, the speed for the tip should be changed.
     * positive value = tip up
     * negative value = tip down
     *
     * @param speed - speed tip
     */
    public void setSpeedTip(float speed) {
        System.out.println("VREP.TIP\t" + speed);
        mInFloats.getArray()[0] = speed;
        int result=mVrep.simxCallScriptFunction(mClientID,SIMULATOR_SCENE_OBJECT,mVrep.sim_scripttype_childscript,
                                                "setSpeedTip_function",null,mInFloats,
                                                null,null,null,null,null,
                                                null,mVrep.simx_opmode_blocking);
        if (result == mVrep.simx_return_ok) {
            mSpeedTip = speed;
        } else {
            System.out.format("setSpeedTip() remote function call failed\n");
        }
    }

    /**
     * If this function is called, the speed for the body should be changed.
     * positive value = body up
     * negative value = body down
     *
     * @param speed - speed body
     */
    public void setSpeedBody(float speed) {
        System.out.println("VREP.BODY\t" + speed);
        mInFloats.getArray()[0] = speed;
        int result=mVrep.simxCallScriptFunction(mClientID,SIMULATOR_SCENE_OBJECT,mVrep.sim_scripttype_childscript,
                                                "setSpeedBody_function",null,mInFloats,
                                                null,null,null,null,null,
                                                null,mVrep.simx_opmode_blocking);
        if (result == mVrep.simx_return_ok) {
            mSpeedBody = speed;
        } else {
            System.out.format("setSpeedBody() remote function call failed\n");
        }
    }

    /**
     * If this function is called, the speed for the rotation should be changed.
     * positive value = rotation to the right
     * negative value = rotation to the left
     *
     * @param speed - speed rotation
     */
    public void setSpeedRotation(float speed) {
        System.out.println("VREP.ROTATION\t" + speed);
        mInFloats.getArray()[0] = speed;
        int result=mVrep.simxCallScriptFunction(mClientID,SIMULATOR_SCENE_OBJECT,mVrep.sim_scripttype_childscript,
                                                "setSpeedRotation_function",null,mInFloats,
                                                null,null,null,null,null,
                                                null,mVrep.simx_opmode_blocking);
        if (result == mVrep.simx_return_ok) {
            mSpeedRotation = speed;
        } else {
            System.out.format("setSpeedRotation() remote function call failed\n");
        }
    }

    private void moveRoboticArm() {
        grab();
        moveTip();
        moveBody();
        rotate();
    }

    private void grab() {
        if (mSpeedGrab != 10) {
            mInFloats.getArray()[0] = mSpeedGrab;
            int result=mVrep.simxCallScriptFunction(
                    mClientID,SIMULATOR_SCENE_OBJECT,mVrep.sim_scripttype_childscript,
                    "grab_function",null,mInFloats,
                    null,null,null,null,null,
                    null,mVrep.simx_opmode_blocking);
            if (result != mVrep.simx_return_ok) {
                System.out.format("grab() remote function call failed\n");
            }
        }
    }

    private void moveTip() {
        if (mSpeedTip != 0) {
            mInFloats.getArray()[0] = mSpeedTip;
            int result=mVrep.simxCallScriptFunction(
                    mClientID,SIMULATOR_SCENE_OBJECT,mVrep.sim_scripttype_childscript,
                    "moveTip_function",null,mInFloats,
                    null,null,null,null,null,
                    null,mVrep.simx_opmode_blocking);
            if (result != mVrep.simx_return_ok) {
                System.out.format("moveTip() remote function call failed\n");
            }
        }
    }

    private void moveBody() {
        if (mSpeedBody != 0) {
            mInFloats.getArray()[0] = mSpeedBody;
            int result=mVrep.simxCallScriptFunction(
                    mClientID,SIMULATOR_SCENE_OBJECT,mVrep.sim_scripttype_childscript,
                    "moveBody_function",null,mInFloats,
                    null,null,null,null,null,
                    null,mVrep.simx_opmode_blocking);
            if (result != mVrep.simx_return_ok) {
                System.out.format("moveBody() remote function call failed\n");
            }
        }
    }

    private void rotate() {
        if (mSpeedRotation != 0) {
            mInFloats.getArray()[0] = mSpeedRotation;
            int result=mVrep.simxCallScriptFunction(
                    mClientID,SIMULATOR_SCENE_OBJECT,mVrep.sim_scripttype_childscript,
                    "rotate_function",null,mInFloats,
                    null,null,null,null,null,
                    null,mVrep.simx_opmode_blocking);
            if (result != mVrep.simx_return_ok) {
                System.out.format("rotate() remote function call failed\n");
            }
        }
    }

    private void sleepUntilNextRoboticArmUpdate() {
        try {
            Thread.sleep(UPDATE_FREQUENCY_MILLI_SECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
