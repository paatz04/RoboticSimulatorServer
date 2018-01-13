package vrep;

public class VRepControllerClass {
    public static void main(String[] args) {
        VRepController vrep = new VRepController();
        try {
            vrep.start();
            sleep(1000);
            moveRotate(vrep);
            moveBody(vrep);
            moveTip(vrep);
            moveGrab(vrep);
        }catch(VRepControllerException e) {
            System.out.println(e.getMessage());
        }

    }

    private static void moveGrab(VRepController vrep) throws VRepControllerException {
        // grab
        vrep.setSpeedGrab(2);
        sleep(1000);

        vrep.setSpeedGrab(-2);
        //sleep(1000);

        // stop
        vrep.setSpeedGrab(0);
    }

    private static void moveTip(VRepController vrep) throws VRepControllerException {
        // move tip down
        vrep.setSpeedTip(-0.5f);
        sleep(500);
        //vrep.setSpeedTip(-2);
        //sleep(1000);

        // move tip up
        vrep.setSpeedTip(0.5f);
        //sleep(800);
        //vrep.setSpeedTip(2);
        //sleep(1000);

        // stop
        vrep.setSpeedTip(0);
    }

    private static void moveBody(VRepController vrep) throws VRepControllerException {
        // move body down
        vrep.setSpeedBody(0.5f);
        sleep(500);
        //vrep.setSpeedBody(-2);
        //sleep(1000);

        // move body up
        vrep.setSpeedBody(-0.5f);
        //sleep(500);
        //vrep.setSpeedBody(2);
        //sleep(1000);

        // stop
        vrep.setSpeedBody(0);
    }

    private static void moveRotate(VRepController vrep) throws VRepControllerException {
        // rotate right
        vrep.setSpeedRotation(1);
        sleep(1000);
        //vrep.setSpeedRotation(2);
        //sleep(1000);

        // rotate left
        vrep.setSpeedRotation(-1);
        //sleep(2000);
        //vrep.setSpeedRotation(-2);
        //sleep(2000);

        // stop
        vrep.setSpeedRotation(0);
    }

    private static void sleep(int milliseconds) {
        try {
            Thread.sleep(milliseconds);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
