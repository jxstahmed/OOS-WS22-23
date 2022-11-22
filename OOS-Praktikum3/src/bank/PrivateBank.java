package bank;

import bank.exceptions.*;

import java.util.*;

public class PrivateBank implements Bank {

    /**
     * Repräsentiert den Namen der privaten Bank.
     */
    private String name;

    /**
     * Zinsen die bei einer Einzahlung anfallen. (in Prozent von 0 bis 1).
     */
    private double incomingInterest;

    /**
     * Zinsen die bei einer Auszahlung anfallen. (in Prozent von 0 bis 1).
     */
    private double outgoingInterest;

    /**
     * Verknüpft Konten mit Transaktionen (im 0 bis N Verhältnis).
     */
    private Map<String, List<Transaction>> accountsToTransactions =  new HashMap<>();


    /**
     *  Setter für den Namen.
     */
    public void setName(String name)
    {
        this.name = name;
    }

    /**
     *  Getter für den Namen.
     */
    public String getName()
    {
        return this.name;
    }

    /**
     *  Setter für die Zinsen bei Einzahlung.
     */
    public void setIncomingInterest(double incomingInterest)
    {
        if (0 > incomingInterest || incomingInterest > 1)
        {
            System.out.println("Die Zinsen müssen einen Prozentwert zwischen 0 und 1 haben!");
            return;
        }
        else
        {
            this.incomingInterest = incomingInterest;
        }
    }

    /**
     *  Getter für die Zinsen bei Einzahlung.
     */
    public double getIncomingInterest()
    {
        return incomingInterest;
    }

    /**
     *  Setter für die Zinsen bei Auszahlung.
     */
    public void setOutgoingInterest(double outgoingInterest)
    {
        if ( 0 > outgoingInterest || outgoingInterest > 1)
        {
            System.out.println("Die Zinsen müssen einen Prozentwert zwischen 0 und 1 haben!");
            return;
        }
        else
        {
            this.outgoingInterest = outgoingInterest;
        }
    }

    /**
     *  Getter für die Zinsen bei Auszahlung.
     */
    public double getOutgoingInterest()
    {
        return outgoingInterest;
    }


    /**
     * Konstruktor mit 3 Attributen.
     */
    public PrivateBank(String newName, double newIncomingInterest, double newOutgoingInterest)
    {
        this.name = newName;
        this.setIncomingInterest(newIncomingInterest);
        this.setOutgoingInterest(newOutgoingInterest);

    }

    /**
     * Copy Konstruktor
     */
    public PrivateBank(PrivateBank newPrivateBank)
    {
        this(newPrivateBank.name, newPrivateBank.incomingInterest, newPrivateBank.outgoingInterest);
        this.accountsToTransactions = newPrivateBank.accountsToTransactions;
    }

    /**
     * @return gibt alle Attribute des Objekts als String wieder.
     */
    @Override
    public String toString()
    {
        StringBuilder str = new StringBuilder();
        Set<String> setKey = accountsToTransactions.keySet();
        for (String key : setKey)
        {
            str.append(key).append(" => \n");
            List<Transaction> transactionsList = accountsToTransactions.get(key);
            for (Transaction transaction : transactionsList)
            {
                str.append("\t\t").append(transaction);
            }
        }
        return "Name: " + name +
                "\nIncoming Interest: " + incomingInterest +
                "\noutgoing Interest: " + outgoingInterest +
                "\n" + str;
    }

    /**
     * Es werden zwei Objekte von PrivateBank verglichen.
     * @param obj = Hierbei handelt es sich um das zu vergleichende Objekt
     * @return = Wenn beide gleich → true, wenn beide ungleich → false
     */
    public boolean equals(Object obj)
    {
        if (obj instanceof PrivateBank privateBank)
        {
            return (name.equals(privateBank.name)
                    && incomingInterest == privateBank.incomingInterest
                    && outgoingInterest == privateBank.outgoingInterest &&
                    accountsToTransactions.equals(privateBank.accountsToTransactions));
        }
        else
        {
            return false;
        }
    }

    /**
     *Fügt einen Account zur Bank hinzu. Wenn der Account bereits existiert, wirft er eine exception.
     *
     * @param account der hinzugefügt werden soll.
     * @throws AccountAlreadyExistsException wenn der Account bereits existiert.
     */
    @Override
    public void createAccount(String account) throws AccountAlreadyExistsException
    {
        System.out.println("Erstelle einen neuen Account <" + account +
                "> für die Bank <" + name + ">");

        if (accountsToTransactions.containsKey(account))
        {
            throw new AccountAlreadyExistsException("Der Account <" + account +"> existiert bereits.!\n");
        }
        else
        {
            accountsToTransactions.put(account, List.of());
            System.out.println("=> Erfolgreich erstellt.!\n");
        }
    }

