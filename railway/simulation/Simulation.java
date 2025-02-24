package railway.simulation;

import java.util.ArrayList;
import java.util.List;

public class Simulation {
    private RailwayNetwork network;
    private List<Train> trains;
    private String trainLine;
    private String trainDirection;
    private String lastTrainLine;
    private String lastTrainDirection;
    private String lastSession;
    private int lastTotalCO2;
    private int lastTotalComplaints;
    private int lastTotalBoarded;
    private String lastFinalStation;
    private boolean lastRunExists = false;  // Track if a valid run has happened


    public Simulation(RailwayNetwork network) {
        this.network = network;
        this.trains = new ArrayList<>();
    }

    public String getTrainDirection() {
        return trainDirection;
    }
    

    public void setTrainLine(String trainLine, String direction) {
        this.trainLine = trainLine;
        this.trainDirection = direction;
    }

    public void addTrain(String trainLine, String direction, int carriages) {
        TrainStation startStation;
        boolean isInbound = direction.equalsIgnoreCase("Inbound");

        if (isInbound) {
            startStation = network.getStation("Oakleigh"); 
        } else {
            startStation = network.getStation("Flinders Street");
        }

        if (startStation == null) {
            System.out.println(" Error: Starting station not found.");
            return;
        }

        Train newTrain = new Train(startStation, carriages, isInbound);
        trains.add(newTrain);
        System.out.println(" Train added on " + trainLine + " in " + direction + " direction with " + carriages + " carriages.");
    }

    public void listAllTrains() {
        if (trains.isEmpty()) {
            System.out.println(" No trains are currently available.");
            return;
        }

        System.out.println("\n Current Trains in the System:");
        for (int i = 0; i < trains.size(); i++) {
            Train train = trains.get(i);
            System.out.println((i + 1) + ". Train at " + train.getCurrentStation().getName() +
                    " | Direction: " + (train.isInbound() ? "Inbound" : "Outbound") +
                    " | Carriages: " + train.getCarriages());
        }
    }

    public void runSimulation(String session, int carriages, String direction) {
        TrainStation startStation;
        boolean isInbound = direction.equalsIgnoreCase("Inbound");
    
        startStation = isInbound ? network.getStation("Oakleigh") : network.getStation("Flinders Street");
    
        if (startStation == null) {
            System.err.println(" Error: Starting station not found.");
            return;
        }
    
        Train train = new Train(startStation, carriages, isInbound);
        train.resetTrainStats();
        trains.add(train);
    
        int totalCO2 = 0;
        int totalComplaints = 0;
        int totalBoarded = 0;
        String finalStation = "Unknown";
    
        System.out.println("\n===== Train Run (" + session + " Session) =====");
    
        TrainStation currentStation = train.getCurrentStation();
    
        while (currentStation != null) {
            System.out.println("\n Train arriving at " + currentStation.getName());
    
            train.unboardPassengers(session);
            int beforeBoarding = train.getRemainingCapacity();
            train.boardPassengers(session, currentStation.getLoadData(session));
            int afterBoarding = train.getRemainingCapacity();
    
            int boardedPassengers = beforeBoarding - afterBoarding;
            totalBoarded += boardedPassengers;
    
            train.calculateCO2();
            totalCO2 += train.getTotalCO2();
            totalComplaints += train.getTotalComplaints();
    
            if ((isInbound && currentStation.getName().equals("Flinders Street")) ||
                (!isInbound && currentStation.getName().equals("Oakleigh"))) {
                System.out.println(" Train has reached the final station: " + currentStation.getName());
                finalStation = currentStation.getName();
                break;
            }
    
            List<TrainStation> connections = currentStation.getConnections();
            if (connections.isEmpty()) {
                System.out.println(" No more connections. Train stopping.");
                break;
            }
    
            currentStation = isInbound ? connections.get(0) : connections.get(connections.size() - 1);
            train.moveToStation(currentStation);
        }
    
        // Store the last run statistics
        lastTrainLine = trainLine;
        lastTrainDirection = trainDirection;
        lastSession = session;
        lastTotalCO2 = totalCO2;
        lastTotalComplaints = totalComplaints;
        lastTotalBoarded = totalBoarded;
        lastFinalStation = finalStation;
        lastRunExists = true;
    
        System.out.println("\n===== Train Run Summary =====");
        System.out.println(" Total CO2 emitted: " + totalCO2 + " gCO2e");
        System.out.println(" Total complaints: " + totalComplaints);
    }
    
    public void showLastRunStatistics() {
        if (!lastRunExists) {
            System.out.println(" No train run has been simulated yet.");
            return;
        }
    
        System.out.println("\n===== Last Train Run Statistics =====");
        System.out.println(" Train Line: " + lastTrainLine);
        System.out.println(" Direction: " + lastTrainDirection);
        System.out.println(" Session: " + lastSession);
        System.out.println(" Total Passengers Boarded: " + lastTotalBoarded);
        System.out.println(" Final Station: " + lastFinalStation);
        System.out.println(" Total CO2 Emissions: " + lastTotalCO2 + " gCO2e");
        System.out.println(" Total Complaints: " + lastTotalComplaints);
    }
    
    
    public void simulateFullDay() {
        if (trainLine == null || trainDirection == null) {
            System.out.println(" Please specify a train line and direction first (Option 7b).");
            return;
        }
    
        System.out.println("\n Starting Full Day Simulation...");
    
        int defaultCarriages = 3;
        int totalCO2Emissions = 0;
        int totalPassengerComplaints = 0;
    
        String[] sessions = {"Morning", "Afternoon", "Evening"};
        for (String session : sessions) {
            System.out.println("\n===== Running " + session + " Session =====");
    
            // Reset train for each session and capture its stats
            runSimulation(session, defaultCarriages, trainDirection);
    
            // Fetch total CO2 and complaints from train instance
            if (!trains.isEmpty()) {
                Train lastTrain = trains.get(trains.size() - 1); // Get last train that was simulated
    
                // Ensure CO2 is retrieved before resetting train stats
                totalCO2Emissions += lastTrain.getTotalCO2();
                totalPassengerComplaints += lastTrain.getTotalComplaints();
    
                // Reset CO2 and complaints before next session
                lastTrain.resetTrainStats();
            }
        }
    
        System.out.println("\n===== Full Day Simulation Summary =====");
        System.out.println(" Total CO2 Emissions: " + totalCO2Emissions + " gCO2e");
        System.out.println(" Total Passenger Complaints: " + totalPassengerComplaints);
        System.out.println(" Full day simulation completed.");
    }
    
    
    
    
    
}
