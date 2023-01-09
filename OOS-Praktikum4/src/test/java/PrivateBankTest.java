import bank.*;
import bank.exceptions.*;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Testmethoden für PrivateBank")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class PrivateBankTest {

    static PrivateBank privateBank;
    static PrivateBank copyPrivateBank;

    static PrivateBank testBank;

    static PrivateBank copyTestBank;

    @DisplayName("Erstelle eine PrivateBank")
    @BeforeAll
    public static void setUp() throws AccountAlreadyExistsException, IOException, TransactionAlreadyExistException, AccountDoesNotExistException, ZinsenException {

        final File folder = new File("data/test/junit5");
        if (folder.exists()) {
            final File[] listOfFiles = folder.listFiles();
            assert listOfFiles != null;
            for (File file : listOfFiles)
                file.delete();
            Files.deleteIfExists(Path.of("data/test/junit5"));
        }

        privateBank = new PrivateBank("JUnit 5", "test/junit5", 0, 0.12);

        privateBank.createAccount("Ahmed");
        privateBank.createAccount("Mohammad");
        try {
            privateBank.addTransaction("Ahmed", new Payment("19.01.2011", "Payment", -789, 0.9, 0.25));
            privateBank.addTransaction("Mohammad", new IncomingTransfer("03.03.2000", "Ali für Mohammad; 80", 80, "Ali", "Mohammad"));
            copyPrivateBank = new PrivateBank(privateBank);
        } catch (ZinsenException | BetragException e) {
            throw new RuntimeException(e);
        }

    }

    @DisplayName("Teste Konstruktor")
    @Test
    @Order(0)
    public void constructorTest() {
        assertAll("PrivateBank",
                () -> assertEquals("JUnit 5", privateBank.getName()),
                () -> assertEquals("test/junit5", privateBank.getDirectoryName()),
                () -> assertEquals(0, privateBank.getIncomingInterest()),
                () -> assertEquals(0.12, privateBank.getOutgoingInterest()));
    }

    @DisplayName("Teste Copy-Konstruktor")
    @Test
    @Order(1)
    public void copyConstructorTest() {
        assertAll("CopyPrivateBank",
                () -> assertEquals(privateBank.getName(), copyPrivateBank.getName()),
                () -> assertEquals(privateBank.getDirectoryName(), copyPrivateBank.getDirectoryName()),
                () -> assertEquals(privateBank.getIncomingInterest(), copyPrivateBank.getIncomingInterest()),
                () -> assertEquals(privateBank.getOutgoingInterest(), copyPrivateBank.getOutgoingInterest()));
    }

    @DisplayName("Erstelle einen doppelten Account.")
    @ParameterizedTest
    @Order(2)
    @ValueSource(strings = {"Ahmed", "Mohammad"})
    public void createDuplicateAccountTest(String account) {
        Exception e = assertThrows(AccountAlreadyExistsException.class,
                () -> privateBank.createAccount(account));
        System.out.println(e.getMessage());
    }

    @DisplayName("Erstelle einen gültigen Account.")
    @ParameterizedTest
    @Order(3)
    @ValueSource(strings = {"Ali", "Hamudi", "Walid"})
    public void createValidAccountTest(String account) {
        assertDoesNotThrow(
                () -> privateBank.createAccount(account)
        );
    }

    @DisplayName("Erstelle einen gültigen Account, mit einer Transkations Liste.")
    @ParameterizedTest
    @Order(4)
    @ValueSource(strings = {"Lukas", "Marvin", "Ben"})
    public void createValidAccountWithTransactionsListTest(String account) {
        assertDoesNotThrow(
                () -> privateBank.createAccount(account, List.of(
                        new Payment("23.09.1897", "Payment 02", -2500, 0.8, 0.5),
                        new OutgoingTransfer("30.07.2020", "Überweisung an Ahmed", 1890, account, "Ahmed")
                ))
        );
    }

    @DisplayName("Erstelle einen doppelten Account, mit einer Transkations Liste.")
    @ParameterizedTest
    @Order(5)
    @ValueSource(strings = {"Ahmed", "Lukas", "Mohammad", "Hamudi", "Ali", "Walid", "Marvin", "Ben"})
    public void createInvalidAccountWithTransactionsListTest(String account) {
        Exception e = assertThrows(AccountAlreadyExistsException.class,
                () -> privateBank.createAccount(account, List.of(
                        new Payment("23.09.1897", "Payment 02", -2500, 0.8, 0.5)
                ))
        );
        System.out.println(e.getMessage());
    }

    @DisplayName("Füge eine gültige Transkation, einem gültigen Account hinzu.")
    @ParameterizedTest
    @Order(6)
    @ValueSource(strings = {"Ahmed", "Hamudi", "Walid", "Ali", "Lukas"})
    public void addValidTransactionValidAccountTest(String account) {
        assertDoesNotThrow(
                () -> privateBank.addTransaction(account, new IncomingTransfer("30.07.2020", "Überweisung an Ahmed", 1890, "Tom", account))
        );
    }

    @DisplayName("Füge eine doppelte Transkation, einem gültigen Account hinzu.")
    @ParameterizedTest
    @Order(7)
    @ValueSource(strings = {"Lukas", "Marvin", "Ben"})
    public void addDuplicateTransactionTest(String account) {
        Exception e = assertThrows(TransactionAlreadyExistException.class,
                () -> privateBank.addTransaction(account, new Payment("23.09.1897", "Payment 02", -2500, 0.8, 0.5))
        );
        System.out.println(e.getMessage());
    }

    @DisplayName("Füge eine gültige Transkation, einem ungültigen Account hinzu.")
    @ParameterizedTest
    @Order(8)
    @ValueSource(strings = {"Gina", "Benita", "Lea"})
    public void addTransactionInvalidAccountTest(String account) {
        Exception e = assertThrows(AccountDoesNotExistException.class,
                () -> privateBank.addTransaction(account, new Payment("19.01.2011", "Payment", -789, 0.9, 0.25))
        );
        System.out.println(e.getMessage());
    }

    @DisplayName("Entferne eine gültige Transaktion.")
    @ParameterizedTest
    @Order(9)
    @ValueSource(strings = {"Marvin", "Ben", "Lukas"})
    public void removeValidTransactionTest(String account) {
        assertDoesNotThrow(
                () -> privateBank.removeTransaction(account, new Payment("23.09.1897", "Payment 02", -2500, 0.8, 0.5))
        );
    }

    @DisplayName("Entferne eine ungültige Transaktion.")
    @ParameterizedTest
    @Order(10)
    @ValueSource(strings = {"Marvin", "Ben", "Lukas"})
    public void removeInvalidTransactionTest(String account) {
        Exception e = assertThrows(TransactionDoesNotExistException.class,
                () -> privateBank.removeTransaction(account, new Payment("19.01.2011", "Payment", -789, 0.9, 0.25))
        );
        System.out.println(e.getMessage());
    }

    @DisplayName("Teste ob eine besteimmte Transaktion vorhanden ist.")
    @ParameterizedTest
    @Order(11)
    @ValueSource(strings = {"Marvin", "Ben", "Lukas"})
    public void containsTransactionTrueTest(String account) throws AccountDoesNotExistException {
        try {
            assertTrue(privateBank.containsTransaction(account, new OutgoingTransfer("30.07.2020", "Überweisung an Ahmed", 1890, account, "Ahmed")));
        } catch (BetragException | ZinsenException e) {
            throw new RuntimeException(e);
        }
        System.out.println("Der Test im Account: " + account + ", ist gültig.");
    }

    @DisplayName("Teste ob eine Transkation vorhanden ist. [false]")
    @ParameterizedTest
    @Order(12)
    @ValueSource(strings = {"Ahmed", "Hamudi", "Walid", "Ali", "Mohammad"})
    public void containsTransactionFalseTest(String account) throws AccountDoesNotExistException {
        try {
            assertFalse(privateBank.containsTransaction(account, new OutgoingTransfer("30.07.2020", "Überweisung an Ahmed", 1890, account, "Ahmed")));
        } catch (BetragException | ZinsenException e) {
            throw new RuntimeException(e);
        }
        System.out.println("Der Test im Account: " + account + ", ist gültig.");
    }

    @DisplayName("Kontostand ermitteln.")
    @ParameterizedTest
    @Order(14)
    @CsvSource({"Lukas, 0", "Mohammad, 80", "Ahmed, 1006.32"})
    public void getAccountBalanceTest(String account, double balance) throws AccountDoesNotExistException {
        System.out.println("Erwarte den Stand: " + balance + ", im Account " + account + ".");
        assertEquals(balance, privateBank.getAccountBalance(account));
    }

    @DisplayName("Alle Transaktionen anzeigen lassen.")
    @Test  @Order(15)
    public void getTransactionTest() throws AccountDoesNotExistException {
        List<Transaction> transactionList = null;
        try {
            transactionList = List.of(
                    new Payment("19.01.2011", "Payment", -789, 0, 0.12),
                    new IncomingTransfer("30.07.2020", "Überweisung an Ahmed", 1890, "Tom", "Ahmed"));
            assertEquals(transactionList, privateBank.getTransactions("Ahmed"));
            System.out.println("Alle Transkationen von <Ahmed> sind gültig.");
        } catch (ZinsenException | BetragException e) {
            throw new RuntimeException(e);
        }
    }

    @DisplayName("Transaktionen Liste nach dem Typen.")
    @Test @Order(16)
    public void getTransactionsByTypeTest() {
        try {
            List<Transaction> transactionList = List.of(
                    new OutgoingTransfer("30.07.2020", "Überweisung an Ahmed", 1890, "Lukas", "Ahmed"));
            assertEquals(transactionList, privateBank.getTransactionsByType("Lukas", false));
            System.out.println("Alle Transkationen nach Typen von <Lukas> sind gültig.");
        } catch (BetragException e) {
            throw new RuntimeException(e);
        }
    }

    @Test @Order(17)
    @DisplayName("Transkationen Liste sortiert anzeigen.")
    public void getTransactionsSortedTest() throws AccountDoesNotExistException {
        try {
            assertEquals(List.of(
                            new IncomingTransfer("03.03.2000", "Ali für Mohammad; 80", 80, "Ali", "Mohammad"))
                    , privateBank.getTransactionsSorted("Mohammad", true));
        } catch (BetragException e) {
            throw new RuntimeException(e);
        }

    }

    @Test @Order(18)
    @DisplayName("ToString Testen")
    public void checkToString() throws ZinsenException, BetragException, TransactionAlreadyExistException, AccountDoesNotExistException, AccountAlreadyExistsException {

        testBank = new PrivateBank("Test", "test/string", 0, 0.0);
        Exception e = assertThrows(AccountAlreadyExistsException.class,
                () -> testBank.createAccount("ToString")
        );

        Exception f = assertThrows(TransactionAlreadyExistException.class,
                () -> testBank.addTransaction("ToString", new Payment("01.01.2023", "Payment", 500, 0.0, 0.0))
        );

            assertEquals("Name: Test\n" +
                    "Incoming Interest: 0.0\n" +
                    "outgoing Interest: 0.0\n" +
                    "ToString => \n" +
                    "\t\tDate: 01.01.2023, Description: Payment, Amount: 500.0 €, Incoming Interest: 0.0, Outgoing Interest: 0.0\n", testBank.toString());
    }

    @Test @Order(18)
    @DisplayName("Teste equals() Methode")
    public void checkequals() throws ZinsenException {

        copyTestBank = new PrivateBank(testBank);
        assertEquals(true, testBank.equals(copyTestBank));
    }
}
