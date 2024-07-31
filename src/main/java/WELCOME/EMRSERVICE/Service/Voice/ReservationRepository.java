package WELCOME.EMRSERVICE.Service.Voice;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Long> {
    boolean existsByDateAndTime(LocalDate date, LocalTime time);
    List<Reservation> findByDoctorDoctorIdAndDate(Long doctorId, LocalDate date);
    List<Reservation> findByDoctorDoctorId(Long doctorId);
    List<Reservation> findByPatientPatientId(Long patientId);
}
