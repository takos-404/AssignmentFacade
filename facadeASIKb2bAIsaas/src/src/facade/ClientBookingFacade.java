package facade;

import subsystemA.*;
import subsystemB.*;
import subsystemC.*;

public class ClientBookingFacade {
    private ReservationRepository repoA = new ReservationRepository();
    private TicketRepository repoB = new TicketRepository();
    private LoyaltyRepository repoC = new LoyaltyRepository();
    private ChainRepository chainRepo = new ChainRepository();

    private FareCalculator fareCalc = new FareCalculator();
    private BaggageRulesEngine baggageEngine = new BaggageRulesEngine();
    private TicketIssuer ticketIssuer = new TicketIssuer();
    private LoyaltyProgram loyaltyProgram = new LoyaltyProgram();

    public String bookFlight(String seat, double baggageWeight, String accountId) {
        String txId = "TX-" + System.currentTimeMillis();
        TransactionChain chain = new TransactionChain(txId, ChainStatus.PENDING_A);
        chainRepo.save(chain);

        try {
            // ШАГ A
            double price = fareCalc.calculate(seat);
            SeatReservation res = new SeatReservation(txId, seat, price, ReservationStatus.HELD);
            repoA.save(res);

            chain.status = ChainStatus.PENDING_B;
            chainRepo.save(chain);

            // ШАГ B
            baggageEngine.validateBaggage(baggageWeight); // Может бросить исключение!
            ETicket ticket = ticketIssuer.issue(txId);
            repoB.save(ticket);

            chain.status = ChainStatus.PENDING_C;
            chainRepo.save(chain);

            // ШАГ C
            loyaltyProgram.addMiles(accountId); // Может бросить исключение!
            LoyaltyRecord loyalty = new LoyaltyRecord(txId, accountId, LoyaltyStatus.CONFIRMED);
            repoC.save(loyalty);

            // Успех!
            chain.status = ChainStatus.COMPLETED;
            chainRepo.save(chain);
            return "SUCCESS: Booking " + txId + " completed!";

        } catch (exceptions.BaggageOverweightException e) {
            // Компенсация: B упал -> Откат A
            System.out.println("❌ Error at Step B: " + e.getMessage() + ". Rolling back Step A...");
            rollbackA(txId);
            chain.status = ChainStatus.ROLLED_BACK_A;
            chainRepo.save(chain);
            return "FAILED: " + e.getMessage();

        } catch (exceptions.AccountBlockedException e) {
            // Компенсация: C упал -> Откат B, потом A
            System.out.println("❌ Error at Step C: " + e.getMessage() + ". Rolling back Step B and A...");
            rollbackB(txId);
            rollbackA(txId);
            chain.status = ChainStatus.ROLLED_BACK_AB;
            chainRepo.save(chain);
            return "FAILED: " + e.getMessage();
        }
    }

    private void rollbackA(String txId) {
        SeatReservation res = repoA.findById(txId);
        if (res != null) {
            res.status = ReservationStatus.RELEASED;
            repoA.save(res);
        }
    }

    private void rollbackB(String txId) {
        ETicket ticket = repoB.findByReservationId(txId);
        if (ticket != null) {
            ticket.status = TicketStatus.REVOKED;
            repoB.save(ticket);
        }
    }
}