import facade.*;
import subsystemA.*;
import subsystemB.*;
import subsystemC.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.nio.file.Files;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.*;

public class FacadeTest {

    private ClientBookingFacade facade;
    private ReservationRepository repoA;
    private TicketRepository repoB;
    private LoyaltyRepository repoC;
    private ChainRepository chainRepo;

    @BeforeEach
    public void setUp() {
        // Очищаем БД перед каждым тестом (Используем Files.deleteIfExists чтобы IDE не ругалась)
        try {
            Files.deleteIfExists(Paths.get("A_reservations.txt"));
            Files.deleteIfExists(Paths.get("B_tickets.txt"));
            Files.deleteIfExists(Paths.get("C_loyalty.txt"));
            Files.deleteIfExists(Paths.get("Chain_states.txt"));
        } catch (Exception e) {
            System.out.println("No files to delete.");
        }

        facade = new ClientBookingFacade();
        repoA = new ReservationRepository();
        repoB = new TicketRepository();
        repoC = new LoyaltyRepository();
        chainRepo = new ChainRepository();
    }

    // --- Domain-Specific Testing Language (DSL) ---
    private void assertReservationState(String txId, ReservationStatus expected) {
        SeatReservation res = repoA.findById(txId);
        assertNotNull(res, "Reservation should exist in DB");
        assertEquals(expected, res.status, "Reservation status mismatch");
    }

    private void assertTicketState(String txId, TicketStatus expected) {
        ETicket ticket = repoB.findByReservationId(txId);
        assertNotNull(ticket, "Ticket should exist in DB");
        assertEquals(expected, ticket.status, "Ticket status mismatch");
    }

    private String getLastTransactionId() {
        // Заменили get(0) на первый элемент списка, чтобы IDE не просила getFirst()
        return chainRepo.findAll().iterator().next().id;
    }

    // --- Tests ---

    @Test
    public void testHappyPath_AllSubsystemsCommit() {
        facade.bookFlight("A01", 15.0, "USER_OK");
        String txId = getLastTransactionId();

        assertReservationState(txId, ReservationStatus.HELD);
        assertTicketState(txId, TicketStatus.ISSUED);

        LoyaltyRecord lr = repoC.findByTransaction(txId);
        assertNotNull(lr);
        assertEquals(LoyaltyStatus.CONFIRMED, lr.status);
    }

    @Test
    public void testBaggageFails_RollsBackReservationA() {
        facade.bookFlight("B05", 25.0, "USER_OK");
        String txId = getLastTransactionId();

        assertReservationState(txId, ReservationStatus.RELEASED);
        assertNull(repoB.findByReservationId(txId));
        assertEquals(ChainStatus.ROLLED_BACK_A, chainRepo.findById(txId).status);
    }

    @Test
    public void testAccountBlocked_RollsBackTicketB_andReservationA() {
        facade.bookFlight("C09", 10.0, "BLOCKED");
        String txId = getLastTransactionId();

        assertTicketState(txId, TicketStatus.REVOKED);
        assertReservationState(txId, ReservationStatus.RELEASED);
        assertEquals(ChainStatus.ROLLED_BACK_AB, chainRepo.findById(txId).status);
    }

    @Test
    public void testPersistenceReload_SimulateRestartMidChain() {
        String fakeTxId = "TX-RESTART";
        chainRepo.save(new TransactionChain(fakeTxId, ChainStatus.PENDING_B));
        repoA.save(new SeatReservation(fakeTxId, "A1", 500.0, ReservationStatus.HELD));

        ChainRepository newChainRepo = new ChainRepository();
        ReservationRepository newRepoA = new ReservationRepository();

        TransactionChain loadedChain = newChainRepo.findById(fakeTxId);
        SeatReservation loadedRes = newRepoA.findById(fakeTxId);

        assertEquals(ChainStatus.PENDING_B, loadedChain.status);
        assertEquals(ReservationStatus.HELD, loadedRes.status);
    }
}