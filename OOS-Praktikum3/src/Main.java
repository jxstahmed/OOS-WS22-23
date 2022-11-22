import bank.*;
import bank.exceptions.*;
import java.util.List;

public class Main {
    public static void main(String[] args) throws AccountAlreadyExistsException, AccountDoesNotExistException, TransactionAlreadyExistException, TransactionAttributeException, TransactionDoesNotExistException, BetragException, ZinsenException
    {

        PrivateBankAlt privateBankAlt = new PrivateBankAlt("Sparkasse", 0.25, 0.3);

        privateBankAlt.createAccount("Ahmed", List.of(
                new Payment("12.03.2022", "Payment", 400),
                new Payment("23.09.1897", "Payment", 2500, 0.8, 0.5),
                new Payment("23.09.1897", "Payment", -2500, 0.8, 0.5),
                new Transfer("03.03.2000", "Transfer", 80, "Ahmed", "Mohammad")
        ));

        try {
            privateBankAlt.createAccount("Mohammad", List.of(
                    new Payment("22.06.1998", "Payment", 435, 0., 0.),
                    new Transfer("03.03.2000", "Transfer", 80, "Mohammad", "Ahmed"),
                    new Payment("05.08.2022", "Payment", -118, 0., 0.),
                    new Transfer("15.04.1990", "Transfer", 185, "Ahmed", "Mohammad"),
                    new Payment("19.01.2011", "Payment", 100, 1.1, 0.25),
                    new Transfer("30.07.2020", "Transfer", 1890, "Mohammad", "Ahmed")
            ));
        } catch (ZinsenException e){
            System.out.println(e);
        }

        System.out.println(privateBankAlt);

        try {
            privateBankAlt.createAccount("Ahmed");
        } catch (AccountAlreadyExistsException e){
            System.out.println(e);
        }

        try {
            privateBankAlt.createAccount("Mohammad", List.of(
                    new Transfer("03.03.2001", "Transfer", 80, "Mohammad", "Ahmed")
            ));
        } catch (AccountAlreadyExistsException e){
            System.out.println(e);
        }

        try {
            privateBankAlt.createAccount("Alex");
        } catch (AccountAlreadyExistsException e) {
            System.out.println(e);
        }

        System.out.println(privateBankAlt);

        try {
            privateBankAlt.addTransaction("Lukas", new Payment("11.11.2000", "Payment", 890));
        } catch (AccountDoesNotExistException | TransactionAlreadyExistException e) {
            System.out.println(e);
        }

        try {
            privateBankAlt.addTransaction("Peter", new Transfer("03.03.2000", "Transfer", 80, "Peter", "Mohammad"));
        } catch (AccountDoesNotExistException | TransactionAlreadyExistException e) {
            System.out.println(e);
        }

        try {
            privateBankAlt.addTransaction("Heinz", new Payment("22.03.2003", "Payment", 90, 0.9, 0.75));
        } catch (AccountDoesNotExistException | TransactionAlreadyExistException e) {
            System.out.println(e);
        }

        try {
            privateBankAlt.addTransaction("Hamudi", new Transfer("19.04.2023", "Transfer", 3890,"Lukas", "Hamudi"));
        } catch (AccountDoesNotExistException | TransactionAlreadyExistException e) {
            System.out.println(e);
        }

        System.out.println(privateBankAlt);

        try {
            privateBankAlt.removeTransaction("Anand", new Transfer("19.04.2023", "Transfer", 3890,"Anand", "Peter"));
        } catch (TransactionDoesNotExistException e) {
            System.out.println(e);
        }

        try {
            privateBankAlt.removeTransaction("Mahmud", new Transfer("15.04.1990", "Transfer", 185, "Alushi", "Mahmud"));
        } catch (TransactionDoesNotExistException e) {
            System.out.println(e);
        }

        System.out.println(privateBankAlt);
        try
        {
                privateBankAlt.getTransactionsSorted("Ahmed", true);
                privateBankAlt.getTransactionsSorted("Ahmed", false);
                privateBankAlt.getTransactionsByType("Ahmed", true);
                privateBankAlt.getTransactionsByType("Ahmed", false);
                privateBankAlt.getTransactions("Ahmed");
                privateBankAlt.getAccountBalance("Ahmed");
                privateBankAlt.containsTransaction("Mohammad", new Transfer("28.02.1908", "Transfer", 1095, "Mohammad", "Ahmed"));
                privateBankAlt.containsTransaction("Mohammad", new Transfer("03.03.2000", "Transfer", 80, "Mohammad", "Ahmed"));
                privateBankAlt.containsTransaction("Anand", new Payment("22.03.2003", "Payment", 90, 0.9, 0.75));
        } catch(ZinsenException | AccountDoesNotExistException e)
        {
            System.out.println(e);
        }


        PrivateBank privateBank = new PrivateBank("Volksbank", 0.25, 0.3);
        privateBank.createAccount("Aloushi", List.of(
                new Payment("12.03.2008", "Payment", 321),
                new Payment("23.09.1897", "Payment", -10000, 0.8, 0.5),
                new OutgoingTransfer("03.03.2000", "OutgoingTransfer", 80, "Aloushi", "HamudiCousin")
        ));
        privateBank.createAccount("HamudiCousin", List.of(
                new Payment("22.06.1998", "Payment", 435, 0., 0.),
                new IncomingTransfer("03.03.2000", "IncomingTransfer", 80, "Aloushi", "HamudiCousin"),
                new Payment("05.08.2022", "Payment", -118, 0., 0.),
                new OutgoingTransfer("15.04.1990", "OutgoingTransfer", 185, "HamudiCousin", "Aloushi"),
                new OutgoingTransfer("30.07.2020", "OutgoingTransfer", 1890, "HamudiCousin", "Aloushi"),
                new Payment("19.01.2011", "Payment", -789, 0.9, 0.25)
        ));

        try {
        privateBank.getTransactions("HamudiCousin");
        privateBank.getAccountBalance("HamudiCousin");
        privateBankAlt.getTransactions("HamudiCousin");
        }
        catch(AccountDoesNotExistException e)
        {
            System.out.println(e);
        }

        System.out.println("Teste die Funktion equals() :\nprivateBank und privateBankAlt vergleichen. Erwartet wird <false> => " +
                            privateBankAlt.equals(privateBank));

    }
}