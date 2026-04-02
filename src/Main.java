import java.util.*;

// ------------------------------
// Custom Exception
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
// Room Inventory
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
            throw new InvalidBookingException("Room count cannot be negative.");
        }
        inventory.put(roomType, count);
    }

    public boolean hasRoomType(String roomType) {
        return inventory.containsKey(roomType);
    }

    public int getAvailability(String roomType) {
        return inventory.getOrDefault(roomType, 0);
    }

    public void reduceAvailability(String roomType, int roomsNeeded) throws InvalidBookingException {
        if (!inventory.containsKey(roomType)) {
            throw new InvalidBookingException("Invalid room type: " + roomType);
        }

        int available = inventory.get(roomType);

        if (roomsNeeded <= 0) {
            throw new InvalidBookingException("Rooms requested must be greater than zero.");
        }

        if (available < roomsNeeded) {
            throw new InvalidBookingException("Not enough rooms available for " + roomType);
        }

        inventory.put(roomType, available - roomsNeeded);
    }

    public void increaseAvailability(String roomType, int roomsToRestore) throws InvalidBookingException {
        if (!inventory.containsKey(roomType)) {
            throw new InvalidBookingException("Cannot restore inventory. Invalid room type: " + roomType);
        }

        if (roomsToRestore <= 0) {
            throw new InvalidBookingException("Rooms to restore must be greater than zero.");
        }

        inventory.put(roomType, inventory.get(roomType) + roomsToRestore);
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
}

// ------------------------------
// Validator
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
// Booking Queue
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
}

// ------------------------------
// Confirmed Reservation
// ------------------------------
class ConfirmedReservation {
    private String reservationId;
    private String guestName;
    private String roomType;
    private int numberOfRooms;
    private List<String> allocatedRoomIds;
    private boolean cancelled;

    public ConfirmedReservation(String reservationId, String guestName, String roomType,
                                int numberOfRooms, List<String> allocatedRoomIds) {
        this.reservationId = reservationId;
        this.guestName = guestName;
        this.roomType = roomType;
        this.numberOfRooms = numberOfRooms;
        this.allocatedRoomIds = allocatedRoomIds;
        this.cancelled = false;
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

    public List<String> getAllocatedRoomIds() {
        return allocatedRoomIds;
    }

    public boolean isCancelled() {
        return cancelled;
    }

    public void markCancelled() {
        cancelled = true;
    }

    public void displayConfirmedReservation() {
        System.out.println("Reservation ID : " + reservationId);
        System.out.println("Guest Name     : " + guestName);
        System.out.println("Room Type      : " + roomType);
        System.out.println("Rooms Booked   : " + numberOfRooms);
        System.out.println("Allocated IDs  : " + allocatedRoomIds);
        System.out.println("Status         : " + (cancelled ? "CANCELLED" : "CONFIRMED"));
    }
}

// ------------------------------
// Booking History
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
// Booking Service
// ------------------------------
class BookingService {
    private RoomInventory inventory;
    private BookingRequestQueue bookingQueue;
    private BookingHistory bookingHistory;

    private Set<String> allocatedRoomIds;
    private Stack<String> rollbackStack;
    private HashMap<String, ConfirmedReservation> confirmedReservations;
    private int reservationCounter = 1;

