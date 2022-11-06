package bank;

public class Transfer extends Transaction
{
    /**
     * Name des Absenders, der die Überweisung gemacht hat
     */
    private String sender;

    /**
     * Name des Empfängers, der die Überweisung bekommen hat
     */
    private String recipient;

    /**
     * Setter für das Attribut sender
     * @param absender = neuer Wert für sender
     */
    public void setSender(String absender)
    {
        this.sender = absender;
    }

    /**
     * Getter für den Sender
     * @return = Der aktuelle Sender
     */
    public String getSender()
    {
        return this.sender;
    }

    /**
     * Setter für das Attribut recipient
     * @param empfänger = neuer Wert von recipient
     */
    public void setRecipient(String empfänger)
    {
        this.recipient = empfänger;
    }

    /**
     * Getter für recipient
     * @return den aktuellen Wert von recipient
     */
    public String getRecipient()
    {
        return this.recipient;
    }

    /**
     * Konstruktor für einen neuen Transfer mit 3 Attributen
     * @param datum = Datum des Transfers
     * @param beschreibung = Beschreibung des Transfers
     * @param betrag = Betrag des Transfers
     */
    public Transfer(String datum, String beschreibung, double betrag)
    {
        super(datum, beschreibung, betrag);
    }

    /**
     * Konstruktor für einen neuen Transfer mit allen Attributen
     * @param datum = Datum des Transfers
     * @param beschreibung = Beschreibung des Transfers
     * @param betrag = Betrag des Transfers
     * @param absender = Absender des Transfers
     * @param empfänger = Empfänger des Transfers
     */
    public Transfer(String datum, String beschreibung, double betrag, String absender, String empfänger)
    {
        this(datum, beschreibung, betrag);
        this.sender = absender;
        this.recipient = empfänger;
    }

    /**
     * Copy Constructor für Transfer
     * @param neuerTransfer = Das neue Objekt Transfer
     */
    public Transfer(Transfer neuerTransfer)
    {
        this(neuerTransfer.date, neuerTransfer.description, neuerTransfer.amount, neuerTransfer.sender, neuerTransfer.recipient);
    }

    /**
     * @return = Gibt den unveränderten Wert von amount zurück
     */
    @Override
    public double calculate()
    {
        return amount;
    }

    /**
     * @return = Gibt alle Klassenattribute als einen String zurück
     * super = Vaterklasse
     */
    @Override
    public String toString()
    {
        return super.toString() + ", Sender: " + sender + ", Recipient: " + recipient + "\n";
    }

    /**
     * Es werden zwei Objekte von Transfer verglichen.
     * @param obj = Hierbei handelt es sich um das zu vergleichende Objekt
     * @return = Wenn beide gleich → true, wenn beide ungleich → false
     */
    @Override
    public boolean equals(Object obj)
    {
        if (obj instanceof Transfer transfer)
        {
            return (super.equals(transfer) && this.sender.equals(transfer.sender) && this.recipient.equals(transfer.recipient));
        }
        else
        {
            return false;
        }
    }
}