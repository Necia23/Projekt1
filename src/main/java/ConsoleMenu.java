import java.io.IOException;
import java.util.*;

public class ConsoleMenu {
    private final ExchangeRateService exchangeRateService;
    private final TransactionService transactionService;

    public ConsoleMenu(ExchangeRateService exchangeRateService, TransactionService transactionService) {
        this.exchangeRateService = exchangeRateService;
        this.transactionService = transactionService;
    }

    public void showMenu() {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("1. Przewalutowanie");
            System.out.println("2. Zapis listy transakcji do pliku CSV");
            System.out.println("3. Wyświetlenie ilości poszczególnych walut z pliku CSV");
            System.out.println("4. Wyjście");

            System.out.print("Wybierz opcję: ");
            int choice = scanner.nextInt();
            scanner.nextLine(); // consume newline

            switch (choice) {
                case 1:
                    performCurrencyExchange(scanner);
                    break;
                case 2:
                    saveTransactionsToFile(scanner);
                    break;
                case 3:
                    displayPortfolioFromFile(scanner);
                    break;
                case 4:
                    System.out.println("Koniec programu.");
                    System.exit(0);
                default:
                    System.out.println("Nieprawidłowy wybór. Spróbuj ponownie.");
            }
        }
    }

    private void performCurrencyExchange(Scanner scanner) {
        System.out.print("Podaj walutę źródłową: ");
        String sourceCurrency = scanner.nextLine();

        System.out.print("Podaj walutę docelową: ");
        String targetCurrency = scanner.nextLine();

        try {
            CurrencyExchangeRate exchangeRate = exchangeRateService.getExchangeRate(sourceCurrency, targetCurrency);
            System.out.print("Podaj kwotę do przewalutowania: ");
            double amount = scanner.nextDouble();

            double convertedAmount = amount * exchangeRate.getRate();
            System.out.println("Przewalutowano: " + amount + " " + sourceCurrency + " na " + convertedAmount + " " + targetCurrency);
        } catch (IOException e) {
            System.out.println("Błąd przy pobieraniu kursu wymiany. Spróbuj ponownie.");
        }
    }

    private void saveTransactionsToFile(Scanner scanner) {
        try {
            List<Transaction> transactions = new ArrayList<>();

            while (true) {
                System.out.print("Podaj walutę źródłową transakcji (lub 'exit' aby zakończyć): ");
                String sourceCurrency = scanner.nextLine();

                if (sourceCurrency.equalsIgnoreCase("exit")) {
                    break;
                }

                System.out.print("Podaj walutę docelową transakcji: ");
                String targetCurrency = scanner.nextLine();

                System.out.print("Podaj kwotę transakcji: ");
                double amount = scanner.nextDouble();
                scanner.nextLine(); // consume newline

                Transaction transaction = new Transaction();
                transaction.setSourceCurrency(sourceCurrency);
                transaction.setTargetCurrency(targetCurrency);
                transaction.setAmount(amount);
                transaction.setDate(new Date());

                transactions.add(transaction);

                System.out.println("Transakcja dodana.");
            }

            System.out.print("Podaj nazwę pliku do zapisu (bez rozszerzenia .csv): ");
            String fileName = scanner.nextLine() + ".csv";

            transactionService.saveTransactionsToFile(transactions, fileName);

            System.out.println("Transakcje zapisane do pliku " + fileName);
        } catch (IOException e) {
            System.out.println("Błąd podczas zapisu transakcji do pliku. Spróbuj ponownie.");
        }
    }

    private void displayPortfolioFromFile(Scanner scanner) {
        try {
            System.out.print("Podaj nazwę pliku CSV do wczytania: ");
            String fileName = scanner.nextLine();

            List<Transaction> transactions = transactionService.readTransactionsFromFile(fileName);

            if (transactions.isEmpty()) {
                System.out.println("Brak transakcji do wyświetlenia.");
                return;
            }

            Map<String, Double> currencyAmounts = new HashMap<>();

            for (Transaction transaction : transactions) {
                String sourceCurrency = transaction.getSourceCurrency();
                double amount = transaction.getAmount();

                currencyAmounts.put(sourceCurrency, currencyAmounts.getOrDefault(sourceCurrency, 0.0) + amount);
            }

            System.out.println("Portfel użytkownika:");

            for (Map.Entry<String, Double> entry : currencyAmounts.entrySet()) {
                System.out.println(entry.getKey() + ": " + entry.getValue());
            }

        } catch (IOException e) {
            System.out.println("Błąd podczas wczytywania transakcji z pliku. Spróbuj ponownie.");
        }
    }
}

