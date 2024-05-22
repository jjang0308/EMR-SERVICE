package WELCOME.EMRSERVICE.Repository.Doctor;

import WELCOME.EMRSERVICE.Domain.Doctor.Doctor;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DoctorRepository extends JpaRepository<Doctor, Long> { // Ensure the primary key type matches
        Doctor findByDoctorLoginId(String doctorLoginId);
}