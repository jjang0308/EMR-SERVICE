package WELCOME.EMRSERVICE.Repository;

import WELCOME.EMRSERVICE.Domain.Appointment;
import WELCOME.EMRSERVICE.Domain.Doctor.Doctor;
import WELCOME.EMRSERVICE.Domain.Member.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface AppointmentRepository extends JpaRepository<Appointment, Long> {
    boolean existsByDoctorAndAppointmentDate(Doctor doctor, LocalDateTime appointmentDate);
    List<Appointment> findByPatient(Member patient);
}