package transfer;

import simulator.received.data.ReceivedConnectionData;
import simulator.received.data.ReceivedSimulatorData;
import simulator.received.data.RoboticArmPart;

public class TransferDataConverter {

    public static ReceivedConnectionData getReceivedBluetoothData(String receivedString) throws TransferDataConverterException {
        String[] partsReceivedString = receivedString.split("=");
        if (partsReceivedString.length == 2)
            return handleReceivedBluetoothDataParts(partsReceivedString[0], partsReceivedString[1]);
        else
            throw new TransferDataConverterException(receivedString + " isn't in correct Format");
    }

    private static ReceivedConnectionData handleReceivedBluetoothDataParts(String strRoboticArmPart, String strValue) throws TransferDataConverterException {
        RoboticArmPart roboticArmPart = convertStrToRoboticArmPart(strRoboticArmPart);
        float value = convertStrToFloat(strValue);
        return new ReceivedConnectionData(roboticArmPart, value);
    }

    private static RoboticArmPart convertStrToRoboticArmPart(String strRoboticArmPart) throws TransferDataConverterException {
        switch(strRoboticArmPart) {
            case "GRAB":
                return RoboticArmPart.GRAB;
            case "TIP":
                return RoboticArmPart.TIP;
            case "BODY":
                return RoboticArmPart.BODY;
            case "ROTATION":
                return RoboticArmPart.ROTATION;
            default:
                throw new TransferDataConverterException("First part (RoboticArmPart) of received Data isn't correct: " + strRoboticArmPart);
        }
    }

    private static float convertStrToFloat(String strValue) throws TransferDataConverterException {
        try {
            return Float.parseFloat(strValue);
        }catch(NumberFormatException e){
            throw new TransferDataConverterException("Second part (Value) of received Data isn't Float: " + strValue);
        }
    }

    public static String getMessageToSend(ReceivedSimulatorData receivedDataVRep) throws TransferDataConverterException {
        // ToDo
        return null;
    }

}
