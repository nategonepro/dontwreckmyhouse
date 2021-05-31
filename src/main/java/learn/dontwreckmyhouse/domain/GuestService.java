package learn.dontwreckmyhouse.domain;

import learn.dontwreckmyhouse.data.GuestRepository;
import learn.dontwreckmyhouse.models.Guest;

import java.util.List;

public class GuestService {

    private final GuestRepository repository;

    public GuestService(GuestRepository repository) {
        this.repository = repository;
    }

    public List<Guest> findAll(){
        return repository.findAll();
    }

    public List<Guest> findByState(String state){
        return repository.findByState(state);
    }

    public Guest findByEmail(String email){
        return repository.findByEmail(email);
    }
}
