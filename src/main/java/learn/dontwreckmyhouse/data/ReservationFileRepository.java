package learn.dontwreckmyhouse.data;

import learn.dontwreckmyhouse.models.Guest;
import learn.dontwreckmyhouse.models.Host;
import learn.dontwreckmyhouse.models.Reservation;

import java.io.*;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//used to remove extension from filename
import org.apache.commons.io.FilenameUtils;

public class ReservationFileRepository implements ReservationRepository {

    private static final String HEADER = "id,start_date,end_date,guest_id,total";
    private final String directory;

    public ReservationFileRepository(String directory){
        this.directory = directory;
    }

    @Override
    public List<Reservation> findByHostId(String hostId) {
        ArrayList<Reservation> result = new ArrayList<>();

        try(BufferedReader reader = new BufferedReader(new FileReader(getFilePath(hostId)))){
            reader.readLine();

            for(String line = reader.readLine(); line!=null; line = reader.readLine()){
                String[] fields = line.split(",", -1);
                if(fields.length==5){
                    result.add(deserialize(fields,hostId));
                }
            }
        }catch (IOException | DataException ex){
            //ok, means file hasn't been created
        }

        return result;
    }

    @Override
    public Reservation findByHostIdAndResId(String hostId, int reservationId){
        List<Reservation> byHost = findByHostId(hostId);
        return byHost.stream()
                .filter(res -> res.getId() == reservationId)
                .findFirst()
                .orElse(null);
    }

    @Override
    public HashMap<String, List<Reservation>> findAll() {
        HashMap<String, List<Reservation>> result = new HashMap<>();

        File dir = new File(directory);
        if(dir.listFiles() != null) {
            for (File subfile : dir.listFiles()) {
                String hostId = FilenameUtils.removeExtension(subfile.getName());
                result.put(hostId, findByHostId(hostId));
            }
        }

        return result;
    }

    @Override
    public Reservation add(Reservation reservation) throws DataException {
        List<Reservation> all = findByHostId(reservation.getHost().getId());
        reservation.setId(getNextId(all));
        all.add(reservation);
        writeAll(all, reservation.getHost().getId());
        return reservation;
    }

    @Override
    public boolean update(Reservation reservation) throws DataException {
        List<Reservation> all = findByHostId(reservation.getHost().getId());
        for(int i=0; i<all.size(); i++){
            if(all.get(i).getId() == reservation.getId()){
                all.set(i, reservation);
                writeAll(all, reservation.getHost().getId());
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean delete(Reservation reservation) throws DataException {
        List<Reservation> all = findByHostId(reservation.getHost().getId());
        for(int i=0; i<all.size(); i++){
            if(all.get(i).getId() == reservation.getId()){
                all.remove(i);
                writeAll(all, reservation.getHost().getId());
                return true;
            }
        }
        return false;
    }

    private String getFilePath(String hostId) throws DataException {
        Path path = Paths.get(directory,hostId + ".csv");
//        if(Files.exists(path)){
//            return path.toString();
//        }else{
//            throw new DataException("Reservations file for host " + hostId + " does not exist.");
//        }
        return path.toString();
    }

    private int getNextId(List<Reservation> reservations){
        int maxId = 0;
        for(Reservation reservation : reservations){
            if(maxId < reservation.getId()){
                maxId = reservation.getId();
            }
        }
        return maxId+1;
    }

    private String serialize(Reservation reservation){
        return String.format("%s,%s,%s,%s,%s",
            reservation.getId(),
            reservation.getStartDate(),
            reservation.getEndDate(),
            reservation.getGuest().getId(),
            reservation.getTotal()
        );
    }

    private Reservation deserialize(String[] fields, String hostId){
        Reservation reservation = new Reservation();
        reservation.setId(Integer.parseInt(fields[0]));
        reservation.setStartDate(LocalDate.parse(fields[1]));
        reservation.setEndDate(LocalDate.parse(fields[2]));

        Guest guest = new Guest();
        guest.setId(Integer.parseInt(fields[3]));
        reservation.setGuest(guest);

        Host host = new Host();
        host.setId(hostId);
        reservation.setHost(host);

        reservation.setTotal(new BigDecimal(fields[4]));

        return reservation;
    }

    private void writeAll(List<Reservation> reservations, String hostId) throws DataException{
        try(PrintWriter writer = new PrintWriter(getFilePath(hostId))){
            writer.println(HEADER);
            for(Reservation reservation : reservations){
                writer.println(serialize(reservation));
            }
        }catch (FileNotFoundException ex){
            throw new DataException(ex);
        }
    }
}
