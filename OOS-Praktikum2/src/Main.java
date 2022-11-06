import bank.Payment;
import bank.Transfer;

public class Main {
    public static void main(String[] args)
    {
        Payment ersteEinzahlung = new Payment("06.11.2022", "1. Einzahlung", 123.456);
        Payment zweiteEinzahlung = new Payment("07.11.2022", "2. Einzahlung", 1099, 0.5, 1);
        Payment auszahlung = new Payment("05.11.2022", "Auszahlung", -1000, 0.6, 0.789);
        Payment auszahlung2 = new Payment(auszahlung);

        System.out.println("toString() von ersteEinzahlung:");
        System.out.println(ersteEinzahlung);

        System.out.println("toString() von zweiteEinzahlung:");
        System.out.println(zweiteEinzahlung);

        System.out.println("\ntoString() von auszahlung:");
        System.out.println(auszahlung);

        System.out.println("\nauszahlung equals auszahlung2:");
        System.out.println(auszahlung.equals(auszahlung2));


        Transfer ersterTransfer = new Transfer("06.11.2022", "1. Einzahlung", 123.456);
        Transfer zweiterTransfer = new Transfer("07.11.2022", "2. Transfer", 100, "Ahmed", "Mohammed");
        Transfer dritterTransfer = new Transfer(zweiterTransfer);


        System.out.println("\n\ntoString() von ersterTransfer:");
        System.out.println(ersterTransfer);

        System.out.println("\ntoString() von zweiterTransfer:");
        System.out.println(zweiterTransfer);

        System.out.println("\nzweiterTransfer equals dritterTransfer:");
        System.out.println(zweiterTransfer.equals(dritterTransfer));

    }
}