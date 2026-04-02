import java.util.HashMap;
import java.util.Map;

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

    // Add room type with count
    public void addRoomType(String roomType, int count) {
        if (count < 0) {
            System.out.println("Invalid room count for " + roomType);
            return;
        }
        inventory.put(roomType, count);
    }

    // Read-only availability access
    public int getAvailability(String roomType) {
        return inventory.getOrDefault(roomType, 0);
    }

    // Display full inventory
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

    // Search and display only available rooms
    public void searchAvailableRooms() {
        System.out.println("\n===== AVAILABLE ROOMS =====");

        boolean found = false;

        for (Map.Entry<String, Room> entry : roomCatalog.entrySet()) {
            String roomType = entry.getKey();
            Room room = entry.getValue();

            int availableCount = inventory.getAvailability(roomType);

            // Show only available rooms
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
// Main Class
// ------------------------------
public class Main {
    public static void main(String[] args) {

        // Step 1: Initialize centralized inventory
        RoomInventory inventory = new RoomInventory();

        // Step 2: Register room availability
        inventory.addRoomType("Single", 5);
        inventory.addRoomType("Double", 3);
        inventory.addRoomType("Deluxe", 0);   // unavailable
        inventory.addRoomType("Suite", 2);

        // Step 3: Create room catalog (room details)
        HashMap<String, Room> roomCatalog = new HashMap<>();

        roomCatalog.put("Single", new Room("Single", 2000, "AC, WiFi, TV"));
        roomCatalog.put("Double", new Room("Double", 3500, "AC, WiFi, TV, Mini Fridge"));
        roomCatalog.put("Deluxe", new Room("Deluxe", 5000, "AC, WiFi, TV, Mini Fridge, Balcony"));
        roomCatalog.put("Suite", new Room("Suite", 8000, "AC, WiFi, TV, Mini Fridge, Living Area"));

        // Step 4: Display inventory before search
        System.out.println("Inventory Before Search:");
        inventory.displayInventory();

        // Step 5: Guest performs room search
        SearchService searchService = new SearchService(inventory, roomCatalog);
        searchService.searchAvailableRooms();

        // Step 6: Display inventory after search
        // This proves search did not modify system state
        System.out.println("Inventory After Search (Unchanged):");
        inventory.displayInventory();
    }
}