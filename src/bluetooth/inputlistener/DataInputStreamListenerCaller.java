package bluetooth.inputlistener;

public interface DataInputStreamListenerCaller {
    void addReceivedData(String received);
    void dataInputStreamStopped();
}
