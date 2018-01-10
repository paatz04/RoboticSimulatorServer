package transfer;

import vrep.RoboticArmPart;

public class ReceivedData {
    private RoboticArmPart mRoboticArmPart;
    private float mValue;

    public ReceivedData(RoboticArmPart roboticArmPart, float value) {
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
