import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public class HospitalQueueSimulation {
    public static void main(String[] args) {
        PriorityQueue queue = new PriorityQueue();
        int numPatients = 10;
        Random rand = new Random();
        
        // Set to track unique arrival times
        Set<Double> usedArrivalTimes = new HashSet<>();
        
        double[] arrivalTimes = new double[numPatients];
        double[] waitingTimes = new double[numPatients];
        double[] serviceTimes = new double[numPatients];
        double[] departureTimes = new double[numPatients];
        
        // Generate patients with unique arrival times
        for (int i = 0; i < numPatients; i++) {
            // Generate a unique arrival time
            double arrivalTime;
            do {
                arrivalTime = PriorityQueue.getPoissonRandom(5);
                // Round to 2 decimal places for practical uniqueness
                arrivalTime = Math.round(arrivalTime * 100) / 100.0;
            } while (usedArrivalTimes.contains(arrivalTime));
            
            usedArrivalTimes.add(arrivalTime);
            
            double serviceTime = PriorityQueue.getGaussianRandom(10, 2);
            int priority = rand.nextInt(2); // 0 = Normal, 1 = Critical
            
            CriticalPatient patient = new CriticalPatient(i + 1, arrivalTime, priority);
            patient.setServiceTime(serviceTime);
            queue.enqueue(patient);
        }
        
        double currentTime = 0;
        int index = 0;
        
        while (!queue.isEmpty()) {
            CriticalPatient patient = queue.dequeue();
            if (currentTime < patient.getArrivalTime()) {
                currentTime = patient.getArrivalTime();
            }
            
            patient.setWaitingTime(currentTime - patient.getArrivalTime());  // Calculate waiting time
            currentTime += patient.getServiceTime();   // Update current time with service time
            patient.setDepartureTime(currentTime); // Calculate departure time
            
            // Store data in arrays
            arrivalTimes[index] = patient.getArrivalTime();
            waitingTimes[index] = patient.getWaitingTime();
            serviceTimes[index] = patient.getServiceTime();
            departureTimes[index] = patient.getDepartureTime();
            index++;
            
            patient.displayInfo();
        }
        
        System.out.println("\nSummary:");
        System.out.printf("%-10s %-15s %-15s %-15s %-15s %-10s\n", "PatientID", "Arrival Time", "Waiting Time", "Service Time", "Departure Time", "Priority");
        for (int i = 0; i < numPatients; i++) {
            CriticalPatient patient = new CriticalPatient(i + 1, arrivalTimes[i], 0); // Temporary object to get priority info
            System.out.printf("%-10d %-15.2f %-15.2f %-15.2f %-15.2f %-10s\n",
                    i + 1, arrivalTimes[i], waitingTimes[i], serviceTimes[i], departureTimes[i], 
                    (patient.getPriority() == 0 ? "Normal" : "Critical"));
        }
    }
}
