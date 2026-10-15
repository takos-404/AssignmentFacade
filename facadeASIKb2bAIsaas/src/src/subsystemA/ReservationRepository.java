package subsystemA;
import java.util.*;
import utils.FileUtil;
public class ReservationRepository {
    private final String FILE = "A_reservations.txt";

    public void save(SeatReservation res) {
        List<SeatReservation> all = findAll();
        all.removeIf(r -> r.id.equals(res.id));
        all.add(res);
        List<String> lines = new ArrayList<>();
        for (SeatReservation r : all) lines.add(r.toCsv());
        FileUtil.writeLines(FILE, lines);
    }

    public SeatReservation findById(String id) {
        return findAll().stream().filter(r -> r.id.equals(id)).findFirst().orElse(null);
    }

    public List<SeatReservation> findAll() {
        List<SeatReservation> list = new ArrayList<>();
        for (String line : FileUtil.readLines(FILE)) list.add(SeatReservation.fromCsv(line));
        return list;
    }
}