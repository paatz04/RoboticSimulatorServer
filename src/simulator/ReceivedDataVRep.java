package simulator;

public class ReceivedDataVRep {
    private RoboticSensorPart mSensor;
    private int mValue;

    public ReceivedDataVRep(RoboticSensorPart sensor, int value) {
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
