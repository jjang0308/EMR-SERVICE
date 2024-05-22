package WELCOME.EMRSERVICE.Repository.Doctor;

import WELCOME.EMRSERVICE.Domain.Doctor.Dept;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DeptRepository extends JpaRepository<Dept, Long> {
}

