import java.util.*;

// Main class
public class Main {

    // Booking Request Class
    static class BookingRequest {
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
    }

    // Shared Room Inventory
    static class RoomInventory {
        private Map<String, Integer> rooms = new HashMap<>();

        public RoomInventory() {
            rooms.put("Standard", 2);
            rooms.put("Deluxe", 1);
            rooms.put("Suite", 1);
        }

        // Critical Section - synchronized for thread safety
        public synchronized boolean allocateRoom(String roomType, String guestName) {
            int available = rooms.getOrDefault(roomType, 0);

            if (available > 0) {
                rooms.put(roomType, available - 1);
                System.out.println("Booking SUCCESS: " + guestName + " got " + roomType + " room.");
                return true;
            } else {
                System.out.println("Booking FAILED: " + guestName + " could not get " + roomType + " room.");
                return false;
            }
        }

        public synchronized void displayInventory() {
            System.out.println("\nFinal Room Inventory:");
            for (Map.Entry<String, Integer> entry : rooms.entrySet()) {
                System.out.println(entry.getKey() + " Rooms Left: " + entry.getValue());
            }
        }
    }

    // Shared Booking Queue
    static class BookingQueue {
        private Queue<BookingRequest> queue = new LinkedList<>();

        public synchronized void addRequest(BookingRequest request) {
            queue.add(request);
            System.out.println(request.getGuestName() + " added booking request for " + request.getRoomType());
        }

        public synchronized BookingRequest getNextRequest() {
            return queue.poll();
        }

        public synchronized boolean isEmpty() {
            return queue.isEmpty();
        }
    }

    // Booking Processor Thread
    static class BookingProcessor extends Thread {
        private BookingQueue bookingQueue;
        private RoomInventory inventory;

        public BookingProcessor(String threadName, BookingQueue bookingQueue, RoomInventory inventory) {
            super(threadName);
            this.bookingQueue = bookingQueue;
            this.inventory = inventory;
        }

        @Override
        public void run() {
            while (true) {
                BookingRequest request;

                synchronized (bookingQueue) {
                    if (bookingQueue.isEmpty()) {
                        break;
                    }
                    request = bookingQueue.getNextRequest();
                }

                if (request != null) {
                    inventory.allocateRoom(request.getRoomType(), request.getGuestName());

                    try {
                        Thread.sleep(500); // simulate booking delay
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    // Main Method
    public static void main(String[] args) {
        BookingQueue bookingQueue = new BookingQueue();
        RoomInventory inventory = new RoomInventory();

        // Multiple Guests submit requests simultaneously
        bookingQueue.addRequest(new BookingRequest("Alice", "Standard"));
        bookingQueue.addRequest(new BookingRequest("Bob", "Standard"));
        bookingQueue.addRequest(new BookingRequest("Charlie", "Standard"));
        bookingQueue.addRequest(new BookingRequest("David", "Deluxe"));
        bookingQueue.addRequest(new BookingRequest("Eva", "Deluxe"));
        bookingQueue.addRequest(new BookingRequest("Frank", "Suite"));

        // Multiple processors handle requests concurrently
        BookingProcessor processor1 = new BookingProcessor("Processor-1", bookingQueue, inventory);
        BookingProcessor processor2 = new BookingProcessor("Processor-2", bookingQueue, inventory);
        BookingProcessor processor3 = new BookingProcessor("Processor-3", bookingQueue, inventory);

        processor1.start();
        processor2.start();
        processor3.start();

        try {
            processor1.join();
            processor2.join();
            processor3.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        inventory.displayInventory();
        System.out.println("\nAll concurrent booking requests processed safely.");
    }
}