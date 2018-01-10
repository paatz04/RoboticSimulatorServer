package vrep;

public class VRepControllerClass {
    public static void main(String[] args) {
        VRepController vrep = new VRepController();
        vrep.start();

        moveGrab(vrep);
    }

    private static void moveGrab(VRepController vrep) {
        // grab
        vrep.setSpeedGrab(1);
        sleep(2000);
        vrep.setSpeedGrab(3);
        sleep(1000);

        // release
        vrep.setSpeedGrab(-1);
        sleep(2000);
        vrep.setSpeedGrab(-3);
        sleep(1000);
    }

    private static void moveTip(VRepController vrep) {
        // move tip down
        vrep.setSpeedTip(-1);
        sleep(2000);
        vrep.setSpeedTip(-3);
        sleep(1000);

        // move tip up
        vrep.setSpeedTip(1);
        sleep(2000);
        vrep.setSpeedTip(3);
        sleep(1000);
    }

    private static void moveBody(VRepController vrep) {
        // move body down
        vrep.setSpeedBody(-1);
        sleep(2000);
        vrep.setSpeedBody(-3);
        sleep(1000);

        // move body up
        vrep.setSpeedBody(1);
        sleep(2000);
        vrep.setSpeedBody(3);
        sleep(1000);
    }

    private static void moveRotate(VRepController vrep) {
        // rotate right
        vrep.setSpeedRotation(1);
        sleep(2000);
        vrep.setSpeedRotation(3);
        sleep(1000);

        // rotate left
        vrep.setSpeedRotation(-1);
        sleep(2000);
        vrep.setSpeedRotation(-3);
        sleep(1000);
    }

    private static void sleep(int milliseconds) {
        try {
            Thread.sleep(milliseconds);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
