package simulator;

import bluetooth.ConnectionThread;
import bluetooth.ConnectionCaller;
import bluetooth.BluetoothManager;
import bluetooth.BluetoothManagerException;
import simulator.received.ReceivedConnectionDataHandler;
import simulator.received.ReceivedSimulatorDataHandler;
import simulator.received.data.ReceivedSimulatorData;
import vrep.VRepController;
import vrep.VRepControllerCaller;
import vrep.VRepControllerException;

import java.util.EmptyStackException;
import java.util.Stack;

public class RoboticSimulatorServer implements ConnectionCaller, VRepControllerCaller {
    private VRepController mSimulator;
    private BluetoothManager mBluetoothManager;
    private ConnectionThread mConnection;

    private boolean mStopped = false;

    private Stack<String> mReceivedConnectionData = new Stack<>();
    private Stack<ReceivedSimulatorData> mReceivedSimulatorData = new Stack<>();

    private ReceivedConnectionDataHandler mReceivedConnectionDataHandler;
    private ReceivedSimulatorDataHandler mReceivedSimulatorDataHandler;

    public RoboticSimulatorServer() {
        mSimulator = new VRepController(this);
        mBluetoothManager = new BluetoothManager();
    }

    public void startRoboticSimulatorServer() throws RoboticSimulatorServerException {
        startSimulatorThread();
        startBluetoothManager();
        run();
        stopRoboticSimulatorServer();
    }

    private void startSimulatorThread() throws RoboticSimulatorServerException {
        try {
            mSimulator.start();
        } catch (VRepControllerException e) {
            throw new RoboticSimulatorServerException("Couldn't start VRepController");
        }
    }

    private void startBluetoothManager() throws RoboticSimulatorServerException {
        try {
            mBluetoothManager.startBluetoothManager();
        } catch (BluetoothManagerException e) {
            throw new RoboticSimulatorServerException("Couldn't start BluetoothManager.");
        }
    }

    public synchronized void stopRoboticSimulatorServer() {
        mStopped = true;
        notify();
    }

    private void run() {
        while(!mStopped) {
            try {
                mConnection = mBluetoothManager.getBluetoothConnection();
                handleConnection();
                mConnection.stopConnection();
                mConnection = null;
            } catch (BluetoothManagerException e) {
                System.out.println(e.getMessage());
            }
            System.out.println("Server: Connection Closed");
        }
        if (mConnection != null)
            mConnection.stopConnection();
        mBluetoothManager.stopBluetoothManager();
        mSimulator.stopVRepController();
        System.out.println("Server: finished");
    }

    private void handleConnection() {
        mConnection.startConnectionThread(this);
        initReceivedDataHandler();
        while (!mStopped && !mConnection.isConnectionStopped()) {
            handleReceivedConnectionData();
            handleReceivedSimulatorData();
            if (!mStopped)
                waitForData();
        }
    }

    private void initReceivedDataHandler() {
        mReceivedConnectionDataHandler = new ReceivedConnectionDataHandler(mSimulator);
        mReceivedSimulatorDataHandler = new ReceivedSimulatorDataHandler(mConnection);
    }

    private void handleReceivedConnectionData() {
        try {
            mReceivedConnectionDataHandler.handle(popReceivedConnectionData());
        } catch (EmptyStackException ignored) { }
    }

    private synchronized String popReceivedConnectionData() {
        return mReceivedConnectionData.pop();
    }

    private void handleReceivedSimulatorData() {
        try {
            mReceivedSimulatorDataHandler.handle(popReceivedSimulatorData());
        } catch (EmptyStackException ignored) {}
    }

    private synchronized ReceivedSimulatorData popReceivedSimulatorData() {
        return mReceivedSimulatorData.pop();
    }

    private synchronized void waitForData() {
        if (mReceivedConnectionData.size() + mReceivedSimulatorData.size() > 0) {
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public synchronized void addReceivedConnectionData(String receivedData) {
        mReceivedConnectionData.push(receivedData);
        notify();
    }

    public synchronized void addReceivedSimulatorData(ReceivedSimulatorData receivedData) {
        mReceivedSimulatorData.push(receivedData);
        notify();
    }
}
