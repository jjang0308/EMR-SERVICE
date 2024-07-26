package WELCOME.EMRSERVICE.Voice;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

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

    public List<Reservation> getReservationsByDate(LocalDate date) {
        return reservationRepository.findByDate(date);
    }

    private List<LocalTime> generateAllTimes() {
        LocalTime startTime = LocalTime.of(9, 0);
        LocalTime endTime = LocalTime.of(18, 0);
        return IntStream.iterate(0, i -> i + 30)
                .limit(((endTime.toSecondOfDay() - startTime.toSecondOfDay()) / 60) / 30)
                .mapToObj(startTime::plusMinutes)
                .collect(Collectors.toList());
    }

    public List<LocalTime> getAvailableTimes(LocalDate date) {
        List<Reservation> reservations = getReservationsByDate(date);
        List<LocalTime> reservedTimes = reservations.stream()
                .map(Reservation::getTime)
                .collect(Collectors.toList());

        LocalTime lunchStart = LocalTime.of(12, 0);
        LocalTime lunchEnd = LocalTime.of(12, 59);

        return generateAllTimes().stream()
                .filter(time -> !reservedTimes.contains(time))
                .filter(time -> time.isBefore(lunchStart) || time.isAfter(lunchEnd))
                .collect(Collectors.toList());
    }

    public boolean isFullyBooked(LocalDate date) {
        return getAvailableTimes(date).isEmpty();
    }

    public List<LocalDate> getFullyBookedDates() {
        List<LocalDate> fullyBookedDates = new ArrayList<>();
        List<Reservation> allReservations = getAllReservations();

        Map<LocalDate, List<Reservation>> reservationsByDate = allReservations.stream()
                .collect(Collectors.groupingBy(Reservation::getDate));

        for (Map.Entry<LocalDate, List<Reservation>> entry : reservationsByDate.entrySet()) {
            if (isFullyBooked(entry.getKey())) {
                fullyBookedDates.add(entry.getKey());
            }
        }

        return fullyBookedDates;
    }
}
