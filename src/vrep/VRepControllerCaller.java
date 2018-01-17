package vrep;

import simulator.received.data.ReceivedSimulatorData;

public interface VRepControllerCaller {
    void receivedDataFromVRep(ReceivedSimulatorData receivedDataVRep);
}
