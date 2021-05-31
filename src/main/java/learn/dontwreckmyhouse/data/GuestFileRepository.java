package learn.dontwreckmyhouse.data;

import learn.dontwreckmyhouse.models.Guest;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class GuestFileRepository implements GuestRepository{
    private static final String HEADER = "guest_id,first_name,last_name,email,phone,state";
    private final String filePath;

    public GuestFileRepository(String filePath) {
        this.filePath = filePath;
    }

    @Override
    public List<Guest> findAll() {
        ArrayList<Guest> result = new ArrayList<>();

        try(BufferedReader reader = new BufferedReader(new FileReader(filePath))){
            reader.readLine();
            for(String line = reader.readLine(); line!=null; line = reader.readLine()){
                String[] fields = line.split(",", -1);
                if(fields.length == 6){
                    result.add(deserialize(fields));
                }
            }
        }catch (IOException ex){
            //ok, means file hasn't been created yet
        }
        return result;
    }

    @Override
    public List<Guest> findByState(String state) {
        if(state == null){
            return null;
        }
        return findAll().stream()
                .filter(i -> i.getState().equalsIgnoreCase(state))
                .collect(Collectors.toList());
    }

    @Override
    public Guest findById(int id) {
        return findAll().stream()
                .filter(i -> i.getId() == id)
                .findFirst()
                .orElse(null);
    }

    @Override
    public Guest findByFullName(String firstName, String lastName) {
        if(firstName == null || lastName == null){
            return null;
        }
        String fullName = firstName + " " + lastName;
        return findAll().stream()
                .filter(i -> (i.getFirstName() + " " + i.getLastName()).equalsIgnoreCase(fullName))
                .findFirst()
                .orElse(null);
    }

    @Override
    public Guest findByEmail(String email) {
        if(email == null){
            return null;
        }
        return findAll().stream()
                .filter(i -> i.getEmail().equalsIgnoreCase(email))
                .findFirst()
                .orElse(null);
    }

    @Override
    public Guest add(Guest guest) throws DataException {
        List<Guest> all = findAll();
        guest.setId(getNextId(all));
        all.add(guest);
        writeAll(all);
        return guest;
    }

    @Override
    public boolean update(Guest guest) throws DataException {
        List<Guest> all = findAll();
        for(int i=0; i<all.size(); i++){
            if(all.get(i).getId() == guest.getId()){
                all.set(i, guest);
                writeAll(all);
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean delete(Guest guest) throws DataException {
        List<Guest> all = findAll();
        for(int i=0; i<all.size(); i++){
            if(all.get(i).getId() == guest.getId()){
                all.remove(i);
                writeAll(all);
                return true;
            }
        }
        return false;
    }

    private int getNextId(List<Guest> guests){
        int maxId = 0;
        for(Guest guest: guests){
            if(maxId < guest.getId()){
                maxId = guest.getId();
            }
        }
        return maxId + 1;
    }

    private String serialize(Guest guest){
        return String.format("%s,%s,%s,%s,%s,%s",
            guest.getId(),
            guest.getFirstName(),
            guest.getLastName(),
            guest.getEmail(),
            guest.getPhone(),
            guest.getState());
    }

    private Guest deserialize(String[] fields){
        Guest guest = new Guest();
        guest.setId(Integer.parseInt(fields[0]));
        guest.setFirstName(fields[1]);
        guest.setLastName(fields[2]);
        guest.setEmail(fields[3]);
        guest.setPhone(fields[4]);
        guest.setState(fields[5]);
        return guest;
    }

    private void writeAll(List<Guest> guests) throws DataException{
        try(PrintWriter writer = new PrintWriter(filePath)){
            writer.println(HEADER);
            for(Guest guest : guests){
                writer.println(serialize(guest));
            }
        }catch (FileNotFoundException ex){
            throw new DataException(ex);
        }
    }
}
