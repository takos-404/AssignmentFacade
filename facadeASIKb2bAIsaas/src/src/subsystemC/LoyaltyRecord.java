package subsystemC;
public class LoyaltyRecord {
    public String transactionId;
    public String accountId;
    public LoyaltyStatus status;

    public LoyaltyRecord(String transactionId, String accountId, LoyaltyStatus status) {
        this.transactionId = transactionId; this.accountId = accountId; this.status = status;
    }
    public String toCsv() { return transactionId + "," + accountId + "," + status; }
    public static LoyaltyRecord fromCsv(String csv) {
        String[] parts = csv.split(",");
        return new LoyaltyRecord(parts[0], parts[1], LoyaltyStatus.valueOf(parts[2]));
    }
}