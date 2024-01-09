
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class TransactionService {
    private static final ObjectMapper objectMapper = new ObjectMapper();

    public void saveTransactionsToFile(List<Transaction> transactions, String fileName) throws IOException {
        objectMapper.writeValue(new File(fileName), transactions);
    }

    public List<Transaction> readTransactionsFromFile(String fileName) throws IOException {
        return objectMapper.readValue(new File(fileName), objectMapper.getTypeFactory().constructCollectionType(List.class, Transaction.class));
    }
}

