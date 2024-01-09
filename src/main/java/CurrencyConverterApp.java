public class CurrencyConverterApp {
    public static void main(String[] args) {
        ExchangeRateService exchangeRateService = new ExchangeRateService();
        TransactionService transactionService = new TransactionService();
        ConsoleMenu consoleMenu = new ConsoleMenu(exchangeRateService, transactionService);

        consoleMenu.showMenu();
    }
}
