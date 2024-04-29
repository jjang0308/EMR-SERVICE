package WELCOME.EMRSERVICE.Repository.Doctor;

import WELCOME.EMRSERVICE.Domain.Doctor.Doctor;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DoctorRepository extends JpaRepository<Doctor, Long> {
    Optional<Doctor> findByDoctorLoginId(String username);


}
