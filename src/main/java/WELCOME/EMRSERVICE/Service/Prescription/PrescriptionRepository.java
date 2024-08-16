package WELCOME.EMRSERVICE.Service.Prescription;

import WELCOME.EMRSERVICE.Domain.Member.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PrescriptionRepository extends JpaRepository<Prescription, Long> {
    List<Prescription> findByMember(Member member);
    List<Prescription> findByReservationReservationId(Long reservationId);
    Optional<Prescription> findByReservation_ReservationId(Long reservationId);
}
