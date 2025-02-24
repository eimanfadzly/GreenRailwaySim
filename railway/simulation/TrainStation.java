package railway.simulation;

import java.util.ArrayList;
import java.util.List;

public class TrainStation {
    private String name;
    private int morningLoad, morningUnload;
    private int afternoonLoad, afternoonUnload;
    private int eveningLoad, eveningUnload;
    private int upPlatforms;
    private int downPlatforms;
    private List<TrainStation> connections;

    public TrainStation(String name) {
        this.name = name;
        this.connections = new ArrayList<>();
        this.morningLoad = 0;
        this.morningUnload = 0;
        this.afternoonLoad = 0;
        this.afternoonUnload = 0;
        this.eveningLoad = 0;
        this.eveningUnload = 0;
        this.upPlatforms = 1; // Default value
        this.downPlatforms = 1; // Default value
    }

    public String getName() {
        return name;
    }

    public void addConnection(TrainStation station) {
        if (!connections.contains(station)) {
            connections.add(station);
        }
    }

    public List<TrainStation> getConnections() {
        return connections;
    }

    public void setLoadData(String session, int passengers) {
        switch (session.toLowerCase()) {
            case "morning":
                this.morningLoad = passengers;
                break;
            case "afternoon":
                this.afternoonLoad = passengers;
                break;
            case "evening":
                this.eveningLoad = passengers;
                break;
        }
    }

    public void setUnloadData(String session, int passengers) {
        switch (session.toLowerCase()) {
            case "morning":
                this.morningUnload = passengers;
                break;
            case "afternoon":
                this.afternoonUnload = passengers;
                break;
            case "evening":
                this.eveningUnload = passengers;
                break;
        }
    }

    public int getLoadData(String session) {
        switch (session.toLowerCase()) {
            case "morning": return morningLoad;
            case "afternoon": return afternoonLoad;
            case "evening": return eveningLoad;
            default: return 0;
        }
    }

    public int getUnloadData(String session) {
        switch (session.toLowerCase()) {
            case "morning": return morningUnload;
            case "afternoon": return afternoonUnload;
            case "evening": return eveningUnload;
            default: return 0;
        }
    }

    public int getUpPlatforms() {
        return upPlatforms;
    }

    public void setUpPlatforms(int upPlatforms) {
        this.upPlatforms = upPlatforms;
    }

    public int getDownPlatforms() {
        return downPlatforms;
    }

    public void setDownPlatforms(int downPlatforms) {
        this.downPlatforms = downPlatforms;
    }
}
