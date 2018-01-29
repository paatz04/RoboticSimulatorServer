package simulator.received.data;

public class ReceivedConnectionData {
    private RoboticArmPart mRoboticArmPart;
    private float mValue;

    public ReceivedConnectionData(RoboticArmPart roboticArmPart, float value) {
        mRoboticArmPart = roboticArmPart;
        mValue = value;
    }

    public RoboticArmPart getRoboticArmPart() {
        return mRoboticArmPart;
    }

    public float getValue() {
        return mValue;
    }
}
