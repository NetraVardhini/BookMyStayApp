import java.util.*;

// ------------------------------
// Room Class (Domain Model)
// ------------------------------
class Room {
    private String roomType;
    private double pricePerNight;
    private String amenities;

    public Room(String roomType, double pricePerNight, String amenities) {
        this.roomType = roomType;
        this.pricePerNight = pricePerNight;
        this.amenities = amenities;
    }

    public String getRoomType() {
        return roomType;
    }

    public double getPricePerNight() {
        return pricePerNight;
    }

    public String getAmenities() {
        return amenities;
    }

    public void displayRoomDetails() {
        System.out.println("Room Type       : " + roomType);
        System.out.println("Price Per Night : Rs. " + pricePerNight);
        System.out.println("Amenities       : " + amenities);
    }
}

// ------------------------------
// RoomInventory Class
// ------------------------------
class RoomInventory {
    private HashMap<String, Integer> inventory;

    public RoomInventory() {
        inventory = new HashMap<>();
    }

    public void addRoomType(String roomType, int count) {
        if (count < 0) {
            System.out.println("Invalid room count for " + roomType);
            return;
        }
        inventory.put(roomType, count);
    }

    public int getAvailability(String roomType) {
        return inventory.getOrDefault(roomType, 0);
    }

    public boolean reduceAvailability(String roomType, int roomsNeeded) {
        int available = getAvailability(roomType);

        if (available >= roomsNeeded) {
            inventory.put(roomType, available - roomsNeeded);
            return true;
        }
        return false;
    }

    public void displayInventory() {
        System.out.println("\n===== CURRENT ROOM INVENTORY =====");
        for (Map.Entry<String, Integer> entry : inventory.entrySet()) {
            System.out.println("Room Type: " + entry.getKey() + " | Available Rooms: " + entry.getValue());
        }
        System.out.println("==================================\n");
    }
}

// ------------------------------
// SearchService Class
// ------------------------------
class SearchService {
    private RoomInventory inventory;
    private HashMap<String, Room> roomCatalog;

    public SearchService(RoomInventory inventory, HashMap<String, Room> roomCatalog) {
        this.inventory = inventory;
        this.roomCatalog = roomCatalog;
    }

    public void searchAvailableRooms() {
        System.out.println("\n===== AVAILABLE ROOMS =====");

        boolean found = false;

        for (Map.Entry<String, Room> entry : roomCatalog.entrySet()) {
            String roomType = entry.getKey();
            Room room = entry.getValue();

            int availableCount = inventory.getAvailability(roomType);

            if (availableCount > 0) {
                found = true;
                room.displayRoomDetails();
                System.out.println("Available Rooms : " + availableCount);
                System.out.println("----------------------------------");
            }
        }

        if (!found) {
            System.out.println("No rooms are currently available.");
        }

        System.out.println("==============================\n");
    }
}

// ------------------------------
// Reservation Class
// ------------------------------
class Reservation {
    private String guestName;
    private String roomType;
    private int numberOfRooms;

    public Reservation(String guestName, String roomType, int numberOfRooms) {
        this.guestName = guestName;
        this.roomType = roomType;
        this.numberOfRooms = numberOfRooms;
    }

    public String getGuestName() {
        return guestName;
    }

    public String getRoomType() {
        return roomType;
    }

    public int getNumberOfRooms() {
        return numberOfRooms;
    }

    public void displayReservation() {
        System.out.println("Guest Name   : " + guestName);
        System.out.println("Room Type    : " + roomType);
        System.out.println("Rooms Needed : " + numberOfRooms);
    }
}

// ------------------------------
// BookingRequestQueue Class
// ------------------------------
class BookingRequestQueue {
    private Queue<Reservation> requestQueue;

    public BookingRequestQueue() {
        requestQueue = new LinkedList<>();
    }

    public void addRequest(Reservation reservation) {
        requestQueue.offer(reservation);
        System.out.println("Booking request added for " + reservation.getGuestName());
    }

    public Reservation getNextRequest() {
        return requestQueue.poll();
    }

    public boolean isEmpty() {
        return requestQueue.isEmpty();
    }

    public void displayQueue() {
        System.out.println("\n===== BOOKING REQUEST QUEUE =====");

        if (requestQueue.isEmpty()) {
            System.out.println("No booking requests in queue.");
            System.out.println("=================================\n");
            return;
        }

        int position = 1;
        for (Reservation reservation : requestQueue) {
            System.out.println("Request Position: " + position);
            reservation.displayReservation();
            System.out.println("----------------------------------");
            position++;
        }

        System.out.println("=================================\n");
    }
}

// ------------------------------
// ConfirmedReservation Class
// ------------------------------
class ConfirmedReservation {
    private String reservationId;
    private String guestName;
    private String roomType;
    private List<String> allocatedRoomIds;

