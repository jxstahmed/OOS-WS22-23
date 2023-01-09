import bank.*;
import bank.exceptions.*;

import java.io.IOException;
import java.util.List;


public class Main {
    public static void main(String[] args) throws AccountAlreadyExistsException, TransactionAlreadyExistException, AccountDoesNotExistException, TransactionDoesNotExistException, IOException, ZinsenException {


        PrivateBank deutscheBank = new PrivateBank("Volksbank", "Banken/Volksbank", 0.25, 0.3);
        try {
            deutscheBank.createAccount("Ahmed", List.of(
                    new Payment("09.12.2022", "Payment", 321),
                    new Payment("10.12.2022", "Payment", -2500, 0.8, 0.5),
                    new OutgoingTransfer("11.12.2022", "Schulden an Mohammad", 50, "Ahmed", "Mohammad")
            ));
        } catch (AccountAlreadyExistsException | BetragException | ZinsenException e) {
            System.out.println(e);
        }

        try {
            deutscheBank.createAccount("Mohammad", List.of(
                    new Payment("10.12.2022", "Payment", 435, 0., 0.),
                    new IncomingTransfer("11.12.2022", "Schulden von Ahmed; 50", 50, "Ahmed", "Mohammad"),
                    new Payment("08.12.2022", "Payment", -118, 0., 0.),
                    new OutgoingTransfer("11.12.2022", "Geschenk an Ali", 200, "Mohammad", "Ali")
            ));
        } catch (AccountAlreadyExistsException | BetragException | ZinsenException  e) {
            System.out.println(e);
        }

        try {
            deutscheBank.createAccount("Ali");
        } catch (AccountAlreadyExistsException e) {
            System.out.println(e);
        }

        try {
            deutscheBank.removeTransaction("Ali", new Payment("01.01.2023", "Payment", -1000, 0.9, 0.25));
        } catch (TransactionDoesNotExistException  | BetragException | ZinsenException e) {
            System.out.println(e);
        }

        try {
            deutscheBank.addTransaction("Ali", new Payment("02.01.2023", "Payment", 789, 0.9, 0.25));
        } catch (TransactionAlreadyExistException  | BetragException | ZinsenException e) {
            System.out.println(e);
        }

        try {
            deutscheBank.addTransaction("Ahmed", new Payment("19.01.2023", "Payment", -500, 0.9, 0.25));
        } catch (TransactionAlreadyExistException  | BetragException | ZinsenException e) {
            System.out.println(e);
        }
        System.out.println("\n" + deutscheBank);

        PrivateBank sparkasse = new PrivateBank("Sparkasse", "Banken/Sparkasse", 0.11, 0.05);
        try {
            sparkasse.addTransaction("Lukas", new Payment("19.01.2023", "Payment", -600, 0.9, 0.25));
        } catch (AccountDoesNotExistException  | BetragException | ZinsenException e) {
            System.out.println(e);
        }

        try {
            sparkasse.createAccount("Marvin");
        } catch (AccountAlreadyExistsException e) {
            System.out.println(e);
        }

        System.out.println("\n" + sparkasse);


        PrivateBank aachenerBank = new PrivateBank("Postbank", "Banken/Postbank", 0.11, 0.26);
        System.out.println(aachenerBank);
    }
}