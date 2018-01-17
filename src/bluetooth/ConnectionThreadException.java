package bluetooth;

import javax.bluetooth.BluetoothConnectionException;

public class ConnectionThreadException extends Exception {
    public ConnectionThreadException(String message) {
        super(message);
    }
}
