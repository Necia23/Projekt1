import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.net.URL;

public class ExchangeRateService {
    private static final String API_URL = "https://";

    public CurrencyExchangeRate getExchangeRate(String fromCurrency, String toCurrency) throws IOException {
        String apiUrl = API_URL + "?base=" + fromCurrency + "&symbols=" + toCurrency;
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readValue(new URL(apiUrl), CurrencyExchangeRate.class);
    }
}
