package subsystemC;
import java.util.*;
import utils.FileUtil;
public class LoyaltyRepository {
    private final String FILE = "C_loyalty.txt";

    public void save(LoyaltyRecord rec) {
        List<LoyaltyRecord> all = findAll();
        all.removeIf(r -> r.transactionId.equals(rec.transactionId));
        all.add(rec);
        List<String> lines = new ArrayList<>();
        for (LoyaltyRecord r : all) lines.add(r.toCsv());
        FileUtil.writeLines(FILE, lines);
    }

    public LoyaltyRecord findByTransaction(String txId) {
        return findAll().stream().filter(r -> r.transactionId.equals(txId)).findFirst().orElse(null);
    }

    public List<LoyaltyRecord> findAll() {
        List<LoyaltyRecord> list = new ArrayList<>();
        for (String line : FileUtil.readLines(FILE)) list.add(LoyaltyRecord.fromCsv(line));
        return list;
    }
}