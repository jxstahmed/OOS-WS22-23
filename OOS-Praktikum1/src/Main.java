import bank.Payment;
import bank.Transfer;

import javax.xml.transform.TransformerFactory;


public class Main {
    public static void main(String[] args) {

        //Payment Beispiele
       Payment einzahlung = new Payment("11.10.2022", "Einzahlung", 123.456);
       einzahlung.printObject();

       Payment auszahlung = new Payment("12.10.2022", "Auszahlung", -11.456, 10, 0.6);
       auszahlung.printObject();

       Payment zahlung = new Payment(auszahlung);
       zahlung.printObject();


       //Transfer Beispiele
       Transfer miete = new Transfer("12.10.2022", "Miete", -999);
       miete.printObject();

       Transfer auto = new Transfer("04.10.2022", "Autoverkauf", 15.423,"Ahmed", "Mohammed");
       auto.printObject();

       Transfer kopie = new Transfer(auto);
       kopie.printObject();
    }
}