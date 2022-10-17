package bank;

public class Transfer {
    private String date;                    //Zeitpunkt der Ein- oder Auszahlung
    private String description;             //Beschreibung der Ein- oder Auszahlung
    private double amount;                  //Geldbetrag der Ein- oder Auszahlung (Dieser darf nicht negativ sein)
    private String sender;                  //Absender der Überweisung/Zahlung
    private String recipient;               //Empfänger der Überweisung/Zahlung

    //Getter für die Attribute
    public String getDate()
    {
        return this.date;
    }
    public String getDesc()
    {
        return this.description;
    }
    public double getAmount()
    {
        return this.amount;
    }
    public String getSender()
    {
        return this.sender;
    }
    public String getRec()
    {
        return this.recipient;
    }

    //Setter für die Attribute
    public void setDate(String datum)
    {
        this.date = datum;
    }
    public void setDesc(String beschreibung)
    {
        this.description = beschreibung;
    }
    public void setAmount(double wert)
    {
        if (wert < 0)
        {
            System.out.println("Der Betrag darf nicht negativ sein. Bitte Betrag ändern!");
            return;
        }
        else
        {
            this.amount = wert;
        }
    }
    public void setSender(String send)
    {
        this.sender = send;
    }
    public void setRec(String erhalten)
    {
        this.recipient = erhalten;
    }


    //Konstruktor für die Klasse
    public Transfer(String datum, String beschreibung, double wert)
    {
        this.date = datum;
        this.description = beschreibung;
        this.setAmount(wert);
    }
    public Transfer(String datum, String beschreibung, double wert, String von, String fuer)
    {
        this(datum, beschreibung, wert);
        this.sender = von;
        this.recipient = fuer;
    }
    public Transfer(Transfer neu)
    {
        this(neu.date, neu.description, neu.amount, neu.sender,  neu.recipient);
    }

    //Print Methode für die Ausgabe
    public void printObject()
    {
        System.out.println(
                "Datum: " + date +
                "\nBeschreibung: " + description +
                "\nBetrag: " + amount +
                "\nSender: " + sender +
                "\nEmpfaenger: " + recipient + "\n\n"
        );
    }
}
