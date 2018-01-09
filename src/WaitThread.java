import javax.bluetooth.DiscoveryAgent;
import javax.bluetooth.LocalDevice;
import javax.bluetooth.UUID;
import javax.microedition.io.Connector;
import javax.microedition.io.StreamConnection;
import javax.microedition.io.StreamConnectionNotifier;

import coppelia.IntW;
import coppelia.IntWA;
import coppelia.remoteApi;

//import java.util.UUID;

public class WaitThread implements Runnable {

    private remoteApi vrep;
    private int clientID;

    /** Constructor */
    public WaitThread(remoteApi vrep, int clientID) {
        this.vrep = vrep;
        this.clientID = clientID;
    }

    @Override
    public void run() {
        waitForConnection();
    }

    /** Waiting for connection from devices */
    private void waitForConnection() {
        // retrieve the local Bluetooth device object
        LocalDevice local = null;

        StreamConnectionNotifier notifier;
        StreamConnection connection = null;

        // setup the server to listen for connection
        try {
            local = LocalDevice.getLocalDevice();
            local.setDiscoverable(DiscoveryAgent.GIAC);

            UUID uuid = new UUID(80087355); // "04c6093b-0000-1000-8000-00805f9b34fb"
            //UUID uuid = UUID.fromString("2af6284c-f540-11e7-8c3f-9a214cf093ae");
            String url = "btspp://localhost:" + uuid.toString() + ";name=RemoteBluetooth";
            notifier = (StreamConnectionNotifier)Connector.open(url);
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }
                // waiting for connection
        while(true) {
            try {
                System.out.println("waiting for connection...");

                connection = notifier.acceptAndOpen();

                Thread processThread = new Thread(new ProcessConnectionThread(connection, vrep, clientID));
                processThread.start();

                System.out.println("connected to device");
            } catch (Exception e) {
                e.printStackTrace();
                return;
            }
        }
    }
}
