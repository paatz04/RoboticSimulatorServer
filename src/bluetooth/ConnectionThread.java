package bluetooth;

import bluetooth.inputlistener.DataInputStreamListener;
import bluetooth.inputlistener.DataInputStreamListenerCaller;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.EmptyStackException;
import java.util.Stack;
import javax.microedition.io.StreamConnection;

public class ConnectionThread extends Thread implements DataInputStreamListenerCaller{

    private ConnectionCaller mCaller;
    private StreamConnection mStreamConnection;
    private DataInputStreamListener mInputStreamListener;
    private DataOutputStream mOutputStream;

    private boolean mStopped = false;

    private Stack<String> mDataToBeSent = new Stack<>();
    private Stack<String> mReceivedData = new Stack<>();

    ConnectionThread(StreamConnection connection) {
        mStreamConnection = connection;
    }

    public synchronized void stopConnection() {
        mStopped = true;
        notify();
    }

    public synchronized boolean isConnectionStopped() {
        return mStopped;
    }

    public void startConnectionThread(ConnectionCaller caller) {
        mCaller = caller;
        start();
    }

    @Override
    public void dataInputStreamStopped() {
        mInputStreamListener = null;
        stopConnection();
    }

    @Override
    public void run() {
        try {
            initOutPutStream();
            startDataInputStreamListener();
            handleConnection();
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("ConnectionThread finished");
        closeAllConnections();
    }

    private void initOutPutStream() throws IOException {
        mOutputStream = mStreamConnection.openDataOutputStream();
    }

    private void startDataInputStreamListener() throws IOException{
        DataInputStream inputStream = mStreamConnection.openDataInputStream();

        mInputStreamListener = new DataInputStreamListener(inputStream, this);
        mInputStreamListener.start();
    }

    private void handleConnection() {
        while (!mStopped) {
            handleReceivedData();
            handleDataToBeSent();
            if (!mStopped)
                waitForData();
        }
    }

    private void handleReceivedData() {
        try {
            mCaller.addReceivedConnectionData(popReceivedData());
        } catch (EmptyStackException ignored) { }
    }

    private synchronized String popReceivedData() {
        return mReceivedData.pop();
    }

    private void handleDataToBeSent() {
        try {
            String dataToBeSent = popDataToBeSent();
            mOutputStream.writeUTF(dataToBeSent);
            System.out.println("Sended: " + dataToBeSent);
        } catch (IOException e) {
            e.printStackTrace();
            stopConnection();
        } catch (EmptyStackException ignored) { }

    }

    private synchronized String popDataToBeSent() {
        return mDataToBeSent.pop();
    }

    private synchronized void waitForData() {
         if (mReceivedData.size() + mDataToBeSent.size() > 0) {
             try {
                 wait();
             } catch (InterruptedException e) {
                 e.printStackTrace();
             }
         }
    }

    private void closeAllConnections() {
        if (mInputStreamListener != null)
            mInputStreamListener.stopDataInputStreamListener();

        try {
            mOutputStream.close();
        } catch (IOException ignored) {}

        try {
            mStreamConnection.close();
        } catch (IOException ignored) { }
    }

    @Override
    public synchronized void addReceivedData(String received) {
        mReceivedData.add(received);
        notify();
    }

    public synchronized void write(String message) {
        mDataToBeSent.add(message);
        notify();
    }
}
