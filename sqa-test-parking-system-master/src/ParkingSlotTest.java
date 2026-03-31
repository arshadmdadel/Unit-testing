import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;


public class ParkingSlotTest {

    ParkingSlotType slotType= ParkingSlotType.COMPACT;

    ParkingSlot parkingSlot = new ParkingSlot("128",slotType);



    @Test
    public void isActive() {
        assertTrue(parkingSlot.isActive());
    }

    @Test
    public void getSlotId() {
        String slotID = parkingSlot.getSlotId();
        assertEquals(slotID, parkingSlot.getSlotId());
    }

    @Test
    public void getSlotType() {
        assertSame(slotType, parkingSlot.getSlotType());
    }


    @Test
    public void checkWallet() {
        assertNotNull(parkingSlot.getWallet());
    }
    @Test
    public void checkBookings() {
        assertNotNull(parkingSlot.getBookings());
    }

    @Test
    public void validDate() {

        VehicleType vType = VehicleType.MOTORCYCLE;
        LocalDateTime start = LocalDateTime.of(2022, 2, 4, 5, 0);
        LocalDateTime end = LocalDateTime.of(2025, 2, 5, 5, 0);
        assertTrue(parkingSlot.isCompatible(vType,start,end));

    }

    @Test
    public void startGreater() {
        VehicleType vType = VehicleType.MOTORCYCLE;
        LocalDateTime start = LocalDateTime.of(2026, 2, 4, 5, 0);
        LocalDateTime end = LocalDateTime.of(2025, 2, 5, 5, 0);
        assertFalse(parkingSlot.isCompatible(vType,start,end));
    }

    @Test
    public void startEndSame() {
        VehicleType vType = VehicleType.MOTORCYCLE;
        LocalDateTime start = LocalDateTime.of(2026, 2, 4, 5, 0);
        LocalDateTime end = LocalDateTime.of(2026, 2, 4, 5, 0);
        assertFalse(parkingSlot.isCompatible(vType,start,end));
    }

    @Test
    public void startEndNull() {
        VehicleType vType = VehicleType.MOTORCYCLE;
        LocalDateTime start = null;
        LocalDateTime end = null;
        assertFalse(parkingSlot.isCompatible(vType,start,end));
    }

    @Test
    public void bookAfterDeactivate() {

        parkingSlot.deactivate();
        assertFalse(parkingSlot.isActive());
        VehicleType vType=VehicleType.MOTORCYCLE;
        LocalDateTime start = LocalDateTime.of(2022, 2, 4, 5, 0);
        LocalDateTime end = LocalDateTime.of(2025, 2, 5, 5, 0);
        assertFalse(parkingSlot.isCompatible(vType,start,end));

    }


    @Test
    public void parkingSlotIdNull() {
        assertThrows(Exception.class,()->new ParkingSlot(null,slotType));
    }

    @Test
    public void  parkingSlotTypeNull() {
        ParkingSlot parkingSlot = new ParkingSlot(null,slotType);
        assertThrows(Exception.class, () -> new ParkingSlot("128", null));

    }

}