    /**
     * Fügt einen Account hinzu (mit bereits existierenden Transaktionen) zur Bank. Wenn der Account bereits existiert, wirft er eine exception.
     *
     * @param account der hinzugefügt wird.
     * @param transactions die mit hinzugefügt werden.
     * @throws AccountAlreadyExistsException wenn der Account bereits existiert.
     */
    @Override
    public void createAccount(String account, List<Transaction> transactions) throws AccountAlreadyExistsException, ZinsenException
    {
        System.out.print("Füge neuen Account <" + account +
                "> zur bank <" + name +
                "> mit folgenden Transaktionen hinzu: \n\t\t" +
                transactions.toString().replaceAll("[]]|[\\[]", "").replace("\n, ", "\n\t\t"));

        if ( (accountsToTransactions.containsKey(account)) || (accountsToTransactions.containsKey(account) && accountsToTransactions.containsValue(transactions)) )
        {
            throw new AccountAlreadyExistsException("Der Account <" + account + "> existiert bereits!\n");
        }
        else
        {
            for (Transaction valueOfTransactions : transactions)
            {
                if (valueOfTransactions instanceof Payment payment)
                {
                    payment.setIncomingInterest(PrivateBank.this.incomingInterest);
                    payment.setOutgoingInterest(PrivateBank.this.outgoingInterest);
                }
            }
            accountsToTransactions.put(account, transactions);
            System.out.println("=> Erfolgreich hinzugefügt!\n");
        }
    }

    /**
     * Fügt eine neue Transaktion zu einem Account. Wenn der Account nicht existiert oder die Transaktion bereits vorhanden ist,
     * wirft er eine exception.
     *
     * @param account dem eine Transaktion hinzugefügt wird.
     * @param transaction welche zum Account hinzugefügt werden soll.
     * @throws TransactionAlreadyExistException wenn die Transaktion bereits exisitert.
     */
    @Override
    public void addTransaction(String account, Transaction transaction) throws TransactionAlreadyExistException, AccountDoesNotExistException, ZinsenException
    {
        System.out.println("Füge folgende neue Transaktion hinzu<" +
                transaction.toString().replace("\n", "") +
                "> zum Account <" + account +
                "> in der Bank <" + name + ">");

        if (!accountsToTransactions.containsKey(account))
        {
            throw new AccountDoesNotExistException("Der Account <" + account + "> existiert nicht!\n");
        }
        else
        {
            if (accountsToTransactions.get(account).contains(transaction))
            {
                throw new TransactionAlreadyExistException("Diese Transaktion ist bereits vorhanden.!\n");
            }
            else
            {
                if (transaction instanceof Payment payment)
                {
                    payment.setIncomingInterest(PrivateBank.this.incomingInterest);
                    payment.setOutgoingInterest(PrivateBank.this.outgoingInterest);
                }
                List<Transaction> transactionsList = new ArrayList<>(accountsToTransactions.get(account));
                transactionsList.add(transaction);
                accountsToTransactions.put(account, transactionsList);
                System.out.println("=> Erfolgreich hinzugefügt!\n");
            }
        }
    }

    /**
     * Entfernt eine Transaktion von einem Account.
     * Sollte diese Transaktion nicht exisiteren, so wirft er eine exception.
     *
     * @param account von dem eine Transaktion entfernt wird.
     * @param transaction welche entfernt werden soll vom Account.
     * @throws TransactionDoesNotExistException wenn die Transaktion nicht exisitert.
     */
    @Override
    public void removeTransaction(String account, Transaction transaction) throws TransactionDoesNotExistException
    {
        System.out.println("Entferne die Transaktion <" + transaction.toString().replace("\n", "") +
                "> vom Account <" + account +
                "> in der Bank <" + name + ">");

        if (accountsToTransactions.get(account) == null || !accountsToTransactions.get(account).contains(transaction))
        {
            throw new TransactionDoesNotExistException("Diese Transaktion existiert nicht!\n");
        }
        else
        {
            List<Transaction> transactionsList = new ArrayList<>(accountsToTransactions.get(account));
            transactionsList.remove(transaction);
            accountsToTransactions.put(account, transactionsList);
            System.out.println("=> Erfolgreich entfernt!\n");
        }

    }

    /**
     * Überprüft ob eine Transaktion in einem Account exisitert.
     *
     * @param account welcher überprüft wird.
     * @param transaction nach welcher gesucht wird.
     */
    @Override
    public boolean containsTransaction(String account, Transaction transaction) throws ZinsenException, AccountDoesNotExistException
    {
        if (transaction instanceof Payment payment) {
            payment.setIncomingInterest(PrivateBank.this.incomingInterest);
            payment.setOutgoingInterest(PrivateBank.this.outgoingInterest);
        }

        if(accountsToTransactions.get(account) == null)
        {
            throw new AccountDoesNotExistException("Der Account <" + account + "> existiert nicht!\n");
        }

        System.out.println("Überprüfe den Account <" + account +
                "> ob er folgende Transkation besitzt <" + transaction.toString().replace("\n", "") +
                "> : " + accountsToTransactions.get(account).contains(transaction) + "\n");

        return accountsToTransactions.get(account).contains(transaction);
    }

