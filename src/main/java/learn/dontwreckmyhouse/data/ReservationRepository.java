package learn.dontwreckmyhouse.data;

import learn.dontwreckmyhouse.models.Reservation;

import java.util.List;
import java.util.Map;

public interface ReservationRepository {
    List<Reservation> findByHostId(String hostId);

    Map<String, List<Reservation>> findAll();

    Reservation findByHostIdAndResId(String hostId, int reservationId);

    Reservation add(Reservation reservation) throws DataException;

    boolean update(Reservation reservation) throws DataException;

    boolean delete(Reservation reservation) throws DataException;
}