    public ConfirmedReservation(String reservationId, String guestName, String roomType, List<String> allocatedRoomIds) {
        this.reservationId = reservationId;
        this.guestName = guestName;
        this.roomType = roomType;
        this.allocatedRoomIds = allocatedRoomIds;
    }

    public String getReservationId() {
        return reservationId;
    }

    public String getGuestName() {
        return guestName;
    }

    public String getRoomType() {
        return roomType;
    }

    public List<String> getAllocatedRoomIds() {
        return allocatedRoomIds;
    }

    public void displayConfirmedReservation() {
        System.out.println("Reservation ID : " + reservationId);
        System.out.println("Guest Name     : " + guestName);
        System.out.println("Room Type      : " + roomType);
        System.out.println("Allocated IDs  : " + allocatedRoomIds);
    }
}

// ------------------------------
// BookingService Class
// ------------------------------
class BookingService {
    private RoomInventory inventory;
    private BookingRequestQueue bookingQueue;

    private Set<String> allocatedRoomIds;
    private HashMap<String, Set<String>> allocatedRoomsByType;
    private HashMap<String, ConfirmedReservation> confirmedReservations;
    private int reservationCounter = 1;

    public BookingService(RoomInventory inventory, BookingRequestQueue bookingQueue) {
        this.inventory = inventory;
        this.bookingQueue = bookingQueue;
        this.allocatedRoomIds = new HashSet<>();
        this.allocatedRoomsByType = new HashMap<>();
        this.confirmedReservations = new HashMap<>();
    }

    private String generateRoomId(String roomType) {
        String prefix = roomType.substring(0, 2).toUpperCase();
        int roomNumber = 1;
        String roomId;

        do {
            roomId = prefix + String.format("%03d", roomNumber);
            roomNumber++;
        } while (allocatedRoomIds.contains(roomId));

        return roomId;
    }

    private String generateReservationId() {
        return "RES" + String.format("%03d", reservationCounter++);
    }

    public void processNextBooking() {
        System.out.println("\n===== PROCESSING NEXT BOOKING =====");

        if (bookingQueue.isEmpty()) {
            System.out.println("No booking requests available.");
            System.out.println("===================================\n");
            return;
        }

        Reservation reservation = bookingQueue.getNextRequest();

        String guestName = reservation.getGuestName();
        String roomType = reservation.getRoomType();
        int roomsNeeded = reservation.getNumberOfRooms();

        System.out.println("Processing request for: " + guestName);

        int available = inventory.getAvailability(roomType);

        if (available >= roomsNeeded) {
            Set<String> assignedRoomSet = allocatedRoomsByType.getOrDefault(roomType, new HashSet<>());
            List<String> assignedRoomList = new ArrayList<>();

            for (int i = 0; i < roomsNeeded; i++) {
                String roomId = generateRoomId(roomType);
                allocatedRoomIds.add(roomId);
                assignedRoomSet.add(roomId);
                assignedRoomList.add(roomId);
            }

            allocatedRoomsByType.put(roomType, assignedRoomSet);
            inventory.reduceAvailability(roomType, roomsNeeded);

            String reservationId = generateReservationId();
            ConfirmedReservation confirmed = new ConfirmedReservation(
                    reservationId, guestName, roomType, assignedRoomList
            );

            confirmedReservations.put(reservationId, confirmed);

            System.out.println("Booking Confirmed!");
            confirmed.displayConfirmedReservation();
        } else {
            System.out.println("Booking Failed!");
            System.out.println("Not enough rooms available for room type: " + roomType);
        }

        System.out.println("===================================\n");
    }

    public void processAllBookings() {
        while (!bookingQueue.isEmpty()) {
            processNextBooking();
        }
    }

    public void displayConfirmedReservations() {
        System.out.println("\n===== CONFIRMED RESERVATIONS =====");

        if (confirmedReservations.isEmpty()) {
            System.out.println("No confirmed reservations.");
            System.out.println("==================================\n");
            return;
        }

        for (ConfirmedReservation reservation : confirmedReservations.values()) {
            reservation.displayConfirmedReservation();
            System.out.println("----------------------------------");
        }

        System.out.println("==================================\n");
    }

    public HashMap<String, ConfirmedReservation> getConfirmedReservations() {
        return confirmedReservations;
    }
}

// ------------------------------
// AddOnService Class
// ------------------------------
class AddOnService {
    private String serviceName;
    private double serviceCost;

    public AddOnService(String serviceName, double serviceCost) {
        this.serviceName = serviceName;
        this.serviceCost = serviceCost;
    }

    public String getServiceName() {
        return serviceName;
    }

