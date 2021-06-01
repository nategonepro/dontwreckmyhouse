package learn.dontwreckmyhouse.ui;

import learn.dontwreckmyhouse.data.DataException;
import learn.dontwreckmyhouse.domain.GuestService;
import learn.dontwreckmyhouse.domain.HostService;
import learn.dontwreckmyhouse.domain.ReservationService;
import learn.dontwreckmyhouse.domain.Result;
import learn.dontwreckmyhouse.models.Guest;
import learn.dontwreckmyhouse.models.Host;
import learn.dontwreckmyhouse.models.Reservation;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

public class Controller {

    private final GuestService guestService;
    private final HostService hostService;
    private final ReservationService reservationService;
    private final View view;

    public Controller(GuestService guestService, HostService hostService, ReservationService reservationService, View view) {
        this.guestService = guestService;
        this.hostService = hostService;
        this.reservationService = reservationService;
        this.view = view;
    }

    public void run(){
        view.displayHeader("Welcome to Don't Wreck My House");
        try {
            runAppLoop();
        }catch (DataException ex){
            view.displayException(ex);
        }
        view.displayHeader("Goodbye.");
    }

    private void runAppLoop() throws DataException{
        MainMenuOption option;
        do{
            option = view.selectMainMenuOption();
            switch (option){
                case VIEW_RESERVATION:
                    viewReservations();
                    break;
                case MAKE_RESERVATION:
                    makeReservations();
                    break;
                case UPDATE_RESERVATION:
                    updateReservation();
                    break;
                case REMOVE_RESERVATION:
                    removeReservation();
                    break;
                case VIEW_BY_GUEST:
                    viewByGuest();
                    break;
                case VIEW_BY_STATE:
                    viewByState();
                    break;
            }
        }while(option != MainMenuOption.EXIT);
    }

    private void viewReservations(){
        view.displayHeader(MainMenuOption.VIEW_RESERVATION.getMessage());
        Host host = getHost();

        List<Reservation> reservations = reservationService.findByHost(host);
        if(reservations == null){
            return;
        }

        view.displayHeader(host.getLastName() + ": " + host.getCity() + ", " + host.getState());
        reservations = reservations.stream().sorted(Comparator.comparing(Reservation::getStartDate)).collect(Collectors.toList());
        view.displayReservations(reservations);
    }

    private void makeReservations() throws DataException {
        view.displayHeader(MainMenuOption.MAKE_RESERVATION.getMessage());
        Guest guest = getGuest();
        if(guest == null){
            view.displayStatus(false, "Guest does not exist.");
            return;
        }
        Host host = getHost();
        if(host == null){
            view.displayStatus(false, "Host does not exist.");
            return;
        }

        List<Reservation> reservations = reservationService.findByHost(host);
        view.displayHeader(host.getLastName() + ": " + host.getCity() + ", " + host.getState());
        reservations = reservations.stream()
                .filter(r -> r.getStartDate().isAfter(LocalDate.now()))
                .sorted(Comparator.comparing(Reservation::getStartDate)).collect(Collectors.toList());
        view.displayReservations(reservations);

        Reservation reservation = view.makeReservation(guest, host);

        if(!view.displaySummaryIsOk(reservation)){
            view.displayStatus(true, "Reservation not created.");
            return;
        }

        Result<Reservation> result = reservationService.add(reservation);
        if(!result.isSuccess()){
            view.displayStatus(false, result.getErrorMessages());
        }else{
            String successMessage = String.format("Reservation ID #%s created.", result.getPayload().getId());
            view.displayStatus(true, successMessage);
        }
    }

    private void updateReservation() throws DataException {
        view.displayHeader(MainMenuOption.UPDATE_RESERVATION.getMessage());

        Host host = getHost();
        if(host == null){
            view.displayStatus(false, "Host does not exist.");
            return;
        }

        List<Reservation> reservations = reservationService.findByHost(host);
        view.displayHeader(host.getLastName() + ": " + host.getCity() + ", " + host.getState());
        reservations = reservations.stream().sorted(Comparator.comparing(Reservation::getStartDate)).collect(Collectors.toList());
        view.displayReservations(reservations);

        Reservation reservation = view.findReservation(reservations);
        if(reservation == null){
            return;
        }

        reservation = view.updateReservation(reservation);

        if(!view.displaySummaryIsOk(reservation)){
            view.displayStatus(true, "Reservation not updated.");
            return;
        }

        Result<Reservation> result = reservationService.update(reservation);
        if(!result.isSuccess()){
            view.displayStatus(false, result.getErrorMessages());
        }else{
            view.displayStatus(true, "Reservation ID #" + result.getPayload().getId() + " updated.");
        }
    }

    private void removeReservation() throws DataException {
        view.displayHeader(MainMenuOption.REMOVE_RESERVATION.getMessage());

        Host host = getHost();
        if(host == null){
            view.displayStatus(false, "Host does not exist.");
            return;
        }

        List<Reservation> reservations = reservationService.findByHost(host);
        view.displayHeader(host.getLastName() + ": " + host.getCity() + ", " + host.getState());
        reservations = reservations.stream()
                .filter(r -> r.getStartDate().isAfter(LocalDate.now()))
                .sorted(Comparator.comparing(Reservation::getStartDate)).collect(Collectors.toList());
        view.displayReservations(reservations);

        Reservation reservation = view.findReservation(reservations);
        if(reservation == null){
            return;
        }

        Result<Reservation> result = reservationService.delete(reservation);
        if(!result.isSuccess()){
            view.displayStatus(false, result.getErrorMessages());
        }else{
            view.displayStatus(true, "Reservation ID #" + result.getPayload().getId() + " deleted.");
        }
    }

    private void viewByGuest(){
        view.displayHeader(MainMenuOption.VIEW_BY_GUEST.getMessage());

        Guest guest = getGuest();

        if(guest == null){
            return;
        }

        ArrayList<Reservation> reservationsByGuest = new ArrayList<>();

        Map<String, List<Reservation>> allReservations = reservationService.findAll();

        if(allReservations == null){
            return;
        }

        for(List<Reservation> reservations : allReservations.values()){
            for(Reservation r : reservations){
                if(r.getGuest().getId() == guest.getId()){
                    reservationsByGuest.add(r);
                }
            }
        }

        view.displayReservations(reservationsByGuest);
    }

    private void viewByState(){
        view.displayHeader(MainMenuOption.VIEW_BY_STATE.getMessage());

        String state = view.getState();

        if(state == null){
            return;
        }

        ArrayList<Reservation> reservationsByState = new ArrayList<>();

        Map<String, List<Reservation>> allReservations = reservationService.findAll();

        if(allReservations == null){
            return;
        }

        for(List<Reservation> reservations : allReservations.values()){
            for(Reservation r : reservations){
                if(r.getHost().getState().equalsIgnoreCase(state)){
                    reservationsByState.add(r);
                }
            }
        }

        view.displayReservations(reservationsByState);
    }

    //helper methods

    private Guest getGuest() {
        String guestEmailPrefix = view.getGuestEmailPrefix();
        List<Guest> guests = guestService.findAll().stream().filter(g -> g.getEmail().startsWith(guestEmailPrefix)).collect(Collectors.toList());
        return view.chooseGuest(guests);
    }

    private Host getHost(){
        String hostEmailPrefix = view.getHostEmailPrefix();
        List<Host> hosts = hostService.findAll().stream().filter(h -> h.getEmail().startsWith(hostEmailPrefix)).collect(Collectors.toList());
        return view.chooseHost(hosts);
    }
}
