import java.util.HashMap;
import java.util.Map;

// ------------------------------
// RoomInventory Class
// ------------------------------
class RoomInventory {
    private HashMap<String, Integer> inventory;

    // Constructor initializes inventory
    public RoomInventory() {
        inventory = new HashMap<>();
    }

    // Register a room type with available count
    public void addRoomType(String roomType, int count) {
        if (count < 0) {
            System.out.println("Invalid room count for " + roomType);
            return;
        }
        inventory.put(roomType, count);
        System.out.println(roomType + " added with " + count + " rooms.");
    }

    // Get current availability of a room type
    public int getAvailability(String roomType) {
        return inventory.getOrDefault(roomType, 0);
    }

    // Book rooms (decrease availability)
    public boolean bookRoom(String roomType, int roomsNeeded) {
        if (!inventory.containsKey(roomType)) {
            System.out.println("Room type '" + roomType + "' does not exist.");
            return false;
        }

        int available = inventory.get(roomType);

        if (roomsNeeded <= 0) {
            System.out.println("Invalid booking quantity.");
            return false;
        }

        if (available >= roomsNeeded) {
            inventory.put(roomType, available - roomsNeeded);
            System.out.println(roomsNeeded + " " + roomType + " room(s) booked successfully.");
            return true;
        } else {
            System.out.println("Booking failed! Only " + available + " " + roomType + " room(s) available.");
            return false;
        }
    }

    // Cancel booking / add back rooms (increase availability)
    public boolean cancelBooking(String roomType, int roomsToAdd) {
        if (!inventory.containsKey(roomType)) {
            System.out.println("Room type '" + roomType + "' does not exist.");
            return false;
        }

        if (roomsToAdd <= 0) {
            System.out.println("Invalid cancellation quantity.");
            return false;
        }

        int available = inventory.get(roomType);
        inventory.put(roomType, available + roomsToAdd);

        System.out.println(roomsToAdd + " " + roomType + " room(s) restored successfully.");
        return true;
    }

    // Update room count directly (controlled update)
    public boolean updateAvailability(String roomType, int newCount) {
        if (!inventory.containsKey(roomType)) {
            System.out.println("Room type '" + roomType + "' does not exist.");
            return false;
        }

        if (newCount < 0) {
            System.out.println("Availability cannot be negative.");
            return false;
        }

        inventory.put(roomType, newCount);
        System.out.println(roomType + " availability updated to " + newCount);
        return true;
    }

    // Display full inventory
    public void displayInventory() {
        System.out.println("\n===== CURRENT ROOM INVENTORY =====");
        if (inventory.isEmpty()) {
            System.out.println("No room types registered.");
            return;
        }

        for (Map.Entry<String, Integer> entry : inventory.entrySet()) {
            System.out.println("Room Type: " + entry.getKey() + " | Available Rooms: " + entry.getValue());
        }
        System.out.println("==================================\n");
    }
}

// ------------------------------
// Main Application Class
// ------------------------------
public class BookMyStayApp {
    public static void main(String[] args) {

        // Step 1: Initialize inventory component
        RoomInventory inventory = new RoomInventory();

        // Step 2: Register room types with available counts
        inventory.addRoomType("Single", 10);
        inventory.addRoomType("Double", 7);
        inventory.addRoomType("Deluxe", 5);
        inventory.addRoomType("Suite", 2);

        // Step 3: Display initial inventory
        inventory.displayInventory();

        // Step 4: Retrieve current availability
        System.out.println("Available Deluxe Rooms: " + inventory.getAvailability("Deluxe"));
        System.out.println("Available Suite Rooms: " + inventory.getAvailability("Suite"));

        // Step 5: Perform booking operations
        inventory.bookRoom("Double", 2);
        inventory.bookRoom("Suite", 1);
        inventory.bookRoom("Suite", 2); // should fail

        // Step 6: Display updated inventory
        inventory.displayInventory();

        // Step 7: Cancel a booking / restore rooms
        inventory.cancelBooking("Suite", 1);

        // Step 8: Controlled direct update
        inventory.updateAvailability("Single", 8);

        // Step 9: Final inventory state
        inventory.displayInventory();
    }
}