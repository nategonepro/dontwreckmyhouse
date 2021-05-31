package learn.dontwreckmyhouse.data;

import learn.dontwreckmyhouse.models.Guest;
import learn.dontwreckmyhouse.models.Host;
import learn.dontwreckmyhouse.models.Reservation;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ReservationFileRepositoryDouble implements ReservationRepository{
    Reservation reservation = makeReservation();

    private final ArrayList<Reservation> reservations = new ArrayList<>();

    public ReservationFileRepositoryDouble(){
        reservations.add(reservation);
    }

    @Override
    public List<Reservation> findByHostId(String hostId) {
        if(hostId!=null && hostId.equalsIgnoreCase("a7fd9dd1-2cee-4efe-a495-5fd002414675")){
            return reservations;
        }else {
            return null;
        }
    }

    @Override
    public Map<String, List<Reservation>> findAll() {
        return null;
    }

    @Override
    public Reservation findByHostIdAndResId(String hostId, int reservationId) {
        if(hostId!=null && hostId.equalsIgnoreCase("a7fd9dd1-2cee-4efe-a495-5fd002414675") && reservationId == 1){
            return reservation;
        }else{
            return null;
        }
    }

    @Override
    public Reservation add(Reservation reservation) throws DataException {
        if(reservation.getId() <=0 ){
            return reservation;
        }else {
            return null;
        }
    }

    @Override
    public boolean update(Reservation reservation) throws DataException {
        if(reservation.getId()==1){
            return true;
        }else{
            return false;
        }
    }

    @Override
    public boolean delete(Reservation reservation) throws DataException {
        if(reservation.getId()==1){
            return true;
        }else {
            return false;
        }
    }

    private static Reservation makeReservation(){
        Reservation reservation = new Reservation();
        reservation.setId(1);
        reservation.setStartDate(LocalDate.of(2020, 1, 1));
        reservation.setEndDate(LocalDate.of(2020, 1, 2));
        reservation.setHost(makeHost());
        reservation.setGuest(makeGuest());
        reservation.setTotal(reservation.getHost().getStandardRate());
        return reservation;
    }

    private static Guest makeGuest(){
        Guest guest = new Guest();
        guest.setId(1);
        guest.setFirstName("Bob");
        guest.setLastName("Roberts");
        guest.setState("WI");
        guest.setEmail("bob.bob@hotmail.com");
        guest.setPhone("414 464-8488");
        return guest;
    }

    private static Host makeHost(){
        Host host = new Host();
        host.setId("a7fd9dd1-2cee-4efe-a495-5fd002414675");
        host.setLastName("Epstein");
        host.setEmail("captain@lolexpress.com");
        host.setPhone("920 999-9999");
        host.setAddress("1234 Hidden Drive");
        host.setCity("Miami");
        host.setState("FL");
        host.setPostalCode("56473");
        host.setStandardRate(new BigDecimal(1200000));
        host.setWeekendRate(new BigDecimal(1800000));
        return host;
    }
}
