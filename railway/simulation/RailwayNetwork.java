package railway.simulation;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class RailwayNetwork {
    private Map<String, TrainStation> stations = new LinkedHashMap<>();

    public void addStation(String name) {
        if (!stations.containsKey(name)) {
            stations.put(name, new TrainStation(name));
        }
    }

    public void connectStations(String stationA, String stationB) {
        if (stationA.equals(stationB)) return; // Avoid self-connection
    
        TrainStation sA = stations.get(stationA);
        TrainStation sB = stations.get(stationB);
    
        if (sA != null && sB != null) {
            if (!sA.getConnections().contains(sB)) {
                sA.addConnection(sB);
                System.out.println(" Connected " + stationA + " ↔ " + stationB);
            }
            if (!sB.getConnections().contains(sA)) {
                sB.addConnection(sA); // Ensure reverse connection is also made
            }
        } else {
            System.err.println(" Error: Could not connect " + stationA + " to " + stationB);
        }
    }
    
    
    public TrainStation getStation(String name) {
        return stations.get(name);
    }

    public void printNetwork() {
        System.out.println("\n Railway Network:");
        for (TrainStation station : stations.values()) {
            StringBuilder connectionsList = new StringBuilder();
            for (TrainStation connectedStation : station.getConnections()) {
                connectionsList.append(connectedStation.getName()).append(", ");
            }
            String connectionsOutput = connectionsList.length() > 0 
                ? connectionsList.substring(0, connectionsList.length() - 2) // Remove last comma
                : "None";
    
            System.out.println("Station: " + station.getName() + " (Connections: " + station.getConnections().size() + ") → [" + connectionsOutput + "]");
        }
    }

    public void showStationDetails(String stationName) {
        TrainStation station = getStation(stationName);
        if (station == null) {
            System.out.println(" Error: Station \"" + stationName + "\" does not exist!");
            return;
        }

        System.out.println("\n Station Details: " + station.getName());
        System.out.println("   - Number of Connections: " + station.getConnections().size());
        System.out.println("   - Connected Stations: ");
        for (TrainStation connectedStation : station.getConnections()) {
            System.out.println("     • " + connectedStation.getName());
        }
        System.out.println("   - Up Platforms: " + station.getUpPlatforms());
        System.out.println("   - Down Platforms: " + station.getDownPlatforms());
    }

    public void addRemovePlatforms(String stationName, boolean isAdding, int platformCount) {
        TrainStation station = getStation(stationName);
        if (station == null) {
            System.out.println(" Error: Station \"" + stationName + "\" does not exist!");
            return;
        }
        
        if (isAdding) {
            station.setUpPlatforms(station.getUpPlatforms() + platformCount);
            station.setDownPlatforms(station.getDownPlatforms() + platformCount);
            System.out.println(" Added " + platformCount + " platforms to " + stationName + ".");
        } else {
            if (station.getUpPlatforms() - platformCount < 1 || station.getDownPlatforms() - platformCount < 1) {
                System.out.println(" Error: Cannot remove platforms. Each station must have at least one platform in each direction.");
                return;
            }
            station.setUpPlatforms(station.getUpPlatforms() - platformCount);
            station.setDownPlatforms(station.getDownPlatforms() - platformCount);
            System.out.println(" Removed " + platformCount + " platforms from " + stationName + ".");
        }
    }
    

    public void registerOrUpdateStation(String stationName, List<String> connections) {
    if (!stations.containsKey(stationName)) {
        System.out.println(" Station does not exist. Creating new station...");
        addStation(stationName);
    } else {
        System.out.println(" Updating existing station...");
    }

    TrainStation station = stations.get(stationName);

    for (String conn : connections) {
        conn = conn.trim();
        if (conn.isEmpty() || conn.equals("_")) {
            continue; // Skip invalid station names
        }

        if (!stations.containsKey(conn)) {
            System.out.println(" Warning: " + conn + " does not exist. Creating it...");
            addStation(conn); // Ensure the connection exists before adding
        }

        connectStations(stationName, conn);
    }

    System.out.println(" Station " + stationName + " updated successfully.");
    }

    public void registerOrUpdateStationDemand(Scanner scanner) {
    System.out.print("Enter station name: ");
    String stationName = scanner.nextLine().trim();

    TrainStation station = stations.get(stationName);
    if (station == null) {
        System.out.println(" Error: Station \"" + stationName + "\" does not exist!");
        return;
    }

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

    System.out.println(" Demand updated successfully for " + stationName + ":");
    System.out.println("   Morning: Load " + morningLoad + ", Unload " + morningUnload);
    System.out.println("   Afternoon: Load " + afternoonLoad + ", Unload " + afternoonUnload);
    System.out.println("   Evening: Load " + eveningLoad + ", Unload " + eveningUnload);
}


// Helper method to get a valid passenger count from the user.

private int getValidPassengerCount(Scanner scanner, String message) {
    int passengers;
    while (true) {
        System.out.print("Enter " + message + " (0 - 5000): ");
        String input = scanner.nextLine().trim();
        try {
            passengers = Integer.parseInt(input);
            if (passengers < 0 || passengers > 5000) {
                System.out.println(" Error: Passenger values must be between 0 and 5000.");
            } else {
                break;
            }
        } catch (NumberFormatException e) {
            System.out.println(" Invalid input! Please enter a valid number.");
        }
    }
    return passengers;
}

    public void importTrainStationsCSV(String filePath) {
    try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
        String line;
        boolean firstLine = true; // Skip CSV header
        List<String[]> connections = new ArrayList<>();

        while ((line = br.readLine()) != null) {
            if (firstLine) {
                firstLine = false;
                continue;
            }

            String[] data = line.split(",");
            if (data.length < 3) {
                System.err.println(" Skipping invalid row: " + line);
                continue;
            }

            String stationName = data[0].trim();
            String upside = data[1].trim();
            String downside = data[2].trim();

            //  Add station if not already added
            addStation(stationName);

            //  Store connections for later
            connections.add(new String[]{stationName, upside});
            connections.add(new String[]{stationName, downside});
        }

        //  Now connect the stations
        for (String[] pair : connections) {
            String station = pair[0];
            String connectedStation = pair[1].trim();

            if (!connectedStation.equals("_")) { 
                if (getStation(connectedStation) != null) {
                    connectStations(station, connectedStation);
                } else {
                    System.err.println(" Warning: Cannot connect " + station + " -> " + connectedStation + " (Station does not exist)");
                }
            }
            
        }

        System.out.println(" Train stations map successfully imported from " + filePath);
    } catch (IOException e) {
        System.err.println(" Error loading train stations map: " + e.getMessage());
    }
}

