
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class VehicleTest {


    VehicleType vehicleType = VehicleType.CAR;

    Wallet wallet = new Wallet(45);

    Vehicle vehicle = new Vehicle(9,vehicleType,wallet);

    @Test
    public void hellowrold(){

    }
    @Test
    public void getVehicleId() {
        assertEquals(9,vehicle.getVehicleId());
    }

    @Test
    public void getVehicleType() {
        assertEquals(vehicleType,vehicle.getVehicleType());
    }

    @Test
    public void getWallet() {
        assertEquals(wallet,vehicle.getWallet());
    }


    @Test
    public void matchVehicleAndWalletBalance() {
        Vehicle test = new Vehicle(199,vehicleType,52);
        assertEquals(52,test.getWallet().getBalance());
    }


    @Test
    public void constructorShouldThrowExceptionForNegativeBalance() {
        assertThrows(InvalidAmountException.class,
                () -> new Vehicle(78, vehicleType, -99));
    }

    @Test
    public void vehicleTypeNull () {
        Vehicle v = new Vehicle(3, null, wallet);
        System.out.println(v.getVehicleType());
        assertThrows(Exception.class,()->new Vehicle(3, null, wallet));

    }

    @Test
    public void vehicleWalletNull() {
        assertThrows(Exception.class,
                () -> new Vehicle(787, vehicleType, null));
    }

    @Test
    public void getNullVehicleWallet() {
        Vehicle vec = new Vehicle(388, vehicleType, null);
        assertThrows(Exception.class, () -> vec.getWallet());
    }

    @Test
    public void getNullVehicleType() {
        Vehicle vec = new Vehicle(3888, null, wallet);
        assertThrows(Exception.class, () -> vec.getVehicleType());
    }


    @Test
    public void checkToStringFunction() {

        Vehicle vehicle = new Vehicle(199, vehicleType, 52);

        String expectedOutput = "Vehicle{vehicleId=199, vehicleType=CAR, walletBalance=52.0}";

        assertNotNull(vehicle, "This object should not be null");
        assertEquals(expectedOutput, vehicle.toString(), "Object output is not correct");
    }

}