package facade;
public class TransactionChain {
    public String id;
    public ChainStatus status;

    public TransactionChain(String id, ChainStatus status) { this.id = id; this.status = status; }
    public String toCsv() { return id + "," + status; }
    public static TransactionChain fromCsv(String csv) {
        String[] parts = csv.split(",");
        return new TransactionChain(parts[0], ChainStatus.valueOf(parts[1]));
    }
}