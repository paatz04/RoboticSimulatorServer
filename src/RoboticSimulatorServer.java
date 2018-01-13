import bluetooth.BluetoothConnectionManagerCaller;
import bluetooth.BluetoothManager;
import transfer.ReceivedData;
import transfer.TransferDataConverter;
import transfer.TransferDataConverterException;
import vrep.VRepController;
import vrep.VRepControllerException;

public class RoboticSimulatorServer implements BluetoothConnectionManagerCaller {
    private final static float DIVISOR_SPEED_REDUCTION = 8;

    private VRepController vRepController;
    private BluetoothManager bluetoothManager;

    RoboticSimulatorServer() {
        vRepController = new VRepController();
        bluetoothManager = new BluetoothManager(this);
    }

    public void startRoboticSimulatorServer() throws VRepControllerException {
        vRepController.start();
        bluetoothManager.start();
    }

    public void stopRoboticSimulatorServer() {
        bluetoothManager.stopBluetoothManager();
        vRepController.stopVRepController();
    }

    @Override
    public void receivedDataViaBluetooth(String strReceivedData) {
        try {
            ReceivedData receivedData = convertStrToReceivedData(strReceivedData);
            moveRoboticArm(receivedData);
        }catch (TransferDataConverterException e) {
            System.out.println(e.getMessage());
        }
    }

    private ReceivedData convertStrToReceivedData(String strReceivedData) throws TransferDataConverterException {
        return TransferDataConverter.getReceivedData(strReceivedData);
    }

    private void moveRoboticArm(ReceivedData receivedData) {
        float speed = reduceSpeed(receivedData.getValue());
        try {
            switch (receivedData.getRoboticArmPart()) {
                case GRAB:
                    vRepController.setSpeedGrab(speed);
                    break;
                case TIP:
                    vRepController.setSpeedTip(speed);
                    break;
                case BODY:
                    vRepController.setSpeedBody(speed);
                    break;
                case ROTATION:
                    vRepController.setSpeedRotation(speed);
                    break;
            }
        }catch (VRepControllerException e) {
            System.out.println(e.getMessage());
            stopRoboticSimulatorServer();
        }
    }

    private float reduceSpeed(float speed) {
        return speed / RoboticSimulatorServer.DIVISOR_SPEED_REDUCTION;
    }
}
