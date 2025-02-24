package railway.simulation;

import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        RailwayNetwork network = new RailwayNetwork();
        ConfigManager configManager = new ConfigManager(network);
        Simulation simulation = new Simulation(network);

        while (true) {
            System.out.println("\nGreen Railway Sim Main Menu");
            System.out.println("1) Register / update a station");
            System.out.println("   1a) Station details (including line name, connections, platforms)");
            System.out.println("   2a) Add / remove platforms");
            System.out.println("2) Register / update a station’s demand");
            System.out.println("3) Import train stations map CSV");
            System.out.println("4) Import a train station’s demand");
            System.out.println("5) Show train stations map");
            System.out.println("6) Show a train station’s demand");
            System.out.println("7) Simulate a train run");
            System.out.println("   7a) Add/remove a carriage");
            System.out.println("   7b) Specify line and direction");
            System.out.println("   7c) Run sim");
            System.out.println("8) Simulate a day");
            System.out.println("   8b) List All Trains");
            System.out.println("9) Show statistics");
            System.out.println("10) Exit");
            System.out.print("Enter choice: ");

            String choice = scanner.nextLine().trim();

            switch (choice) {
                case "1":
                    registerOrUpdateStation(scanner, network);
                    break;
                case "1a":
                    System.out.print("Enter station name: ");
                    String stationName = scanner.nextLine().trim();
                    network.showStationDetails(stationName);
                    break;
                case "2a":
                    System.out.print("Enter station name: ");
                    stationName = scanner.nextLine().trim();
                    System.out.print("Would you like to add (A) or remove (R) platforms? ");
                    String action = scanner.nextLine().trim().toUpperCase();

                    if (!action.equals("A") && !action.equals("R")) {
                        System.out.println(" Invalid choice! Please enter 'A' to add or 'R' to remove.");
                        break;
                    }

                    boolean isAdding = action.equals("A");
                    System.out.print("Enter number of platforms to " + (isAdding ? "add" : "remove") + ": ");
                    int platformCount;

                    try {
                        platformCount = Integer.parseInt(scanner.nextLine().trim());
                        if (platformCount < 1) {
                            System.out.println(" Error: Platform count must be at least 1.");
                            break;
                        }
                        network.addRemovePlatforms(stationName, isAdding, platformCount);
                    } catch (NumberFormatException e) {
                        System.out.println(" Invalid input! Please enter a valid number.");
                    }
                    break;
                case "2":
                    registerOrUpdateStationDemand(scanner, network);
                    break;
                case "3":
                    System.out.print("Enter train stations map CSV file path: ");
                    String mapFilePath = scanner.nextLine().trim();
                    network.importTrainStationsCSV(mapFilePath);
                    break;
                case "4":
                    importTrainStationDemand(scanner, network);
                    break;
                case "5":
                    network.printNetwork();
                    break;
                case "6":
                    showStationDemand(scanner, network);
                    break;
                case "7":
                    String session; // Declare session before using it
                
                    while (true) {
                        System.out.print("Enter session (Morning/Afternoon/Evening): ");
                        session = scanner.nextLine().trim();
                
                        if (session.equalsIgnoreCase("Morning") || session.equalsIgnoreCase("Afternoon") || session.equalsIgnoreCase("Evening")) {
                            break; // Valid session, exit loop
                        } else {
                            System.out.println(" Invalid session! Please enter 'Morning', 'Afternoon', or 'Evening'.");
                        }
                    }
                
                    int carriages = 5; // Default carriage count
                
                    System.out.print("Would you like to manually adjust carriages? (Y/N): ");
                    String adjustCarriages = scanner.nextLine().trim().toLowerCase();
                
                    if (adjustCarriages.equals("y")) {
                        while (true) {
                            System.out.print("Enter number of carriages (1-5): ");
                            try {
                                carriages = Integer.parseInt(scanner.nextLine().trim());
                                if (carriages < 1 || carriages > 5) {
                                    System.out.println(" Invalid input! Please enter a number between 1 and 5.");
                                } else {
                                    break;
                                }
                            } catch (NumberFormatException e) {
                                System.out.println(" Invalid input! Please enter a valid number.");
                            }
                        }
                    }
                
                    System.out.print("Enter direction (Inbound/Outbound): ");
                    String direction = scanner.nextLine().trim();
                    simulation.runSimulation(session, carriages, direction);

                case "7a":
                    addOrRemoveCarriages(scanner);
                    break;
                case "7b":
                    specifyTrainLineAndDirection(scanner, simulation);
                    break;
                case "7c":
                    runTrainSimulation(scanner, simulation);
                    break;
                case "8":
                    simulation.simulateFullDay();
                    break;
                case "8b":
                    simulation.listAllTrains();
                    break;
                case "9":
                    simulation.showLastRunStatistics();
                    break;
                case "10":
                    System.out.println("Exiting...");
                    scanner.close();
                    return;
                default:
                    System.out.println(" Invalid choice! Please enter a valid option (1-10).");
            }
        }
    }

    private static void registerOrUpdateStation(Scanner scanner, RailwayNetwork network) {
        System.out.print("Enter station name: ");
        String stationName = scanner.nextLine().trim();

        System.out.print("Enter new connections (comma-separated): ");
        String connectionsInput = scanner.nextLine().trim();
        List<String> connections = Arrays.asList(connectionsInput.split("\\s*,\\s*")); // Split input into a list

        network.registerOrUpdateStation(stationName, connections);

    }
    

    private static void registerOrUpdateStationDemand(Scanner scanner, RailwayNetwork network) {
        System.out.print("Enter station name: ");
        String stationName = scanner.nextLine().trim();
    
        TrainStation station = network.getStation(stationName);
        if (station == null) {
            System.out.println(" Error: Station \"" + stationName + "\" does not exist!");
            return;
        }
    
        System.out.println(" Updating passenger demand for " + stationName + "...");
        
        int morningLoad = getValidPassengerCount(scanner, "morning load");
        int morningUnload = getValidPassengerCount(scanner, "morning unload");
        int afternoonLoad = getValidPassengerCount(scanner, "afternoon load");
        int afternoonUnload = getValidPassengerCount(scanner, "afternoon unload");
        int eveningLoad = getValidPassengerCount(scanner, "evening load");
        int eveningUnload = getValidPassengerCount(scanner, "evening unload");
    
        station.setLoadData("Morning", morningLoad);
        station.setUnloadData("Morning", morningUnload);
        station.setLoadData("Afternoon", afternoonLoad);
        station.setUnloadData("Afternoon", afternoonUnload);
        station.setLoadData("Evening", eveningLoad);
        station.setUnloadData("Evening", eveningUnload);
    
        System.out.println("✅ Demand updated successfully for " + stationName + ":");
        System.out.println("   Morning: Load " + morningLoad + ", Unload " + morningUnload);
        System.out.println("   Afternoon: Load " + afternoonLoad + ", Unload " + afternoonUnload);
        System.out.println("   Evening: Load " + eveningLoad + ", Unload " + eveningUnload);
    }
    
    
    // Helper method to validate passenger input.
    
    private static int getValidPassengerCount(Scanner scanner, String message) {
        int passengers;
        while (true) {
            System.out.print("Enter " + message + " (0 - 5000): ");
            String input = scanner.nextLine().trim();
            try {
                passengers = Integer.parseInt(input);
                if (passengers < 0 || passengers > 5000) {
                    System.out.println(" Error: Passenger values must be between 0 and 5000.");
                } else {
                    return passengers;
                }
            } catch (NumberFormatException e) {
                System.out.println(" Invalid input! Please enter a valid number.");
            }
        }
    }
    

    private static void importStationsCSV(Scanner scanner, ConfigManager configManager) {
        System.out.print("Enter CSV file path for stations: ");
        String filePath = scanner.nextLine().trim();
        configManager.loadStationsFromCSV(filePath);
    }

    private static void importStationDemandCSV(Scanner scanner, ConfigManager configManager) {
        System.out.print("Enter CSV file path for station demand: ");
        String filePath = scanner.nextLine().trim();
        configManager.loadPassengerDemandFromCSV(filePath);
    }

    private static void importTrainStationDemand(Scanner scanner, RailwayNetwork network) {
        System.out.print("Enter train station demand CSV file path: ");
        String filePath = scanner.nextLine().trim();
    
        System.out.println(" Importing passenger demand data from: " + filePath);
        network.loadPassengerData(filePath);
    }

    private static void showStationDemand(Scanner scanner, RailwayNetwork network) {
        System.out.print("Enter station name: ");
        String stationName = scanner.nextLine().trim();
    
        TrainStation station = network.getStation(stationName);
        if (station == null) {
            System.out.println(" Error: Station \"" + stationName + "\" does not exist!");
            return;
        }
    
        // Display passenger demand for the station
        System.out.println("\n Passenger Demand for " + stationName + ":");
        System.out.println("   Morning:   Load " + station.getLoadData("Morning") + ", Unload " + station.getUnloadData("Morning"));
        System.out.println("   Afternoon: Load " + station.getLoadData("Afternoon") + ", Unload " + station.getUnloadData("Afternoon"));
        System.out.println("   Evening:   Load " + station.getLoadData("Evening") + ", Unload " + station.getUnloadData("Evening"));
    }
    
    private static void simulateTrainRun(Scanner scanner, Simulation simulation) {
        String session;
        
        while (true) {
            System.out.print("Enter session (Morning/Afternoon/Evening): ");
            session = scanner.nextLine().trim();
            
            if (session.equalsIgnoreCase("Morning") || session.equalsIgnoreCase("Afternoon") || session.equalsIgnoreCase("Evening")) {
                break;
            } else {
                System.out.println(" Invalid session! Please enter 'Morning', 'Afternoon', or 'Evening'.");
            }
        }
    
        int carriages = 5; // Default number of carriages
    
        System.out.print("Would you like to manually adjust carriages? (Y/N): ");
        String adjustCarriages = scanner.nextLine().trim().toLowerCase();
    
        if (adjustCarriages.equals("y")) {
            while (true) {
                System.out.print("Enter number of carriages (1-5): ");
                try {
                    carriages = Integer.parseInt(scanner.nextLine().trim());
                    if (carriages < 1 || carriages > 5) {
                        System.out.println(" Invalid input! Please enter a number between 1 and 5.");
                    } else {
                        break;
                    }
                } catch (NumberFormatException e) {
                    System.out.println(" Invalid input! Please enter a valid number.");
                }
            }
        }
    
        // Pass both `session` and `carriages` to the simulation
        simulation.runSimulation(session, carriages, simulation.getTrainDirection());
    }
    

    private static void simulateFullDay(Scanner scanner, Simulation simulation) {
        System.out.println(" Simulate a Full Day (To be implemented)");
    }

    private static void showStatistics(Scanner scanner, Simulation simulation) {
        System.out.println(" Show Statistics (To be implemented)");
    }

    private static void addOrRemoveCarriages(Scanner scanner) {
        System.out.println("\n Modify Train Carriages");
        
        System.out.print("Would you like to (A)dd or (R)emove a carriage? ");
        String action = scanner.nextLine().trim().toLowerCase();
    
        int carriages = 5; // Default value
    
        if (action.equals("a")) {
            System.out.print("Enter number of carriages to add (1-5): ");
        } else if (action.equals("r")) {
            System.out.print("Enter number of carriages to remove (1-5): ");
        } else {
            System.out.println(" Invalid choice! Please enter 'A' to add or 'R' to remove.");
            return;
        }
    
        try {
            int change = Integer.parseInt(scanner.nextLine().trim());
            if (change < 1 || change > 5) {
                System.out.println(" Invalid input! Must be between 1 and 5.");
                return;
            }
    
            if (action.equals("a")) {
                carriages = Math.min(5, carriages + change);
                System.out.println(" Added " + change + " carriages. Total carriages now: " + carriages);
            } else {
                carriages = Math.max(1, carriages - change);
                System.out.println(" Removed " + change + " carriages. Total carriages now: " + carriages);
            }
    
        } catch (NumberFormatException e) {
            System.out.println(" Invalid input! Please enter a valid number.");
        }
    }

    public static void specifyTrainLineAndDirection(Scanner scanner, Simulation simulation) {
        System.out.print("Enter train line (e.g., Pakenham Line): ");
        String trainLine = scanner.nextLine().trim();
    
        // Validate if the entered train line is valid
        List<String> validLines = Arrays.asList("Pakenham Line", "Cranbourne Line");
        if (!validLines.contains(trainLine)) {
            System.out.println(" Invalid train line! Please enter a valid line (e.g., Pakenham Line, Cranbourne Line).");
            return;
        }
    
        System.out.print("Enter direction (Inbound/Outbound): ");
        String direction = scanner.nextLine().trim();
    
        if (!direction.equalsIgnoreCase("Inbound") && !direction.equalsIgnoreCase("Outbound")) {
            System.out.println(" Invalid direction! Please enter 'Inbound' or 'Outbound'.");
            return;
        }
    
        System.out.println(" Train set for " + trainLine + " in " + direction.toLowerCase() + " direction.");
        simulation.setTrainLine(trainLine, direction);
    }
    

    private static void runTrainSimulation(Scanner scanner, Simulation simulation) {
        if (simulation.getTrainDirection() == null) {
            System.out.println(" No train line and direction specified! Set it first using option 7b.");
            return;
        }
    
        System.out.print("Enter session (Morning/Afternoon/Evening): ");
        String session = scanner.nextLine().trim();
    
        int carriages = 5; // Default number of carriages
    
        System.out.print("Would you like to manually adjust carriages? (Y/N): ");
        String adjustCarriages = scanner.nextLine().trim().toLowerCase();
    
        if (adjustCarriages.equals("y")) {
            while (true) {
                System.out.print("Enter number of carriages (1-5): ");
                try {
                    carriages = Integer.parseInt(scanner.nextLine().trim());
                    if (carriages < 1 || carriages > 5) {
                        System.out.println(" Invalid input! Please enter a number between 1 and 5.");
                    } else {
                        break;
                    }
                } catch (NumberFormatException e) {
                    System.out.println(" Invalid input! Please enter a valid number.");
                }
            }
        }
    
        // Use the stored direction from 7b instead of asking again
        simulation.runSimulation(session, carriages, simulation.getTrainDirection());
    }
    
    
    
    
    
}
