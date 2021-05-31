package learn.dontwreckmyhouse;

import learn.dontwreckmyhouse.data.GuestFileRepository;
import learn.dontwreckmyhouse.data.HostFileRepository;
import learn.dontwreckmyhouse.data.ReservationFileRepository;
import learn.dontwreckmyhouse.domain.GuestService;
import learn.dontwreckmyhouse.domain.HostService;
import learn.dontwreckmyhouse.domain.ReservationService;
import learn.dontwreckmyhouse.ui.ConsoleIO;
import learn.dontwreckmyhouse.ui.Controller;
import learn.dontwreckmyhouse.ui.View;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class App {
    public static void main(String[] args) {
//        ConsoleIO io = new ConsoleIO();
//        View view = new View(io);
//
//        GuestFileRepository guestFileRepository = new GuestFileRepository("./data/guests.csv");
//        HostFileRepository hostFileRepository = new HostFileRepository("./data/hosts.csv");
//        ReservationFileRepository reservationFileRepository = new ReservationFileRepository("./data/reservations");
//
//        GuestService guestService = new GuestService(guestFileRepository);
//        HostService hostService = new HostService(hostFileRepository);
//        ReservationService reservationService = new ReservationService(reservationFileRepository, hostFileRepository, guestFileRepository);

        ApplicationContext context = new ClassPathXmlApplicationContext("di.xml");

        Controller controller = context.getBean(Controller.class);
        controller.run();
    }
}
