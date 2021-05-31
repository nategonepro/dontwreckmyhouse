package learn.dontwreckmyhouse.data;

import learn.dontwreckmyhouse.models.Host;

import java.util.List;

public interface HostRepository {
    List<Host> findAll();

    List<Host> findByState(String state);

    List<Host> findByCity(String city);

    List<Host> findByLastName(String lastName);

    Host findById(String id);

    Host findByEmail(String email);

    Host add(Host host) throws DataException;

    boolean update(Host host) throws DataException;

    boolean delete(Host host) throws DataException;
}
