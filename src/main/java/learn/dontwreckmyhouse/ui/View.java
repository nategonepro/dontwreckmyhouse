package learn.dontwreckmyhouse.ui;

import learn.dontwreckmyhouse.models.Guest;
import learn.dontwreckmyhouse.models.Host;
import learn.dontwreckmyhouse.models.Reservation;

import java.math.BigDecimal;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class View {
    private final ConsoleIO io;

    public View(ConsoleIO io) {
        this.io = io;
    }

    public MainMenuOption selectMainMenuOption(){
        displayHeader("Main Menu");
        int min = Integer.MAX_VALUE;
        int max = Integer.MIN_VALUE;
        for(MainMenuOption option : MainMenuOption.values()){
            io.printf("%s. %s%n", option.getValue(), option.getMessage());
            min = Math.min(min, option.getValue());
            max = Math.max(max, option.getValue());
        }

        String message = String.format("Select [%s-%s]: ", min, max);
        return MainMenuOption.fromValue(io.readInt(message, min, max));
    }

    public String getState(){
        String state = "";
        do {
            state = io.readRequiredString("Enter state [XX]: ");
            if(state.length() != 2){
                io.println("State must be in two-character abbreviated format [XX].");
            }
        }while(state.length() != 2);
        return state;
    }

    public String getHostEmailPrefix(){
        return io.readRequiredString("Host email prefix: ");
    }

    public Host chooseHost(List<Host> hosts){
        if(hosts.size() == 0){
            io.println("No hosts found.");
            return null;
        }

        int index = 1;
        for(Host host : hosts.stream().limit(25).collect(Collectors.toList())){
            io.printf("%s: %s, %s%n", index++, host.getLastName(), host.getEmail());
        }
        index--;

        if(hosts.size() > 25){
            io.println("More than 25 hosts found, showing first 25. Please refine your search.");
        }
        io.println("0: Exit");
        String message = String.format("Select a host by their index [0-%s]: ", index);

        index = io.readInt(message, 0, index);
        if(index<=0){
            return null;
        }
        return hosts.get(index-1);
    }

    public String getGuestEmailPrefix(){
        return io.readRequiredString("Guest email prefix: ");
    }

    public Guest chooseGuest(List<Guest> guests){
        if(guests.size() == 0){
            io.println("No guests found.");
            return null;
        }

        int index = 1;
        for(Guest guest : guests.stream().limit(25).collect(Collectors.toList())){
            io.printf("%s: %s %s, %s%n", index++, guest.getFirstName(), guest.getLastName(), guest.getEmail());
        }
        index--;

        if(guests.size() > 25){
            io.println("More than 25 guests found, showing first 25. Please refine your search.");
        }
        io.println("0: Exit");
        String message = String.format("Select a guest by their index [0-%s]: ", index);

        index = io.readInt(message, 0, index);
        if(index<=0){
            return null;
        }
        return guests.get(index-1);
    }

    public Reservation findReservation(List<Reservation> reservations){
        if(reservations.size() == 0){
            return null;
        }

        int resId = io.readInt("Reservation ID: ");
        for(Reservation r : reservations){
            if(r.getId() == resId){
                return r;
            }
        }
        displayStatus(false, "Reservation ID #" + resId + " not found.");
        return null;
    }

    public Reservation makeReservation(Guest guest, Host host){
        Reservation reservation = new Reservation();
        reservation.setGuest(guest);
        reservation.setHost(host);
        reservation.setStartDate(io.readLocalDate("Enter start date [MM/dd/yyyy]: "));
        reservation.setEndDate(io.readLocalDate("Enter end date [MM/dd/yyyy]: "));

        reservation.setTotal(calculateTotal(reservation, host));

        return reservation;
    }

    public Reservation updateReservation(Reservation reservation){
        LocalDate newStart = io.readOptionalLocalDate("Start (" + io.formatDate(reservation.getStartDate()) + "): ");
        LocalDate newEnd = io.readOptionalLocalDate("End (" + io.formatDate(reservation.getEndDate()) + "): ");

        if(newStart != null){
            reservation.setStartDate(newStart);
        }
        if(newEnd != null){
            reservation.setEndDate(newEnd);
        }

        reservation.setTotal(calculateTotal(reservation, reservation.getHost()));

        return reservation;
    }

    private BigDecimal calculateTotal(Reservation reservation, Host host){
        List<LocalDate> days = new ArrayList<>();
        for(LocalDate date = reservation.getStartDate(); date.isBefore(reservation.getEndDate()); date = date.plusDays(1)){
            days.add(date);
        }

        BigDecimal total = BigDecimal.ZERO;
        for(LocalDate day : days){
            if(day.getDayOfWeek() == DayOfWeek.FRIDAY || day.getDayOfWeek() == DayOfWeek.SATURDAY){
                total = total.add(host.getWeekendRate());
            }else{
                total = total.add(host.getStandardRate());
            }
        }
        total = total.setScale(2);
        return total;
    }

    public void displayHeader(String message){
        io.println("");
        io.println(message);
        io.println("═".repeat(message.length()));
    }

    public void displayException(Exception ex){
        displayHeader("A critical error occured:");
        io.println(ex.getMessage());
    }

    public void displayStatus(boolean success, String message){
        displayStatus(success, List.of(message));
    }

    public void displayStatus(boolean success, List<String> messages){
        displayHeader(success ? "Success" : "Error");
        for(String message : messages){
            io.println(message);
        }
    }

    public void displayReservations(List<Reservation> reservations){
        if(reservations.size() == 0){
            io.println("No items found");
        }

        for(Reservation r : reservations){
            io.printf("ID: %s, %s - %s, Guest: %s, %s, Email: %s%n",
                    r.getId(),
                    io.formatDate(r.getStartDate()),
                    io.formatDate(r.getEndDate()),
                    r.getGuest().getLastName(),
                    r.getGuest().getFirstName(),
                    r.getGuest().getEmail());
        }
    }

    public boolean displaySummaryIsOk(Reservation reservation){
        displayHeader("Summary");
        io.println("Start: " + io.formatDate(reservation.getStartDate()));
        io.println("End: " + io.formatDate(reservation.getEndDate()));
        io.println("Total: " + reservation.getTotal());
        return io.readBoolean("Is this okay? [y/n]: ");
    }
}
