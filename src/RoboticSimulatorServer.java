import bluetooth.BluetoothConnectionManagerCaller;
import bluetooth.BluetoothManager;
import org.jetbrains.annotations.Nullable;
import transfer.ReceivedData;
import transfer.TransferDataConverter;
import transfer.TransferDataConverterException;
import vrep.VRepController;

public class RoboticSimulatorServer implements BluetoothConnectionManagerCaller {
    private VRepController vRepController;
    private BluetoothManager bluetoothManager;

    RoboticSimulatorServer() {
        vRepController = new VRepController();
        bluetoothManager = new BluetoothManager(this);
    }

    public void startRoboticSimulatorServer() {
        vRepController.start();
        bluetoothManager.start();
    }

    public void stopRoboticSimulatorServer() {
        bluetoothManager.stopBluetoothManager();
        vRepController.stopVRepController();
    }

    @Override
    public void receivedDataViaBluetooth(String strReceivedData) {
        ReceivedData receivedData = convertStrToReceivedData(strReceivedData);
        if (receivedData != null)
            moveRoboticArm(receivedData);
    }

    @Nullable
    private ReceivedData convertStrToReceivedData(String strReceivedData) {
        try {
            return TransferDataConverter.getReceivedData(strReceivedData);
        } catch (TransferDataConverterException e) {
            System.err.println(e.getMessage());
            return null;
        }
    }

    private void moveRoboticArm(ReceivedData receivedData) {
        switch (receivedData.getRoboticArmPart()) {
            case GRAB:
                vRepController.setSpeedGrab(receivedData.getValue());
                break;
            case TIP:
                vRepController.setSpeedTip(receivedData.getValue());
                break;
            case BODY:
                vRepController.setSpeedBody(receivedData.getValue());
                break;
            case ROTATION:
                vRepController.setSpeedRotation(receivedData.getValue());
                break;
        }
    }
}
