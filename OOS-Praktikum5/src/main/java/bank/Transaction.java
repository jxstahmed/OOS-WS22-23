package bank;

import bank.exceptions.BetragException;

public abstract class Transaction implements CalculateBill
{

    /**
     * Zeitstempel der Transaktion im folgenden Format: Tag.Monat.Jahr
     */
    protected String date;

    /**
     * Eine Beschreibung für die Transaktion
     */
    protected String description;

    /**
     * Der Betrag der Transaktion
     */
    protected double amount;

    /**
     * Ein Setter für das Attribute date
     * @param datum = Neuer Wert des Datums
     */
    public void setDate (String datum)
    {
        this.date = datum;
    }

    /**
     * Getter für das Attribute date
     * @return = gibt das Datum von date wieder
     */
    public String getDate()
    {
        return this.date;
    }

    /**
     * Ein Setter für das Attribute description
     * @param beschreibung = Neue Beschreibung
     */
    public void setDescription (String beschreibung)
    {
        this.description = beschreibung;
    }

    /**
     * Ein Getter für das Attribute description
     * @return = gibt die Beschreibung wieder
     */
    public String getDescription()
    {
        return this.description;
    }

    /**
     * Setter für das Attribut amount
     * @param newAmount = Neuer Betrag
     * @throws BetragException wenn der Betrag negativ ist.
     */
    public void setAmount (double newAmount) throws BetragException
    {
        if(this instanceof Payment payment)
        {
            this.amount = newAmount;
        }
        else
        {
            if (newAmount < 0)
                {
                    throw new BetragException("Der eingegebene Betrag <" + newAmount + "> darf nicht negativ sein!\n");
                }
            else
                {
                    this.amount = newAmount;
                }
        }

    }

    /**
     * Ein Getter für das Attribute amount
     * @return = Gibt den Betrag wieder
     */
    public double getAmount()
    {
        return this.amount;
    }

    /**
     * Der Konstruktor für eine neue Transaktion
     * @param datum = Datum der Transaktion
     * @param beschreibung = Beschreibung zur Transaktion
     * @param betrag = Wert der Transaktion
     */
    public Transaction (String datum, String beschreibung, double betrag) throws BetragException
    {
        this.date = datum;
        this.description = beschreibung;
        this.setAmount(betrag);
    }

    /**
     * @return = Gibt alle Klassenattribute als einen String zurück
     */
    @Override
    public String toString()
    {
        return "Datum: " + date + ", Beschreibung: " + description + ", Betrag: " + calculate() + " €";
    }

    /**
     * Es werden zwei Objekte von Transaktion verglichen.
     * @param obj = Hierbei handelt es sich um das zu vergleichende Objekt
     * @return = Wenn beide gleich → true, wenn beide ungleich → false
     */
    @Override
    public boolean equals(Object obj)
    {
        if (obj instanceof Transaction transaction)
        {
            return (this.date.equals(transaction.date) && this.description.equals(transaction.description) && this.amount == transaction.amount);
        }
        else
        {
            return false;
        }
    }
}