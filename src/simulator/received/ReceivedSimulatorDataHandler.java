package simulator.received;

import bluetooth.ConnectionThread;
import simulator.received.data.ReceivedSimulatorData;

public class ReceivedSimulatorDataHandler {

    private ConnectionThread mConnection;

    public ReceivedSimulatorDataHandler(ConnectionThread connection) {
        mConnection = connection;
    }

    public void handle(ReceivedSimulatorData receivedData) {
        // ToDo
    }
}
