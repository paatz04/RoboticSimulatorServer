package vrep;

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

    private boolean mStopped = false;

    private float mSpeedGrab = 0;
    private float mSpeedTip = 0;
    private float mSpeedBody = 0;
    private float mSpeedRotation = 0;


    public VRepController() {
        // ToDo
    }

    public void stopVRepController() {
        mStopped = true;
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
        mSpeedGrab = speed;
    }

    /**
     * If this function is called, the speed for the tip should be changed.
     * positive value = tip up
     * negative value = tip down
     *
     * Example: setSpeedTip(-3) -> the tip of the robotic arm should move with a speed of 3 units down.
     *
     * @param speed - speed tip
     */
    public void setSpeedTip(float speed) {
        System.out.println("VREP.TIP\t" + speed);
        mSpeedTip = speed;
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
        mSpeedBody = speed;
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
        mSpeedRotation = speed;
    }


    public void run() {
        while(!mStopped) {
            moveRoboticArm();
            sleepUntilNextRoboticArmUpdate();
        }
    }

    private void moveRoboticArm() {
        grab();
        moveTip();
        moveBody();
        rotate();
    }

    private void grab() {
        if (mSpeedGrab != 0) {
            // ToDo: move the grab part of the robotic arm for mSpeedGrab units
        }
    }

    private void moveTip() {
        if (mSpeedTip != 0) {
            // ToDo: move the tip part of the robotic arm for mSpeedTip units
        }
    }

    private void moveBody() {
        if (mSpeedBody != 0) {
            // ToDo: move the body part of the robotic arm for mSpeedBody units
        }
    }

    private void rotate() {
        if (mSpeedRotation != 0) {
            // ToDo: rotate the robotic arm for mSpeedRotation units
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
