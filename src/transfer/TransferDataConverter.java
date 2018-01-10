package transfer;

import org.jetbrains.annotations.NotNull;
import vrep.RoboticArmPart;

public class TransferDataConverter {

    @NotNull
    public static ReceivedData getReceivedData(String receivedString) throws TransferDataConverterException {
        String[] partsReceivedString = receivedString.split("=");
        if (partsReceivedString.length == 2)
            return handleReceivedDataParts(partsReceivedString[0], partsReceivedString[1]);
        else
            throw new TransferDataConverterException(receivedString + " isn't in correct Format");
    }

    @NotNull
    private static ReceivedData handleReceivedDataParts(String strRoboticArmPart, String strValue) throws TransferDataConverterException {
        RoboticArmPart roboticArmPart = convertStrToRoboticArmPart(strRoboticArmPart);
        float value = convertStrToFloat(strValue);
        return new ReceivedData(roboticArmPart, value);
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

}
