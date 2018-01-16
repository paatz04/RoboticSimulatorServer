package simulator;

public class ReceivedDataBluetooth {
    private RoboticArmPart mRoboticArmPart;
    private float mValue;

    public ReceivedDataBluetooth(RoboticArmPart roboticArmPart, float value) {
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
