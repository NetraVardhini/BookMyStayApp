import java.util.*;

// ------------------------------
// Custom Exception Class
// ------------------------------
class InvalidBookingException extends Exception {
    public InvalidBookingException(String message) {
        super(message);
    }
}

// ------------------------------
// Room Class
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

    public void addRoomType(String roomType, int count) throws InvalidBookingException {
        if (roomType == null || roomType.trim().isEmpty()) {
            throw new InvalidBookingException("Room type cannot be empty.");
        }

        if (count < 0) {
            throw new InvalidBookingException("Room count cannot be negative for " + roomType);
        }

        inventory.put(roomType, count);
    }

    public int getAvailability(String roomType) {
        return inventory.getOrDefault(roomType, 0);
    }

    public boolean hasRoomType(String roomType) {
        return inventory.containsKey(roomType);
    }

    public boolean reduceAvailability(String roomType, int roomsNeeded) throws InvalidBookingException {
        if (!inventory.containsKey(roomType)) {
            throw new InvalidBookingException("Invalid room type: " + roomType);
        }

        int available = getAvailability(roomType);

        if (roomsNeeded <= 0) {
            throw new InvalidBookingException("Rooms requested must be greater than zero.");
        }

        if (available < roomsNeeded) {
            throw new InvalidBookingException("Not enough rooms available for room type: " + roomType);
        }

        inventory.put(roomType, available - roomsNeeded);

        if (inventory.get(roomType) < 0) {
            throw new InvalidBookingException("Inventory cannot become negative.");
        }

        return true;
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
// Booking Validator Class
// ------------------------------
class InvalidBookingValidator {

    public static void validateReservation(Reservation reservation, RoomInventory inventory)
            throws InvalidBookingException {

        if (reservation == null) {
            throw new InvalidBookingException("Reservation cannot be null.");
        }

        if (reservation.getGuestName() == null || reservation.getGuestName().trim().isEmpty()) {
            throw new InvalidBookingException("Guest name cannot be empty.");
        }

        if (reservation.getRoomType() == null || reservation.getRoomType().trim().isEmpty()) {
            throw new InvalidBookingException("Room type cannot be empty.");
        }

        if (!inventory.hasRoomType(reservation.getRoomType())) {
            throw new InvalidBookingException("Room type '" + reservation.getRoomType() + "' does not exist.");
        }

        if (reservation.getNumberOfRooms() <= 0) {
            throw new InvalidBookingException("Number of rooms must be greater than zero.");
        }
    }
}

// ------------------------------
// Booking Request Queue
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
// Confirmed Reservation Class
// ------------------------------
class ConfirmedReservation {
    private String reservationId;
    private String guestName;
    private String roomType;
    private int numberOfRooms;
    private List<String> allocatedRoomIds;

    public ConfirmedReservation(String reservationId, String guestName, String roomType,
                                int numberOfRooms, List<String> allocatedRoomIds) {
        this.reservationId = reservationId;
        this.guestName = guestName;
        this.roomType = roomType;
        this.numberOfRooms = numberOfRooms;
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

    public int getNumberOfRooms() {
        return numberOfRooms;
    }

    public void displayConfirmedReservation() {
        System.out.println("Reservation ID : " + reservationId);
        System.out.println("Guest Name     : " + guestName);
        System.out.println("Room Type      : " + roomType);
        System.out.println("Rooms Booked   : " + numberOfRooms);
        System.out.println("Allocated IDs  : " + allocatedRoomIds);
    }
}

// ------------------------------
// Booking History Class
// ------------------------------
class BookingHistory {
    private List<ConfirmedReservation> bookingHistoryList;

    public BookingHistory() {
        bookingHistoryList = new ArrayList<>();
    }

    public void addToHistory(ConfirmedReservation reservation) {
        bookingHistoryList.add(reservation);
    }

    public List<ConfirmedReservation> getBookingHistory() {
        return bookingHistoryList;
    }

    public void displayBookingHistory() {
        System.out.println("\n===== BOOKING HISTORY =====");

        if (bookingHistoryList.isEmpty()) {
            System.out.println("No confirmed bookings found.");
            System.out.println("===========================\n");
            return;
        }

        for (ConfirmedReservation reservation : bookingHistoryList) {
            reservation.displayConfirmedReservation();
            System.out.println("----------------------------------");
        }

        System.out.println("===========================\n");
    }
}

// ------------------------------
// Booking Service Class
// ------------------------------
class BookingService {
    private RoomInventory inventory;
    private BookingRequestQueue bookingQueue;
    private BookingHistory bookingHistory;

    private Set<String> allocatedRoomIds;
    private int reservationCounter = 1;

    public BookingService(RoomInventory inventory, BookingRequestQueue bookingQueue, BookingHistory bookingHistory) {
        this.inventory = inventory;
        this.bookingQueue = bookingQueue;
        this.bookingHistory = bookingHistory;
        this.allocatedRoomIds = new HashSet<>();
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

        try {
            // Step 1: Validate before processing
            InvalidBookingValidator.validateReservation(reservation, inventory);

            String guestName = reservation.getGuestName();
            String roomType = reservation.getRoomType();
            int roomsNeeded = reservation.getNumberOfRooms();

            System.out.println("Processing request for: " + guestName);

            // Step 2: Reduce inventory safely
            inventory.reduceAvailability(roomType, roomsNeeded);

            // Step 3: Allocate unique room IDs
            List<String> assignedRoomIds = new ArrayList<>();
            for (int i = 0; i < roomsNeeded; i++) {
                String roomId = generateRoomId(roomType);
                allocatedRoomIds.add(roomId);
                assignedRoomIds.add(roomId);
            }

            // Step 4: Confirm reservation
            String reservationId = generateReservationId();
            ConfirmedReservation confirmed = new ConfirmedReservation(
                    reservationId, guestName, roomType, roomsNeeded, assignedRoomIds
            );

            bookingHistory.addToHistory(confirmed);

            System.out.println("Booking Confirmed!");
            confirmed.displayConfirmedReservation();

        } catch (InvalidBookingException e) {
            // Graceful failure handling
            System.out.println("Booking Failed!");
            System.out.println("Error: " + e.getMessage());
        }

        System.out.println("===================================\n");
    }

    public void processAllBookings() {
        while (!bookingQueue.isEmpty()) {
            processNextBooking();
        }
    }
}

// ------------------------------
// Booking Report Service
// ------------------------------
class BookingReportService {
    private BookingHistory bookingHistory;

    public BookingReportService(BookingHistory bookingHistory) {
        this.bookingHistory = bookingHistory;
    }

    public void generateSummaryReport() {
        System.out.println("\n===== BOOKING SUMMARY REPORT =====");

        List<ConfirmedReservation> history = bookingHistory.getBookingHistory();

        if (history.isEmpty()) {
            System.out.println("No booking data available.");
            System.out.println("==================================\n");
            return;
        }

        int totalReservations = history.size();
        int totalRoomsBooked = 0;
        HashMap<String, Integer> roomTypeCount = new HashMap<>();

        for (ConfirmedReservation reservation : history) {
            totalRoomsBooked += reservation.getNumberOfRooms();
            String roomType = reservation.getRoomType();
            roomTypeCount.put(roomType, roomTypeCount.getOrDefault(roomType, 0) + reservation.getNumberOfRooms());
        }

        System.out.println("Total Confirmed Reservations : " + totalReservations);
        System.out.println("Total Rooms Booked           : " + totalRoomsBooked);

        System.out.println("\nRoom Type Wise Booking Count:");
        for (Map.Entry<String, Integer> entry : roomTypeCount.entrySet()) {
            System.out.println(entry.getKey() + " : " + entry.getValue());
        }

        System.out.println("==================================\n");
    }
}

// ------------------------------
// Main Class
// ------------------------------
public class Main {
    public static void main(String[] args) {

        try {
            // Step 1: Initialize inventory
            RoomInventory inventory = new RoomInventory();

            inventory.addRoomType("Single", 5);
            inventory.addRoomType("Double", 3);
            inventory.addRoomType("Deluxe", 1);
            inventory.addRoomType("Suite", 2);

            inventory.displayInventory();

            // Step 2: Create booking queue
            BookingRequestQueue bookingQueue = new BookingRequestQueue();

            // Valid bookings
            bookingQueue.addRequest(new Reservation("Aarav", "Double", 1));
            bookingQueue.addRequest(new Reservation("Priya", "Suite", 1));

            // Invalid bookings for testing validation
            bookingQueue.addRequest(new Reservation("", "Single", 1));          // Empty guest name
            bookingQueue.addRequest(new Reservation("Rahul", "Luxury", 1));     // Invalid room type
            bookingQueue.addRequest(new Reservation("Sneha", "Deluxe", 0));     // Zero rooms
            bookingQueue.addRequest(new Reservation("Kiran", "Deluxe", 2));     // Not enough availability

            bookingQueue.displayQueue();

            // Step 3: Initialize booking history
            BookingHistory bookingHistory = new BookingHistory();

            // Step 4: Process all bookings safely
            BookingService bookingService = new BookingService(inventory, bookingQueue, bookingHistory);
            bookingService.processAllBookings();

            // Step 5: Display booking history
            bookingHistory.displayBookingHistory();

            // Step 6: Generate booking report
            BookingReportService reportService = new BookingReportService(bookingHistory);
            reportService.generateSummaryReport();

            // Step 7: Final inventory state
            System.out.println("Final Inventory After Validation & Error Handling:");
            inventory.displayInventory();

        } catch (InvalidBookingException e) {
            System.out.println("System Initialization Failed: " + e.getMessage());
        }
    }
}