import bluetooth.BluetoothManager;
import vrep.VRepController;

public class RoboticSimulatorServer {
    private VRepController vRepController;
    private BluetoothManager bluetoothManager;

    public RoboticSimulatorServer() {
        // ToDo init vRep
        bluetoothManager = new BluetoothManager();
    }

    public void startRoboticSimulatorServer() {
        // ToDo start vRep
        bluetoothManager.start();
    }

    public void stopRoboticSimulatorServer() {
        bluetoothManager.stopBluetoothManager();
        // ToDo stop vRep
    }
}
