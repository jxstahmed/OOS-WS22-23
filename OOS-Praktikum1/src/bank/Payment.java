package bank;

public class Payment {

    private String date;                    //Zeitpunkt der Ein- oder Auszahlung
    private String description;             //Beschreibung der Ein- oder Auszahlung
    private double amount;                  //Geldbetrag der Ein- oder Auszahlung
    private double incomingInterest;        //Zinsen, in positiven prozent (als Dezimal), der Einzahlung
    private double outgoingInterest;        //Zinsen, in positiven prozent (als Dezimal), der Auszahlung

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
    public double getIncom()
    {
        return this.incomingInterest;
    }
    public double getOutgo()
    {
        return this.outgoingInterest;
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
        this.amount = wert;
    }
    public void setIncom(double incom)
    {
        if (0 > incom || incom > 1)
        {
            System.out.println("Die Zinsen müssen einen Prozentwert zwischen 0 und 1 haben!");
            return;
        }
        else
        {
            this.incomingInterest = incom;
        }
    }
    public void setOutgo(double outgo)
    {
        if ( 0 > outgo || outgo > 1)
        {
            System.out.println("Die Zinsen müssen einen Prozentwert zwischen 0 und 1 haben!");
            return;
        }
        else
        {
            this.outgoingInterest = outgo;
        }
    }


    //Konstruktor für die Klasse
    public Payment(String datum, String beschreibung, double wert)
    {
        this.date = datum;
        this.description = beschreibung;
        this.amount = wert;
    }
    public Payment(String datum, String beschreibung, double wert, double Incom, double Outgo)
    {
        this(datum, beschreibung, wert);
        this.setIncom(Incom);
        this.setOutgo(Outgo);
    }
    public Payment(Payment neu) //Copy-Konstruktor
    {
        this(neu.date, neu.description, neu.amount, neu.incomingInterest,  neu.outgoingInterest);
    }

    //Print Methode für die Ausgabe
    public void printObject()
    {
        System.out.println(
               "Datum: " + date +
               "\nBeschreibung: " + description +
               "\nBetrag: " + amount +
               "\nZinsen bei Einzahlung: " + incomingInterest +
               "\nZinsen bei Auszahlung: " + outgoingInterest + "\n\n"
        );
    }

}
