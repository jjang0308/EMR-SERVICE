package WELCOME.EMRSERVICE.Voice;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@Service
public class ReservationService {

    @Autowired
    private ReservationRepository reservationRepository;

    public boolean isAvailable(LocalDate date, LocalTime time) {
        Optional<Reservation> existingReservation = reservationRepository.findByDateAndTime(date, time);
        return existingReservation.isEmpty();
    }

    public List<Reservation> getAllReservations() {
        return reservationRepository.findAll();
    }

    public Reservation makeReservation(LocalDate date, LocalTime time) {
        if (isAvailable(date, time)) {
            Reservation reservation = new Reservation();
            reservation.setDate(date);
            reservation.setTime(time);
            return reservationRepository.save(reservation);
        } else {
            throw new IllegalStateException("이미 예약된 시간입니다.");
        }
    }
}
