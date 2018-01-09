import bluetooth.BluetoothConnectionManagerCaller;
import bluetooth.BluetoothManager;
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
    public void receivedDataViaBluetooth(String received) {
        System.out.println(received);
        // convert date into controller commands
        // send the controller commands to the vRepController
    }
}