    public double getServiceCost() {
        return serviceCost;
    }

    public void displayService() {
        System.out.println(serviceName + " - Rs. " + serviceCost);
    }
}

// ------------------------------
// AddOnServiceManager Class
// ------------------------------
class AddOnServiceManager {
    private HashMap<String, List<AddOnService>> reservationServicesMap;

    public AddOnServiceManager() {
        reservationServicesMap = new HashMap<>();
    }

    // Add a service to a reservation
    public void addServiceToReservation(String reservationId, AddOnService service) {
        List<AddOnService> services = reservationServicesMap.getOrDefault(reservationId, new ArrayList<>());
        services.add(service);
        reservationServicesMap.put(reservationId, services);

        System.out.println(service.getServiceName() + " added to Reservation ID: " + reservationId);
    }

    // Display services for a reservation
    public void displayServicesForReservation(String reservationId) {
        System.out.println("\n===== ADD-ON SERVICES FOR " + reservationId + " =====");

        List<AddOnService> services = reservationServicesMap.get(reservationId);

        if (services == null || services.isEmpty()) {
            System.out.println("No add-on services selected.");
            System.out.println("=========================================\n");
            return;
        }

        for (AddOnService service : services) {
            service.displayService();
        }

        System.out.println("=========================================\n");
    }

    // Calculate total add-on cost
    public double calculateTotalServiceCost(String reservationId) {
        List<AddOnService> services = reservationServicesMap.get(reservationId);

        if (services == null) {
            return 0;
        }

        double total = 0;
        for (AddOnService service : services) {
            total += service.getServiceCost();
        }

        return total;
    }

    // Display service bill
    public void displayServiceBill(String reservationId) {
        System.out.println("\n===== ADD-ON SERVICE BILL =====");
        displayServicesForReservation(reservationId);
        System.out.println("Total Add-On Cost for " + reservationId + " : Rs. " + calculateTotalServiceCost(reservationId));
        System.out.println("================================\n");
    }
}

// ------------------------------
// Main Class
// ------------------------------
public class Main {
    public static void main(String[] args) {

        // Step 1: Initialize inventory
        RoomInventory inventory = new RoomInventory();

        // Step 2: Register room availability
        inventory.addRoomType("Single", 5);
        inventory.addRoomType("Double", 3);
        inventory.addRoomType("Deluxe", 2);
        inventory.addRoomType("Suite", 2);

        // Step 3: Create room catalog
        HashMap<String, Room> roomCatalog = new HashMap<>();
        roomCatalog.put("Single", new Room("Single", 2000, "AC, WiFi, TV"));
        roomCatalog.put("Double", new Room("Double", 3500, "AC, WiFi, TV, Mini Fridge"));
        roomCatalog.put("Deluxe", new Room("Deluxe", 5000, "AC, WiFi, TV, Mini Fridge, Balcony"));
        roomCatalog.put("Suite", new Room("Suite", 8000, "AC, WiFi, TV, Mini Fridge, Living Area"));

        // Step 4: Show available rooms
        SearchService searchService = new SearchService(inventory, roomCatalog);
        searchService.searchAvailableRooms();

        // Step 5: Create booking queue
        BookingRequestQueue bookingQueue = new BookingRequestQueue();

        // Step 6: Add booking requests
        bookingQueue.addRequest(new Reservation("Aarav", "Double", 1));
        bookingQueue.addRequest(new Reservation("Priya", "Suite", 1));
        bookingQueue.addRequest(new Reservation("Rahul", "Single", 2));

        // Step 7: Process all bookings
        BookingService bookingService = new BookingService(inventory, bookingQueue);
        bookingService.processAllBookings();

        // Step 8: Display confirmed reservations
        bookingService.displayConfirmedReservations();

        // Step 9: Initialize Add-On Service Manager
        AddOnServiceManager serviceManager = new AddOnServiceManager();

        // Step 10: Add services to existing reservations
        serviceManager.addServiceToReservation("RES001", new AddOnService("Breakfast", 500));
        serviceManager.addServiceToReservation("RES001", new AddOnService("Airport Pickup", 1200));
        serviceManager.addServiceToReservation("RES002", new AddOnService("Extra Bed", 800));
        serviceManager.addServiceToReservation("RES002", new AddOnService("Spa Access", 1500));
        serviceManager.addServiceToReservation("RES003", new AddOnService("Dinner Package", 1000));

        // Step 11: Display services and cost
        serviceManager.displayServiceBill("RES001");
        serviceManager.displayServiceBill("RES002");
        serviceManager.displayServiceBill("RES003");

        // Step 12: Show inventory remains unchanged after add-ons
        System.out.println("Inventory After Add-On Selection (Unchanged):");
        inventory.displayInventory();
    }
}