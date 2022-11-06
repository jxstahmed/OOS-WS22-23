package bank;

public class Payment extends Transaction
{

    /**
     * Zinsen die bei einer Einzahlung aufkommen (positiver Wert in Prozent von 0 bis 1)
     */
    private double incomingInterest;

    /**
     * Zinsen die bei einer Auszahlung aufkommen (positiver Wert in Prozent von 0 bis 1)
     */
    private double outgoingInterest;

    /**
     * Setter für das Attribut incomingInterest
     * @param income = neuer Wert für incomingInterest
     */
    public void setIncomingInterest (double income)
    {
        this.incomingInterest = income;
    }

    /**
     * Getter für das Attribute incomingInterest
     * @return = Gibt den aktuellen Wert von incomingInterest wieder
     */
    public double getIncomingInterest()
    {
        return this.incomingInterest;
    }

    /**
     * Setter für das Attribut outgoingInterest
     * @param outgoing = neuer Wert für outgoingInterest
     */
    public void setOutgoingInterest (double outgoing)
    {
        this.outgoingInterest = outgoing;
    }

    /**
     * Getter für das Attribute outgoingInterest
     * @return = Gibt den aktuellen Wert von outgoingInterest wieder
     */
    public double getOutgoingInterest ()
    {
        return this.outgoingInterest;
    }

    /**
     * Konstruktor für einen neuen Payment mit 3 Attributen
     * @param datum = Wert für date
     * @param beschreibung = Wert für description
     * @param betrag = Wert für amount
     */
    public Payment (String datum, String beschreibung, double betrag)
    {
        super(datum, beschreibung, betrag);
    }

    /**
     * Konstruktor für einen neuen Payment mit allen Attributen
     * @param datum = Wert für date
     * @param beschreibung = Wert für description
     * @param betrag = Wert für amount
     * @param income = Wert für incomingInterest
     * @param outgoing = Wert für outgoingInterest
     */
    public Payment (String datum, String beschreibung, double betrag, double income, double outgoing)
    {
        this(datum, beschreibung, betrag);
        this.incomingInterest = income;
        this.outgoingInterest = outgoing;
    }

    /**
     * Copy Constructor für Payment
     * @param neuesPayment = Das neue Objekt Transfer
     */
    public Payment (Payment neuesPayment)
    {
        this(neuesPayment.date, neuesPayment.description, neuesPayment.amount, neuesPayment.incomingInterest, neuesPayment.outgoingInterest);
    }

    /**
     * @return = Gibt das Attribut je nach Interest wieder. (Einzahlung oder Auszahlung)
     */
    @Override
    public double calculate()
    {
        if (amount >= 0)
        {
            return (amount - incomingInterest * amount);
        }
        else
        {
            return (amount + outgoingInterest * amount);
        }
    }

    /**
     * @return = Gibt alle Klassenattribute als String wieder
     */
    @Override
    public String toString()
    {
        return super.toString() + ", Incoming Interest: " + incomingInterest + ", outgoing Interest: " + outgoingInterest + "\n";
    }

    /**
     * Es werden zwei Objekte von Payment verglichen.
     * @param obj = Hierbei handelt es sich um das zu vergleichende Objekt
     * @return = Wenn beide gleich → true, wenn beide ungleich → false
     */
    @Override
    public boolean equals(Object obj)
    {
        if (obj instanceof Payment payment)
        {
            return (super.equals(payment) && incomingInterest == payment.incomingInterest && outgoingInterest == payment.outgoingInterest);
        }
        else
        {
            return false;
        }
    }
}