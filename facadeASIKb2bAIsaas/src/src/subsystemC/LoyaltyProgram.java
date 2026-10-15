package subsystemC;
import exceptions.AccountBlockedException;
public class LoyaltyProgram {
    public void addMiles(String accountId) {
        // Умышленно бросаем ошибку, если аккаунт заблокирован, чтобы симулировать падение Шага C
        if ("BLOCKED".equalsIgnoreCase(accountId)) throw new AccountBlockedException("Account " + accountId + " is blocked for fraud!");
    }
}