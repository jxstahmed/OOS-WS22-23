package bank;

import bank.exceptions.*;
import com.google.gson.*;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.*;

public class PrivateBank implements Bank {

    /**
     * Repräsentiert den Namen der privaten Bank.
     */
    private String name;

    /**
     * Verzeichnis für die Json Files.
     */
    private String directoryName;

    /**
     * Ganzer Verzeichnis zu den Dateien.
     */
    private String fullPath;

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
     *  Setter für das Verzeichnis von den Json.
     */
    public void setDirectoryName(String directoryName)
    {
        this.directoryName = directoryName;
    }

    /**
     *  Getter für das Verzeichnis der Json.
     */
    public String getDirectoryName()
    {
        return directoryName;
    }

    /**
     *  Setter für den ganzen Pfad des Verzeichnis.
     */
    public void setFullPath(String directoryName, boolean kopiert)
    {
        if (kopiert)
            fullPath = "data/Kopiert/" + directoryName;
        else
            fullPath = "data/" + directoryName;
    }

    /**
     *  Getter für den ganzen Pfad des Verzeichnis.
     */
    public String getFullPath()
    {
        return fullPath;
    }


    /**
     * Konstruktor mit 3 Attributen.
     */
    public PrivateBank(String newName, String newDirectoryName, double newIncomingInterest, double newOutgoingInterest) throws ZinsenException {
        this.name = newName;
        this.directoryName = newDirectoryName;
        this.setIncomingInterest(newIncomingInterest);
        this.setOutgoingInterest(newOutgoingInterest);

        setFullPath(newDirectoryName, false);

        try {
            Path path = Paths.get(fullPath);

            if (Files.notExists(path)) {
                Files.createDirectories(path);
                System.out.println("\nDas Verzeichnis für " + PrivateBank.this.getName() + " wurde erstellt!");
            }
            else {
                System.out.println("\nDas Verzeichnis für " + PrivateBank.this.getName() + " ist bereits vorhanden!");
                System.out.println("Lese die Accounts vom Verzeichnis " + PrivateBank.this.getName() + ":");
                readAccounts();
                System.out.println("Accounts wurden gelesen, aus dem Verzeichnis " + PrivateBank.this.getName() + "\n");
            }
        } catch (IOException | AccountAlreadyExistsException e) {
            System.out.println("Konnte Verzeichnis nicht erstellen für: " + PrivateBank.this.getName() + "!");
        }

    }

