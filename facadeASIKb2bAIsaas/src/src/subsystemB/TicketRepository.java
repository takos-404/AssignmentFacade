package subsystemB;
import java.util.*;
import utils.FileUtil;
public class TicketRepository {
    private final String FILE = "B_tickets.txt";

    public void save(ETicket ticket) {
        List<ETicket> all = findAll();
        all.removeIf(t -> t.ticketId.equals(ticket.ticketId));
        all.add(ticket);
        List<String> lines = new ArrayList<>();
        for (ETicket t : all) lines.add(t.toCsv());
        FileUtil.writeLines(FILE, lines);
    }

    public ETicket findByReservationId(String resId) {
        return findAll().stream().filter(t -> t.reservationId.equals(resId)).findFirst().orElse(null);
    }

    public List<ETicket> findAll() {
        List<ETicket> list = new ArrayList<>();
        for (String line : FileUtil.readLines(FILE)) list.add(ETicket.fromCsv(line));
        return list;
    }
}