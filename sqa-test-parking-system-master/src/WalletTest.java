import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class WalletTest {

    @Test
    public void zeroBalance() {
        Wallet wallet = new Wallet();
        assertEquals(0.0,wallet.getBalance());
    }

    @Test
    public void someBalance() {
        Wallet wallet = new Wallet(50);
        assertEquals(50.0,wallet.getBalance());
    }

    @Test
    public void addMoney() {
        Wallet wallet = new Wallet(50);
        wallet.addFunds(10);
        assertEquals(60,wallet.getBalance());
    }

    @Test
    public void addNegative() {
        Wallet wallet = new Wallet(50);

        assertThrows(
                InvalidAmountException.class,
                () -> wallet.addFunds(-8));
    }


    @Test
    public void withdrawSmaller() {
        Wallet wallet = new Wallet(50);
        wallet.addFunds(10);
        wallet.deductFunds(30);
        assertEquals(30,wallet.getBalance());
    }

    // withdraw more than available
    @Test
    public void withdrawLarger() {
        Wallet wallet = new Wallet(50);
        wallet.addFunds(10);
        assertThrows(
                InsufficientFundsException.class,
                () -> wallet.deductFunds(1000)
        );
    }

    //deducting fund with negative amount
    @Test
    public void withdrawNegative() {
        Wallet wallet = new Wallet(50);

        assertThrows(
                InvalidAmountException.class,
                () -> wallet.deductFunds(-8));
    }

    @Test
    public void withdrawZero() {
        Wallet wallet = new Wallet(50);

        assertThrows(
                InvalidAmountException.class,
                () -> wallet.deductFunds(0));
    }


    @Test
    public void moveMoneyWithNegativeAndZero() {

        Wallet wallet = new Wallet(50);
        Wallet wallet2 = new Wallet();
        assertThrows(
                InvalidAmountException.class,
                () -> wallet.transferFunds(wallet2,-8));
        assertThrows(
                InvalidAmountException.class,
                () -> wallet.transferFunds(wallet2,0));
    }

    @Test
    public void moveLarger() {
        Wallet wallet = new Wallet(50);
        Wallet wallet2 = new Wallet();
         assertThrows(
                InsufficientFundsException.class,
                () -> wallet.transferFunds(wallet2,10000));
    }

    @Test
    public void initialBalanceNegative () {
        assertThrows(InvalidAmountException.class,()-> new Wallet(-98));
    }

}