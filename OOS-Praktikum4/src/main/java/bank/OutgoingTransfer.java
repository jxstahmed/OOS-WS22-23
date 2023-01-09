package bank;

import bank.exceptions.*;

public class OutgoingTransfer extends Transfer
{
    /**
     * Konstruktor mit drei Attributen
     *
     * @param newDate ist der Wert für das Datum.
     * @param newDescription ist der Wert für die Beschreibung.
     * @param newAmount ist der Wert für den Betrag.
     */
    public OutgoingTransfer(String newDate, String newDescription, double newAmount) throws BetragException
    {
        super(newDate, newDescription, newAmount);
    }

    /**
     * Konstruktor mit allen Attributen
     *
     * @param newDate ist der Wert für das Datum.
     * @param newDescription ist der Wert für die Beschreibung.
     * @param newAmount ist der Wert für den Betrag.
     * @param newSender ist der Wert für den Sender.
     * @param newRecipient ist der Wert für den Empfänger.
     */
    public OutgoingTransfer(String newDate, String newDescription, double newAmount, String newSender, String newRecipient) throws BetragException
    {
        super(newDate, newDescription, newAmount, newSender, newRecipient);
    }

    /**
     * Copy Konstruktor
     *
     * @param newTransfer das neue Objekt
     */
    public OutgoingTransfer(Transfer newTransfer) throws BetragException
    {
        super(newTransfer);
    }

    @Override
    public double calculate()
    {
        return -amount;
    }
}