    public BookingService(RoomInventory inventory, BookingRequestQueue bookingQueue, BookingHistory bookingHistory) {
        this.inventory = inventory;
        this.bookingQueue = bookingQueue;
        this.bookingHistory = bookingHistory;
        this.allocatedRoomIds = new HashSet<>();
        this.rollbackStack = new Stack<>();
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

        try {
            InvalidBookingValidator.validateReservation(reservation, inventory);

            String guestName = reservation.getGuestName();
            String roomType = reservation.getRoomType();
            int roomsNeeded = reservation.getNumberOfRooms();

            System.out.println("Processing request for: " + guestName);

            inventory.reduceAvailability(roomType, roomsNeeded);

            List<String> assignedRoomIds = new ArrayList<>();

            for (int i = 0; i < roomsNeeded; i++) {
                String roomId = generateRoomId(roomType);
                allocatedRoomIds.add(roomId);
                assignedRoomIds.add(roomId);
            }

            String reservationId = generateReservationId();

            ConfirmedReservation confirmed = new ConfirmedReservation(
                    reservationId, guestName, roomType, roomsNeeded, assignedRoomIds
            );

            confirmedReservations.put(reservationId, confirmed);
            bookingHistory.addToHistory(confirmed);

            System.out.println("Booking Confirmed!");
            confirmed.displayConfirmedReservation();

        } catch (InvalidBookingException e) {
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

    // ------------------------------
    // Use Case 10: Cancellation & Rollback
    // ------------------------------
    public void cancelBooking(String reservationId) {
        System.out.println("\n===== CANCELLATION REQUEST =====");

        try {
            if (reservationId == null || reservationId.trim().isEmpty()) {
                throw new InvalidBookingException("Reservation ID cannot be empty.");
            }

            if (!confirmedReservations.containsKey(reservationId)) {
                throw new InvalidBookingException("Reservation ID does not exist: " + reservationId);
            }

            ConfirmedReservation reservation = confirmedReservations.get(reservationId);

            if (reservation.isCancelled()) {
                throw new InvalidBookingException("Reservation is already cancelled: " + reservationId);
            }

            // Step 1: Record released room IDs in rollback stack
            for (String roomId : reservation.getAllocatedRoomIds()) {
                rollbackStack.push(roomId);
                allocatedRoomIds.remove(roomId);
            }

            // Step 2: Restore inventory immediately
            inventory.increaseAvailability(reservation.getRoomType(), reservation.getNumberOfRooms());

            // Step 3: Mark reservation as cancelled in history
            reservation.markCancelled();

            System.out.println("Cancellation Successful!");
            System.out.println("Cancelled Reservation ID: " + reservationId);
            System.out.println("Released Room IDs (Rollback Stack): " + rollbackStack);
            System.out.println("Inventory Restored for Room Type: " + reservation.getRoomType());

        } catch (InvalidBookingException e) {
            System.out.println("Cancellation Failed!");
            System.out.println("Error: " + e.getMessage());
        }

        System.out.println("=================================\n");
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

    public void displayRollbackStack() {
        System.out.println("\n===== ROLLBACK STACK =====");
        if (rollbackStack.isEmpty()) {
            System.out.println("No released room IDs found.");
        } else {
            System.out.println("Released Room IDs in LIFO order: " + rollbackStack);
        }
        System.out.println("==========================\n");
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

        int totalReservations = 0;
        int totalCancelled = 0;
        int totalRoomsBooked = 0;

        for (ConfirmedReservation reservation : history) {
            if (reservation.isCancelled()) {
                totalCancelled++;
            } else {
                totalReservations++;
                totalRoomsBooked += reservation.getNumberOfRooms();
            }
        }

        System.out.println("Active Confirmed Reservations : " + totalReservations);
        System.out.println("Cancelled Reservations       : " + totalCancelled);
        System.out.println("Currently Active Rooms Booked: " + totalRoomsBooked);

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
            inventory.addRoomType("Deluxe", 2);
            inventory.addRoomType("Suite", 2);

            inventory.displayInventory();

            // Step 2: Create booking queue
            BookingRequestQueue bookingQueue = new BookingRequestQueue();

            bookingQueue.addRequest(new Reservation("Aarav", "Double", 1));
            bookingQueue.addRequest(new Reservation("Priya", "Suite", 1));
            bookingQueue.addRequest(new Reservation("Rahul", "Single", 2));

            // Step 3: Initialize booking history
            BookingHistory bookingHistory = new BookingHistory();

            // Step 4: Process bookings
            BookingService bookingService = new BookingService(inventory, bookingQueue, bookingHistory);
            bookingService.processAllBookings();

            // Step 5: Show current reservations
            bookingService.displayConfirmedReservations();

            // Step 6: Cancel a valid booking
            bookingService.cancelBooking("RES002");

            // Step 7: Try invalid cancellation
            bookingService.cancelBooking("RES999");

            // Step 8: Try duplicate cancellation
            bookingService.cancelBooking("RES002");

            // Step 9: Show rollback stack
            bookingService.displayRollbackStack();

            // Step 10: Show booking history
            bookingHistory.displayBookingHistory();

            // Step 11: Generate updated report
            BookingReportService reportService = new BookingReportService(bookingHistory);
            reportService.generateSummaryReport();

            // Step 12: Final inventory state
            System.out.println("Final Inventory After Cancellation & Rollback:");
            inventory.displayInventory();

        } catch (InvalidBookingException e) {
            System.out.println("System Initialization Failed: " + e.getMessage());
        }
    }
}