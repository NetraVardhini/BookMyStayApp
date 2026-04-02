import java.io.*;
import java.util.*;

// Main class
public Main {

    // Booking Request Class
    static class BookingRequest implements Serializable {
        private static final long serialVersionUID = 1L;

        private String guestName;
        private String roomType;

        public BookingRequest(String guestName, String roomType) {
            this.guestName = guestName;
            this.roomType = roomType;
        }

        public String getGuestName() {
            return guestName;
        }

        public String getRoomType() {
            return roomType;
        }

        @Override
        public String toString() {
            return guestName + " booked " + roomType + " room";
        }
    }

    // Hotel System State Class
    static class HotelSystemState implements Serializable {
        private static final long serialVersionUID = 1L;

        Map<String, Integer> inventory;
        List<BookingRequest> bookingHistory;

        public HotelSystemState(Map<String, Integer> inventory, List<BookingRequest> bookingHistory) {
            this.inventory = inventory;
            this.bookingHistory = bookingHistory;
        }
    }

    // Room Inventory Class
    static class RoomInventory {
        private Map<String, Integer> rooms = new HashMap<>();

        public RoomInventory() {
            rooms.put("Standard", 2);
            rooms.put("Deluxe", 1);
            rooms.put("Suite", 1);
        }

        public boolean allocateRoom(String roomType, String guestName, List<BookingRequest> bookingHistory) {
            int available = rooms.getOrDefault(roomType, 0);

            if (available > 0) {
                rooms.put(roomType, available - 1);
                bookingHistory.add(new BookingRequest(guestName, roomType));
                System.out.println("Booking SUCCESS: " + guestName + " got " + roomType + " room.");
                return true;
            } else {
                System.out.println("Booking FAILED: " + guestName + " could not get " + roomType + " room.");
                return false;
            }
        }

        public void setRooms(Map<String, Integer> restoredRooms) {
            this.rooms = restoredRooms;
        }

        public Map<String, Integer> getRooms() {
            return rooms;
        }

        public void displayInventory() {
            System.out.println("\nCurrent Room Inventory:");
            for (Map.Entry<String, Integer> entry : rooms.entrySet()) {
                System.out.println(entry.getKey() + " Rooms Left: " + entry.getValue());
            }
        }
    }

    // Persistence Service Class
    static class PersistenceService {
        private static final String FILE_NAME = "hotel_data.ser";

        // Save system state
        public static void saveState(RoomInventory inventory, List<BookingRequest> bookingHistory) {
            try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(FILE_NAME))) {
                HotelSystemState state = new HotelSystemState(inventory.getRooms(), bookingHistory);
                out.writeObject(state);
                System.out.println("\nSystem state saved successfully.");
            } catch (IOException e) {
                System.out.println("\nError while saving system state: " + e.getMessage());
            }
        }

        // Load system state
        public static HotelSystemState loadState() {
            File file = new File(FILE_NAME);

            if (!file.exists()) {
                System.out.println("No saved data found. Starting with fresh system state.");
                return null;
            }

            try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(FILE_NAME))) {
                HotelSystemState state = (HotelSystemState) in.readObject();
                System.out.println("System state restored successfully.");
                return state;
            } catch (IOException | ClassNotFoundException e) {
                System.out.println("Persistence file is missing/corrupted. Starting safely with default state.");
                return null;
            }
        }
    }

    // Main Method
    public static void main(String[] args) {
        RoomInventory inventory = new RoomInventory();
        List<BookingRequest> bookingHistory = new ArrayList<>();

        // Step 1: Restore persisted data if available
        HotelSystemState restoredState = PersistenceService.loadState();

        if (restoredState != null) {
            inventory.setRooms(restoredState.inventory);
            bookingHistory = restoredState.bookingHistory;
        }

        // Step 2: Display recovered system state
        System.out.println("\n--- System Startup ---");
        inventory.displayInventory();

        System.out.println("\nRecovered Booking History:");
        if (bookingHistory.isEmpty()) {
            System.out.println("No previous bookings found.");
        } else {
            for (BookingRequest booking : bookingHistory) {
                System.out.println(booking);
            }
        }

        // Step 3: Simulate new bookings
        System.out.println("\n--- New Booking Operations ---");
        inventory.allocateRoom("Standard", "Alice", bookingHistory);
        inventory.allocateRoom("Deluxe", "Bob", bookingHistory);
        inventory.allocateRoom("Suite", "Charlie", bookingHistory);
        inventory.allocateRoom("Deluxe", "David", bookingHistory); // should fail if Deluxe already booked

        // Step 4: Show updated system state
        inventory.displayInventory();

        System.out.println("\nUpdated Booking History:");
        for (BookingRequest booking : bookingHistory) {
            System.out.println(booking);
        }

        // Step 5: Save current system state before shutdown
        System.out.println("\n--- System Shutdown ---");
        PersistenceService.saveState(inventory, bookingHistory);
    }
}