public void loadPassengerData(String filePath) {
    try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
        String line;
        boolean isFirstRow = true;
        List<String> stationNames = new ArrayList<>();

        System.out.println(" Importing passenger demand data from: " + filePath);

        while ((line = br.readLine()) != null) {
            String[] data = line.split(",");

            //  Skip headers correctly
            if (isFirstRow) {
                stationNames = Arrays.asList(Arrays.copyOfRange(data, 2, data.length)); // Get actual station names
                isFirstRow = false;
                continue;
            }

            if (data.length < 3) {
                System.out.println(" Warning: Skipping invalid row: " + line);
                continue;
            }

            String activity = data[0].trim(); // Load or Unload
            String session = data[1].trim();  // Morning, Afternoon, Evening

            for (int i = 2; i < data.length; i++) {
                String stationName = stationNames.get(i - 2).trim(); // Match station correctly

                TrainStation station = getStation(stationName);
                if (station == null) {
                    System.out.println(" Warning: Station \"" + stationName + "\" not found in network. Skipping.");
                    continue;
                }

                try {
                    int passengers = Integer.parseInt(data[i].trim());

                    if (activity.equalsIgnoreCase("Load")) {
                        station.setLoadData(session, passengers);
                    } else if (activity.equalsIgnoreCase("Unload")) {
                        station.setUnloadData(session, passengers);
                    }
                } catch (NumberFormatException e) {
                    System.out.println(" Warning: Skipping invalid number at station " + stationName);
                }
            }
        }

        System.out.println(" Passenger demand successfully loaded from " + filePath);
    } catch (IOException e) {
        System.err.println(" Error loading passenger demand: " + e.getMessage());
    }
}
}









