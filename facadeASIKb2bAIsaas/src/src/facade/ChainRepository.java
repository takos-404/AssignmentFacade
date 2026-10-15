package facade;
import java.util.*;
import utils.FileUtil;
public class ChainRepository {
    private final String FILE = "Chain_states.txt";
    public void save(TransactionChain chain) {
        List<TransactionChain> all = findAll();
        all.removeIf(c -> c.id.equals(chain.id));
        all.add(chain);
        List<String> lines = new ArrayList<>();
        for (TransactionChain c : all) lines.add(c.toCsv());
        FileUtil.writeLines(FILE, lines);
    }
    public TransactionChain findById(String id) {
        return findAll().stream().filter(c -> c.id.equals(id)).findFirst().orElse(null);
    }
    public List<TransactionChain> findAll() {
        List<TransactionChain> list = new ArrayList<>();
        for (String line : FileUtil.readLines(FILE)) list.add(TransactionChain.fromCsv(line));
        return list;
    }
}