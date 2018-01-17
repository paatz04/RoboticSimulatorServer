package transfer;

import simulator.received.data.ReceivedConnectionData;
import simulator.received.data.ReceivedSimulatorData;
import simulator.received.data.RoboticArmPart;
import simulator.received.data.RoboticSensorPart;

import static simulator.received.data.RoboticSensorPart.COLOR_GRAB;

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
        return getStringOfType(receivedDataVRep.getRoboticSensorPart()) + "=" + receivedDataVRep.getValue();
    }

    private static String getStringOfType(RoboticSensorPart roboticSensorPart) throws TransferDataConverterException {
        switch (roboticSensorPart) {
            case COLOR_GRAB:
                return "COLOR_GRAB";
            case SCORE_RED:
                return "SCORE_RED";
            case SCORE_BLUE:
                return "SCORE_BLUE";
            case SCORE_GREEN:
                return "SCORE_GREEN";
            case SCORE_MISSED:
                return "SCORE_MISSED";
            default:
                throw new TransferDataConverterException("RoboticSensorPart not correct");
        }
    }
}
