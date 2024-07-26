package WELCOME.EMRSERVICE.Voice;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.HashMap;
import java.util.Map;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("/api")
public class ReservationController {

    @Autowired
    private ReservationService reservationService;

    @GetMapping("/check")
    public Map<String, Boolean> checkAvailability(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.TIME) LocalTime time) {
        boolean available = reservationService.isAvailable(date, time);
        Map<String, Boolean> response = new HashMap<>();
        response.put("available", available);
        return response;
    }

    @GetMapping("/reservations")
    public List<Reservation> getAllReservations() {
        return reservationService.getAllReservations();
    }

    @PostMapping("/reserve")
    public Reservation reserve(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.TIME) LocalTime time) {
        return reservationService.makeReservation(date, time);
    }

    @GetMapping("/available-times")
    public List<LocalTime> getAvailableTimes(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        return reservationService.getAvailableTimes(date);
    }

    @GetMapping("/fully-booked-dates")
    public List<LocalDate> getFullyBookedDates() {
        return reservationService.getFullyBookedDates();
    }
}
