package learn.dontwreckmyhouse.data;

import learn.dontwreckmyhouse.models.Host;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class HostFileRepositoryDouble implements HostRepository{
    Host host = makeHost();

    private final ArrayList<Host> hosts = new ArrayList<>();

    public HostFileRepositoryDouble(){
        hosts.add(host);
    }

    @Override
    public List<Host> findAll() {
        return hosts;
    }

    @Override
    public List<Host> findByState(String state) {
        if(state!=null && state.equalsIgnoreCase("FL")){
            return hosts;
        }else{
            return null;
        }
    }

    @Override
    public List<Host> findByCity(String city) {
        if(city!=null && city.equalsIgnoreCase("Miami")){
            return hosts;
        }else{
            return null;
        }
    }

    @Override
    public List<Host> findByLastName(String lastName) {
        if(lastName!=null && lastName.equalsIgnoreCase("Epstein")){
            return hosts;
        }else{
            return null;
        }
    }

    @Override
    public Host findById(String id) {
        if(id!=null && id.equalsIgnoreCase("a7fd9dd1-2cee-4efe-a495-5fd002414675")){
            return host;
        }else{
            return null;
        }
    }

    @Override
    public Host findByEmail(String email) {
        if(email!=null && email.equalsIgnoreCase("captain@lolexpress.com")){
            return host;
        }else{
            return null;
        }
    }

    @Override
    public Host add(Host host) throws DataException {
        return null;
    }

    @Override
    public boolean update(Host host) throws DataException {
        return false;
    }

    @Override
    public boolean delete(Host host) throws DataException {
        return false;
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
