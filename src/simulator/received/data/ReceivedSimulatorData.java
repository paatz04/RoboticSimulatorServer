package simulator.received.data;

public class ReceivedSimulatorData {
    private RoboticSensorPart mSensor;
    private int mValue;

    public ReceivedSimulatorData(RoboticSensorPart sensor, int value) {
        mSensor = sensor;
        mValue = value;
    }

    public RoboticSensorPart getRoboticSensorPart() {
        return mSensor;
    }

    public int getValue() {
        return mValue;
    }
}
