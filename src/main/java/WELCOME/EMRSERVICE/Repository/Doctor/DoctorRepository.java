package WELCOME.EMRSERVICE.Repository.Doctor;

import WELCOME.EMRSERVICE.Domain.Doctor.Doctor;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DoctorRepository extends JpaRepository<Doctor, Integer> {
//    Optional<Doctor> findByDoctorLoginId(String username);
        Doctor findByDoctorLoginId(String username);


}
