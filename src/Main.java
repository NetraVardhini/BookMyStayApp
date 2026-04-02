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

    // Decrease availability after successful allocation
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
        return requestQueue.poll(); // FIFO removal
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
// BookingService Class
// ------------------------------
class BookingService {
    private RoomInventory inventory;
    private BookingRequestQueue bookingQueue;

    // Stores all allocated room IDs globally
    private Set<String> allocatedRoomIds;

    // Maps room type -> allocated room IDs
    private HashMap<String, Set<String>> allocatedRoomsByType;

    public BookingService(RoomInventory inventory, BookingRequestQueue bookingQueue) {
        this.inventory = inventory;
        this.bookingQueue = bookingQueue;
        this.allocatedRoomIds = new HashSet<>();
        this.allocatedRoomsByType = new HashMap<>();
    }

    // Generate unique room ID
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

    // Process one booking request
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
            Set<String> assignedRoomIds = allocatedRoomsByType.getOrDefault(roomType, new HashSet<>());

            System.out.println("Booking Confirmed!");
            System.out.println("Guest Name   : " + guestName);
            System.out.println("Room Type    : " + roomType);
            System.out.println("Rooms Given  : " + roomsNeeded);
            System.out.print("Assigned IDs : ");

            for (int i = 0; i < roomsNeeded; i++) {
                String roomId = generateRoomId(roomType);

                // Store globally and by room type
                allocatedRoomIds.add(roomId);
                assignedRoomIds.add(roomId);

                System.out.print(roomId + " ");
            }

            allocatedRoomsByType.put(roomType, assignedRoomIds);

            // Inventory update immediately after allocation
            inventory.reduceAvailability(roomType, roomsNeeded);

            System.out.println("\nReservation successfully confirmed.");
        } else {
            System.out.println("Booking Failed!");
            System.out.println("Not enough rooms available for room type: " + roomType);
        }

        System.out.println("===================================\n");
    }

    // Process all booking requests
    public void processAllBookings() {
        while (!bookingQueue.isEmpty()) {
            processNextBooking();
        }
    }

    // Display allocated rooms
    public void displayAllocatedRooms() {
        System.out.println("\n===== ALLOCATED ROOM REPORT =====");

        if (allocatedRoomsByType.isEmpty()) {
            System.out.println("No rooms allocated yet.");
            System.out.println("=================================\n");
            return;
        }

        for (Map.Entry<String, Set<String>> entry : allocatedRoomsByType.entrySet()) {
            System.out.println("Room Type: " + entry.getKey());
            System.out.println("Allocated Room IDs: " + entry.getValue());
            System.out.println("----------------------------------");
        }

        System.out.println("=================================\n");
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
        inventory.addRoomType("Deluxe", 1);
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
        bookingQueue.addRequest(new Reservation("Sneha", "Deluxe", 1));
        bookingQueue.addRequest(new Reservation("Kiran", "Deluxe", 1)); // should fail if no rooms left

        // Step 7: Display queue
        bookingQueue.displayQueue();

        // Step 8: Process all bookings in FIFO order
        BookingService bookingService = new BookingService(inventory, bookingQueue);
        bookingService.processAllBookings();

        // Step 9: Show allocated room report
        bookingService.displayAllocatedRooms();

        // Step 10: Show updated inventory after allocation
        System.out.println("Inventory After Reservation Confirmation:");
        inventory.displayInventory();
    }
}