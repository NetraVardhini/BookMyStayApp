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

    // Add booking request to queue
    public void addRequest(Reservation reservation) {
        requestQueue.offer(reservation);
        System.out.println("Booking request added for " + reservation.getGuestName());
    }

    // Display all queued requests
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

    // View next request without removing
    public void peekNextRequest() {
        System.out.println("\n===== NEXT REQUEST TO PROCESS =====");

        Reservation next = requestQueue.peek();

        if (next == null) {
            System.out.println("No pending booking requests.");
        } else {
            next.displayReservation();
        }

        System.out.println("===================================\n");
    }
}

// ------------------------------
// Main Class
// ------------------------------
public class Main {
    public static void main(String[] args) {

        // Step 1: Initialize centralized inventory
        RoomInventory inventory = new RoomInventory();

        // Step 2: Register room availability
        inventory.addRoomType("Single", 5);
        inventory.addRoomType("Double", 3);
        inventory.addRoomType("Deluxe", 0);
        inventory.addRoomType("Suite", 2);

        // Step 3: Create room catalog
        HashMap<String, Room> roomCatalog = new HashMap<>();
        roomCatalog.put("Single", new Room("Single", 2000, "AC, WiFi, TV"));
        roomCatalog.put("Double", new Room("Double", 3500, "AC, WiFi, TV, Mini Fridge"));
        roomCatalog.put("Deluxe", new Room("Deluxe", 5000, "AC, WiFi, TV, Mini Fridge, Balcony"));
        roomCatalog.put("Suite", new Room("Suite", 8000, "AC, WiFi, TV, Mini Fridge, Living Area"));

        // Step 4: Search available rooms
        SearchService searchService = new SearchService(inventory, roomCatalog);
        searchService.searchAvailableRooms();

        // Step 5: Initialize booking request queue
        BookingRequestQueue bookingQueue = new BookingRequestQueue();

        // Step 6: Guests submit booking requests
        Reservation r1 = new Reservation("Aarav", "Double", 1);
        Reservation r2 = new Reservation("Priya", "Suite", 1);
        Reservation r3 = new Reservation("Rahul", "Single", 2);
        Reservation r4 = new Reservation("Sneha", "Deluxe", 1);

        bookingQueue.addRequest(r1);
        bookingQueue.addRequest(r2);
        bookingQueue.addRequest(r3);
        bookingQueue.addRequest(r4);

        // Step 7: Display all requests in arrival order
        bookingQueue.displayQueue();

        // Step 8: Show next request to be processed
        bookingQueue.peekNextRequest();

        // Step 9: Show inventory remains unchanged
        System.out.println("Inventory After Booking Requests (Still Unchanged):");
        inventory.displayInventory();
    }
}