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

    public VRepController() {
        // ToDo
    }


    public void stopVRepController() {
        // ToDo
    }

    public void run() {
        // ToDo
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
        // ToDo
    }

    /**
     * If this function is called, the speed for the tip should be changed.
     * positive value = tip up
     * negative value = tip down
     *
     * Example: setSpeedTip(-3) -> the tip of the robotic arm should move with a speed of 3 units down.
     *
     * @param speed
     */
    public void setSpeedTip(double speed) {
        System.out.println("VREP.TIP\t" + speed);
        // ToDo
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
        // ToDo
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
        // ToDo
    }
}
