import bluetooth.BluetoothConnectionManagerCaller;
import bluetooth.BluetoothManager;
import vrep.VRepController;

public class RoboticSimulatorServer implements BluetoothConnectionManagerCaller {
    private VRepController vRepController;
    private BluetoothManager bluetoothManager;

    public RoboticSimulatorServer() {
        // ToDo init vRep
        bluetoothManager = new BluetoothManager(this);
    }

    public void startRoboticSimulatorServer() {
        // ToDo start vRep
        bluetoothManager.start();
    }

    public void stopRoboticSimulatorServer() {
        bluetoothManager.stopBluetoothManager();
        // ToDo stop vRep
    }

    @Override
    public void receivedData(String received) {
        System.out.println(received);
        // convert date into controller commands
        // send the controller commands to the vRepController
    }
}
