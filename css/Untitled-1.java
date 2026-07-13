import java.util.*;

// ==========================================
// 1. CUSTOM SYSTEM EXCEPTIONS
// ==========================================
class AuthenticationException extends Exception {
    public AuthenticationException(String msg) { super(msg); }
}

class InvalidRouteException extends Exception {
    public InvalidRouteException(String msg) { super(msg); }
}

// ==========================================
// 2. CORE DOMAIN MODEL ENTITIES
// ==========================================
enum BookingStatus { BOOKED, CANCELLED }

class Ticket {
    private String ticketID;
    private String sourceStation;
    private String destinationStation;
    private double totalFare;
    private int numberOfSeats;
    private BookingStatus status;

    public Ticket(String sourceStation, String destinationStation, double totalFare, int numberOfSeats) {
        this.ticketID = "TKT-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        this.sourceStation = sourceStation;
        this.destinationStation = destinationStation;
        this.totalFare = totalFare;
        this.numberOfSeats = numberOfSeats;
        this.status = BookingStatus.BOOKED;
    }

    public String getTicketID() { return ticketID; }
    public String getSource() { return sourceStation; }
    public String getDestination() { return destinationStation; }
    public double getFare() { return totalFare; }
    public int getSeats() { return numberOfSeats; }
    public BookingStatus getStatus() { return status; }
    
    public void setStatus(BookingStatus status) { this.status = status; }

    public void displayTicketSummary() {
        System.out.println("-------------------------------------------------");
        System.out.println("               METRO PASSENGER TICKET            ");
        System.out.println("-------------------------------------------------");
        System.out.println(" Ticket ID    : " + ticketID);
        System.out.println(" Journey      : " + sourceStation + " -> " + destinationStation);
        System.out.println(" Passengers   : " + numberOfSeats + " Commuter(s)");
        System.out.println(" Total Cost   : $" + String.format("%.2f", totalFare));
        System.out.println(" Status       : " + status);
        System.out.println("-------------------------------------------------");
    }
}

class User {
    private String username;
    private String password;
    private String contactNumber;
    private List<Ticket> bookingHistory;

    public User(String username, String password, String contactNumber) {
        this.username = username;
        this.password = password;
        this.contactNumber = contactNumber;
        this.bookingHistory = new ArrayList<>();
    }

    public String getUsername() { return username; }
    public boolean checkPassword(String pass) { return this.password.equals(pass); }
    public List<Ticket> getBookingHistory() { return bookingHistory; }
    public void addTicketToHistory(Ticket t) { bookingHistory.add(t); }
}

// ==========================================
// 3. SERVICE & TRANSIT CONFIGURATION MANAGERS
// ==========================================
class MetroRoute {
    // Static system layout representing the primary Metro line stations
    public static final String[] STATIONS = {
        "Terminal-1", "Aerocity", "West-End", "Central-Hub", "Business-District", "South-Park", "Terminal-2"
    };
    
    private static final double BASE_FARE = 10.0;
    private static final double RATE_PER_STATION = 5.0;
    private static int availableInventory = 50; // Maximum safe dynamic dynamic load limit

    public static void displayRouteMap() {
        System.out.println("\n--- ACTIVE METRO LINE ROUTE ---");
        for (int i = 0; i < STATIONS.length; i++) {
            System.out.print("[" + i + "] " + STATIONS[i]);
            if (i < STATIONS.length - 1) System.out.print(" <-> ");
        }
        System.out.println("\n-------------------------------");
    }

    public static int getStationIndex(String name) {
        for (int i = 0; i < STATIONS.length; i++) {
            if (STATIONS[i].equalsIgnoreCase(name.trim())) return i;
        }
        return -1;
    }

    public static double calculateFare(int srcIdx, int destIdx) throws InvalidRouteException {
        if (srcIdx == -1 || destIdx == -1) {
            throw new InvalidRouteException("Error: One or both stations do not exist on this network line.");
        }
        if (srcIdx == destIdx) {
            throw new InvalidRouteException("Error: Source and Destination stations cannot be identical.");
        }
        int physicalDistance = Math.abs(destIdx - srcIdx);
        return BASE_FARE + (physicalDistance * RATE_PER_STATION);
    }

