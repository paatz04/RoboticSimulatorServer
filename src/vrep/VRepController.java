package vrep;

import coppelia.*;

/* ToDo
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
    private final static String SIMULATOR_SCENE = "RoboticArmSimulator";

    private remoteApi mVrep;
    private int mClientID;

    public VRepController() {
        mVrep = new remoteApi();
    }

    public void stopVRepController() {
        mVrep.simxFinish(mClientID);
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
     * @param speed
     */
    public void setSpeedGrab(double speed) {
        System.out.println("VREP.GRAB\t" + speed);
        FloatWA inFloats = new FloatWA(1);
        inFloats.getArray()[0] = (float) speed;
        int result=mVrep.simxCallScriptFunction(mClientID,SIMULATOR_SCENE,mVrep.sim_scripttype_childscript,
                                                "setSpeedGrab_function",null,inFloats,
                                                null,null,null,null,null,
                                                null,mVrep.simx_opmode_blocking);
        if (result != mVrep.simx_return_ok) {
            System.out.format("VREP.GRAB remote function call failed\n");
        }
    }

    /**
     * If this function is called, the speed for the tip should be changed.
     * positive value = tip up
     * negative value = tip down
     *
     * @param speed
     */
    public void setSpeedTip(double speed) {
        System.out.println("VREP.TIP\t" + speed);
        FloatWA inFloats = new FloatWA(1);
        inFloats.getArray()[0] = (float) speed;
        int result=mVrep.simxCallScriptFunction(mClientID,SIMULATOR_SCENE,mVrep.sim_scripttype_childscript,
                                                "setSpeedTip_function",null,inFloats,
                                                null,null,null,null,null,
                                                null,mVrep.simx_opmode_blocking);
        if (result != mVrep.simx_return_ok) {
            System.out.format("VREP.TIP remote function call failed\n");
        }
    }

    /**
     * If this function is called, the speed for the body should be changed.
     * positive value = body up
     * negative value = body down
     *
     * @param speed
     */
    public void setSpeedBody(double speed) {
        System.out.println("VREP.BODY\t" + speed);
        FloatWA inFloats = new FloatWA(1);
        inFloats.getArray()[0] = (float) speed;
        int result=mVrep.simxCallScriptFunction(mClientID,SIMULATOR_SCENE,mVrep.sim_scripttype_childscript,
                                                "setSpeedBody_function",null,inFloats,
                                                null,null,null,null,null,
                                                null,mVrep.simx_opmode_blocking);
        if (result != mVrep.simx_return_ok) {
            System.out.format("VREP.BODY remote function call failed\n");
        }
    }

    /**
     * If this function is called, the speed for the rotation should be changed.
     * positive value = rotation to the right
     * positive value = rotation to the left
     *
     * @param speed
     */
    public void setSpeedRotation(double speed) {
        System.out.println("VREP.ROTATION\t" + speed);
        FloatWA inFloats = new FloatWA(1);
        inFloats.getArray()[0] = (float) speed;
        int result=mVrep.simxCallScriptFunction(mClientID,SIMULATOR_SCENE,mVrep.sim_scripttype_childscript,
                                                "setSpeedRotation_function",null,inFloats,
                                                null,null,null,null,null,
                                                null,mVrep.simx_opmode_blocking);
        if (result != mVrep.simx_return_ok) {
            System.out.format("VREP.ROTATION remote function call failed\n");
        }
    }
}
