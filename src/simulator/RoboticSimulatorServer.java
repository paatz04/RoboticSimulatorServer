package simulator;

import bluetooth.ConnectionThread;
import bluetooth.ConnectionThreadCaller;
import bluetooth.BluetoothManager;
import bluetooth.BluetoothManagerException;
import simulator.received.ReceivedConnectionDataHandler;
import simulator.received.ReceivedSimulatorDataHandler;
import simulator.received.data.ReceivedSimulatorData;
import vrep.VRepController;
import vrep.VRepControllerCaller;
import vrep.VRepControllerException;

import java.util.*;


public class RoboticSimulatorServer implements ConnectionThreadCaller, VRepControllerCaller {
    private VRepController mSimulator;
    private BluetoothManager mBluetoothManager;
    private ConnectionThread mConnection;

    private boolean mStopped = false;

    private Queue<String> mReceivedConnectionData = new LinkedList<>();
    private Queue<ReceivedSimulatorData> mReceivedSimulatorData = new LinkedList<>();

    private ReceivedConnectionDataHandler mReceivedConnectionDataHandler;
    private ReceivedSimulatorDataHandler mReceivedSimulatorDataHandler;

    public RoboticSimulatorServer() {
        mSimulator = new VRepController(this);
        mBluetoothManager = new BluetoothManager();
    }

    public void startRoboticSimulatorServer() throws RoboticSimulatorServerException {
        System.out.println("RoboticSimulatorServer started");
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

    private synchronized void stopRoboticSimulatorServer() {
        mStopped = true;
        notify();
    }

    private void run() {
        while(!mStopped) {
            try {
                System.out.println("RoboticSimulatorServer wait for new connection");
                mConnection = mBluetoothManager.getBluetoothConnection();
                System.out.println("RoboticSimulatorServer got new connection");
                handleConnection();
                mConnection.stopConnection();
                mConnection = null;
            } catch (BluetoothManagerException e) {
                System.out.println(e.getMessage());
            }
            System.out.println("RoboticSimulatorServer connection closed");
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
        } catch (NoSuchElementException ignored) { }
    }

    private synchronized String popReceivedConnectionData() {
        return mReceivedConnectionData.remove();
    }

    private void handleReceivedSimulatorData() {
        try {
            mReceivedSimulatorDataHandler.handle(popReceivedSimulatorData());
        } catch (NoSuchElementException ignored) {}
    }

    private synchronized ReceivedSimulatorData popReceivedSimulatorData() {
        return mReceivedSimulatorData.remove();
    }

    private synchronized void waitForData() {
        if (mReceivedConnectionData.size() + mReceivedSimulatorData.size() > 0) {
            try {
                wait(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public synchronized void addReceivedConnectionData(String receivedData) {
        mReceivedConnectionData.add(receivedData);
        notify();
    }

    public synchronized void addReceivedSimulatorData(ReceivedSimulatorData receivedData) {
        mReceivedSimulatorData.add(receivedData);
        notify();
    }
}
