package bluetooth;//
// Android Praktikum
//
//	Robotic Arm Simulator
//
// 	V-REP process commands script
//

import bluetooth.inputlistener.DataInputStreamListener;
import bluetooth.inputlistener.DataInputStreamListenerCaller;
import org.jetbrains.annotations.Nullable;

import java.io.DataInputStream;
import java.io.IOException;
import javax.microedition.io.StreamConnection;

public class BluetoothConnectionManager extends Thread implements DataInputStreamListenerCaller{
    private StreamConnection mConnection;
    private DataInputStreamListener mInputStreamListener;

    public BluetoothConnectionManager(StreamConnection connection) {
        mConnection = connection;
    }

    public void stopBluetoothConnectionManager() {
        mInputStreamListener.stopDataInputStreamListener();
        System.out.println("Connection closed");
    }

    @Override
    public void receivedStringFromInputStream(String received) {
        // ToDo
        System.out.println(received);
    }

    public void dataInputStreamClosed() {
        System.out.println("Connection closed");
    }

    @Override
    public void run() {
        System.out.println("New Connection");
        handleConnection();
    }

    private void handleConnection() {
        DataInputStream inputStream = getDataInputStream();
        if (inputStream != null)
            startDataInputStreamListener(inputStream);
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

    private void startDataInputStreamListener(DataInputStream inputStream) {
        mInputStreamListener = new DataInputStreamListener(inputStream, this);
        mInputStreamListener.start();
    }
}
