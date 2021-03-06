package bluetooth.inputlistener;

import org.jetbrains.annotations.Nullable;

import java.io.DataInputStream;
import java.io.IOException;

public class DataInputStreamListener extends Thread {
    private DataInputStream mInputStream;
    private DataInputStreamListenerCaller mCaller;
    private boolean mStopped = false;

    public DataInputStreamListener(DataInputStream inputStream, DataInputStreamListenerCaller caller) {
        mInputStream = inputStream;
        mCaller = caller;
    }

    public void stopDataInputStreamListener() {
        mStopped = true;
    }

    @Override
    public void run() {
        while (!mStopped) {
            String receivedString = waitForInputStreamData();
            if (receivedString != null)
                mCaller.addReceivedData(receivedString);
        }
        mCaller.dataInputStreamStopped();
    }

    private String waitForInputStreamData() {
        try {
            return mInputStream.readUTF();
        } catch (IOException e) {
            e.printStackTrace();
            stopDataInputStreamListener();
        }
        return null;
    }
}
