import bank.IncomingTransfer;
import bank.OutgoingTransfer;

import bank.Transfer;
import bank.exceptions.BetragException;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;


public class TransferTest {

    static Transfer incomingTransfer;
    static Transfer outgoingTransfer;
    static Transfer copyTransfer;

    @BeforeAll
    public static void setUp() {
        try {
            incomingTransfer = new IncomingTransfer("03.03.2000", "Überweisung von Anas an Basti; 80", 80);
            outgoingTransfer = new OutgoingTransfer("30.07.2020", "Überweisung an Ahmed", 1890, "Basti", "Ahmed");
            copyTransfer = new OutgoingTransfer(outgoingTransfer);
        } catch (BetragException e) {
            throw new RuntimeException(e);
        }
    }


    @Test
    public void threeAttributesConstructorTest() {
        assertEquals("03.03.2000", incomingTransfer.getDate());
        assertEquals("Überweisung von Anas an Basti; 80", incomingTransfer.getDescription());
        assertEquals(80, incomingTransfer.getAmount());

    }

    @Test
    public void allAttributesConstructorTest() {
        assertEquals("30.07.2020", outgoingTransfer.getDate());
        assertEquals("Überweisung an Ahmed", outgoingTransfer.getDescription());
        assertEquals(1890, outgoingTransfer.getAmount());
        assertEquals("Basti", outgoingTransfer.getSender());
        assertEquals("Ahmed", outgoingTransfer.getRecipient());
    }

    @Test
    public void copyConstructorTester() {
        assertEquals(outgoingTransfer.getAmount(), copyTransfer.getAmount());
        assertEquals(outgoingTransfer.getDate(), copyTransfer.getDate());
        assertEquals(outgoingTransfer.getRecipient(), copyTransfer.getRecipient());
        assertEquals(outgoingTransfer.getAmount(), copyTransfer.getAmount());
        assertEquals(outgoingTransfer.getSender(), copyTransfer.getSender());
        assertEquals(outgoingTransfer.getDescription(), copyTransfer.getDescription());
    }

    @Test
    public void calculateIncomingTransferTest() {
        assertInstanceOf(IncomingTransfer.class, incomingTransfer);
        assertEquals(incomingTransfer.getAmount(), incomingTransfer.calculate());
    }

    @Test
    public void calculateOutgoingTransferTest() {
        assertInstanceOf(OutgoingTransfer.class, outgoingTransfer);
        assertEquals(-outgoingTransfer.getAmount(), outgoingTransfer.calculate());
    }

    @Test
    public void equalsTrueTest() {
        assertEquals(outgoingTransfer, copyTransfer);
    }

    @Test
    public void equalsFalseTest() {
        assertNotEquals(incomingTransfer, outgoingTransfer);
    }

    @Test
    public void toStringTester() {
        assertEquals("Datum: 30.07.2020, Beschreibung: Überweisung an Ahmed, Betrag: -1890.0 €, Sender: Basti, Empfänger: Ahmed\n", outgoingTransfer.toString());
    }
    @Test
    public void calculateTester() {
        assertEquals(80, incomingTransfer.calculate());
    }
}
