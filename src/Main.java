import simulator.RoboticSimulatorServer;
import simulator.RoboticSimulatorServerException;
import vrep.VRepControllerException;

public class Main {
    // ToDo: show status from the components (e.g. started, finished)
    // ToDo: check if Simulation is still running (don't reconnect. Show message and shut down the server)
    public static void main(String[] args) {
        RoboticSimulatorServer roboticSimulatorServer = new RoboticSimulatorServer();
        try {
            roboticSimulatorServer.startRoboticSimulatorServer();
        }catch (RoboticSimulatorServerException e) {
            System.out.println("Error: " + e.getMessage());
            System.out.println("Couldn't start RoboticSimulationServer.");
        }
    }
}
