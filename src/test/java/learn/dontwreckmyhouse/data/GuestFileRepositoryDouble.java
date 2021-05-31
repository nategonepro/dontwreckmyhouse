package learn.dontwreckmyhouse.data;

import learn.dontwreckmyhouse.models.Guest;

import java.util.ArrayList;
import java.util.List;

public class GuestFileRepositoryDouble implements GuestRepository{

    public final static Guest guest = makeGuest();

    private final ArrayList<Guest> guests = new ArrayList<>();

    public GuestFileRepositoryDouble(){
        guests.add(guest);
    }
    @Override
    public List<Guest> findAll() {
        return guests;
    }

    @Override
    public List<Guest> findByState(String state) {
        if(state!=null && state.equalsIgnoreCase("WI")){
            return guests;
        }else{
            return null;
        }
    }

    @Override
    public Guest findById(int id) {
        if(id == 1){
            return guest;
        }else{
            return null;
        }
    }

    @Override
    public Guest findByFullName(String firstName, String lastName) {
        if((firstName + " " + lastName).equalsIgnoreCase("Bob Roberts")){
            return guest;
        }else{
            return null;
        }
    }

    @Override
    public Guest findByEmail(String email) {
        if(email!=null && email.equalsIgnoreCase("bob.bob@hotmail.com")){
            return guest;
        }else{
            return null;
        }
    }

    @Override
    public Guest add(Guest guest) throws DataException {
        return null;
    }

    @Override
    public boolean update(Guest guest) throws DataException {
        return false;
    }

    @Override
    public boolean delete(Guest guest) throws DataException {
        return false;
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
}
