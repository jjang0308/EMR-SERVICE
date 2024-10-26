package WELCOME.EMRSERVICE.Repository.Reservation;

import WELCOME.EMRSERVICE.Domain.Doctor.Doctor;
import WELCOME.EMRSERVICE.Domain.Member.Member;
import WELCOME.EMRSERVICE.Domain.Reservation.Reservation;
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
    List<Reservation> findByMemberPatientId(Long patientId);
    List<Reservation> findByDoctor(Doctor doctor);
    List<Reservation> findByMember(Member member);
}
