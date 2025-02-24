package railway.simulation;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ConfigManager {
    private RailwayNetwork network;

    public ConfigManager(RailwayNetwork network) {
        this.network = network;
    }

    public void loadStationsFromCSV(String filePath) {
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            boolean firstLine = true;
            List<String[]> connections = new ArrayList<>();
    
            while ((line = br.readLine()) != null) {
                if (firstLine) {
                    firstLine = false;
                    continue;
                }
    
                String[] data = line.split(",");
                if (data.length >= 3) {
                    String stationName = data[0].trim();
                    String upside = data[1].trim();
                    String downside = data[2].trim();
    
                    // Add station to network
                    network.addStation(stationName);
    
                    // Ensure valid connections
                    if (!upside.equals("_")) connections.add(new String[]{stationName, upside});
                    if (!downside.equals("_")) connections.add(new String[]{stationName, downside});
                }
            }
    
            // Now connect stations AFTER all stations are added
            for (String[] pair : connections) {
                if (network.getStation(pair[1]) != null) {
                    network.connectStations(pair[0], pair[1]);
                } else {
                    System.err.println(" Skipping invalid station connection: " + pair[0] + " -> " + pair[1]);
                }
            }
    
            System.out.println(" Stations successfully loaded from " + filePath);
        } catch (IOException e) {
            System.err.println(" Error loading stations file: " + e.getMessage());
        }
    }
    

    public void loadPassengerDemandFromCSV(String filePath) {
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            boolean firstLine = true;
            List<String> stationNames = new ArrayList<>();
    
            System.out.println(" Importing passenger demand data from: " + filePath);
    
            while ((line = br.readLine()) != null) {
                String[] data = line.split(",");
    
                if (firstLine) {
                    stationNames = Arrays.asList(Arrays.copyOfRange(data, 2, data.length)); // Store actual station names
                    firstLine = false;
                    continue;
                }
    
                if (data.length < 3) {
                    System.out.println(" Warning: Skipping invalid row: " + line);
                    continue;
                }
    
                String activity = data[0].trim();  // Load or Unload
                String session = data[1].trim();  // Morning, Afternoon, Evening
    
                for (int i = 2; i < data.length; i++) {
                    String stationName = stationNames.get(i - 2).trim(); // Match correct station
    
                    TrainStation station = network.getStation(stationName);
                    if (station == null) {
                        System.out.println(" Warning: Station \"" + stationName + "\" not found in network. Skipping.");
                        continue;
                    }
    
                    int passengers;
                    try {
                        passengers = Integer.parseInt(data[i].trim());
                    } catch (NumberFormatException e) {
                        System.out.println(" Warning: Skipping invalid number at station " + stationName);
                        continue;
                    }
    
                    if (activity.equalsIgnoreCase("Load")) {
                        station.setLoadData(session, passengers);
                    } else if (activity.equalsIgnoreCase("Unload")) {
                        station.setUnloadData(session, passengers);
                    }
                }
            }
    
            System.out.println(" Passenger demand successfully loaded from " + filePath);
        } catch (IOException e) {
            System.err.println(" Error loading passenger demand file: " + e.getMessage());
        }
    }
    
    
    
    
}
