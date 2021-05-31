package learn.dontwreckmyhouse.data;

import learn.dontwreckmyhouse.models.Guest;

import java.util.List;

public interface GuestRepository {
    List<Guest> findAll();

    List<Guest> findByState(String state);

    Guest findById(int id);

    Guest findByFullName(String firstName, String lastName);

    Guest findByEmail(String email);

    Guest add(Guest guest) throws DataException;

    boolean update(Guest guest) throws DataException;

    boolean delete(Guest guest) throws DataException;
}
