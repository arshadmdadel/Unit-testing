import org.junit.jupiter.api.Test;
import java.time.LocalDateTime;
import static org.junit.jupiter.api.Assertions.*;

public class BookingTest {

    LocalDateTime startTime = LocalDateTime.of(2026, 2, 4, 5, 0);
    LocalDateTime endTime = LocalDateTime.of(2026, 2, 4, 5, 0);

    VehicleType vehicleType = VehicleType.MOTORCYCLE;
    Vehicle vehicle = new Vehicle(1, vehicleType, 500);
    ParkingSlot parkingSlot = new ParkingSlot("1", ParkingSlotType.COMPACT);

    Booking booking = new Booking(1, vehicle, parkingSlot, startTime, endTime, 1000);

    @Test
    void shouldReturnCorrectBookingId() {
        assertEquals(1, booking.getBookingId());
    }

    @Test
    void shouldReturnVehicle() {
        assertNotNull(booking.getVehicle());
        assertEquals(vehicle, booking.getVehicle());
    }

    @Test
    void shouldReturnParkingSlot() {
        assertNotNull(booking.getParkingSlot());
        assertEquals(parkingSlot, booking.getParkingSlot());
    }

    @Test
    void shouldReturnStartTime() {
        assertEquals(startTime, booking.getStartTime());
    }

    @Test
    void shouldReturnEndTime() {
        assertEquals(endTime, booking.getEndTime());
    }

    @Test
    void shouldReturnAmount() {
        assertEquals(1000, booking.getAmount());
    }

    @Test
    void shouldReturnActiveStatusInitially() {
        assertEquals(BookingStatus.ACTIVE, booking.getBookingStatus());
    }

    @Test
    void shouldChangeStatusToCompleted() {
        Booking completedBooking =
                new Booking(2, vehicle, parkingSlot, startTime, endTime, 300.0);

        completedBooking.completeBooking();

        assertEquals(BookingStatus.COMPLETED, completedBooking.getBookingStatus());
    }

    @Test
    void shouldChangeStatusToCancelled() {
        Booking cancelledBooking =
                new Booking(3, vehicle, parkingSlot, startTime, endTime, 400.0);

        cancelledBooking.cancelBooking();

        assertEquals(BookingStatus.CANCELLED, cancelledBooking.getBookingStatus());
    }

    @Test
    void cancelledBookingShouldNotBecomeCompleted() {
        Booking bookingStateTest =
                new Booking(4, vehicle, parkingSlot, startTime, endTime, 400.0);

        bookingStateTest.cancelBooking();
        bookingStateTest.completeBooking();

        assertNotEquals(BookingStatus.COMPLETED, bookingStateTest.getBookingStatus());
    }



    @Test
    void shouldThrowExceptionWhenParkingSlotIsNull() {
        assertThrows(Exception.class, () ->
                new Booking(5, vehicle, null, startTime, endTime, 100.0));
    }

    @Test
    void shouldThrowExceptionWhenVehicleIsNull() {
        assertThrows(Exception.class, () ->
                new Booking(6, null, parkingSlot, startTime, endTime, 200.0));
    }

    @Test
    void shouldThrowExceptionForNegativeAmount() {
        assertThrows(Exception.class, () ->
                new Booking(7, vehicle, parkingSlot, startTime, endTime, -20.0));
    }

    @Test
    void shouldThrowExceptionForstartTimeAfterEndTime() {
        LocalDateTime invalidStart = LocalDateTime.of(2024, 1, 8, 5, 0);
        LocalDateTime invalidEnd = LocalDateTime.of(2025, 2, 8, 3, 0);

        assertThrows(Exception.class, () ->
                new Booking(85, vehicle, parkingSlot, invalidStart, invalidEnd, 50));
    }

    @Test
    void shouldThrowExceptionForstartTimeEqualToEndTime() {
        LocalDateTime invalidStart = LocalDateTime.of(2025, 2, 8, 3, 0);
        LocalDateTime invalidEnd = LocalDateTime.of(2025, 2, 8, 3, 0);

        assertThrows(Exception.class, () ->
                new Booking(85, vehicle, parkingSlot, invalidStart, invalidEnd, 50));
    }

    @Test
    void shouldThrowExceptionWhenStartAndEndTimeAreNull() {
        assertThrows(Exception.class, () ->
                new Booking(10, vehicle, parkingSlot, null, null, 200.0));
    }

    @Test
    void shouldThrowExceptionForZeroAmount() {
        assertThrows(Exception.class, () ->
                new Booking(11, vehicle, parkingSlot, startTime, endTime, 0.0));
    }
}
