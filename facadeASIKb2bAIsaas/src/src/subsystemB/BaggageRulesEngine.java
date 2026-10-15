package subsystemB;
import exceptions.BaggageOverweightException;
public class BaggageRulesEngine {
    public void validateBaggage(double weight) {
        // Умышленно бросаем ошибку, если вес > 20, чтобы симулировать падение Шага B
        if (weight > 20.0) throw new BaggageOverweightException("Baggage exceeds 20kg limit: " + weight);
    }
}

