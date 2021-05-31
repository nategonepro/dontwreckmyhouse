package learn.dontwreckmyhouse.domain;

import learn.dontwreckmyhouse.data.HostRepository;
import learn.dontwreckmyhouse.models.Host;

import java.util.List;

public class HostService {
    private final HostRepository repository;

    public HostService(HostRepository repository) {
        this.repository = repository;
    }

    public List<Host> findAll(){
        return repository.findAll();
    }

    public List<Host> findByState(String state){
        return repository.findByState(state);
    }

    public Host findByEmail(String email){
        return repository.findByEmail(email);
    }
}
