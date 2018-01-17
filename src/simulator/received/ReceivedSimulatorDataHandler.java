package simulator.received;

import bluetooth.ConnectionThread;
import simulator.received.data.ReceivedSimulatorData;
import transfer.TransferDataConverter;
import transfer.TransferDataConverterException;

public class ReceivedSimulatorDataHandler {

    private ConnectionThread mConnection;

    public ReceivedSimulatorDataHandler(ConnectionThread connection) {
        mConnection = connection;
    }

    public void handle(ReceivedSimulatorData receivedData) {
        try {
            mConnection.write(TransferDataConverter.getMessageToSend(receivedData));
        } catch (TransferDataConverterException e) {
            System.out.println(e.getMessage());
        }
    }
}
