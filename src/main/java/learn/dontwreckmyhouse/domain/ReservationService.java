package learn.dontwreckmyhouse.domain;

import learn.dontwreckmyhouse.data.DataException;
import learn.dontwreckmyhouse.data.GuestRepository;
import learn.dontwreckmyhouse.data.HostRepository;
import learn.dontwreckmyhouse.data.ReservationRepository;
import learn.dontwreckmyhouse.models.Guest;
import learn.dontwreckmyhouse.models.Host;
import learn.dontwreckmyhouse.models.Reservation;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ReservationService {
    private final ReservationRepository reservationRepository;
    private final HostRepository hostRepository;
    private final GuestRepository guestRepository;

    public ReservationService(ReservationRepository reservationRepository, HostRepository hostRepository, GuestRepository guestRepository) {
        this.reservationRepository = reservationRepository;
        this.hostRepository = hostRepository;
        this.guestRepository = guestRepository;
    }

    public List<Reservation> findByHost(Host host){
        if(host == null){
            return null;
        }
        Map<String, Host> hostMap = hostRepository.findAll().stream()
                .collect(Collectors.toMap(i -> i.getId(), i-> i));
        Map<Integer, Guest> guestMap = guestRepository.findAll().stream()
                .collect(Collectors.toMap(i -> i.getId(), i -> i));

        List<Reservation> result = reservationRepository.findByHostId(host.getId());
        if(result==null || result.size() == 0){
            return result;
        }
        for(Reservation reservation : result){
            reservation.setHost(hostMap.get(reservation.getHost().getId()));
            reservation.setGuest(guestMap.get(reservation.getGuest().getId()));
        }

        return result;
    }

    public Reservation findByHostIdAndResId(String hostId, int reservationId){
        return reservationRepository.findByHostIdAndResId(hostId, reservationId);
    }

    public Result<Reservation> add(Reservation reservation) throws DataException {
        Result<Reservation> result = validate(reservation);

        if(!result.isSuccess()){
            return result;
        }

        if(reservationOverlaps(
                reservationRepository.findByHostId(reservation.getHost().getId()),
                reservation,
                false)
        ){
            result.addErrorMessage("New reservation cannot overlap with existing reservations.");
        }

        if(!result.isSuccess()){
            return result;
        }

        result.setPayload(reservationRepository.add(reservation));

        return result;
    }

    public Result<Reservation> update(Reservation reservation) throws DataException {
        Result<Reservation> result = validate(reservation);

        if(!result.isSuccess()){
            return result;
        }

        if(reservationOverlaps(
                reservationRepository.findByHostId(reservation.getHost().getId()),
                reservation,
                true)
        ){
            result.addErrorMessage("New reservation cannot overlap with existing reservations.");
        }

        if(!result.isSuccess()){
            return result;
        }

        Reservation existing = reservationRepository.findByHostIdAndResId(reservation.getHost().getId(), reservation.getId());
        if(existing==null){
            result.addErrorMessage("Reservation id #" + reservation.getId() + " not found for host #" + reservation.getHost().getId());
            return result;
        }

        boolean success = reservationRepository.update(reservation);
        if(!success){
            result.addErrorMessage("Failed to update reservation.");
        }

        result.setPayload(reservation);

        return result;
    }

    public Result<Reservation> delete(Reservation reservation) throws DataException {
        Result<Reservation> result = validateNulls(reservation);

        //cannot remove a reservation that doesn't exist
        if(!result.isSuccess()){
            result.addErrorMessage("Cannot delete null reservation.");
            return result;
        }

        //check if reservation exists in database
        Reservation existing = reservationRepository.findByHostIdAndResId(reservation.getHost().getId(), reservation.getId());
        if(existing == null){
            result.addErrorMessage("Reservation not found in the database.");
            return result;
        }

        //cannot delete a reservation in the past
        if(reservation.getEndDate().isBefore(LocalDate.now())){
            result.addErrorMessage("Cannot cancel a reservation in the past.");
            return result;
        }

        boolean success = reservationRepository.delete(reservation);
        if(!success){
            result.addErrorMessage("Failed to delete reservation.");
        }

        result.setPayload(reservation);

        return result;
    }

    private Result<Reservation> validate(Reservation reservation){
        Result<Reservation> result = validateNulls(reservation);

        if(!result.isSuccess()){
            return result;
        }

        validateFields(reservation, result);
        if(!result.isSuccess()){
            return result;
        }

        validateChildrenExist(reservation, result);

        return result;
    }

    private boolean reservationOverlaps(List<Reservation> reservations, Reservation reservation, boolean ignoreCurrent){
        if(reservations == null || reservations.size() == 0){
            return false;
        }
        for(Reservation r : reservations){
            if(ignoreCurrent && r.getId() == reservation.getId()){
                continue;
            }

            if(reservation.getStartDate().isAfter(r.getStartDate()) && reservation.getStartDate().isBefore(r.getEndDate())){
                return true;
            }

            if(reservation.getEndDate().isAfter(r.getStartDate()) && reservation.getEndDate().isBefore(r.getEndDate())){
                return true;
            }

            if(reservation.getStartDate().isBefore(r.getStartDate()) && reservation.getEndDate().isAfter(r.getEndDate())){
                return true;
            }
        }
        return false;
    }

    private Result<Reservation> validateNulls(Reservation reservation){
        Result<Reservation> result = new Result<>();

        if(reservation == null){
            result.addErrorMessage("Nothing to save.");
            return result;
        }

        if(reservation.getStartDate() == null){
            result.addErrorMessage("Start date is required.");
        }

        if(reservation.getEndDate() == null){
            result.addErrorMessage("End date is required.");
        }

        if(reservation.getHost() == null){
            result.addErrorMessage("Host is required.");
        }

        if(reservation.getGuest() == null){
            result.addErrorMessage("Guest is required.");
        }

//        if(reservation.getTotal() == null){
//            result.addErrorMessage("Total is required.");
//        }

        return result;
    }

    private void validateFields(Reservation reservation, Result<Reservation> result){
        if(reservation.getStartDate().isAfter(reservation.getEndDate())){
            result.addErrorMessage("Start date cannot be after end date.");
        }

        if(reservation.getStartDate().isBefore(LocalDate.now())){
            result.addErrorMessage("Start date must be in the future.");
        }

        if(reservation.getTotal().compareTo(BigDecimal.ZERO) < 0){
            result.addErrorMessage("Total must be greater or equal to zero.");
        }
    }

    private void validateChildrenExist(Reservation reservation, Result<Reservation> result){
        if(reservation.getHost().getId() == null ||
                hostRepository.findById(reservation.getHost().getId()) == null){
            result.addErrorMessage("Host does not exist.");
        }

        if(reservation.getGuest().getId() <= 0 ||
                guestRepository.findById(reservation.getGuest().getId()) == null){
            result.addErrorMessage("Guest does not exist.");
        }
    }
}
