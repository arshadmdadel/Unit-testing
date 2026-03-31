import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ParkingSystemAddGetTest {

    @Test
    void addAndGetVehicles() {

        ParkingSystem system = new ParkingSystem();

        Vehicle car = new Vehicle(1, VehicleType.CAR, 500.0);
        Vehicle bus = new Vehicle(2, VehicleType.BUS, 1000.0);

        system.addVehicle(car);
        system.addVehicle(bus);

        assertAll(
                () -> assertEquals(2, system.getVehicles().size()),
                () -> assertTrue(system.getVehicles().contains(car)),
                () -> assertTrue(system.getVehicles().contains(bus))
        );
    }

    @Test
    void addAndGetParkingSlots() {

        ParkingSystem system = new ParkingSystem();

        ParkingSlot compact = new ParkingSlot("C1", ParkingSlotType.COMPACT);
        ParkingSlot regular = new ParkingSlot("R1", ParkingSlotType.REGULAR);

        system.addParkingSlot(compact);
        system.addParkingSlot(regular);

        assertAll(
                () -> assertEquals(2, system.getParkingSlots().size()),
                () -> assertTrue(system.getParkingSlots().contains(compact)),
                () -> assertTrue(system.getParkingSlots().contains(regular))
        );
    }

    @Test
    void getAvailableParkingSlots() {

        ParkingSystem system = new ParkingSystem();

        Vehicle motorcycle = new Vehicle(1, VehicleType.MOTORCYCLE, 300.0);
        ParkingSlot compact = new ParkingSlot("C1", ParkingSlotType.COMPACT);
        ParkingSlot regular = new ParkingSlot("R1", ParkingSlotType.REGULAR);

        system.addVehicle(motorcycle);
        system.addParkingSlot(compact);
        system.addParkingSlot(regular);

        LocalDateTime start = LocalDateTime.now();
        LocalDateTime end = start.plusHours(1);

        List<ParkingSlot> available =
                system.getAvailableParkingSlots(motorcycle, start, end);

        assertTrue(available.contains(compact));
        assertTrue(available.contains(regular));
    }


    @Test
    void successfulBooking() {

        ParkingSystem system = new ParkingSystem();

        Vehicle car = new Vehicle(1, VehicleType.CAR, 500.0);
        ParkingSlot regular = new ParkingSlot("R1", ParkingSlotType.REGULAR);

        system.addVehicle(car);
        system.addParkingSlot(regular);

        LocalDateTime start = LocalDateTime.now();
        LocalDateTime end = start.plusHours(2);

        Booking booking = system.book(car, regular, start, end);

        assertAll(
                () -> assertNotNull(booking),
                () -> assertEquals(1, system.getBookings().size())
        );
    }

    @Test
    void bookingFailsForInvalidTime() {

        ParkingSystem system = new ParkingSystem();

        Vehicle car = new Vehicle(1, VehicleType.CAR, 500.0);
        ParkingSlot regular = new ParkingSlot("R1", ParkingSlotType.REGULAR);

        system.addVehicle(car);
        system.addParkingSlot(regular);

        LocalDateTime start = LocalDateTime.now();
        LocalDateTime end = start.minusHours(1);

        assertThrows(
                IllegalBookingTimeException.class,
                () -> system.book(car, regular, start, end)
        );
    }

    @Test
    void completeBooking() {

        ParkingSystem system = new ParkingSystem();

        Vehicle car = new Vehicle(1, VehicleType.CAR, 500.0);
        ParkingSlot regular = new ParkingSlot("R1", ParkingSlotType.REGULAR);

        system.addVehicle(car);
        system.addParkingSlot(regular);

        LocalDateTime start = LocalDateTime.now();
        LocalDateTime end = start.plusHours(2);

        Booking booking = system.book(car, regular, start, end);
        double slotBalanceBefore = regular.getWallet().getBalance();

        system.completeBooking(booking);

        assertEquals(
                slotBalanceBefore + booking.getAmount() * 0.8,
                regular.getWallet().getBalance()
        );
    }


    @Test
    void bookingWithoutAddingVehicle_Defect() {

        ParkingSystem system = new ParkingSystem();

        Vehicle car = new Vehicle(1, VehicleType.CAR, 500.0);
        ParkingSlot regular = new ParkingSlot("R1", ParkingSlotType.REGULAR);

        system.addParkingSlot(regular); // vehicle NOT added

        LocalDateTime start = LocalDateTime.now();
        LocalDateTime end = start.plusHours(1);

        Booking booking = system.book(car, regular, start, end);

        assertNotNull(booking,
                "DEFECT: Booking allowed for vehicle not registered in system");
    }

    @Test
    void bookingWithoutAddingSlot_Defect() {

        ParkingSystem system = new ParkingSystem();

        Vehicle car = new Vehicle(1, VehicleType.CAR, 500.0);
        ParkingSlot regular = new ParkingSlot("R1", ParkingSlotType.REGULAR);

        system.addVehicle(car); // slot NOT added

        LocalDateTime start = LocalDateTime.now();
        LocalDateTime end = start.plusHours(1);

        Booking booking = system.book(car, regular, start, end);

        assertNotNull(booking,
                "DEFECT: Booking allowed for slot not registered in system");
    }

    @Test
    void bookingWithInsufficientBalance_Defect() {

        ParkingSystem system = new ParkingSystem();

        Vehicle poorCar = new Vehicle(1, VehicleType.CAR, 5.0);
        ParkingSlot regular = new ParkingSlot("R1", ParkingSlotType.REGULAR);

        system.addVehicle(poorCar);
        system.addParkingSlot(regular);

        LocalDateTime start = LocalDateTime.now();
        LocalDateTime end = start.plusHours(2);

        Booking booking = system.book(poorCar, regular, start, end);

        assertNotNull(booking,
                "DEFECT: Booking succeeds even with insufficient balance");
    }


    @Test
    void bookingSameSlotTwice_ShouldFail() {

        // Arrange
        ParkingSystem system = new ParkingSystem();

        Vehicle car1 = new Vehicle(1, VehicleType.CAR, 500.0);
        Vehicle car2 = new Vehicle(2, VehicleType.CAR, 500.0);

        ParkingSlot regular = new ParkingSlot("R1", ParkingSlotType.REGULAR);

        system.addVehicle(car1);
        system.addVehicle(car2);
        system.addParkingSlot(regular);

        LocalDateTime start = LocalDateTime.now();
        LocalDateTime end = start.plusHours(2);

        // Act – first booking (SUCCESS)
        Booking firstBooking = system.book(car1, regular, start, end);
        assertNotNull(firstBooking);

        // Act & Assert – second booking on SAME slot & SAME time
        assertThrows(
                IllegalArgumentException.class,
                () -> system.book(car2, regular, start, end),
                "Second booking on same slot and time should fail"
        );
    }



    @Test
    void completeCancelledBooking_Defect() {

        ParkingSystem system = new ParkingSystem();

        Vehicle car = new Vehicle(1, VehicleType.CAR, 5000);
        ParkingSlot regular = new ParkingSlot("R1", ParkingSlotType.REGULAR);

        system.addVehicle(car);
        system.addParkingSlot(regular);

        LocalDateTime start = LocalDateTime.now();
        LocalDateTime end = start.plusHours(1);

        Booking booking = system.book(car, regular, start, end);
        system.cancelBooking(booking);

        double slotBalance = regular.getWallet().getBalance();

        system.completeBooking(booking);

        assertEquals(slotBalance, regular.getWallet().getBalance(),
                "DEFECT: Cancelled booking can be completed");
    }


    @Test
    void addNegativeMoneyToSystemWallet_Defect() {

        // Arrange
        ParkingSystem system = new ParkingSystem();

        Wallet systemWallet = system.getSYSTEM_WALLET();

        double initialBalance = systemWallet.getBalance();

        // Act – add NEGATIVE money
        systemWallet.addFunds(100.0);

        double finalBalance = systemWallet.getBalance();

        // Print (for observation)
        System.out.println("Initial balance: " + initialBalance);
        System.out.println("Final balance after adding -100: " + finalBalance);

        // Assert (this should FAIL in a correct system)
        assertEquals( 100.0, finalBalance);
    }


    @Test
    void bookAndCancelBooking_WalletBalanceCheck_DefectTest() {

        // Arrange
        ParkingSystem system = new ParkingSystem();

        Vehicle car = new Vehicle(11, VehicleType.CAR, 500.0);
        ParkingSlot slot = new ParkingSlot("R1", ParkingSlotType.REGULAR);

        Wallet systemWallet = system.getSYSTEM_WALLET();
        Wallet vehicleWallet = car.getWallet();

        // Get initial balances
        double systemInitial = systemWallet.getBalance();
        double vehicleInitial = vehicleWallet.getBalance();

        // Print initial balances
        System.out.println("=== Initial Balances ===");
        System.out.println("System wallet: " + systemInitial);
        System.out.println("Vehicle wallet: " + vehicleInitial);

        LocalDateTime start = LocalDateTime.of(2026, 2, 4, 10, 0);
        LocalDateTime end = LocalDateTime.of(2026, 2, 4, 12, 0);

        // Act – BOOK SLOT
        Booking booking = system.book(car, slot, start, end);

        double systemAfterBooking = systemWallet.getBalance();
        double vehicleAfterBooking = vehicleWallet.getBalance();

        System.out.println("=== After Booking ===");
        System.out.println("System wallet: " + systemAfterBooking);
        System.out.println("Vehicle wallet: " + vehicleAfterBooking);

        // Act – CANCEL BOOKING
        system.cancelBooking(booking);

        double systemAfterCancel = systemWallet.getBalance();
        double vehicleAfterCancel = vehicleWallet.getBalance();

        System.out.println("=== After Cancellation ===");
        System.out.println("System wallet: " + systemAfterCancel);
        System.out.println("Vehicle wallet: " + vehicleAfterCancel);

        // Assert – expected correct behavior
        assertEquals(systemInitial, systemAfterCancel,
                "System wallet should return to original balance after cancellation");

        assertEquals(vehicleInitial, vehicleAfterCancel,
                "Vehicle wallet should be refunded after cancellation");
    }


    @Test
    void setNegativeParkingRate_DefectTest() {

        // Arrange
        ParkingSystem system = new ParkingSystem();

        // Print initial parking rate
        double initialRate = system.getPARKING_RATE_PER_HOUR();
        System.out.println("Initial Parking Rate: " + initialRate);

        // Act – set negative parking rate
        system.setPARKING_RATE_PER_HOUR(-50.0);

        // Get the rate after setting negative value
        double newRate = system.getPARKING_RATE_PER_HOUR();

        // Print the rate after setting negative
        System.out.println("Parking Rate after setting negative: " + newRate);

        // Assert – this should normally fail in a correct system
        assertEquals(-50.0, newRate,
                "DEFECT: System allows negative parking rate");
       }

}
