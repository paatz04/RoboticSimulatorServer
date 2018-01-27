package vrep;

import simulator.received.data.ReceivedSimulatorData;
import simulator.received.data.RoboticSensorPart;

public class MockVRepController {

    private VRepControllerCaller mCallerMock;
    private boolean mStopped = false;

    public MockVRepController(VRepControllerCaller caller) {
        mCallerMock = caller;
    }

    public void stopVRepController() {

    }

    public void start() {}

    public void setSpeedGrab(float speed) {
        System.out.println("SpeedGrab: " + speed);
//        mCallerMock.addReceivedSimulatorData(new ReceivedSimulatorData(RoboticSensorPart.SCORE_BLUE, 3));
    }

    public void setSpeedTip(float speed) {
        System.out.println("SpeedTip: " + speed);
//        mCallerMock.addReceivedSimulatorData(new ReceivedSimulatorData(RoboticSensorPart.SCORE_BLUE, 1));
    }

    public void setSpeedRotation(float speed) {
        System.out.println("SpeedRotation: " + speed);
//        mCallerMock.addReceivedSimulatorData(new ReceivedSimulatorData(RoboticSensorPart.SCORE_BLUE, 2));
    }

    public void setSpeedBody(float speed) {
        System.out.println("SpeedBody: " + speed);
//        mCallerMock.addReceivedSimulatorData(new ReceivedSimulatorData(RoboticSensorPart.SCORE_BLUE, 4));
    }

}
