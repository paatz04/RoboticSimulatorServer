import simulator.RoboticSimulatorServer;
import vrep.VRepControllerException;

public class Main {
    public static void main(String[] args) {
        RoboticSimulatorServer roboticSimulatorServer = new RoboticSimulatorServer();
        try {
            roboticSimulatorServer.startRoboticSimulatorServer();
        }catch (VRepControllerException e) {
            System.out.println("Couldn't start RoboticSimulationServer.");
            System.out.println("Error: " + e.getMessage());
        }
    }
}
