package bluetooth;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.Nullable;

import javax.bluetooth.BluetoothStateException;
import javax.bluetooth.DiscoveryAgent;
import javax.bluetooth.LocalDevice;
import javax.bluetooth.UUID;
import javax.microedition.io.Connector;
import javax.microedition.io.StreamConnection;
import javax.microedition.io.StreamConnectionNotifier;
import java.io.IOException;
import java.util.ArrayList;

public class BluetoothManager extends Thread{
    private static final int UUID_ADDRESS = 80087355; // "04c6093b-0000-1000-8000-00805f9b34fb"

    private StreamConnectionNotifier mNotifier;
    private boolean mStopped = false;
    // ToDo we don't need a list, because we only support one device
    private ArrayList<BluetoothConnectionManager> mBluetoothConnectionManagerList;

    public BluetoothManager() {
        mBluetoothConnectionManagerList = new ArrayList<>();
    }

    public synchronized void stopBluetoothManager() {
        mStopped = true;
    }

    @Contract(pure = true)
    private synchronized boolean isBluetoothManagerStopped() {
        return mStopped;
    }

    public void run() {
        setUpServer();
        if (isServerSetUp())
            handleNewConnections();
    }

    private void setUpServer() {
        try {
            setLocalBluetoothDeviceDiscoverable();
            setUpServerToListenForConnections();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Could not set up the server");
            mNotifier = null;
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

    @Contract(pure = true)
    private boolean isServerSetUp() {
        return mNotifier != null;
    }

    private void handleNewConnections() {
        while(!isBluetoothManagerStopped()) {
            StreamConnection connection = waitForConnection();
            if (connection != null)
                startBluetoothConnectionManager(connection);
        }
        stopAllBluetoothConnections();
        waitUntilAllBluetoothConnectionsAreStopped();
    }

    @Nullable
    private StreamConnection waitForConnection(){
        try {
            return mNotifier.acceptAndOpen();
        } catch (IOException e) {
            e.printStackTrace();
            // ToDo: probably we have to stop the Server
        }
        return null;
    }

    private void startBluetoothConnectionManager(StreamConnection connection) {
        BluetoothConnectionManager bluetoothConnectionManager = new BluetoothConnectionManager(connection);
        bluetoothConnectionManager.start();
        addBluetoothConnection(bluetoothConnectionManager);
    }

    private synchronized void addBluetoothConnection(BluetoothConnectionManager bluetoothConnectionManager) {
        mBluetoothConnectionManagerList.add(bluetoothConnectionManager);
    }

    private synchronized void stopAllBluetoothConnections() {
        for (BluetoothConnectionManager bluetoothConnectionManager : mBluetoothConnectionManagerList)
            bluetoothConnectionManager.stopBluetoothConnectionManager();
    }

    private synchronized void waitUntilAllBluetoothConnectionsAreStopped() {
        for (BluetoothConnectionManager bluetoothConnectionManager : mBluetoothConnectionManagerList) {
            try {
                bluetoothConnectionManager.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
