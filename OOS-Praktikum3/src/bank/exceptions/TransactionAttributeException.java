package bank.exceptions;

public class TransactionAttributeException extends Exception
{
    public TransactionAttributeException(String errorMsg)
    {
        super(errorMsg);
    }
}