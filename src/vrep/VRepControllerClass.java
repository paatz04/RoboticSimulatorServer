package vrep;

import simulator.received.data.ReceivedSimulatorData;

public class VRepControllerClass implements VRepControllerCaller {

    private VRepControllerClass () {
        VRepController vrep = new VRepController(this);
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

    private void moveGrab(VRepController vrep) {
        // grab
        vrep.setSpeedGrab(2);
        sleep(1000);

        vrep.setSpeedGrab(-2);
        //sleep(1000);

        // stop
        vrep.setSpeedGrab(0);
    }

    private void moveTip(VRepController vrep) {
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

    private void moveBody(VRepController vrep) {
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

    private void moveRotate(VRepController vrep) {
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

    private void sleep(int milliseconds) {
        try {
            Thread.sleep(milliseconds);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void addReceivedSimulatorData(ReceivedSimulatorData receivedDataVRep) {

    }

    public static void main(String[] args) {
        new VRepControllerClass();
    }
}
