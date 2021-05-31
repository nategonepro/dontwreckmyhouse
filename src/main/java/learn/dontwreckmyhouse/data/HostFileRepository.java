package learn.dontwreckmyhouse.data;

import learn.dontwreckmyhouse.models.Host;

import java.io.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class HostFileRepository implements HostRepository{
    private static final String HEADER = "id,last_name,email,phone,address,city,state,postal_code,standard_rate,weekend_rate";
    private final String filePath;

    public HostFileRepository(String filePath) {
        this.filePath = filePath;
    }

    @Override
    public List<Host> findAll(){
        ArrayList<Host> result = new ArrayList<>();

        try(BufferedReader reader = new BufferedReader((new FileReader(filePath)))){
            reader.readLine();
            for(String line = reader.readLine(); line!=null; line= reader.readLine()){
                String[] fields = line.split(",", -1);
                if(fields.length==10){
                    result.add(deserialize(fields));
                }
            }
        }catch (IOException ex){
            //ok, means file is not created yet
        }

        return result;
    }

    @Override
    public List<Host> findByState(String state){
        if(state == null){
            return null;
        }
        return findAll().stream()
                .filter(i -> i.getState().equalsIgnoreCase(state))
                .collect(Collectors.toList());
    }

    @Override
    public List<Host> findByCity(String city){
        if(city == null){
            return null;
        }
        return findAll().stream()
                .filter(i -> i.getCity().equalsIgnoreCase(city))
                .collect(Collectors.toList());
    }

    @Override
    public List<Host> findByLastName(String lastName){
        if(lastName == null){
            return null;
        }
        return findAll().stream()
                .filter(i -> i.getLastName().equalsIgnoreCase(lastName))
                .collect(Collectors.toList());
    }

    @Override
    public Host findById(String id){
        if(id == null){
            return null;
        }
        return findAll().stream()
                .filter(i -> i.getId().equalsIgnoreCase(id))
                .findFirst()
                .orElse(null);
    }

    @Override
    public Host findByEmail(String email) {
        if(email == null){
            return null;
        }
        return findAll().stream()
                .filter(i -> i.getEmail().equalsIgnoreCase(email))
                .findFirst()
                .orElse(null);
    }

    @Override
    public Host add(Host host) throws DataException {
        List<Host> all = findAll();
        host.setId(java.util.UUID.randomUUID().toString());
        all.add(host);
        writeAll(all);
        return host;
    }

    @Override
    public boolean update(Host host) throws DataException {
        List<Host> all = findAll();
        for(int i=0; i<all.size(); i++){
            if(all.get(i).getId().equalsIgnoreCase(host.getId())){
                all.set(i, host);
                writeAll(all);
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean delete(Host host) throws DataException {
        List<Host> all = findAll();
        for(int i=0; i<all.size(); i++){
            if(all.get(i).getId().equalsIgnoreCase(host.getId())){
                all.remove(i);
                writeAll(all);
                return true;
            }
        }
        return false;
    }

    private String serialize(Host host){
        return String.format("%s,%s,%s,%s,%s,%s,%s,%s,%s,%s",
            host.getId(),
            host.getLastName(),
            host.getEmail(),
            host.getPhone(),
            host.getAddress(),
            host.getCity(),
            host.getState(),
            host.getPostalCode(),
            host.getStandardRate().toPlainString(),
            host.getWeekendRate().toPlainString()
        );
    }

    private Host deserialize(String[] fields){
        Host host = new Host();
        host.setId(fields[0]);
        host.setLastName(fields[1]);
        host.setEmail(fields[2]);
        host.setPhone(fields[3]);
        host.setAddress(fields[4]);
        host.setCity(fields[5]);
        host.setState(fields[6]);
        host.setPostalCode(fields[7]);
        host.setStandardRate(new BigDecimal(fields[8]));
        host.setWeekendRate(new BigDecimal(fields[9]));
        return host;
    }

    private void writeAll(List<Host> hosts) throws DataException{
        try(PrintWriter writer = new PrintWriter(filePath)){
            writer.println(HEADER);
            for(Host host : hosts){
                writer.println(serialize(host));
            }
        }catch (FileNotFoundException ex){
            throw new DataException(ex);
        }
    }
}
