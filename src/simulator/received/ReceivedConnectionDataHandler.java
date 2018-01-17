package simulator.received;

import simulator.received.data.ReceivedConnectionData;
import transfer.TransferDataConverter;
import transfer.TransferDataConverterException;
import vrep.VRepController;


public class ReceivedConnectionDataHandler {
    private final static float DIVISOR_SPEED_REDUCTION = 10;

    private VRepController mSimulator;

    public ReceivedConnectionDataHandler(VRepController simulator) {
        mSimulator = simulator;
    }

    public void handle(String strReceivedData) {
        try {
            ReceivedConnectionData receivedConnectionData = convertStrToReceivedData(strReceivedData);
            moveRoboticArm(receivedConnectionData);
        } catch (TransferDataConverterException e) {
            System.out.println(e.getMessage());
        }
    }

    private ReceivedConnectionData convertStrToReceivedData(String strReceivedData) throws TransferDataConverterException {
        return TransferDataConverter.getReceivedBluetoothData(strReceivedData);
    }

    private void moveRoboticArm(ReceivedConnectionData receivedData) {
        float speed = reduceSpeed(receivedData.getValue());
        switch (receivedData.getRoboticArmPart()) {
            case GRAB:
                mSimulator.setSpeedGrab(speed);
                break;
            case TIP:
                mSimulator.setSpeedTip(speed);
                break;
            case BODY:
                mSimulator.setSpeedBody(speed);
                break;
            case ROTATION:
                mSimulator.setSpeedRotation(speed);
                break;
        }
    }

    private float reduceSpeed(float speed) {
        return speed / ReceivedConnectionDataHandler.DIVISOR_SPEED_REDUCTION;
    }
}
