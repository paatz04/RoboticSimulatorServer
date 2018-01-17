package bluetooth;

import javax.bluetooth.BluetoothStateException;
import javax.bluetooth.DiscoveryAgent;
import javax.bluetooth.LocalDevice;
import javax.bluetooth.UUID;
import javax.microedition.io.Connector;
import javax.microedition.io.StreamConnection;
import javax.microedition.io.StreamConnectionNotifier;
import java.io.IOException;

public class BluetoothManager {
    private static final int UUID_ADDRESS = 80087355; // "04c6093b-0000-1000-8000-00805f9b34fb"

    private StreamConnectionNotifier mNotifier;

    public BluetoothManager() {}

    public void startBluetoothManager() throws BluetoothManagerException {
        try {
            setLocalBluetoothDeviceDiscoverable();
            setUpServerToListenForConnections();
        } catch (IOException e) {
            throw new BluetoothManagerException("Couldn't set up the Bluetooth-server.");
        }
    }

    private void setLocalBluetoothDeviceDiscoverable() throws BluetoothStateException {
        LocalDevice localBluetoothDevice = LocalDevice.getLocalDevice();
        localBluetoothDevice.setDiscoverable(DiscoveryAgent.GIAC);
    }

    private void setUpServerToListenForConnections() throws IOException {
        UUID uuid = new UUID(UUID_ADDRESS);
        String url =  "btspp://localhost:" + uuid.toString() + ";name=RemoteBluetooth";
        mNotifier = (StreamConnectionNotifier) Connector.open(url);
    }

    public void stopBluetoothManager() {
        try {
            mNotifier.close();
        } catch (IOException e) {
            System.out.println("Couldn't close the notifier.");
        }
    }

    public ConnectionThread getBluetoothConnection() throws BluetoothManagerException {
        try {
            StreamConnection connection = mNotifier.acceptAndOpen();
            return new ConnectionThread(connection);
        } catch(IOException e) {
            throw new BluetoothManagerException("Couldn't open a new Connection.");
        }
    }
}
