# Green Railway Sim - README

## **📌 Project Overview**
Green Railway Sim is a **Java-based terminal application** designed to optimize train operations by reducing carbon footprint and managing passenger flow efficiently. The program supports **station management, train scheduling, CO₂ emissions tracking, and passenger demand handling**.

## **🛠 System Requirements**
- **Java Development Kit (JDK 21) or later**
- **Any terminal (Command Prompt, PowerShell, or Terminal on macOS/Linux)**
- **CSV files:** Ensure that the required CSV files (`a1-map-pakenham.csv`, `a1-inbound-pakenham.csv`, `a1-outbound-pakenham.csv`) are present in the same directory as the program.

## **🚀 How to Compile and Run the Program**
### **Using Terminal or Command Prompt**
1. Navigate to the project directory:
   ```sh
   cd path/to/your/project
   ```
2. Compile the Java files:
   ```sh
   javac railway/simulation/*.java
   ```
3. Run the program:
   ```sh
   java railway.simulation.Main
   ```

---
## **📜 Menu Options & How to Test Each Feature**

### **1️⃣ Register / Update a Station**
- **How to test:**
  - Choose `1` and enter a new station name.
  - Provide **connections to other stations** (comma-separated).
- **Example Input:**
  ```
  Enter station name: Clayton
  Enter new connections: Huntingdale, Westall
  ```
- **Expected Output:**
  ```
  ✅ Station Clayton updated successfully.
  ```

### **1a️⃣ Show Station Details**
- **How to test:**
  - Choose `1a` and enter a station name.
- **Example Input:**
  ```
  Enter station name: Clayton
  ```
- **Expected Output:**
  ```
  📍 Station Details: Clayton
  - Connected Stations: Huntingdale, Westall
  - Up Platforms: 1
  - Down Platforms: 1
  ```

### **2️⃣ Register / Update a Station's Demand**
- **How to test:**
  - Choose `2`, enter a **station name**, and provide **passenger demand per session**.
- **Example Input:**
  ```
  Enter station name: Clayton
  Enter morning load: 300
  Enter morning unload: 150
  Enter afternoon load: 250
  Enter afternoon unload: 100
  Enter evening load: 200
  Enter evening unload: 90
  ```
- **Expected Output:**
  ```
  ✅ Demand updated successfully for Clayton.
  ```

### **3️⃣ Import Train Stations Map CSV**
- **How to test:**
  - Choose `3` and provide the path to the train station map file.
- **Example Input:**
  ```
  Enter train stations map CSV file path: a1-map-pakenham.csv
  ```
- **Expected Output:**
  ```
  ✅ Train stations map successfully imported from a1-map-pakenham.csv.
  ```

### **4️⃣ Import Train Station Demand CSV**
- **How to test:**
  - Choose `4` and provide the path to the demand file.
- **Example Input:**
  ```
  Enter train station demand CSV file path: a1-inbound-pakenham.csv
  ```
- **Expected Output:**
  ```
  ✅ Passenger demand successfully loaded from a1-inbound-pakenham.csv.
  ```

### **5️⃣ Show Train Stations Map**
- **How to test:**
  - Choose `5`.
- **Expected Output:**
  ```
  🚆 Railway Network:
  Station: Flinders Street → [Richmond]
  Station: Richmond → [Flinders Street, South Yarra]
  ```

### **6️⃣ Show a Train Station’s Demand**
- **How to test:**
  - Choose `6` and enter a station name.
- **Example Input:**
  ```
  Enter station name: Clayton
  ```
- **Expected Output:**
  ```
  📊 Passenger Demand for Clayton:
  Morning: Load 300, Unload 150
  Afternoon: Load 250, Unload 100
  Evening: Load 200, Unload 90
  ```

### **7️⃣ Simulate a Train Run**
- **How to test:**
  - Choose `7b` to **set train line and direction**.
  - Choose `7c` to **run train simulation**.
- **Example Inputs:**
  ```
  Enter train line: Pakenham Line
  Enter direction (Inbound/Outbound): Inbound
  ```
  ```
  Enter session (Morning/Afternoon/Evening): Morning
  ```
- **Expected Output:**
  ```
  🚆 Train has reached the final station: Flinders Street
  🌍 Total CO₂ Emissions: 1500 gCO2e
  🚨 Total Complaints: 45
  ```

### **8️⃣ Simulate a Full Day** (⚠️ Partially Implemented)
- **How to test:**
  - Choose `8`.
- **Expected Output:**
  ```
  🌍 Total CO₂ Emissions for the Day: 4500 gCO2e
  🚨 Total Passenger Complaints: 130
  ```

### **9️⃣ Show Last Train Run Statistics**
- **How to test:**
  - Choose `9`.
- **Expected Output:**
  ```
  🚆 Last Train Run Statistics
  Train Line: Pakenham Line
  🌍 Total CO₂ Emissions: 1500 gCO2e
  🚨 Total Complaints: 45
  ```

### **🔟 Exit the Program**
- **How to test:**
  - Choose `10` to exit.


