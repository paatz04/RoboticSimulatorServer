package vrep;

import coppelia.*;

/*
 * This class should communicate with the vRep and control the vRep. With this class an external class should be able
 * to move the robotic arm, with using only the functions of this class.
 */
public class VRepController {
    private final static String SIMULATOR_SCENE_OBJECT = "IRB140#0";

    private remoteApi mVrep;
    private int mClientID;

    private FloatWA mInFloats = new FloatWA(1);


    public VRepController() {
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

    /**
     * If this function is called, the speed of grabbing/releasing should be changed.
     * positive value = grabbing
     * negative value = releasing
     *
     * @param speed - speed grab
     */
    public void setSpeedGrab(float speed) throws VRepControllerException {
        System.out.println("VREP.GRAB\t" + speed);
        mInFloats.getArray()[0] = speed;
        int result = mVrep.simxCallScriptFunction(mClientID,SIMULATOR_SCENE_OBJECT, remoteApi.sim_scripttype_childscript,
                                                "setSpeedGrab_function",null,mInFloats,
                                                null,null,null,null,null,
                                                null, remoteApi.simx_opmode_blocking);
        if (result != remoteApi.simx_return_ok)
            throw new VRepControllerException("setSpeedGrab() remote function call failed");
    }

    /**
     * If this function is called, the speed for the tip should be changed.
     * positive value = tip up
     * negative value = tip down
     *
     * @param speed - speed tip
     */
    public void setSpeedTip(float speed) throws VRepControllerException {
        System.out.println("VREP.TIP\t" + speed);
        mInFloats.getArray()[0] = speed;
        int result=mVrep.simxCallScriptFunction(mClientID,SIMULATOR_SCENE_OBJECT, remoteApi.sim_scripttype_childscript,
                                                "setSpeedTip_function",null,mInFloats,
                                                null,null,null,null,null,
                                                null, remoteApi.simx_opmode_blocking);
        if (result != remoteApi.simx_return_ok)
            throw new VRepControllerException("setSpeedTip() remote function call failed");
    }

    /**
     * If this function is called, the speed for the body should be changed.
     * positive value = body up
     * negative value = body down
     *
     * @param speed - speed body
     */
    public void setSpeedBody(float speed) throws VRepControllerException {
        System.out.println("VREP.BODY\t" + speed);
        mInFloats.getArray()[0] = speed;
        int result=mVrep.simxCallScriptFunction(mClientID,SIMULATOR_SCENE_OBJECT, remoteApi.sim_scripttype_childscript,
                                                "setSpeedBody_function",null,mInFloats,
                                                null,null,null,null,null,
                                                null, remoteApi.simx_opmode_blocking);
        if (result != remoteApi.simx_return_ok)
            throw new VRepControllerException("setSpeedBody() remote function call failed");
    }

    /**
     * If this function is called, the speed for the rotation should be changed.
     * positive value = rotation to the right
     * negative value = rotation to the left
     *
     * @param speed - speed rotation
     */
    public void setSpeedRotation(float speed) throws VRepControllerException {
        System.out.println("VREP.ROTATION\t" + speed);
        mInFloats.getArray()[0] = speed;
        int result=mVrep.simxCallScriptFunction(mClientID,SIMULATOR_SCENE_OBJECT, remoteApi.sim_scripttype_childscript,
                                                "setSpeedRotation_function",null,mInFloats,
                                                null,null,null,null,null,
                                                null, remoteApi.simx_opmode_blocking);
        if (result != remoteApi.simx_return_ok)
            throw new VRepControllerException("setSpeedRotation() remote function call failed");
    }
}