    public static int getAvailableInventory() { return availableInventory; }
    public static void deductInventory(int count) { availableInventory -= count; }
    public static void restoreInventory(int count) { availableInventory += count; }
}

// ==========================================
// 4. MAIN CENTRAL APPLICATION SYSTEM
// ==========================================
public class MetroSystemApp {
    private static Map<String, User> userDatabase = new HashMap<>();
    private static User currentUser = null;
    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        seedInitialData();
        while (true) {
            if (currentUser == null) {
                renderGuestMenu();
            } else {
                renderSessionMenu();
            }
        }
    }

    private static void seedInitialData() {
        // Pre-configuring basic student user account credentials for testing convenience
        userDatabase.put("student", new User("student", "pass123", "555-0199"));
    }

    private static void renderGuestMenu() {
        System.out.println("\n=============================================");
        System.out.println("      METRO TICKET BOOKING SYSTEM INFRA      ");
        System.out.println("=============================================");
        System.out.println("1. New User Registration");
        System.out.println("2. Secure System Login");
        System.out.println("3. System Exit");
        System.out.print("Please enter choice: ");
        
        String selection = scanner.nextLine();
        switch (selection) {
            case "1": executeRegistration(); break;
            case "2": executeLogin(); break;
            case "3": 
                System.out.println("\nThank you for using our Transit System infrastructure. Goodbye.");
                System.exit(0);
            default: System.out.println("System Alert: Invalid action selection token.");
        }
    }

    private static void renderSessionMenu() {
        System.out.println("\n=============================================");
        System.out.println("   WELCOME BACK, " + currentUser.getUsername().toUpperCase() + " ");
        System.out.println("=============================================");
        System.out.println("1. View Route Station System Map");
        System.out.println("2. Book Electronic Metro Ticket");
        System.out.println("3. Cancel Active Booking Voucher");
        System.out.println("4. Display My Trip History Records");
        System.out.println("5. Secure Session Logout");
        System.out.print("Please enter choice: ");

        String selection = scanner.nextLine();
        switch (selection) {
            case "1": MetroRoute.displayRouteMap(); break;
            case "2": executeTicketBooking(); break;
            case "3": executeTicketCancellation(); break;
            case "4": displayUserLogs(); break;
            case "5": 
                System.out.println("Session closed. Goodbye " + currentUser.getUsername());
                currentUser = null; 
                break;
            default: System.out.println("System Alert: Invalid action selection token.");
        }
    }

    private static void executeRegistration() {
        System.out.println("\n--- USER ACCOUNT PROFILE CREATION ---");
        System.out.print("Choose unique username: ");
        String user = scanner.nextLine().trim();
        if(user.isEmpty() || userDatabase.containsKey(user)) {
            System.out.println("Error: Username criteria conflict or empty entry.");
            return;
        }
        System.out.print("Set secure account password: ");
        String pass = scanner.nextLine();
        System.out.print("Enter active contact telephone: ");
        String contact = scanner.nextLine();

        User newUser = new User(user, pass, contact);
        userDatabase.put(user, newUser);
        System.out.println("Success: System registration complete. Please log in now.");
    }

    private static void executeLogin() {
        System.out.println("\n--- SECURE SYSTEM LOGIN ENTRY ---");
        System.out.print("Enter registered username: ");
        String user = scanner.nextLine();
        System.out.print("Enter security password string: ");
        String pass = scanner.nextLine();

        try {
            if (!userDatabase.containsKey(user) || !userDatabase.get(user).checkPassword(pass)) {
                throw new AuthenticationException("Access Denied: Bad security parameter values mismatch.");
            }
            currentUser = userDatabase.get(user);
            System.out.println("Access Authorized: Core framework interface loaded successfully.");
        } catch (AuthenticationException e) {
            System.out.println(e.getMessage());
        }
    }

    private static void executeTicketBooking() {
        System.out.println("\n--- NEW TICKET REQUISITION DESK ---");
        MetroRoute.displayRouteMap();
        
        System.out.print("Input Source Station Name: ");
        String src = scanner.nextLine();
        System.out.print("Input Destination Station Name: ");
        String dest = scanner.nextLine();
        
        int srcIdx = MetroRoute.getStationIndex(src);
        int destIdx = MetroRoute.getStationIndex(dest);

        try {
            double individualFare = MetroRoute.calculateFare(srcIdx, destIdx);
            System.out.print("Enter required ticket headcount seat capacity: ");
            int seats = Integer.parseInt(scanner.nextLine());

            if (seats <= 0) {
                System.out.println("Processing Aborted: Seat order matrix size bounds error.");
                return;
            }
            if (seats > MetroRoute.getAvailableInventory()) {
                System.out.println("Processing Aborted: Insufficient remaining coach structural load capacity.");
                return;
            }

            double finalNetCost = individualFare * seats;
            System.out.println("Calculated Net Routing Cost Estimation: $" + String.format("%.2f", finalNetCost));
            System.out.print("Confirm processing payment simulation? (yes/no): ");
            String promptRes = scanner.nextLine();

            if (promptRes.equalsIgnoreCase("yes")) {
                System.out.println("Contacting Gateway... Authorization verified.");
                MetroRoute.deductInventory(seats);
                Ticket confirmedTicket = new Ticket(MetroRoute.STATIONS[srcIdx], MetroRoute.STATIONS[destIdx], finalNetCost, seats);
                currentUser.addTicketToHistory(confirmedTicket);
                System.out.println("\nTransaction Complete: Ticket generated successfully.");
                confirmedTicket.displayTicketSummary();
            } else {
                System.out.println("Transaction Cancelled by User.");
            }

        } catch (InvalidRouteException | NumberFormatException e) {
            System.out.println("Error context parsing issue: " + e.getMessage());
        }
    }

    private static void executeTicketCancellation() {
        System.out.println("\n--- TRANSACTION REVERSAL PROCESSING ---");
        List<Ticket> history = currentUser.getBookingHistory();
        List<Ticket> activeTickets = new ArrayList<>();

        for (Ticket t : history) {
            if (t.getStatus() == BookingStatus.BOOKED) activeTickets.add(t);
        }

        if (activeTickets.isEmpty()) {
            System.out.println("No operational active tickets available for cancellation matching your user scope.");
            return;
        }

        for (int i = 0; i < activeTickets.size(); i++) {
            System.out.print("[" + i + "] ");
            activeTickets.get(i).displayTicketSummary();
        }

        System.out.print("Select active ticket index token for reversal execution: ");
        try {
            int targetIdx = Integer.parseInt(scanner.nextLine());
            if (targetIdx < 0 || targetIdx >= activeTickets.size()) {
                System.out.println("Execution Aborted: System selection array element out-of-bounds error.");
                return;
            }

            Ticket targetTicket = activeTickets.get(targetIdx);
            targetTicket.setStatus(BookingStatus.CANCELLED);
            MetroRoute.restoreInventory(targetTicket.getSeats());
            System.out.println("Processing Complete. Reversed ticket refund total amount: $" + String.format("%.2f", targetTicket.getFare()));
            System.out.println("System Log: Core inventory pools incremented safely.");
        } catch (NumberFormatException e) {
            System.out.println("Processing Aborted: Invalid selection character entry.");
        }
    }

    private static void displayUserLogs() {
        System.out.println("\n--- ALL HISTORIC ARCHIVED TRAVEL LOGS ---");
        List<Ticket> history = currentUser.getBookingHistory();
        if (history.isEmpty()) {
            System.out.println("No historically indexed digital transaction metadata found for this profile.");
            return;
        }
        for (Ticket t : history) {
            t.displayTicketSummary();
        }
    }
}