    /**
     * Berechnet den Betrag welcher Aktuell auf dem Konto ist.
     *
     * @param account welcher überprüft werden soll.
     * @return der aktuelle Betrag auf dem Konto
     */
    @Override
    public double getAccountBalance(String account) throws AccountDoesNotExistException
    {
        if(accountsToTransactions.get(account) == null)
        {
          throw new AccountDoesNotExistException("Der Account <" + account + "> existiert nicht!\n");
        }

        double balance = 0;
        for (Transaction transaction : accountsToTransactions.get(account))
        {
            balance = balance + transaction.calculate();
        }

        System.out.println("Betrag auf dem Account <" + account +
                "> in der Bank <" + name +
                "> : " + (double) Math.round(balance * 100)/100 + "\n");

        return balance;
    }

    /**
     * Gibt die Transaktionen eines Accounts wieder.
     *
     * @param account der gewählte Account.
     * @return eine Liste aller Transaktionen
     */
    @Override
    public List<Transaction> getTransactions(String account) throws AccountDoesNotExistException
    {
        if(accountsToTransactions.get(account) == null)
        {
            throw new AccountDoesNotExistException("Der Account <" + account + "> existiert nicht!\n");
        }

        System.out.println("Liste der Transkationen des Accounts <" + account +
                "> in der Bank <" + name + ">\n" +
                accountsToTransactions.get(account).toString().replace("[", "\t\t").replace("]","").replace("\n, ", "\n\t\t"));

        return accountsToTransactions.get(account);
    }

    /**
     * Gibt eine sortierte Liste der Transkationen wieder.
     * Diese entweder Aufsteigend oder Absteigend sortiert, anhand des Betrags.
     *
     * @param account der gewählte Account.
     * @param asc gibt an ob Aufsteigend oder absteigend sortiert.
     * @return gibt die Liste der Transaktionen wieder.
     */
    @Override
    public List<Transaction> getTransactionsSorted(String account, boolean asc) throws AccountDoesNotExistException
    {
        if (accountsToTransactions.get(account) == null)
        {
            throw new AccountDoesNotExistException("Der Account <" + account + "> existiert nicht!\n");
        }

        List<Transaction> sortedTransactionsList = new ArrayList<>(accountsToTransactions.get(account));

        if(asc)
        {
            sortedTransactionsList.sort(Comparator.comparingDouble(Transaction::calculate));
            System.out.println("Sortierte Liste der Transaktionen des Accounts <" + account +
                    "> in Aufsteigender Folge anhand des Betrags:\n" + sortedTransactionsList.toString().replace("[", "\t\t").replace("]","").replace("\n, ", "\n\t\t"));
        }
        else {
            sortedTransactionsList.sort(Comparator.comparingDouble(Transaction::calculate).reversed());
            System.out.println("Sortierte Liste der Transaktionen des Accounts <" + account +
                    "> in Absteigender Folge anhand des Betrags:\n" + sortedTransactionsList.toString().replace("[", "\t\t").replace("]","").replace("\n, ", "\n\t\t"));
        }
        return sortedTransactionsList;
    }

    /**
     * Gibt entweder eine Liste der positiven oder negativen Transaktionen wieder.
     *
     * @param account der gewählte Account
     * @param positive bestimmt ob alle positiven oder negativen angezeigt werden sollen.
     * @return gibt die Liste der Transaktionen wieder.
     */
    @Override
    public List<Transaction> getTransactionsByType(String account, boolean positive)
    {
        List<Transaction> transactionsListByType = new ArrayList<>();

        if (positive)
        {
            System.out.println("Liste der positiven Transkationen des Accounts <" + account + "> :");
        }
        else
        {
            System.out.println("Liste der negativen Transkationen des Accounts <" + account + "> :");
        }

        for (Transaction transaction : accountsToTransactions.get(account))
        {
            boolean istAbsender = false;

            if(transaction instanceof Transfer transfer)
            {
                istAbsender = transfer.getSender().equals(account);
            }

            if(positive && !istAbsender)
            {
                // Ankommende Transaktionen
                transactionsListByType.add(transaction);

            }
            else if (!positive && istAbsender)
            {
                // Ausgehende Transaktionen
                transactionsListByType.add(transaction);
            }
        }
        System.out.println(transactionsListByType.toString().replace("[", "\t\t").replace("]","").replace("\n, ", "\n\t\t"));

        return transactionsListByType;
    }
}