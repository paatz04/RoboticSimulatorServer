package vrep;

import simulator.ReceivedDataVRep;

public interface VRepControllerCaller {
    void receivedDataFromVRep(ReceivedDataVRep receivedDataVRep);
}
