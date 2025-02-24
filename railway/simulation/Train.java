package railway.simulation;

public class Train {
    private TrainStation currentStation;
    private int carriages;
    private int onboardPassengers;
    private int totalComplaints;
    private int totalCO2;
    private boolean isInbound;

    private final int CAPACITY_PER_CARRIAGE = 100; // Each carriage holds 100 passengers

    public Train(TrainStation startStation, int initialCarriages, boolean isInbound) {
        this.currentStation = startStation;
        this.carriages = Math.max(1, Math.min(initialCarriages, 5)); // Ensure carriages are between 1-5
        this.onboardPassengers = 0;
        this.totalComplaints = 0;
        this.totalCO2 = 0;
        this.isInbound = isInbound;
    }

    public TrainStation getCurrentStation() {
        return currentStation;
    }

    public int getTotalCO2() {
        return totalCO2;
    }

    public int getTotalComplaints() {
        return totalComplaints;
    }

    public int getCarriages() {
        return carriages;
    }

    public void setCarriages(int carriages) {
        this.carriages = Math.max(1, Math.min(carriages, 5)); // Ensure valid range
    }

    public void moveToStation(TrainStation nextStation) {
        this.currentStation = nextStation;
    }

    // Get remaining available seats in train
    public int getRemainingCapacity() {
        return (carriages * CAPACITY_PER_CARRIAGE) - onboardPassengers;
    }

    public void boardPassengers(String session, int boardingPassengers) {
        int availableSeats = getRemainingCapacity();
        int actualBoarding = Math.min(availableSeats, boardingPassengers);
    
        onboardPassengers += actualBoarding;
    
        int remainingAtStation = currentStation.getLoadData(session) - actualBoarding;
        remainingAtStation = Math.max(remainingAtStation, 0); // Prevent negative values
        currentStation.setLoadData(session, remainingAtStation);
    
        // Stop boarding when the train reaches full capacity
        if (getRemainingCapacity() == 0) {
            System.out.println("    Train is full! No more passengers can board.");
        }
    
        System.out.println("   Boarded: " + actualBoarding + ", Left Behind: " + remainingAtStation);
    }
    
    public void resetTrainStats() {
        this.totalCO2 = 0;  // Reset CO2 before a new run starts
        this.totalComplaints = 0; // Reset complaints
        this.onboardPassengers = 0; // Ensure passengers don’t persist across runs
    }
    
    public void boardPassengers(String session) {
        boardPassengers(session, currentStation.getLoadData(session));
    }

    public void unboardPassengers(String session) {
        if (onboardPassengers > 0) {  // Only unboard if passengers are onboard
            int unload = Math.min(onboardPassengers, currentStation.getUnloadData(session));
            onboardPassengers -= unload;
            System.out.println("   Unboarded: " + unload + " (Remaining onboard: " + onboardPassengers + ")");
        }
    }
    

    public void calculateCO2() {
        int co2PerCarriage = 50; // Assume each carriage emits 50g CO2 per stop
        this.totalCO2 += carriages * co2PerCarriage;

        System.out.println("  CO2 Emitted So Far: " + totalCO2 + " gCO2e");
    }

    public boolean isInbound() {
        return isInbound;
    }
}