    /**
     * Copy Konstruktor
     */
    public PrivateBank(PrivateBank newPrivateBank) throws ZinsenException {
        this(newPrivateBank.name, newPrivateBank.directoryName, newPrivateBank.incomingInterest, newPrivateBank.outgoingInterest);
        this.accountsToTransactions = newPrivateBank.accountsToTransactions;

        // Copy for later usagee
        String oldFullPath = getFullPath();

        setFullPath(newPrivateBank.directoryName, true);

        System.out.println("OLD: " + oldFullPath + "\n" + "NEW: " + getFullPath());

        try {
            Path path = Paths.get(fullPath);
            if (Files.notExists(path)) {
                Files.createDirectories(path);

                // Copy content
                copyFiles(oldFullPath, getFullPath());

                System.out.println("\nDas Verzeichnis für " + newPrivateBank.getName() + " wurde erstellt!");
            }
            else {
                System.out.println("\nDas Verzeichnis für " + newPrivateBank.getName() + " ist bereits vorhanden!");
                System.out.println("Lese die Accounts vom Verzeichnis " + newPrivateBank.getName() + ":");

                // Copy content
                copyFiles(oldFullPath, getFullPath());

                System.out.println("Accounts wurden gelesen, aus dem Verzeichnis " + newPrivateBank.getName() + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Konnte Verzeichnis nicht erstellen für: " + newPrivateBank.getName() + "!");
        }
    }

    private void copyFiles(String oldPath, String newPath) throws IOException {
        final File oldFolder = new File(oldPath);
        final File[] oldPathFiles = Objects.requireNonNull(oldFolder.listFiles());
        for (File oldPathFile : oldPathFiles) {
            Path oldFilePath = Path.of(oldPathFile.getPath());
            Path newFilePath = Path.of(newPath + "/" + oldPathFile.getName());
            Files.copy(oldFilePath, newFilePath, StandardCopyOption.REPLACE_EXISTING);
        }
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
        Path path = Path.of(PrivateBank.this.getFullPath() + "/" + account + ".json");

        if (Files.exists(path))
        {
            System.out.print("\nFüge den Account: " + account + ", zu folgender Bank hinzu: " + name + "." );
            if (accountsToTransactions.containsKey(account))
            {
                throw new AccountAlreadyExistsException("FEHLER! Dieser Account: " + account + ", existiert bereits!\n");
            }
            else
            {
                accountsToTransactions.put(account, List.of());
                System.out.println("Account wurde Erfolgreich hinzugefügt.!");
            }
        }
        else
        {
            System.out.print("\nErstelle den Account: " + account + ", zu folgender Bank hinzu: " + name + ".");
            accountsToTransactions.put(account, List.of());
            writeAccount(account);
            System.out.println("Account wurde Erfolgreich erstellt.!");
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
        Path path = Path.of(PrivateBank.this.getFullPath() + "/" + account + ".json");
        String transactionsString = transactions.toString().replaceAll("[]]|[\\[]", "").replace("\n, ", "\n\t\t");

        if (Files.exists(path))
        {
            System.out.print("\nFüge den Account: " + account + ", zu der Bank: " + name + "hinzu , mit folgender Transaktionen Liste: \n\t\t" + transactionsString);
            if (accountsToTransactions.containsKey(account))
            {
                throw new AccountAlreadyExistsException("FEHLER! Dieser Account: " + account + ", existiert bereits!\n");
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
                System.out.println("Account wurde Erfolgreich hinzugefügt.!");
            }
        } else {
            System.out.print("\nErstelle den Account: " + account + ", zur Bank: " + name + "hinzu, mit folgender Transaktionen Liste: \n\t\t" + transactionsString);
            for (Transaction valueOfTransactions : transactions)
                if (valueOfTransactions instanceof Payment payment) {
                    payment.setIncomingInterest(PrivateBank.this.incomingInterest);
                    payment.setOutgoingInterest(PrivateBank.this.outgoingInterest);
                }
            accountsToTransactions.put(account, transactions);
            writeAccount(account);
            System.out.println("Account wurde Erfolgreich erstellt.!");
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
            if (containsTransaction(account, transaction))
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
                writeAccount(account);
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
    public void removeTransaction(String account, Transaction transaction) throws TransactionDoesNotExistException, ZinsenException, AccountDoesNotExistException {
        System.out.println("Entferne die Transaktion <" + transaction.toString().replace("\n", "") +
                "> vom Account <" + account +
                "> in der Bank <" + name + ">");

        if (accountsToTransactions.get(account) == null || !containsTransaction(account, transaction))
        {
            throw new TransactionDoesNotExistException("Diese Transaktion existiert nicht!\n");
        }
        else
        {
            List<Transaction> transactionsList = new ArrayList<>(accountsToTransactions.get(account));
            transactionsList.remove(transaction);
            accountsToTransactions.put(account, transactionsList);
            writeAccount(account);
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

        System.out.println("\nÜberprüfe den Account <" + account +
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

    /**
     * Serialisiert ein Konto und speichert dieses dann in einer Json.
     *
     * @param account der Account der geschrieben werden soll.
     */
    private void writeAccount(String account) {

        try (FileWriter file = new FileWriter(getFullPath() + "/" + account + ".json")) {

            file.write("[\n  ");
            for (Transaction transaction : accountsToTransactions.get(account)) {
                Gson gson = new GsonBuilder()
                        .registerTypeAdapter(transaction.getClass(), new De_Serializer())
                        .setPrettyPrinting()
                        .create();
                String json = gson.toJson(transaction);
                if (accountsToTransactions.get(account).indexOf(transaction) != 0)
                    file.write(",\n  ");
                file.write(json);
            }

            file.write("\n]");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void copyAccounts(String oldPath, String newPath) {

    }

    /**
     * Lese alle Accounts aus dem System und erstelle diese als PrivateBank Objekte.
     *
     * @throws AccountAlreadyExistsException
     * @throws IOException
     */
    private void readAccounts() throws AccountAlreadyExistsException, IOException, ZinsenException {

        final File folder = new File(PrivateBank.this.getFullPath());
        final File[] listOfFiles = Objects.requireNonNull(folder.listFiles());

        for (File file : listOfFiles) {
            String accountName = file.getName().replace(".json", "");
            String accountNameFile = file.getName();
            PrivateBank.this.createAccount(accountName);

            try {

                Reader reader = new FileReader(PrivateBank.this.getFullPath() + "/" + accountNameFile);
                JsonArray parser = JsonParser.parseReader(reader).getAsJsonArray();
                for (JsonElement jsonElement : parser.getAsJsonArray()) {

                    JsonObject jsonObject = jsonElement.getAsJsonObject();

                    Gson customGson = new GsonBuilder()
                            .registerTypeAdapter(Transaction.class, new De_Serializer())
                            .create();

                    String str = customGson.toJson(jsonObject.get("INSTANCE"));

                    if (jsonObject.get("CLASSNAME").getAsString().equals("Payment")) {
                        Payment payment = customGson.fromJson(str, Payment.class);
                        PrivateBank.this.addTransaction(accountName, payment);
                    }
                    else if (jsonObject.get("CLASSNAME").getAsString().equals("IncomingTransfer")) {
                        IncomingTransfer incomingTransfer = customGson.fromJson(str, IncomingTransfer.class);
                        PrivateBank.this.addTransaction(accountName, incomingTransfer);
                    }
                    else {
                        OutgoingTransfer outgoingTransfer = customGson.fromJson(str, OutgoingTransfer.class);
                        PrivateBank.this.addTransaction(accountName, outgoingTransfer);
                    }
                }
                reader.close();

            } catch (IOException e) {
                e.printStackTrace();
            } catch (TransactionAlreadyExistException | AccountDoesNotExistException e) {
                System.out.println(e);
            }
        }
    }

    /**
     * Lösche einen Account in AccountsToTransactions und im Daten System.
     * @param account Account der gelöscht werden soll.
     * @throws AccountDoesNotExistException, wenn dieser Account nicht existiert.
     */
    public void deleteAccount(String account) throws AccountDoesNotExistException, IOException
    {
        System.out.print("\nLösche den Account <" + account + "> von der Bank <" + this.getName() + "> ");
        if (!accountsToTransactions.containsKey(account))
        {
            throw new AccountDoesNotExistException("=> Fehler! Der Account <" + account + "> existiert nicht!\n");
        }
        else
        {
            accountsToTransactions.remove(account);
            Path path = Path.of(this.getFullPath() + "/" + account + ".json");
            Files.deleteIfExists(path);
            System.out.println("=> Erfolgreich gelöscht.!");
        }
    }

    /**
     * Zeigte alle Account in AccountsToTransactions.
     * @return Liste aller accounts
     */
    public List<String> getAllAccounts()
    {
        Set<String> setKey = accountsToTransactions.keySet();
        return new ArrayList<>(setKey);
    }


}