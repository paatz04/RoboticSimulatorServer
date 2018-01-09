package bluetooth;//
// Android Praktikum
//
//	Robotic Arm Simulator
//
// 	V-REP process commands script
//

import coppelia.IntW;
import coppelia.IntWA;
import coppelia.remoteApi;
import org.jetbrains.annotations.Nullable;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import javax.microedition.io.StreamConnection;

public class BluetoothConnectionManager extends Thread {
    private static final int EXIT_CMD = -1;

    private StreamConnection mConnection;

    public BluetoothConnectionManager(StreamConnection connection) {
        mConnection = connection;
    }

    public void stopBluetoothConnectionManager() {
        // ToDo
    }

    @Override
    public void run() {
        handleConnection();
    }

    private void handleConnection() {
        DataInputStream inputStream = getDataInputStream();
        if (inputStream != null) {
            
        }



        try {
            // prepare to receive data
            InputStream inputStream = mConnection.openInputStream();

            System.out.println("waiting for input");



            while (true) {
                int command = inputStream.read();

                if (command == EXIT_CMD) {
                    System.out.println("finish process");
                    break;
                }
                processCommand(command);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Nullable
    private DataInputStream getDataInputStream() {
        try {
            return mConnection.openDataInputStream();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
